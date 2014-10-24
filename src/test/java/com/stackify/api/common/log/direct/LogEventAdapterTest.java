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
package com.stackify.api.common.log.direct;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Optional;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.ErrorItem;
import com.stackify.api.LogMsg;
import com.stackify.api.StackifyError;

/**
 * LogEventAdapterTest
 * @author Eric Martin
 */
public class LogEventAdapterTest {

	/**
	 * testGetThrowable
	 */
	@Test
	public void testGetThrowable() {
		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		LogEventAdapter adapter = new LogEventAdapter(envDetail);
		
		Throwable exception = new NullPointerException();		
		LogEvent eventWithException = LogEvent.newBuilder().exception(exception).build();
		Optional<Throwable> present = adapter.getThrowable(eventWithException);
		Assert.assertTrue(present.isPresent());
		Assert.assertEquals(exception, present.get());
		
		LogEvent eventWithoutException = LogEvent.newBuilder().build();
		Optional<Throwable> absent = adapter.getThrowable(eventWithoutException);
		Assert.assertFalse(absent.isPresent());
	}
	
	/**
	 * testGetStackifyError
	 */
	@Test
	public void testGetStackifyError() {
		String level = "level";
		String message = "message";
		Throwable exception = new NullPointerException();
		LogEvent event = LogEvent.newBuilder().level(level).message(message).exception(exception).build();

		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		LogEventAdapter adapter = new LogEventAdapter(envDetail);

		StackifyError error = adapter.getStackifyError(event, exception);
		Assert.assertEquals(envDetail, error.getEnvironmentDetail());
		Assert.assertEquals(event.getTimestamp(), error.getOccurredEpochMillis().getTime());
		Assert.assertNotNull(error.getServerVariables());
		
		ErrorItem errorItem = error.getError();
		Assert.assertEquals(message, errorItem.getMessage());
		Assert.assertEquals(exception.getClass().getCanonicalName(), errorItem.getErrorType());
	}
	
	/**
	 * testGetLogMsg
	 */
	@Test
	public void testGetLogMsg() {
		String level = "level";
		String message = "message";
		LogEvent event = LogEvent.newBuilder().level(level).message(message).build();

		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		LogEventAdapter adapter = new LogEventAdapter(envDetail);

		StackifyError error = Mockito.mock(StackifyError.class);
		
		LogMsg logMsg = adapter.getLogMsg(event, Optional.of(error));
		Assert.assertEquals(message, logMsg.getMsg());
		Assert.assertEquals(error, logMsg.getEx());
		Assert.assertEquals(event.getTimestamp(), logMsg.getEpochMs().longValue());
		Assert.assertEquals(level, logMsg.getLevel());
	}
	
	/**
	 * testGetLogMsgWithoutLevel
	 */
	@Test
	public void testGetLogMsgWithoutLevel() {
		String message = "message";
		LogEvent event = LogEvent.newBuilder().message(message).build();

		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		LogEventAdapter adapter = new LogEventAdapter(envDetail);

		StackifyError error = Mockito.mock(StackifyError.class);
		
		LogMsg logMsg = adapter.getLogMsg(event, Optional.of(error));
		Assert.assertEquals(message, logMsg.getMsg());
		Assert.assertEquals(error, logMsg.getEx());
		Assert.assertEquals(event.getTimestamp(), logMsg.getEpochMs().longValue());
		Assert.assertNull(logMsg.getLevel());
	}
}
