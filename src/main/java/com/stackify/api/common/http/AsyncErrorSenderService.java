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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractScheduledService;

/**
 * AsyncErrorSenderService
 * 
 * @author Eric Martin
 */
public class AsyncErrorSenderService extends AbstractScheduledService {

	/**
	 * The service logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncErrorSenderService.class);
	
	/**
	 * The scheduler that determines delay timing after errors
	 */
	private final AsyncScheduler scheduler;
	
	/**
	 * The async error sender
	 */
	private final AsyncErrorSender asyncErrorSender;
			
	/**
	 * Constructor
	 * @param scheduler The scheduler that determines delay timing after errors
	 * @param asyncErrorSender The asynchronous HTTP error sender
	 */
	public AsyncErrorSenderService(final AsyncScheduler scheduler, final AsyncErrorSender asyncErrorSender) {
		Preconditions.checkNotNull(scheduler, "AsyncScheduler can't be null");
		Preconditions.checkNotNull(asyncErrorSender, "AsyncErrorSender can't be null");
		this.scheduler = scheduler;
		this.asyncErrorSender = asyncErrorSender;
	}
		
	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#serviceName()
	 */
	@Override
	protected String serviceName() {
		return "Stackify_AsyncErrorSenderService";
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#scheduler()
	 */
	@Override
	protected Scheduler scheduler() {
		return scheduler;
	}
		
	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#startUp()
	 */
	@Override
	protected void startUp() throws Exception {
		super.startUp();
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#shutDown()
	 */
	@Override
	protected void shutDown() throws Exception {
		LOGGER.debug("Shutting down Stackify_AsyncErrorSenderService");
		
		try {
			asyncErrorSender.flush();
		} catch (Exception e) {
			LOGGER.info("Exception running Stackify_AsyncErrorSenderService", e);
		}
		
		super.shutDown();
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#runOneIteration()
	 */
	@Override
	protected void runOneIteration() {		
		try {
			HttpTransmissionStatus status = asyncErrorSender.flush();
			scheduler.update(status);
		} catch (Exception e) {
			LOGGER.info("Exception running Stackify_AsyncErrorSenderService", e);
			scheduler.update(HttpTransmissionStatus.ERROR);
		}
	}
}
