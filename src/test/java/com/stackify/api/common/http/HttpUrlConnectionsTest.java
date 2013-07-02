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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * HttpUrlConnections JUnit Test
 * 
 * @author Eric Martin
 */
public class HttpUrlConnectionsTest {

	/**
	 * testReadAndCloseInputStreamsWithNullConnection
	 */
	@Test
	public void testReadAndCloseInputStreamsWithNullConnection() {
		HttpUrlConnections.readAndCloseInputStreams(null);
	}
	
	/**
	 * testReadAndCloseInputStreams
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStreams() throws IOException {
		
		InputStream inputStream = Mockito.mock(InputStream.class);
		InputStream errorStream = Mockito.mock(InputStream.class);
		
		HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		Mockito.when(connection.getInputStream()).thenReturn(inputStream);
		Mockito.when(connection.getErrorStream()).thenReturn(errorStream);
		
		HttpUrlConnections.readAndCloseInputStreams(connection);
		
		Mockito.verify(inputStream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(inputStream).close();

		Mockito.verify(errorStream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(errorStream).close();
	}
	
	/**
	 * testReadAndCloseInputStreamsGetInputStreamThrowsException
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStreamsGetInputStreamThrowsException() throws IOException {
		
		InputStream errorStream = Mockito.mock(InputStream.class);
		
		HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		Mockito.when(connection.getInputStream()).thenThrow(new RuntimeException());
		Mockito.when(connection.getErrorStream()).thenReturn(errorStream);
		
		HttpUrlConnections.readAndCloseInputStreams(connection);
		
		Mockito.verify(errorStream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(errorStream).close();
	}
	
	/**
	 * testReadAndCloseInputStreamsGetErrorStreamThrowsException
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStreamsGetErrorStreamThrowsException() throws IOException {
		
		InputStream inputStream = Mockito.mock(InputStream.class);
		
		HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		Mockito.when(connection.getInputStream()).thenReturn(inputStream);
		Mockito.when(connection.getErrorStream()).thenThrow(new RuntimeException());
		
		HttpUrlConnections.readAndCloseInputStreams(connection);
		
		Mockito.verify(inputStream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(inputStream).close();
	}
	
	/**
	 * testReadAndCloseInputStreamWithNullStream
	 */
	@Test
	public void testReadAndCloseInputStreamWithNullStream() {
		HttpUrlConnections.readAndCloseInputStream(null);
	}
	
	/**
	 * testReadAndCloseInputStream
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStream() throws IOException {
		ByteArrayInputStream stream = new ByteArrayInputStream("line\n".getBytes());
		ByteArrayInputStream streamSpy = Mockito.spy(stream);
		
		HttpUrlConnections.readAndCloseInputStream(streamSpy);
		
		Mockito.verify(streamSpy, Mockito.atLeastOnce()).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(streamSpy).close();
		Assert.assertEquals(0, streamSpy.available());		
	}
	
	/**
	 * testReadAndCloseInputStreamWithIOException
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStreamWithIOException() throws IOException {
		InputStream stream = Mockito.mock(InputStream.class);
		Mockito.when(stream.read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new IOException());
		
		HttpUrlConnections.readAndCloseInputStream(stream);

		Mockito.verify(stream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(stream).close();
	}
	
	/**
	 * testReadAndCloseInputStreamWithCloseException
	 * @throws IOException 
	 */
	@Test
	public void testReadAndCloseInputStreamWithCloseException() throws IOException {
		InputStream stream = Mockito.mock(InputStream.class);
		Mockito.doThrow(new IOException()).when(stream).close();
		
		HttpUrlConnections.readAndCloseInputStream(stream);
		
		Mockito.verify(stream).read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(stream).close();
	}
}
