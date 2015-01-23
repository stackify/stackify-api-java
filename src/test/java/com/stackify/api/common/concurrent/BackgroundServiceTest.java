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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * BackgroundService JUnit Test
 * @author Eric Martin
 */
public class BackgroundServiceTest {

	/**
	 * testStart
	 */
	@Test
	public void testLifecycle() {
		TestBackgroundService service = Mockito.spy(new TestBackgroundService());
		
		Assert.assertFalse(service.isRunning());
		
		service.start();
		
		Assert.assertTrue(service.isRunning());
		
		Mockito.verify(service).startUp();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		
		Mockito.verify(service).runOneIteration();
		Mockito.verify(service).getNextScheduleDelayMilliseconds();
		
		service.stop();

		Mockito.verify(service).shutDown();

		Assert.assertFalse(service.isRunning());
	}
	
	/**
	 * TestBackgroundService
	 */
	private static class TestBackgroundService extends BackgroundService {

		/**
		 * @see com.stackify.api.common.concurrent.BackgroundService#startUp()
		 */
		@Override
		protected void startUp() {
		}

		/**
		 * @see com.stackify.api.common.concurrent.BackgroundService#runOneIteration()
		 */
		@Override
		protected void runOneIteration() {
		}

		/**
		 * @see com.stackify.api.common.concurrent.BackgroundService#getNextScheduleDelayMilliseconds()
		 */
		@Override
		protected long getNextScheduleDelayMilliseconds() {
			return 1000;
		}

		/**
		 * @see com.stackify.api.common.concurrent.BackgroundService#shutDown()
		 */
		@Override
		protected void shutDown() {
		}
	}
}
