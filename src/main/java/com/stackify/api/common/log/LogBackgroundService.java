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
package com.stackify.api.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.stackify.api.common.concurrent.BackgroundService;

/**
 * LogSenderService
 *
 * @author Eric Martin
 */
public class LogBackgroundService extends BackgroundService {

	/**
	 * The service logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogBackgroundService.class);

	/**
	 * The scheduler that determines delay timing after errors
	 */
	private final LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();

	/**
	 * The LogMsg collector
	 */
	private final LogCollector collector;

	/**
	 * The LogMsgGroup HTTP sender
	 */
	private final LogSender sender;

	/**
	 * Constructor
	 * @param collector The LogMsg collector
	 * @param sender The LogMsgGroup HTTP sender
	 */
	public LogBackgroundService(final LogCollector collector, final LogSender sender) {
		Preconditions.checkNotNull(collector);
		Preconditions.checkNotNull(sender);
		this.collector = collector;
		this.sender = sender;
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#scheduler()
	 */
	@Override
	protected Scheduler scheduler() {
		return scheduler;
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#shutDown()
	 */
	@Override
	protected void shutDown() throws Exception {
		try {
			collector.flush(sender);
		} catch (Throwable t) {
			LOGGER.info("Exception flushing log collector during shut down", t);
		}

		super.shutDown();
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#runOneIteration()
	 */
	@Override
	protected void runOneIteration() {
		try {
			int numSent = collector.flush(sender);
			scheduler.update(numSent);
		} catch (Throwable t) {
			LOGGER.info("Exception running Stackify_LogBackgroundService", t);
			scheduler.update(t);
		}
	}
}
