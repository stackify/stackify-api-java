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
package com.stackify.api.common.lang;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Threads JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Threads.class})
public class ThreadsTest {

	/**
	 * testSleepQuietly
	 * @throws InterruptedException 
	 */
	@Test
	public void testSleepQuietly() throws InterruptedException {
		PowerMockito.mockStatic(Thread.class);
		PowerMockito.doNothing().when(Thread.class);
		Threads.sleepQuietly(10, TimeUnit.SECONDS);
		Mockito.verify(Threads.class);
	}
	
	/**
	 * testSleepQuietlyWithException
	 */
	@Test
	public void testSleepQuietlyWithException() {
		PowerMockito.mockStatic(Thread.class);
		PowerMockito.doThrow(new InterruptedException()).when(Thread.class);
		Threads.sleepQuietly(10, TimeUnit.SECONDS);
		Mockito.verify(Threads.class);
	}
}
