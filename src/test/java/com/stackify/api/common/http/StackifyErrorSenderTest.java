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
package com.stackify.api.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.StackifyError;
import com.stackify.api.json.StackifyErrorConverter;

/**
 * StackifyErrorSender JUnit Test
 * 
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StackifyErrorSender.class, URL.class})
public class StackifyErrorSenderTest {

	/**
	 * testConstructorWithNullConverter
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullConverter() {
		new StackifyErrorSender(null);
	}
	
	/**
	 * testConstructor
	 */
	@Test
	public void testConstructor() {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		new StackifyErrorSender(converter);
	}
	
	/**
	 * testSendApiUrlNull
	 * @throws IOException 
	 */
	@Test(expected = NullPointerException.class)
	public void testSendApiUrlNull() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send(null, "apiKey", Collections.singletonList(StackifyError.newBuilder().build()));
	}
	
	/**
	 * testSendApiUrlEmpty
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSendApiUrlEmpty() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send("", "apiKey", Collections.singletonList(StackifyError.newBuilder().build()));
	}
	
	/**
	 * testSendApiKeyNull
	 * @throws IOException 
	 */
	@Test(expected = NullPointerException.class)
	public void testSendApiKeyNull() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send("apiUrl", null, Collections.singletonList(StackifyError.newBuilder().build()));
	}
	
	/**
	 * testSendApiKeyEmpty
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSendApiKeyEmpty() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send("apiUrl", "", Collections.singletonList(StackifyError.newBuilder().build()));
	}
	
	/**
	 * testSendErrorsNull
	 * @throws IOException 
	 */
	@Test(expected = NullPointerException.class)
	public void testSendErrorsNull() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send("apiUrl", "apiKey", null);
	}
	
	/**
	 * testSendErrorsEmpty
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSendErrorsEmpty() throws IOException {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		sender.send("apiUrl", "apiKey", new ArrayList<StackifyError>());
	}
	
	/**
	 * testSend
	 * @throws Exception
	 */
	@Test
	public void testSend() throws Exception {
		StackifyErrorConverter converter = Mockito.mock(StackifyErrorConverter.class);
		StackifyErrorSender sender = new StackifyErrorSender(converter);
		
		URL url = PowerMockito.mock(URL.class);
		HttpURLConnection connection = PowerMockito.mock(HttpURLConnection.class);
		ByteArrayOutputStream postBody = new ByteArrayOutputStream();
		
		PowerMockito.whenNew(URL.class).withArguments(Mockito.anyString()).thenReturn(url);
		PowerMockito.when(url.openConnection()).thenReturn(connection);
		PowerMockito.when(connection.getOutputStream()).thenReturn(postBody);
		
		sender.send("apiUrl", "apiKey", Collections.singletonList(StackifyError.newBuilder().build()));
		
		Mockito.verify(converter).writeToStream(Mockito.anyList(), Mockito.any(OutputStream.class));
		Mockito.verify(connection).getResponseCode();
	}
}
