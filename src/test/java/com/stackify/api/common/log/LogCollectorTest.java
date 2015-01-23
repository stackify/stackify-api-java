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

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import com.stackify.api.AppIdentity;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.AppIdentityService;
import com.stackify.api.common.http.HttpException;

/**
 * LogCollector JUnit Test
 * @author Eric Martin
 */
public class LogCollectorTest {

	/**
	 * testFlushEmpty
	 * @throws HttpException 
	 * @throws IOException 
	 */
	@Test
	public void testFlushEmpty() throws IOException, HttpException {
		LogSender sender = Mockito.mock(LogSender.class);
		AppIdentityService appIdentityService = Mockito.mock(AppIdentityService.class);
		LogCollector collector = new LogCollector("logger", Mockito.mock(EnvironmentDetail.class), appIdentityService);
		
		collector.flush(sender);
		
		Mockito.verifyZeroInteractions(sender);
		Mockito.verifyZeroInteractions(appIdentityService);
	}

	/**
	 * testAddAndFlushWithoutAppIdentity
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void testAddAndFlushWithoutAppIdentity() throws IOException, HttpException {
		LogSender sender = Mockito.mock(LogSender.class);
		Mockito.when(sender.send(Mockito.any(LogMsgGroup.class))).thenReturn(200);

		AppIdentityService appIdentityService = Mockito.mock(AppIdentityService.class);
		Mockito.when(appIdentityService.getAppIdentity()).thenReturn(null);
		
		LogCollector collector = new LogCollector("logger", Mockito.mock(EnvironmentDetail.class), appIdentityService);
		
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		
		collector.flush(sender);
		
		Mockito.verify(appIdentityService).getAppIdentity();
		Mockito.verify(sender).send(Mockito.any(LogMsgGroup.class));
	}
	
	/**
	 * testAddAndFlushWithAppIdentity
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void testAddAndFlushWithAppIdentity() throws IOException, HttpException {
		LogSender sender = Mockito.mock(LogSender.class);
		Mockito.when(sender.send(Mockito.any(LogMsgGroup.class))).thenReturn(200);

		AppIdentityService appIdentityService = Mockito.mock(AppIdentityService.class);
		Mockito.when(appIdentityService.getAppIdentity()).thenReturn(Mockito.mock(AppIdentity.class));

		LogCollector collector = new LogCollector("logger", Mockito.mock(EnvironmentDetail.class), appIdentityService);
		
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		collector.addLogMsg(Mockito.mock(LogMsg.class));
		
		collector.flush(sender);
		
		Mockito.verify(appIdentityService).getAppIdentity();
		Mockito.verify(sender).send(Mockito.any(LogMsgGroup.class));
	}
}
