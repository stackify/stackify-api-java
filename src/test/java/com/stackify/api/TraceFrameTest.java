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
package com.stackify.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * TraceFrame JUnit Test
 *
 * @author Eric Martin
 */
public class TraceFrameTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String fileName = "fileName";
		Integer lineNumber = Integer.valueOf(14);
		String methodName = "methodName";

		TraceFrame.Builder builder = TraceFrame.newBuilder();
		builder.codeFileName(fileName);
		builder.lineNum(lineNumber);
		builder.method(methodName);
		TraceFrame stackFrame = builder.build();

		Assert.assertNotNull(stackFrame);

		Assert.assertEquals(fileName, stackFrame.getCodeFileName());
		Assert.assertEquals(lineNumber, stackFrame.getLineNum());
		Assert.assertEquals(methodName, stackFrame.getMethod());
	}
}
