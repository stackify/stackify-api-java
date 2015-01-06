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
package com.stackify.api.common.concurrent;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.util.concurrent.Service.State;

/**
 * ShutdownListener JUnit Test
 * @author Eric Martin
 */
public class ShutdownListenerTest {

	/**
	 * testNoInteraction
	 */
	@Test
	public void testNoInteraction() {
		ScheduledExecutorService executor = Mockito.mock(ScheduledExecutorService.class);
		ShutdownListener listener = new ShutdownListener(executor);
		 
		listener.starting();
		listener.running();
		listener.stopping(State.RUNNING);
		
		Mockito.verifyZeroInteractions(executor);
	}
	
	/**
	 * testTerminated
	 */
	@Test
	public void testTerminated() {
		ScheduledExecutorService executor = Mockito.mock(ScheduledExecutorService.class);
		ShutdownListener listener = new ShutdownListener(executor);
		 
		listener.terminated(State.RUNNING);
		
		Mockito.verify(executor).shutdown();
	}
	
	/**
	 * testFailed
	 */
	@Test
	public void testFailed() {
		ScheduledExecutorService executor = Mockito.mock(ScheduledExecutorService.class);
		ShutdownListener listener = new ShutdownListener(executor);
		 
		listener.failed(State.RUNNING, new RuntimeException());
		
		Mockito.verify(executor).shutdown();
	}
}
