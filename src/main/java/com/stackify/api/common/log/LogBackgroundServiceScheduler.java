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

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler;
import com.stackify.api.common.http.HttpException;

/**
 * LogBackgroundServiceScheduler
 * 
 * @author Eric Martin
 */
public class LogBackgroundServiceScheduler extends CustomScheduler {
		
	/**
	 * One second (milliseconds)
	 */
	private static final long ONE_SECOND = 1000;
	
	/**
	 * Five seconds (milliseconds)
	 */
	private static final long FIVE_SECONDS = 5000;
	
	/**
	 * One minute (milliseconds)
	 */
	private static final long ONE_MINUTE = 60000;
	
	/**
	 * Five minutes (milliseconds)
	 */
	private static final long FIVE_MINUTES = 300000;
	
	/**
	 * Schedule delay (in milliseconds)
	 */
	private long scheduleDelay = ONE_SECOND;
	
	/**
	 * UTC timestamp of the HTTP error
	 */
	private long lastHttpError = 0;
	
	/**
	 * Sets the next scheduled delay based on the number of messages sent
	 * @param numSent The number of log messages sent
	 */
	public void update(final int numSent) {
		
		// Reset the last HTTP error
			
		lastHttpError = 0;
		
		// adjust the schedule delay based on the number of messages sent in the last iteration
		
		if (100 <= numSent) {
			
			// messages are coming in quickly so decrease our delay
			// minimum delay is 1 second
			
			scheduleDelay = Math.max(Math.round(scheduleDelay / 2.0), ONE_SECOND);
			
		} else if (numSent < 10) {
			
			// messages are coming in rather slowly so increase our delay
			// maximum delay is 5 seconds
			
			scheduleDelay = Math.min(Math.round(1.25 * scheduleDelay), FIVE_SECONDS);
			
		}
	}

	/**
	 * Sets the next scheduled delay based on the HTTP transmission status
	 * @param t The exception
	 * @param status The HTTP transmission status
	 */
	public void update(final Throwable t) {
		
		// see if the exception indicates an authorization problem
		
		boolean isAuthorized = true;
		
		if (t instanceof HttpException) {
			HttpException httpException = (HttpException) t;
			
			if (httpException.getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				isAuthorized = false;
			}
		}
		
		// adjust the schedule delay based on the type of error 
		
		if (isAuthorized) {
			
			// Set the last HTTP error (if not already set)

			if (lastHttpError == 0) {
				lastHttpError = System.currentTimeMillis();
			}

			// Set the schedule delay to the elapsed time between now and the start of the HTTP errors
			// Min schedule delay = 1 second
			// Max schedule delay = 1 minute

			long sinceFirstError = System.currentTimeMillis() - lastHttpError;
			
			scheduleDelay = Math.min(Math.max(sinceFirstError, ONE_SECOND), ONE_MINUTE);
			
		} else {
			
			// Set the last HTTP error
			// Set the schedule delay to five minutes
			
			lastHttpError = System.currentTimeMillis();
			scheduleDelay = FIVE_MINUTES;
		}
	}
	
	/**
	 * @return the scheduleDelay
	 */
	public long getScheduleDelay() {
		return scheduleDelay;
	}

	/**
	 * @return the lastHttpError
	 */
	public long getLastHttpError() {
		return lastHttpError;
	}

	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler#getNextSchedule()
	 */
	@Override
	protected Schedule getNextSchedule() {
		return new Schedule(scheduleDelay, TimeUnit.MILLISECONDS);
	}
}
