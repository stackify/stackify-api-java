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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.mask.Masker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * LogSender JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LogSender.class, HttpClient.class})
public class LogSenderTest {
	
	/**
	 * testSend
	 * @throws Exception
	 */
	@Test
	public void testSend() throws Exception {
		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
		Masker masker = Mockito.mock(Masker.class);
		Mockito.when(objectMapper.writer()).thenReturn(Mockito.mock(ObjectWriter.class));

		ApiConfiguration apiConfig = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key").build();

		LogSender sender = new LogSender(apiConfig, objectMapper, masker, true);

		HttpClient httpClient = PowerMockito.mock(HttpClient.class);
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(httpClient);
		PowerMockito.when(httpClient.post(Mockito.anyString(), (byte[]) Mockito.any())).thenReturn("");

		sender.send(Mockito.mock(LogMsgGroup.class));
	}
}
