/*
 * Copyright 2015 Stackify
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stackify.api.common.http;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stackify.api.common.collect.SynchronizedEvictingQueue;
import com.stackify.api.common.lang.Threads;

/**
 * HttpRetransmissionQueue
 * @author Eric Martin
 */
public class HttpResendQueue {

	/**
	 * The service logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpResendQueue.class);

	/**
	 * The queue of requests to be retransmitted
	 */
	private final Queue<byte[]> resendQueue; 

	/**
	 * Constructor
	 * @param maxSize Maximum size of the queue
	 */
	public HttpResendQueue(final int maxSize) {
		this.resendQueue = new SynchronizedEvictingQueue<byte[]>(maxSize); 
	}
	
	/**
	 * @return Current size of the resend queue
	 */
	public int size() {
		return resendQueue.size();
	}
	
	/**
	 * Offers a failed request to the resend queue
	 * @param request The failed request
	 * @param e IOException
	 */
	public void offer(final byte[] request, final IOException e) {
		resendQueue.offer(request);
	}
	
	/**
	 * Offers a failed request to the resend queue
	 * @param request The failed request
	 * @param e HttpException
	 */
	public void offer(final byte[] request, final HttpException e) {
		if (!e.isClientError()) {
			resendQueue.offer(request);
		}
	}
	
	/**
	 * Drains the resend queue until empty or error
	 * @param httpClient The HTTP client
	 * @param path REST path
	 */
	public void drain(final HttpClient httpClient, final String path) {
		drain(httpClient, path, false);
	}
	
	/**
	 * Drains the resend queue until empty or error
	 * @param httpClient The HTTP client
	 * @param path REST path
	 * @param gzip True if the post should be gzipped, false otherwise
	 */
	public void drain(final HttpClient httpClient, final String path, final boolean gzip) {
		if (!resendQueue.isEmpty()) {
			try {
				LOGGER.info("Attempting to retransmit {} requests", resendQueue.size());
				
				while (!resendQueue.isEmpty()) {
					byte[] jsonBytes = resendQueue.peek();
					httpClient.post(path, jsonBytes, gzip);
					resendQueue.remove();
					Threads.sleepQuietly(250, TimeUnit.MILLISECONDS);
				}
			} catch (Throwable t) {
				LOGGER.info("Failure retransmitting queued requests", t);
			}
		}
	}
}
