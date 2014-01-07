/*
 * ErrorCounter.java
 * Copyright 2014 Stackify
 */
package com.stackify.api.common.error;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.stackify.api.ErrorItem;
import com.stackify.api.StackifyError;
import com.stackify.api.common.codec.MessageDigests;

/**
 * ErrorCounter
 * @author Eric Martin
 */
public class ErrorCounter {
	
    /**
     * Map from
     *     MD5(<type>-<typeCode>-<method>)
     * to
     *     Unix epoch minute, error count for that minute
     */
    private final Map<String, MinuteCounter> errorCounter = new HashMap<String, MinuteCounter>();

    /**
     * Unix epoch minute and error count for that minute
     * 
     * @author Eric Martin
     */
    protected static class MinuteCounter {

    	/**
    	 * Unix epoch minute
    	 */
    	private final long epochMinute;
    	
    	/**
    	 * Error count for that minute
    	 */
    	private final int errorCount;

    	/**
    	 * Constructs a new minute counter for the specified minute
    	 * @param epochMinute Unix epoch minute
    	 * @return A new minute counter for the specified minute
    	 */
    	public static MinuteCounter newMinuteCounter(final long epochMinute) {
    		return new MinuteCounter(epochMinute, 1);
    	}
    	
    	/**
    	 * Constructs a new minute counter from the existing counter with the count incremented
    	 * @param counter Error count for that minute
    	 * @return A new minute counter from the existing counter with the count incremented
    	 */
    	public static MinuteCounter incrementCounter(final MinuteCounter counter) {
    		return new MinuteCounter(counter.epochMinute, counter.errorCount + 1);
    	}
    	
    	/**
    	 * Private constructor
    	 * @param epochMinute Unix epoch minute
    	 * @param errorCount Error count for that minute
    	 */
    	private MinuteCounter(final long epochMinute, final int errorCount) {
    	    this.epochMinute = epochMinute;
    	    this.errorCount = errorCount;
    	}
    	
    	/**
    	 * @return the epochMinute
    	 */
    	public long getEpochMinute() {
    		return epochMinute;
    	}

    	/**
    	 * @return the errorCount
    	 */
    	public int getErrorCount() {
    		return errorCount;
    	}
    }
    
    /**
     * Gets the base error (the last error in the causal chain)
     * @param error The error
     * @return The inner most error
     */
    public static ErrorItem getBaseError(final StackifyError error) {
		if (error == null) {
			throw new NullPointerException("StackifyError is null");
		}

		ErrorItem errorItem = error.getError();

        if (errorItem != null) {
            while (errorItem.getInnerError() != null) {
                errorItem = errorItem.getInnerError();
            }
        }

        return errorItem;
    }
    
    /**
     * Generates a unique key based on the error. The key will be an MD5 hash of the type, type code, and method.
     * @param errorItem The error item
     * @return The unique key for the error
     */
    public static String getUniqueKey(final ErrorItem errorItem) {
		if (errorItem == null) {
			throw new NullPointerException("ErrorItem is null");
		}
		
		String type = errorItem.getErrorType();
    	String typeCode = errorItem.getErrorTypeCode();
    	String method = errorItem.getSourceMethod();

    	String uniqueKey = String.format("%s-%s-%s", type, typeCode, method);
    	
    	return MessageDigests.md5Hex(uniqueKey);
    }
    
    /**
     * Increments the counter for this error in the epoch minute specified
     * @param error The error
     * @param epochMinute The epoch minute
     * @return The count for the error after it has been incremented
     */
    public int incrementCounter(final StackifyError error, long epochMinute) {
		if (error == null) {
			throw new NullPointerException("StackifyError is null");
		}

        ErrorItem baseError = getBaseError(error);
    	String uniqueKey = getUniqueKey(baseError);

        // get the counter for this error
    	
    	int count = 0;

        if (errorCounter.containsKey(uniqueKey)) {
        	
            // counter exists

        	MinuteCounter counter = errorCounter.get(uniqueKey);

            if (counter.getEpochMinute() == epochMinute) {
                // counter exists for this current minute
                // increment the counter
            	MinuteCounter incCounter = MinuteCounter.incrementCounter(counter);
            	errorCounter.put(uniqueKey, incCounter);
            	count = incCounter.getErrorCount();
            } else {
                // counter did not exist for this minute
            	// overwrite the entry with a new one
            	MinuteCounter newCounter = MinuteCounter.newMinuteCounter(epochMinute);
            	errorCounter.put(uniqueKey, newCounter);
            	count = newCounter.getErrorCount();
            }
        } else {
            // counter did not exist so create a new one
        	MinuteCounter newCounter = MinuteCounter.newMinuteCounter(epochMinute);
        	errorCounter.put(uniqueKey, newCounter);
        	count = newCounter.getErrorCount();
        }
        
        return count;
    }
    
    /**
     * Purges the errorCounter map of expired entries
     * @param epochMinute The current time
     */
    public void purgeCounters(final long epochMinute)
    {
    	for (Iterator<Map.Entry<String, MinuteCounter>> it = errorCounter.entrySet().iterator(); it.hasNext(); ) {
    		Map.Entry<String, MinuteCounter> entry = it.next();
    		
    		if (entry.getValue().getEpochMinute() < epochMinute) {
    			it.remove();
    		}
    	}
    }	
}
