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
package com.stackify.api.common.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.common.ApiConfiguration;

/**
 * HttpClient JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClient.class, URL.class})
public class HttpClientTest {

	/**
	 * testPost
	 * @throws Exception 
	 */
	@Test
	public void testPost() throws Exception {
		
		URL url = PowerMockito.mock(URL.class);
		PowerMockito.whenNew(URL.class).withArguments(Mockito.anyString()).thenReturn(url);
		
		HttpURLConnection connection = PowerMockito.mock(HttpURLConnection.class);
		
		PowerMockito.when(url.openConnection(Proxy.NO_PROXY)).thenReturn(connection);
		
		ByteArrayOutputStream postBody = new ByteArrayOutputStream();
		PowerMockito.when(connection.getOutputStream()).thenReturn(postBody);
		
		ByteArrayInputStream contents = new ByteArrayInputStream("world".getBytes());
		PowerMockito.when(connection.getInputStream()).thenReturn(contents);
		PowerMockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

		HttpClient httpClient = new HttpClient(Mockito.mock(ApiConfiguration.class));
		
		String world = httpClient.post("path", "hello".getBytes());
		
		Assert.assertNotNull(world);
		Assert.assertEquals("world", world);
	}

	/**
	 * testPostWithHttpError
	 * @throws Exception 
	 */
	@Test(expected = HttpException.class)
	public void testPostWithHttpError() throws Exception {
		
		URL url = PowerMockito.mock(URL.class);
		PowerMockito.whenNew(URL.class).withArguments(Mockito.anyString()).thenReturn(url);
		
		HttpURLConnection connection = PowerMockito.mock(HttpURLConnection.class);
		
		PowerMockito.when(url.openConnection(Proxy.NO_PROXY)).thenReturn(connection);
		
		ByteArrayOutputStream postBody = new ByteArrayOutputStream();
		PowerMockito.when(connection.getOutputStream()).thenReturn(postBody);
		
		ByteArrayInputStream contents = new ByteArrayInputStream("world".getBytes());
		PowerMockito.when(connection.getInputStream()).thenReturn(contents);
		PowerMockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

		HttpClient httpClient = new HttpClient(Mockito.mock(ApiConfiguration.class));
		
		httpClient.post("path", "hello".getBytes());
	}
	
	/**
	 * testPostWithGzip
	 * @throws Exception 
	 */
	@Test
	public void testPostWithGzip() throws Exception {
		
		URL url = PowerMockito.mock(URL.class);
		PowerMockito.whenNew(URL.class).withArguments(Mockito.anyString()).thenReturn(url);
		
		HttpURLConnection connection = PowerMockito.mock(HttpURLConnection.class);
		
		PowerMockito.when(url.openConnection(Proxy.NO_PROXY)).thenReturn(connection);
		
		ByteArrayOutputStream postBody = new ByteArrayOutputStream();
		PowerMockito.when(connection.getOutputStream()).thenReturn(postBody);
		
		ByteArrayInputStream contents = new ByteArrayInputStream("world".getBytes());
		PowerMockito.when(connection.getInputStream()).thenReturn(contents);
		PowerMockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

		HttpClient httpClient = new HttpClient(Mockito.mock(ApiConfiguration.class));
		
		String world = httpClient.post("path", "hello".getBytes(), true);
		
		Assert.assertNotNull(world);
		Assert.assertEquals("world", world);
	}
}
