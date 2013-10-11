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
	 * @param t The Throwable to be converted
	 * @return The ErrorItem
	 */
	public static ErrorItem toErrorItem(final Throwable t) {
		
		// get a flat list of the throwable and the causal chain
		
		List<Throwable> throwables = Throwables.getCausalChain(t);

		// create and populate builders for all throwables
		
		List<ErrorItem.Builder> builders = new ArrayList<ErrorItem.Builder>(throwables.size());
		
		for (Throwable throwable : throwables) {
			ErrorItem.Builder builder = toErrorItemBuilderWithoutCause(throwable);
			builders.add(builder);
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
	 * Converts a Throwable to an ErrorItem.Builder and ignores the cause
	 * @param t The Throwable to be converted
	 * @return The ErrorItem.Builder without the innerError populated
	 */
	private static ErrorItem.Builder toErrorItemBuilderWithoutCause(final Throwable t) {
		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.message(t.getMessage());
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
	 * Hidden to prevent construction
	 */
	private Throwables() {
	}
}
