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
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * AsyncSchedulerTest JUnit Test
 * 
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AsyncScheduler.class, System.class})
public class AsyncSchedulerTest {

	/**
	 * testUpdateOk
	 */
	@Test
	public void testNoUpdate() {
		AsyncScheduler scheduler = new AsyncScheduler();
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}
	
	/**
	 * testUpdateOk
	 */
	@Test
	public void testUpdateOk() {
		AsyncScheduler scheduler = new AsyncScheduler();

		scheduler.update(HttpTransmissionStatus.OK);
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}
	
	/**
	 * testUpdateUnauthorized
	 */
	@Test
	public void testUpdateUnauthorized() {
		AsyncScheduler scheduler = new AsyncScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(HttpTransmissionStatus.UNAUTHORIZED);
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(300, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}
	
	/**
	 * testUpdateFirstError
	 */
	@Test
	public void testUpdateFirstError() {
		AsyncScheduler scheduler = new AsyncScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(HttpTransmissionStatus.ERROR);
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}
	
	/**
	 * testUpdateNextError
	 */
	@Test
	public void testUpdateNextError() {
		AsyncScheduler scheduler = new AsyncScheduler();		
		
		long beforeFirstUpdate = System.currentTimeMillis();
		scheduler.update(HttpTransmissionStatus.ERROR);
		long afterFirstUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeFirstUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterFirstUpdate);
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());

		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.currentTimeMillis()).thenReturn(beforeFirstUpdate + 15000);
				
		scheduler.update(HttpTransmissionStatus.ERROR);
		
		Assert.assertTrue(beforeFirstUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterFirstUpdate);
		Assert.assertEquals(15, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}

	/**
	 * testUpdateErrorAndClear
	 */
	@Test
	public void testUpdateErrorAndClear() {
		AsyncScheduler scheduler = new AsyncScheduler();		
		
		long beforeUpdate = System.currentTimeMillis();
		scheduler.update(HttpTransmissionStatus.ERROR);
		long afterUpdate = System.currentTimeMillis();

		Assert.assertTrue(beforeUpdate <= scheduler.getLastHttpError());
		Assert.assertTrue(scheduler.getLastHttpError() <= afterUpdate);
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
		
		scheduler.update(HttpTransmissionStatus.OK);
		
		Assert.assertEquals(0, scheduler.getLastHttpError());
		Assert.assertEquals(1, scheduler.getScheduleDelay());
		Assert.assertNotNull(scheduler.getNextSchedule());
	}
}
