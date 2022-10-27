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

import com.stackify.api.EnvironmentDetail;
import com.stackify.api.LogMsg;
import com.stackify.api.StackifyError;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.error.ErrorGovernor;
import com.stackify.api.common.mask.Masker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * LogAppender JUnit Test
 *
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LogAppender.class, LogBackgroundService.class })
public class LogAppenderTest {

	/**
	 * testActivate
	 *
	 * @throws Exception
	 */
	@Test
	public void testActivate() throws Exception {
		EventAdapter<?> adapter = Mockito.mock(EventAdapter.class);
		LogAppender<?> appender = new LogAppender("logger", adapter, new Masker());

		LogCollector collector = Mockito.mock(LogCollector.class);
		PowerMockito.whenNew(LogCollector.class).withAnyArguments().thenReturn(collector);

		LogBackgroundService background = PowerMockito.mock(LogBackgroundService.class);
		PowerMockito.whenNew(LogBackgroundService.class).withAnyArguments().thenReturn(background);

		ApiConfiguration config = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key")
				.envDetail(Mockito.mock(EnvironmentDetail.class)).build();

		appender.activate(config);

		Mockito.verify(background).start();
	}

	/**
	 * testClose
	 *
	 * @throws Exception
	 */
	@Test
	public void testClose() throws Exception {
		EventAdapter<?> adapter = Mockito.mock(EventAdapter.class);
		LogAppender<?> appender = new LogAppender("logger", adapter, new Masker());

		LogCollector collector = Mockito.mock(LogCollector.class);
		PowerMockito.whenNew(LogCollector.class).withAnyArguments().thenReturn(collector);

		LogBackgroundService background = PowerMockito.mock(LogBackgroundService.class);
		PowerMockito.whenNew(LogBackgroundService.class).withAnyArguments().thenReturn(background);

		ApiConfiguration config = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key")
				.envDetail(Mockito.mock(EnvironmentDetail.class)).build();

		appender.activate(config);

		appender.close();

		Mockito.verify(background).stop();
	}

	/**
	 * testAppend
	 *
	 * @throws Exception
	 */
	@Test
	public void testAppend() throws Exception {
		String event = "log event";
		Throwable t = new NullPointerException();
		StackifyError error = Mockito.mock(StackifyError.class);
		LogMsg logMsg = Mockito.mock(LogMsg.class);

		EventAdapter<String> adapter = Mockito.mock(EventAdapter.class);
		Mockito.when(adapter.getThrowable(event)).thenReturn(t);
		Mockito.when(adapter.getStackifyError(event, t)).thenReturn(error);
		Mockito.when(adapter.getLogMsg(event, error)).thenReturn(logMsg);

		ErrorGovernor governor = Mockito.mock(ErrorGovernor.class);
		Mockito.when(governor.errorShouldBeSent(Mockito.any(StackifyError.class))).thenReturn(true);
		PowerMockito.whenNew(ErrorGovernor.class).withAnyArguments().thenReturn(governor);

		LogAppender<String> appender = new LogAppender<String>("logger", adapter, new Masker());

		LogCollector collector = Mockito.mock(LogCollector.class);
		PowerMockito.whenNew(LogCollector.class).withAnyArguments().thenReturn(collector);

		LogBackgroundService background = PowerMockito.mock(LogBackgroundService.class);
		PowerMockito.whenNew(LogBackgroundService.class).withAnyArguments().thenReturn(background);

		ApiConfiguration config = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key")
				.envDetail(Mockito.mock(EnvironmentDetail.class)).build();

		appender.activate(config);

		Mockito.when(background.isRunning()).thenReturn(true);

		appender.append(event);

		appender.close();

		Mockito.verify(collector).addLogMsg(logMsg);
	}

	/**
	 * testAppendWithoutActivate
	 *
	 * @throws Exception
	 */
	@Test
	public void testAppendWithoutActivate() throws Exception {
		String event = "log event";

		EventAdapter<String> adapter = Mockito.mock(EventAdapter.class);

		LogAppender<String> appender = new LogAppender<String>("logger", adapter, new Masker());

		LogCollector collector = Mockito.mock(LogCollector.class);
		PowerMockito.whenNew(LogCollector.class).withAnyArguments().thenReturn(collector);

		appender.append(event);

		appender.close();

		Mockito.verifyZeroInteractions(collector);
	}

	/**
	 * testAppendInternalEvent
	 *
	 * @throws Exception
	 */
	@Test
	public void testAppendInternalEvent() throws Exception {
		String event = "log event";

		EventAdapter<String> adapter = Mockito.mock(EventAdapter.class);
		Mockito.when(adapter.getClassName(event)).thenReturn("com.stackify.api.common.log.LogBackgroundService");

		ErrorGovernor governor = Mockito.mock(ErrorGovernor.class);
		Mockito.when(governor.errorShouldBeSent(Mockito.any(StackifyError.class))).thenReturn(true);
		PowerMockito.whenNew(ErrorGovernor.class).withAnyArguments().thenReturn(governor);

		LogAppender<String> appender = new LogAppender<String>("logger", adapter, new Masker());

		LogCollector collector = Mockito.mock(LogCollector.class);
		PowerMockito.whenNew(LogCollector.class).withAnyArguments().thenReturn(collector);

		LogBackgroundService background = PowerMockito.mock(LogBackgroundService.class);
		PowerMockito.whenNew(LogBackgroundService.class).withAnyArguments().thenReturn(background);

		ApiConfiguration config = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key")
				.envDetail(Mockito.mock(EnvironmentDetail.class)).build();

		appender.activate(config);

		Mockito.when(background.isRunning()).thenReturn(true);

		appender.append(event);

		appender.close();

		Mockito.verifyZeroInteractions(collector);
	}
}
