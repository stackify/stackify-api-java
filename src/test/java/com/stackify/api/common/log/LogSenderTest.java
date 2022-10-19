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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.mask.Masker;
import com.stackify.api.common.socket.HttpSocketClient;

import org.apache.http.client.methods.HttpPost;
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
@PrepareForTest({LogTransportDirect.class, LogTransportAgentSocket.class, HttpClient.class})
public class LogSenderTest {

	/**
	 * testSend
	 * @throws Exception
	 */
	@Test
	public void testSend() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Masker masker = Mockito.mock(Masker.class);
		LogMsgGroup logMsgGroup = new LogMsgGroup();
		LogMsg logMsg = new LogMsg();
		logMsg.setMsg("test message");

		List<LogMsg> logMsgList = new ArrayList<LogMsg>();
		logMsgList.add(logMsg);

		logMsgGroup.setAppName("AppName Test");
		logMsgGroup.setMsgs(
			logMsgList
		);

		// Mockito.when(objectMapper.writer()).thenReturn(Mockito.mock(ObjectWriter.class));

		ApiConfiguration apiConfig = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key").build();

		LogTransportDirect sender = new LogTransportDirect(apiConfig, objectMapper, masker, true);

		HttpClient httpClient = PowerMockito.mock(HttpClient.class);
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(httpClient);
		PowerMockito.when(httpClient.post(Mockito.anyString(), (byte[]) Mockito.any())).thenReturn("");

		sender.send(logMsgGroup);
	}

	/**
	 * testSend
	 * @throws Exception
	 */
	@Test
	public void testSendSocket() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Masker masker = Mockito.mock(Masker.class);
		LogMsgGroup logMsgGroup = new LogMsgGroup();
		LogMsg logMsg = new LogMsg();
		logMsg.setMsg("test message");

		List<LogMsg> logMsgList = new ArrayList<LogMsg>();
		logMsgList.add(logMsg);

		logMsgGroup.setAppName("AppName Test");
		logMsgGroup.setMsgs(
			logMsgList
		);

		ApiConfiguration apiConfig = ApiConfiguration.newBuilder().apiUrl("url").apiKey("key").build();

		HttpSocketClient httpSocketClient = PowerMockito.spy(new HttpSocketClient("testSocket"));
		PowerMockito.whenNew(HttpSocketClient.class).withAnyArguments().thenReturn(httpSocketClient);
		PowerMockito.doNothing().when(httpSocketClient).send(Mockito.any(HttpPost.class));

		LogTransportAgentSocket sender = new LogTransportAgentSocket(apiConfig, masker, true);
		sender.send(logMsgGroup);
	}
}
