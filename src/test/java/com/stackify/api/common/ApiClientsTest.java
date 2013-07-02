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
package com.stackify.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.ApiClient;

/**
 * ApiClients JUnit Test
 * 
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApiClients.class, Properties.class})
public class ApiClientsTest {

	/**
	 * testGetApiClient
	 */
	@Test
	public void testGetApiClient() {
		ApiClient apiClient = ApiClients.getApiClient(ApiClients.class, "/stackify-api-common.properties");
		Assert.assertEquals("name", apiClient.getName());
		Assert.assertEquals("version", apiClient.getVersion());
		Assert.assertEquals("java", apiClient.getPlatform());
	}
	
	/**
	 * testGetApiClientWithoutProperties
	 */
	@Test
	public void testGetApiClientWithoutProperties() {
		ApiClient apiClient = ApiClients.getApiClient(ApiClients.class, "/does-not-exist.properties");
		Assert.assertNull(apiClient.getName());
		Assert.assertNull(apiClient.getVersion());
		Assert.assertEquals("java", apiClient.getPlatform());
	}
	
	/**
	 * testGetApiClientWithException
	 * @throws Exception 
	 */
	@Test
	public void testGetApiClientWithException() throws Exception {	
		Properties properties = PowerMockito.mock(Properties.class);
		PowerMockito.doThrow(new IOException()).when(properties).load(Mockito.any(InputStream.class));
		PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(properties);
		
		ApiClient apiClient = ApiClients.getApiClient(ApiClients.class, "/stackify-api-common.properties");
		Assert.assertNull(apiClient.getName());
		Assert.assertNull(apiClient.getVersion());
		Assert.assertEquals("java", apiClient.getPlatform());
	}
}
