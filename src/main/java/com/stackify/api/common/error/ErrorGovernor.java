/*
 * Copyright 2013 Stackify
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
package com.stackify.api.common.error;

import com.stackify.api.StackifyError;

/**
 * Utility class for limiting transmission of duplicate errors 
 * 
 * @author Eric Martin
 */
public class ErrorGovernor {

    /**
     * Number of instances of a unique error that are allowed to be sent in one minute 
     */
    private static final int MAX_DUP_ERROR_PER_MINUTE = 100;

    /**
     * Elapsed time before the errorToCounter map is purged of expired entries 
     */
    private static final int CLEAN_UP_MINUTES = 15;
    
    /**
     * Map from
     *     MD5(<type>-<typeCode>-<method>)
     * to
     *     Unix epoch minute, error count for that minute
     */
    private final ErrorCounter errorCounter = new ErrorCounter();

    /**
     * The next time the errorToCounter dictionary needs to be purged of expired entries
     */
    private long nextErrorToCounterCleanUp = getUnixEpochMinutes() + CLEAN_UP_MINUTES;

    /**
     * Determines if the error should be sent based on our throttling criteria
     * @param error The error
     * @return True if this error should be sent to Stackify, false otherwise
     */
    public boolean errorShouldBeSent(final StackifyError error) {
		if (error == null) {
			throw new NullPointerException("StackifyError is null");
		}

    	boolean shouldBeProcessed = false;

        long epochMinute = getUnixEpochMinutes();
    	
    	synchronized (errorCounter) {
    		
    		// increment the counter for this error
    		
    		int errorCount = errorCounter.incrementCounter(error, epochMinute);
    		
            // check our throttling criteria

            if (errorCount <= MAX_DUP_ERROR_PER_MINUTE) {
                shouldBeProcessed = true;
            }
            
            // see if we need to purge our counters of expired entries

            if (nextErrorToCounterCleanUp < epochMinute) {
            	errorCounter.purgeCounters(epochMinute);
                nextErrorToCounterCleanUp = epochMinute + CLEAN_UP_MINUTES;
            }
    	}

        return shouldBeProcessed;
    }
       
    /**
     * @return The current Unix epoch minutes
     */
    private long getUnixEpochMinutes() {
    	return System.currentTimeMillis() / 60000;
    }
}
