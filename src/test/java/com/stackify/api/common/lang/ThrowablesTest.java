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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.stackify.api.ErrorItem;
import com.stackify.api.TraceFrame;

/**
 * Throwables JUnit Test
 * 
 * @author Eric Martin
 */
public class ThrowablesTest {

	/**
	 * testGetCausalChainWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testGetCausalChainWithNull() {
		Throwables.getCausalChain(null);
	}
	
	/**
	 * testGetCausalChainWithoutCause
	 */
	@Test
	public void testGetCausalChainWithoutCause() {
		Throwable t = new NullPointerException();
		
		List<Throwable> throwables = Throwables.getCausalChain(t);
		Assert.assertNotNull(throwables);
		Assert.assertEquals(1, throwables.size());
		Assert.assertEquals(t, throwables.get(0));
	}
	
	/**
	 * testGetCausalChainWithCause
	 */
	@Test
	public void testGetCausalChainWithCause() {
		Throwable c = new NullPointerException();
		Throwable t = new RuntimeException(c);
		
		List<Throwable> throwables = Throwables.getCausalChain(t);
		Assert.assertNotNull(throwables);
		Assert.assertEquals(2, throwables.size());
		Assert.assertEquals(t, throwables.get(0));
		Assert.assertEquals(c, throwables.get(1));
	}
	
	/**
	 * testGetCausalChainWithSelfCausation
	 */
	@Test
	public void testGetCausalChainWithSelfCausation() {
		Throwable c2 = new RuntimeException();
		Throwable c1 = new RuntimeException(c2);
		c2.initCause(c1);
		Throwable t = new RuntimeException(c1);
		
		List<Throwable> throwables = Throwables.getCausalChain(t);
		Assert.assertNotNull(throwables);
		Assert.assertEquals(3, throwables.size());
		Assert.assertEquals(t, throwables.get(0));
		Assert.assertEquals(c1, throwables.get(1));
		Assert.assertEquals(c2, throwables.get(2));
	}
	
