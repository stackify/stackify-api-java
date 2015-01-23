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
package com.stackify.api.common.log;

import java.net.HttpURLConnection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.common.http.HttpException;

/**
 * LogBackgroundServiceScheduler JUnit Test
 * 
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LogBackgroundServiceScheduler.class, System.class})
public class LogBackgroundServiceSchedulerTest {

	/**
	 * testUpdateOk
	 */
	@Test
	public void testNoUpdate() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1000, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateOk
	 */
	@Test
	public void testUpdateOk() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();

		scheduler.update(50);
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1000, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateOkButLow
	 */
	@Test
	public void testUpdateOkButLow() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();

		scheduler.update(5);
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1250, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateUnauthorized
	 */
	@Test
	public void testUpdateUnauthorized() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(new HttpException(HttpURLConnection.HTTP_UNAUTHORIZED));
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(300000, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateFirstError
	 */
	@Test
	public void testUpdateFirstError() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR));
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(1000, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateNextError
	 */
	@Test
	public void testUpdateNextError() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();		
		
		long beforeFirstUpdate = System.currentTimeMillis();
		scheduler.update(new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR));
		long afterFirstUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeFirstUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterFirstUpdate);
		Assert.assertEquals(1000, scheduler.getScheduleDelay());

		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.currentTimeMillis()).thenReturn(scheduler.getLastHttpError() + 15000);
				
		scheduler.update(new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR));
		
		Assert.assertTrue(beforeFirstUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterFirstUpdate);
		Assert.assertEquals(15000, scheduler.getScheduleDelay());
	}

	/**
	 * testUpdateErrorAndClear
	 */
	@Test
	public void testUpdateErrorAndClear() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(new HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR));
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(1000, scheduler.getScheduleDelay());
		
		scheduler.update(10);
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1000, scheduler.getScheduleDelay());
	}
	
	/**
	 * testUpdateWithIncreaseAndDecrease
	 */
	@Test
	public void testUpdateWithIncreaseAndDecrease() {
		LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();
		Assert.assertEquals(1000, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(1250, scheduler.getScheduleDelay());
		
		scheduler.update(2);
		Assert.assertEquals(1563, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(1954, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(2443, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(3054, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(3818, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(4773, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(5000, scheduler.getScheduleDelay());

		scheduler.update(2);
		Assert.assertEquals(5000, scheduler.getScheduleDelay());

		scheduler.update(1000);
		Assert.assertEquals(2500, scheduler.getScheduleDelay());

		scheduler.update(1000);
		Assert.assertEquals(1250, scheduler.getScheduleDelay());

		scheduler.update(1000);
		Assert.assertEquals(1000, scheduler.getScheduleDelay());

		scheduler.update(1000);
		Assert.assertEquals(1000, scheduler.getScheduleDelay());	
	}
}
