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
package com.stackify.api.common.log.direct;

import org.junit.Assert;
import org.junit.Test;

/**
 * LogEventTest
 * @author Eric Martin
 */
public class LogEventTest {

	/**
	 * testBuidler
	 */
	@Test
	public void testBuidler() {
		String level = "level";
		String message = "message";
		Throwable exception = new NullPointerException();
		
		LogEvent.Builder builder = LogEvent.newBuilder();
		builder.level(level);
		builder.message(message);
		builder.exception(exception);
		
		long before = System.currentTimeMillis();
		
		LogEvent event = builder.build();
		
		long after = System.currentTimeMillis();
		
		Assert.assertEquals(level, event.getLevel());
		Assert.assertEquals(message, event.getMessage());
		Assert.assertEquals(exception, event.getException());
		
		Assert.assertTrue(before <= event.getTimestamp());
		Assert.assertTrue(event.getTimestamp() <= after);
		
		Assert.assertNotNull(event.toString());
	}
	
	/**
	 * testBuidlerWithLocation
	 */
	@Test
	public void testBuidlerWithLocation() {
		String className = "className";
		String methodName = "methodName";
		int lineNumber = 14;
		
		LogEvent.Builder builder = LogEvent.newBuilder();
		builder.className(className);
		builder.methodName(methodName);
		builder.lineNumber(lineNumber);
		LogEvent event = builder.build();
		
		Assert.assertEquals(className, event.getClassName());
		Assert.assertEquals(methodName, event.getMethodName());
		Assert.assertEquals(lineNumber, event.getLineNumber());
	}
}
