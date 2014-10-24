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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.log.LogAppender;

/**
 * LogManagerTest
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LogManager.class})
public class LogManagerTest {

	/**
	 * testGetAppenderAndShutdown
	 * @throws Exception 
	 */
	@Test
	public void testGetAppenderAndShutdown() throws Exception {		
		LogAppender<LogEvent> mockAppender = Mockito.mock(LogAppender.class);
		PowerMockito.whenNew(LogAppender.class).withAnyArguments().thenReturn(mockAppender);

		LogAppender<LogEvent> appender = LogManager.getAppender();
		Mockito.verify(mockAppender).activate(Mockito.any(ApiConfiguration.class));
		
		LogManager.shutdown();		
		Mockito.verify(mockAppender).close();
	}
}
