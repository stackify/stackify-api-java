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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.stackify.api.StackifyError;

/**
 * AsyncErrorSender JUnit Test
 * 
 * @author Eric Martin
 */
public class AsyncErrorSenderTest {

	/**
	 * testConstructorWithNullStackifyErrorSender
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullStackifyErrorSender() {
		new AsyncErrorSender(null, 5);
	}
	
	/**
	 * testConstructorWithBadQueueSize
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithBadQueueSize() {
		new AsyncErrorSender(Mockito.mock(StackifyErrorSender.class), 0);
	}
	
	/**
	 * testSendError
	 */
	@Test
	public void testSendError() {
		StackifyErrorSender sender = Mockito.mock(StackifyErrorSender.class);
		AsyncErrorSender asyncSender = new AsyncErrorSender(sender, 10);
		
		Assert.assertEquals(0, asyncSender.getQueueSize());
		
		asyncSender.sendError(Mockito.mock(StackifyError.class));
		
		Assert.assertEquals(1, asyncSender.getQueueSize());
	}
	
	/**
	 * testFlush
	 * @throws IOException 
	 */
	@Test
	public void testFlush() throws IOException {
		StackifyErrorSender sender = Mockito.mock(StackifyErrorSender.class);
		AsyncErrorSender asyncSender = new AsyncErrorSender(sender, 10);
		
		Mockito.when(sender.send(Mockito.anyList())).thenReturn(200);
		
		asyncSender.sendError(Mockito.mock(StackifyError.class));
		
		Assert.assertEquals(1, asyncSender.getQueueSize());
		
		HttpTransmissionStatus status = asyncSender.flush();
		
		Assert.assertEquals(HttpTransmissionStatus.OK, status);
		Mockito.verify(sender).send(Mockito.anyList());
		Assert.assertEquals(0, asyncSender.getQueueSize());
	}

	/**
	 * testFlushWithHttpError
	 * @throws IOException 
	 */
	@Test
	public void testFlushWithHttpError() throws IOException {
		StackifyErrorSender sender = Mockito.mock(StackifyErrorSender.class);
		AsyncErrorSender asyncSender = new AsyncErrorSender(sender, 10);
		
		Mockito.when(sender.send(Mockito.anyList())).thenReturn(500);
		
		asyncSender.sendError(Mockito.mock(StackifyError.class));
		
		Assert.assertEquals(1, asyncSender.getQueueSize());
		
		HttpTransmissionStatus status = asyncSender.flush();
		
		Assert.assertEquals(HttpTransmissionStatus.ERROR, status);
		Mockito.verify(sender).send(Mockito.anyList());
		Assert.assertEquals(0, asyncSender.getQueueSize());
	}

	/**
	 * testFlushWithHttpUnauthorized
	 * @throws IOException 
	 */
	@Test
	public void testFlushWithHttpUnauthorized() throws IOException {
		StackifyErrorSender sender = Mockito.mock(StackifyErrorSender.class);
		AsyncErrorSender asyncSender = new AsyncErrorSender(sender, 10);
		
		Mockito.when(sender.send(Mockito.anyList())).thenReturn(401);
		
		asyncSender.sendError(Mockito.mock(StackifyError.class));
		
		Assert.assertEquals(1, asyncSender.getQueueSize());
		
		HttpTransmissionStatus status = asyncSender.flush();
		
		Assert.assertEquals(HttpTransmissionStatus.UNAUTHORIZED, status);
		Mockito.verify(sender).send(Mockito.anyList());
		Assert.assertEquals(0, asyncSender.getQueueSize());
	}
	
	/**
	 * testFlushWithException
	 * @throws IOException 
	 */
	@Test
	public void testFlushWithException() throws IOException {
		StackifyErrorSender sender = Mockito.mock(StackifyErrorSender.class);
		AsyncErrorSender asyncSender = new AsyncErrorSender(sender, 10);
		
		Mockito.when(sender.send(Mockito.anyList())).thenThrow(new IOException());
		
		asyncSender.sendError(Mockito.mock(StackifyError.class));
		
		Assert.assertEquals(1, asyncSender.getQueueSize());
		
		HttpTransmissionStatus status = asyncSender.flush();
		
		Assert.assertEquals(HttpTransmissionStatus.ERROR, status);
		Mockito.verify(sender).send(Mockito.anyList());
		Assert.assertEquals(0, asyncSender.getQueueSize());
	}
}
