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
package com.stackify.api.common.lang;

import org.junit.Assert;
import org.junit.Test;

import com.stackify.api.TraceFrame;
import com.stackify.api.common.lang.StackTraceElements;

/**
 * StackTraceElements JUnit Test
 * 
 * @author Eric Martin
 */
public class StackTraceElementsTest {

	/**
	 * testToStackFrame
	 */
	@Test
	public void testToStackFrame() {
		String declaringClass = StackTraceElementsTest.class.getCanonicalName();
		String methodName = "methodName";
		String fileName = "fileName";
		int lineNumber = 14;
		StackTraceElement element = new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
		
		TraceFrame frame = StackTraceElements.toTraceFrame(element);
		
		Assert.assertNotNull(frame);
		
		Assert.assertEquals(declaringClass + "." + methodName, frame.getMethod());
		Assert.assertEquals(fileName, frame.getCodeFileName());
		Assert.assertEquals(Integer.valueOf(lineNumber), frame.getLineNum());
		Assert.assertNotNull(frame.getLibraryName());		
	}
}