	/**
	 * testToErrorDetail
	 */
	@Test
	public void testToErrorDetail() {
		String message = "message";
		String causeMessage = "causeMessage";
		Throwable cause = new NullPointerException(causeMessage);
		Throwable throwable = new RuntimeException(message, cause);
		
		ErrorItem errorDetail = Throwables.toErrorItem(throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertEquals("java.lang.RuntimeException", errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(!stackTrace.isEmpty());
	
		Assert.assertEquals(causeMessage, errorDetail.getInnerError().getMessage());
		Assert.assertEquals("java.lang.NullPointerException", errorDetail.getInnerError().getErrorType());

		Assert.assertNull(errorDetail.getInnerError().getInnerError());
	}
	
	/**
	 * testToErrorDetailWithLogMessage
	 */
	@Test
	public void testToErrorDetailWithLogMessage() {
		String throwableMessage = "Throwable message";
		String logMessage = "Log message";
		String causeMessage = "causeMessage";
		Throwable cause = new NullPointerException(causeMessage);
		Throwable throwable = new RuntimeException(throwableMessage, cause);
		
		ErrorItem errorDetail = Throwables.toErrorItem(logMessage, throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(throwableMessage + " (" + logMessage + ")", errorDetail.getMessage());
		Assert.assertEquals("java.lang.RuntimeException", errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(!stackTrace.isEmpty());
	
		Assert.assertEquals(causeMessage, errorDetail.getInnerError().getMessage());
		Assert.assertEquals("java.lang.NullPointerException", errorDetail.getInnerError().getErrorType());

		Assert.assertNull(errorDetail.getInnerError().getInnerError());
	}
	
	/**
	 * testToErrorDetailWithNullStackTrace
	 */
	@Test
	public void testToErrorDetailWithNullStackTrace() {
		String message = "message";
		Throwable throwable = Mockito.mock(Throwable.class);
		Mockito.when(throwable.getMessage()).thenReturn(message);
		Mockito.when(throwable.getStackTrace()).thenReturn(null);
		
		ErrorItem errorDetail = Throwables.toErrorItem(throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertNotNull(errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(stackTrace.isEmpty());
		Assert.assertNull(errorDetail.getInnerError());
	}
	
	/**
	 * testToErrorDetailWithEmptyStackTrace
	 */
	@Test
	public void testToErrorDetailWithEmptyStackTrace() {
		String message = "message";
		Throwable throwable = Mockito.mock(Throwable.class);
		Mockito.when(throwable.getMessage()).thenReturn(message);
		Mockito.when(throwable.getStackTrace()).thenReturn(new StackTraceElement[]{});
		
		ErrorItem errorDetail = Throwables.toErrorItem(throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertNotNull(errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(stackTrace.isEmpty());
		Assert.assertNull(errorDetail.getInnerError());
	}
	
	/**
	 * testToErrorDetailWithRecursiveCause
	 */
	@Test
	public void testToErrorDetailWithRecursiveCause() {
		String message = "message";
		Throwable throwable = Mockito.mock(Throwable.class);
		Mockito.when(throwable.getMessage()).thenReturn(message);
		Mockito.when(throwable.getCause()).thenReturn(throwable);
		
		ErrorItem errorDetail = Throwables.toErrorItem(throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertNotNull(errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(stackTrace.isEmpty());
		Assert.assertNull(errorDetail.getInnerError());
	}
	
	/**
	 * testToErrorDetailWithSubcause
	 */
	@Test
	public void testToErrorDetailWithSubcause() {
		String message = "message";
		String causeMessage = "causeMessage";
		String subcauseMessage = "subcauseMessage";
		Throwable subcause = new NullPointerException(subcauseMessage);
		Throwable cause = new UnsupportedOperationException(causeMessage, subcause);
		Throwable throwable = new RuntimeException(message, cause);
		
		ErrorItem errorDetail = Throwables.toErrorItem(throwable);
		
		Assert.assertNotNull(errorDetail);
		
		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertEquals("java.lang.RuntimeException", errorDetail.getErrorType());
		
		List<TraceFrame> stackTrace = errorDetail.getStackTrace();
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(!stackTrace.isEmpty());
	
		Assert.assertEquals(causeMessage, errorDetail.getInnerError().getMessage());
		Assert.assertEquals("java.lang.UnsupportedOperationException", errorDetail.getInnerError().getErrorType());

		Assert.assertEquals(subcauseMessage, errorDetail.getInnerError().getInnerError().getMessage());
		Assert.assertEquals("java.lang.NullPointerException", errorDetail.getInnerError().getInnerError().getErrorType());

		Assert.assertNull(errorDetail.getInnerError().getInnerError().getInnerError());
	}
	
	/**
	 * testToErrorItemWithStringException
	 */
	@Test
	public void testToErrorItemWithStringException() {
		String logMessage = "message";
		String className = "com.stackify.api.common.lang.ThrowablesTest";
		String methodName = "testToErrorItemWithStringException";
		int lineNumber = 254;
		
		ErrorItem errorItem = Throwables.toErrorItem(logMessage, className, methodName, lineNumber);

		Assert.assertNotNull(errorItem);
		
		Assert.assertEquals(logMessage, errorItem.getMessage());
		Assert.assertEquals("StringException", errorItem.getErrorType());
		Assert.assertEquals(className + "." + methodName, errorItem.getSourceMethod());
		
		List<TraceFrame> stackTrace = errorItem.getStackTrace();
		
		Assert.assertNotNull(stackTrace);
		Assert.assertTrue(0 < stackTrace.size());
		
		TraceFrame topFrame = stackTrace.get(0);
		Assert.assertNotNull(topFrame);
		
		Assert.assertEquals("ThrowablesTest.java", topFrame.getCodeFileName());
		Assert.assertEquals(className + "." + methodName, topFrame.getMethod());
		Assert.assertEquals(Integer.valueOf(lineNumber), topFrame.getLineNum());
	}
}
