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

import java.util.ArrayList;
import java.util.List;

import com.stackify.api.ErrorItem;
import com.stackify.api.TraceFrame;

/**
 * Utility class for converting a Throwable object to an ErrorItem object
 * 
 * @author Eric Martin
 */
public class Throwables {

	/**
	 * Returns the Throwable's cause chain as a list. The first entry is the Throwable followed by the cause chain.
	 * @param throwable The Throwable
	 * @return The Throwable and its cause chain
	 */
	public static List<Throwable> getCausalChain(final Throwable throwable) {
		if (throwable == null) {
			throw new NullPointerException("Throwable is null");
		}
		
		List<Throwable> causes = new ArrayList<Throwable>();
		causes.add(throwable);
		
		Throwable cause = throwable.getCause();
		
		while ((cause != null) && (!causes.contains(cause))) {
			causes.add(cause);
			cause = cause.getCause();
		}
		
		return causes;
	}
	
	/**
	 * Converts a Throwable to an ErrorItem
	 * @param logMessage The log message (can be null)
	 * @param t The Throwable to be converted
	 * @return The ErrorItem
	 */
	public static ErrorItem toErrorItem(final String logMessage, final Throwable t) {
		
		// get a flat list of the throwable and the causal chain
		
		List<Throwable> throwables = Throwables.getCausalChain(t);

		// create and populate builders for all throwables
		
		List<ErrorItem.Builder> builders = new ArrayList<ErrorItem.Builder>(throwables.size());
		
		for (int i = 0; i < throwables.size(); ++i) {
			if (i == 0) {
				ErrorItem.Builder builder = toErrorItemBuilderWithoutCause(logMessage, throwables.get(i));
				builders.add(builder);
			} else {
				ErrorItem.Builder builder = toErrorItemBuilderWithoutCause(null, throwables.get(i));
				builders.add(builder);
			}
		}
		
		// attach child errors to their parent in reverse order
		
		for (int i = builders.size() - 1; 0 < i; --i) {
			ErrorItem.Builder parent = builders.get(i - 1);
			ErrorItem.Builder child = builders.get(i);
			
			parent.innerError(child.build());
		}
		
		// return the assembled original error
		
		return builders.get(0).build();
	}
	
	/**
	 * Converts a Throwable to an ErrorItem
	 * @param t The Throwable to be converted
	 * @return The ErrorItem
	 */
	public static ErrorItem toErrorItem(final Throwable t) {
		return toErrorItem(null, t);
	}
	
	/**
	 * Converts a Throwable to an ErrorItem.Builder and ignores the cause
	 * @param logMessage The log message
	 * @param t The Throwable to be converted
	 * @return The ErrorItem.Builder without the innerError populated
	 */
	private static ErrorItem.Builder toErrorItemBuilderWithoutCause(final String logMessage, final Throwable t) {
		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.message(toErrorItemMessage(logMessage, t.getMessage()));
		builder.errorType(t.getClass().getCanonicalName());
		
		List<TraceFrame> stackFrames = new ArrayList<TraceFrame>();
		
		StackTraceElement[] stackTrace = t.getStackTrace();
		
		if ((stackTrace != null) && (0 < stackTrace.length)) {
			StackTraceElement firstFrame = stackTrace[0];
			builder.sourceMethod(firstFrame.getClassName() + "." + firstFrame.getMethodName());
			
			for (int i = 0; i < stackTrace.length; ++i) {
				TraceFrame stackFrame = StackTraceElements.toTraceFrame(stackTrace[i]);
				stackFrames.add(stackFrame);
			}
		}
		
		builder.stackTrace(stackFrames);
		
		return builder;
	}
	
	/**
	 * Constructs the error item message from the log message and the throwable's message
	 * @param logMessage The log message (can be null)
	 * @param throwableMessage The throwable's message (can be null)
	 * @return The error item message
	 */
	private static String toErrorItemMessage(final String logMessage, final String throwableMessage) {
		
		StringBuilder sb = new StringBuilder();
				
		if ((throwableMessage != null) && (!throwableMessage.isEmpty())) {
			sb.append(throwableMessage);
			
			if ((logMessage != null) && (!logMessage.isEmpty())) {
				sb.append(" (");
				sb.append(logMessage);
				sb.append(")");
			}			
		} else {
			sb.append(logMessage);
		}

		return sb.toString();
	}
	
	/**
	 * Create an error item from a simple log message (without an explicit exception)
	 * @param logMessage The log message
	 * @param className The class that logged the message
	 * @param methodName The method that logged the message
	 * @param lineNumber The line number that logged the message
	 * @return The error item
	 */
	public static ErrorItem toErrorItem(final String logMessage, final String className, final String methodName, final int lineNumber) {
		
		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.message(logMessage);
		builder.errorType("StringException");
		builder.sourceMethod(className + "." + methodName);
		
		List<TraceFrame> stackFrames = new ArrayList<TraceFrame>();

		StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();

		int start = 0;
		
		if ((className != null) && (methodName != null)) {
			for (int i = 0; i < stackTrace.length; ++i) {
				StackTraceElement ste = (stackTrace[i]);
				
				if (className.equals(ste.getClassName()) && 
				    methodName.equals(ste.getMethodName()) && 
				    lineNumber == ste.getLineNumber()) {
					start = i;
					break;
				}
			}
		}
		
		for (int i = start; i < stackTrace.length; ++i) {
			TraceFrame stackFrame = StackTraceElements.toTraceFrame(stackTrace[i]);
			stackFrames.add(stackFrame);
		}

		builder.stackTrace(stackFrames);
		
		return builder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private Throwables() {
	}
}
