/*
 * Copyright 2015 Stackify
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

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * HttpResendQueue JUnit Test
 * @author Eric Martin
 */
public class HttpResendQueueTest {

	/**
	 * testOfferIOException
	 */
	@Test
	public void testOfferIOException() {
		HttpResendQueue resendQueue = new HttpResendQueue(3);
		Assert.assertEquals(0, resendQueue.size());

		resendQueue.offer(new byte[]{}, new IOException());
		Assert.assertEquals(1, resendQueue.size());
	}
	
	/**
	 * testOfferHttpException
	 */
	@Test
	public void testOfferHttpException() {
		HttpResendQueue resendQueue = new HttpResendQueue(3);
		Assert.assertEquals(0, resendQueue.size());

		resendQueue.offer(new byte[]{}, new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR));
		Assert.assertEquals(1, resendQueue.size());
		
		resendQueue.offer(new byte[]{}, new HttpException(HttpURLConnection.HTTP_UNAUTHORIZED));
		Assert.assertEquals(1, resendQueue.size());
	}
	
	/**
	 * testDrain
	 * @throws Exception 
	 */
	@Test
	public void testDrain() throws Exception {
		byte[] request1 = new byte[]{1};
		byte[] request2 = new byte[]{2};
		
		HttpResendQueue resendQueue = new HttpResendQueue(3);
		resendQueue.offer(request1, new IOException());
		resendQueue.offer(request2, new IOException());
		
		Assert.assertEquals(2, resendQueue.size());
		
		HttpClient httpClient = Mockito.mock(HttpClient.class);
		
		resendQueue.drain(httpClient, "/path");

		Assert.assertEquals(0, resendQueue.size());
		
		Mockito.verify(httpClient).post("/path", request1, false);
		Mockito.verify(httpClient).post("/path", request2, false);
	}
	
	/**
	 * testDrainWithException
	 * @throws Exception 
	 */
	@Test
	public void testDrainWithException() throws Exception {
		byte[] request1 = new byte[]{1};
		byte[] request2 = new byte[]{2};
		
		HttpResendQueue resendQueue = new HttpResendQueue(3);
		resendQueue.offer(request1, new IOException());
		resendQueue.offer(request2, new IOException());
		
		Assert.assertEquals(2, resendQueue.size());
		
		HttpClient httpClient = Mockito.mock(HttpClient.class);
		Mockito.when(httpClient.post("/path", request1, false)).thenReturn("");
		Mockito.when(httpClient.post("/path", request2, false)).thenThrow(new RuntimeException()).thenReturn("");
		
		resendQueue.drain(httpClient, "/path");

		Assert.assertEquals(1, resendQueue.size());
		
		Mockito.verify(httpClient).post("/path", request1, false);
		Mockito.verify(httpClient).post("/path", request2, false);
		
		resendQueue.drain(httpClient, "/path");

		Mockito.verify(httpClient, Mockito.times(2)).post("/path", request2, false);

		Assert.assertEquals(0, resendQueue.size());
	}
}
