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

import org.junit.Assert;
import org.junit.Test;

import com.stackify.api.ErrorItem;
import com.stackify.api.StackifyError;
import com.stackify.api.common.codec.MessageDigests;

/**
 * ErrorCounter JUnit Test
 * 
 * @author Eric Martin
 */
public class ErrorCounterTest {

	/**
	 * testGetBaseErrorWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testGetBaseErrorWithNull() {
		ErrorCounter.getBaseError(null);
	}
	
	/**
	 * testGetBaseError
	 */
	@Test
	public void testGetBaseError() {
		ErrorItem errorDetail = ErrorItem.newBuilder().build();
		
		StackifyError.Builder builder = StackifyError.newBuilder();
		builder.error(errorDetail);
		StackifyError stackifyError = builder.build();
		
		Assert.assertEquals(errorDetail, ErrorCounter.getBaseError(stackifyError));
	}
	
	/**
	 * testGetUniqueKeyWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testGetUniqueKeyWithNull() {
		ErrorCounter.getUniqueKey(null);
	}
	
	/**
	 * testGetUniqueKey
	 */
	@Test
	public void testGetUniqueKey() {
		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.errorType("errorType");
		builder.errorTypeCode("errorTypeCode");
		builder.sourceMethod("sourceMethod");

		String expectedHash = MessageDigests.md5Hex("errorType-errorTypeCode-sourceMethod");
		
		ErrorItem errorItem = builder.build();
		
		Assert.assertEquals(expectedHash, ErrorCounter.getUniqueKey(errorItem));
	}
	
	/**
	 * testMinuteCounterNewMinuteCounter
	 */
	@Test
	public void testMinuteCounterNewMinuteCounter() {
		long epochMinute = System.currentTimeMillis() / 60000;
		
		ErrorCounter.MinuteCounter newCounter = ErrorCounter.MinuteCounter.newMinuteCounter(epochMinute);
		
		Assert.assertNotNull(newCounter);
		Assert.assertEquals(epochMinute, newCounter.getEpochMinute());
		Assert.assertEquals(1, newCounter.getErrorCount());
	}
	
	/**
	 * testMinuteCounterIncrementCounter
	 */
	@Test
	public void testMinuteCounterIncrementCounter() {
		long epochMinute = System.currentTimeMillis() / 60000;
		
		ErrorCounter.MinuteCounter newCounter = ErrorCounter.MinuteCounter.newMinuteCounter(epochMinute);
		ErrorCounter.MinuteCounter incCounter = ErrorCounter.MinuteCounter.incrementCounter(newCounter);
		
		Assert.assertNotNull(incCounter);
		Assert.assertEquals(epochMinute, incCounter.getEpochMinute());
		Assert.assertEquals(2, incCounter.getErrorCount());
	}
	
	/**
	 * testIncrementCounterWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testIncrementCounterWithNull() {
		ErrorCounter errorCounter = new ErrorCounter();
		errorCounter.incrementCounter(null, 0);
	}
	
	/**
	 * testIncrementCounter
	 */
	@Test
	public void testIncrementCounter() {
		ErrorCounter errorCounter = new ErrorCounter();
		
		ErrorItem.Builder builder1 = ErrorItem.newBuilder();
		builder1.errorType("errorType_1");
		builder1.errorTypeCode("errorTypeCode_1");
		builder1.sourceMethod("sourceMethod_1");
		ErrorItem errorItem1 = builder1.build();
		StackifyError error1 = StackifyError.newBuilder().error(errorItem1).build();
		
		ErrorItem.Builder builder2 = ErrorItem.newBuilder();
		builder2.errorType("errorType_2");
		builder2.errorTypeCode("errorTypeCode_2");
		builder2.sourceMethod("sourceMethod_2");
		ErrorItem errorItem2 = builder2.build();
		StackifyError error2 = StackifyError.newBuilder().error(errorItem2).build();
		
		Assert.assertEquals(1, errorCounter.incrementCounter(error1, 0));
		Assert.assertEquals(2, errorCounter.incrementCounter(error1, 0));
		Assert.assertEquals(1, errorCounter.incrementCounter(error1, 1));
		Assert.assertEquals(1, errorCounter.incrementCounter(error2, 1));
		Assert.assertEquals(2, errorCounter.incrementCounter(error2, 1));
		Assert.assertEquals(2, errorCounter.incrementCounter(error1, 1));
	}

	/**
	 * testPurgeCounters
	 */
	@Test
	public void testPurgeCounters() {
		ErrorCounter errorCounter = new ErrorCounter();
		errorCounter.purgeCounters(0);

		ErrorItem.Builder builder1 = ErrorItem.newBuilder();
		builder1.errorType("errorType_1");
		builder1.errorTypeCode("errorTypeCode_1");
		builder1.sourceMethod("sourceMethod_1");
		ErrorItem errorItem1 = builder1.build();
		StackifyError error1 = StackifyError.newBuilder().error(errorItem1).build();
		
		ErrorItem.Builder builder2 = ErrorItem.newBuilder();
		builder2.errorType("errorType_2");
		builder2.errorTypeCode("errorTypeCode_2");
		builder2.sourceMethod("sourceMethod_2");
		ErrorItem errorItem2 = builder2.build();
		StackifyError error2 = StackifyError.newBuilder().error(errorItem2).build();
		
		Assert.assertEquals(1, errorCounter.incrementCounter(error1, 0));
		Assert.assertEquals(1, errorCounter.incrementCounter(error2, 1));
		
		errorCounter.purgeCounters(1);
		
		Assert.assertEquals(1, errorCounter.incrementCounter(error1, 1));
		Assert.assertEquals(2, errorCounter.incrementCounter(error2, 1));
		
		errorCounter.purgeCounters(2);

		Assert.assertEquals(1, errorCounter.incrementCounter(error1, 2));
		Assert.assertEquals(1, errorCounter.incrementCounter(error2, 2));
	}
}
