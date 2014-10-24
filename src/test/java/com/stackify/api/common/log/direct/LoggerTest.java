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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.common.log.LogAppender;

/**
 * LoggerTest
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LogManager.class})
public class LoggerTest {

	/**
	 * testQueueMessage
	 */
	@Test
	public void testQueueMessage() {
		LogAppender<LogEvent> appender = Mockito.mock(LogAppender.class);
		PowerMockito.mockStatic(LogManager.class);
		PowerMockito.when(LogManager.getAppender()).thenReturn(appender);

		String level = "level";
		String message = "message";
		Logger.queueMessage(level, message);
		
		ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
		Mockito.verify(appender).append(eventCaptor.capture());
		
		Assert.assertEquals(level, eventCaptor.getValue().getLevel());
		Assert.assertEquals(message, eventCaptor.getValue().getMessage());
		Assert.assertNull(eventCaptor.getValue().getException());
	}
	
	/**
	 * testQueueException
	 */
	@Test
	public void testQueueException() {
		LogAppender<LogEvent> appender = Mockito.mock(LogAppender.class);
		PowerMockito.mockStatic(LogManager.class);
		PowerMockito.when(LogManager.getAppender()).thenReturn(appender);

		String message = "message";
		Throwable exception = new NullPointerException(message);

		Logger.queueException(exception);
		
		ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
		Mockito.verify(appender).append(eventCaptor.capture());
		
		Assert.assertEquals("ERROR", eventCaptor.getValue().getLevel());
		Assert.assertEquals(message, eventCaptor.getValue().getMessage());
		Assert.assertEquals(exception, eventCaptor.getValue().getException());
	}
	
	/**
	 * testQueueMessageException
	 */
	@Test
	public void testQueueMessageException() {
		LogAppender<LogEvent> appender = Mockito.mock(LogAppender.class);
		PowerMockito.mockStatic(LogManager.class);
		PowerMockito.when(LogManager.getAppender()).thenReturn(appender);

		String level = "level";
		String message = "message";
		Throwable exception = new NullPointerException();

		Logger.queueException(level, message, exception);
		
		ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
		Mockito.verify(appender).append(eventCaptor.capture());
		
		Assert.assertEquals(level, eventCaptor.getValue().getLevel());
		Assert.assertEquals(message, eventCaptor.getValue().getMessage());
		Assert.assertEquals(exception, eventCaptor.getValue().getException());
	}
}
