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

import org.junit.Assert;
import org.junit.Test;

/**
 * HttpResendQueueItem JUnit Test
 * @author Eric Martin
 */
public class HttpResendQueueItemTest {

	/**
	 * testConstrcutorAndGetters
	 */
	@Test
	public void testConstrcutorAndGetters() {
		byte[] jsonBytes = "{\"method\": \"testConstrcutorAndGetters\"}".getBytes();
		
		HttpResendQueueItem item = new HttpResendQueueItem(jsonBytes);
		Assert.assertNotNull(item);
		
		Assert.assertEquals(jsonBytes, item.getJsonBytes());
		Assert.assertEquals(1, item.getNumFailures());
	}
	
	/**
	 * testIncrementFailures
	 */
	@Test
	public void testIncrementFailures() {
		byte[] jsonBytes = "{\"method\": \"testIncrementRetries\"}".getBytes();
		
		HttpResendQueueItem item = new HttpResendQueueItem(jsonBytes);
		Assert.assertEquals(1, item.getNumFailures());
		
		item.failed();
		Assert.assertEquals(2, item.getNumFailures());
		
		item.failed();
		Assert.assertEquals(3, item.getNumFailures());
	}
}
