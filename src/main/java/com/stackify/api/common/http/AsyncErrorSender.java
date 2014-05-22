/*
 * Copyright 2014 Stackify
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

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.stackify.api.StackifyError;

/**
 * AsyncErrorSender
 * 
 * @author Eric Martin
 */
public class AsyncErrorSender {
	
	/**
	 * The service logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncErrorSender.class);
					
	/**
	 * The HTTP error sender
	 */
	private final StackifyErrorSender errorSender;
	
	/**
	 * The queue of objects to be transmitted
	 */
	private final Queue<StackifyError> queue; 
	
	/**
	 * Constructor
	 * @param errorSender The HTTP error sender
	 * @param queueMaxSize Queue size
	 */
	public AsyncErrorSender(final StackifyErrorSender errorSender, final int queueMaxSize) {
		Preconditions.checkNotNull(errorSender);
		Preconditions.checkArgument(0 < queueMaxSize, "Queue max size must be greater than zero");
		
		this.errorSender = errorSender;
		this.queue = Queues.synchronizedQueue(EvictingQueue.<StackifyError>create(queueMaxSize)); 
	}
	
	/**
	 * Queues error to be sent
	 * @param error The error
	 */
	public void sendError(final StackifyError error) {
		Preconditions.checkNotNull(error);		
		queue.offer(error);
	}
	
	/**
	 * @return The size of the queue
	 */
	public int getQueueSize() {
		return queue.size();
	}

	/**
	 * Flushes the queue by sending all errors to Stackify
	 * @return The HTTP transmission status
	 */
	public HttpTransmissionStatus flush() {

		int numSent = 0;
		int maxToSend = queue.size();
		
		while (numSent < maxToSend) {
			
			// get the next batch of errors
			
			int batchSize = Math.min(maxToSend - numSent, 10);

			List<StackifyError> batch = Lists.newArrayListWithCapacity(batchSize);
			
			for (int i = 0; i < batchSize; ++i) {
				batch.add(queue.remove());
			}

			// send the batch to Stackify
			
			int httpStatus = HttpURLConnection.HTTP_INTERNAL_ERROR;

			LOGGER.debug("Sending batch of {} error/s to Stackify", batchSize);

			try {
				httpStatus = errorSender.send(batch);	
			} catch (Exception e) {
				LOGGER.info("Exception sending batch of errors to Stackify", e);
				return HttpTransmissionStatus.ERROR;
			}
			
			// if the batch failed to transmit, return the appropriate transmission status
			
			if (httpStatus != HttpURLConnection.HTTP_OK) {
				
				LOGGER.info("HTTP {} error sending batch of errors to Stackify", httpStatus);
				
				if (httpStatus == HttpURLConnection.HTTP_UNAUTHORIZED) {
					return HttpTransmissionStatus.UNAUTHORIZED;
				}
				
				return HttpTransmissionStatus.ERROR;
			}
			
			// next iteration
			
			numSent += batchSize;
		}
		
		return HttpTransmissionStatus.OK;
	}
}
