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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * AsyncErrorSenderService JUnit Test
 * 
 * @author Eric Martin
 */
public class AsyncErrorSenderServiceTest {

	/**
	 * testConstructorWithNullSender
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullScheduler() {
		new AsyncErrorSenderService(null, Mockito.mock(AsyncErrorSender.class));
	}
	
	/**
	 * testConstructorWithNullSender
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullSender() {
		new AsyncErrorSenderService(Mockito.mock(AsyncScheduler.class), null);
	}
	
	/**
	 * testConstructor
	 */
	@Test
	public void testConstructor() {
		AsyncScheduler scheduler = Mockito.mock(AsyncScheduler.class);
		AsyncErrorSender sender = Mockito.mock(AsyncErrorSender.class);
		AsyncErrorSenderService service = new AsyncErrorSenderService(scheduler, sender);
		
		Assert.assertEquals("Stackify_AsyncErrorSenderService", service.serviceName());
		Assert.assertEquals(scheduler, service.scheduler());
	}
	
	/**
	 * testStartup
	 * @throws Exception 
	 */
	@Test
	public void testStartup() throws Exception {
		AsyncScheduler scheduler = Mockito.mock(AsyncScheduler.class);
		AsyncErrorSender sender = Mockito.mock(AsyncErrorSender.class);
		AsyncErrorSenderService service = new AsyncErrorSenderService(scheduler, sender);
		
		service.startUp();
		
		Mockito.verifyZeroInteractions(sender);
		Mockito.verifyZeroInteractions(scheduler);
	}
	
	/**
	 * testShutDown
	 * @throws Exception 
	 */
	@Test
	public void testShutDown() throws Exception {
		AsyncScheduler scheduler = Mockito.mock(AsyncScheduler.class);
		AsyncErrorSender sender = Mockito.mock(AsyncErrorSender.class);
		AsyncErrorSenderService service = new AsyncErrorSenderService(scheduler, sender);
		
		service.shutDown();
		
		Mockito.verify(sender).flush();
		Mockito.verifyZeroInteractions(scheduler);
	}
	
	/**
	 * testRunOneIteration
	 */
	@Test
	public void testRunOneIteration() {
		AsyncScheduler scheduler = Mockito.mock(AsyncScheduler.class);
		AsyncErrorSender sender = Mockito.mock(AsyncErrorSender.class);
		AsyncErrorSenderService service = new AsyncErrorSenderService(scheduler, sender);
		
		Mockito.when(sender.flush()).thenReturn(HttpTransmissionStatus.OK);
		
		service.runOneIteration();
		
		Mockito.verify(sender).flush();
		Mockito.verify(scheduler).update(HttpTransmissionStatus.OK);
	}
	
	/**
	 * testRunOneIterationWithException
	 */
	@Test
	public void testRunOneIterationWithException() {
		AsyncScheduler scheduler = Mockito.mock(AsyncScheduler.class);
		AsyncErrorSender sender = Mockito.mock(AsyncErrorSender.class);
		AsyncErrorSenderService service = new AsyncErrorSenderService(scheduler, sender);
		
		Mockito.when(sender.flush()).thenThrow(new RuntimeException());
		
		service.runOneIteration();
		
		Mockito.verify(sender).flush();
		Mockito.verify(scheduler).update(HttpTransmissionStatus.ERROR);
	}

}
