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

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler;

/**
 * AsyncScheduler
 * 
 * @author Eric Martin
 */
public class AsyncScheduler extends CustomScheduler {
	
	/**
	 * Schedule delay (in seconds)
	 */
	private int scheduleDelay = 1;
	
	/**
	 * UTC timestamp of the HTTP error
	 */
	private long lastHttpError = 0;
	
	/**
	 * Sets the next scheduled delay based on the HTTP transmission status
	 * @param status The HTTP transmission status
	 */
	public void update(final HttpTransmissionStatus status) {
		
		if (status == HttpTransmissionStatus.OK) {
			
			// Reset the last HTTP error
			// Set the schedule delay to one second
			
			lastHttpError = 0;
			scheduleDelay = 1;
			
		} else if (status == HttpTransmissionStatus.UNAUTHORIZED) {
			
			// Set the last HTTP error
			// Set the schedule delay to five minutes
			
			lastHttpError = System.currentTimeMillis();
			scheduleDelay = 300;
			
		} else {
			
			// Set the last HTTP error (if not already set)

			if (lastHttpError == 0) {
				lastHttpError = System.currentTimeMillis();
			}

			// Set the schedule delay to the elapsed time between now and the start of the HTTP errors
			// Min schedule delay = 1 second
			// Max schedule delay = 60 seconds

			int sinceFirstError = (int) Math.ceil((System.currentTimeMillis() - lastHttpError) / 1000.0);
			
			scheduleDelay = Math.min(Math.max(sinceFirstError, 1), 60);
		}
	}
	
	/**
	 * @return the scheduleDelay
	 */
	public int getScheduleDelay() {
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
		return new Schedule(scheduleDelay, TimeUnit.SECONDS);
	}
}
