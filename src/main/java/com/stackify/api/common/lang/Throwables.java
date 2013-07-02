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
	 * Converts a Throwable to an ErrorItem
	 * @param t The Throwable to be converted
	 * @return The ErrorItem
	 */
	public static ErrorItem toErrorItem(final Throwable t) {
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

		Throwable cause = t.getCause();
		
		if (cause != null) {
			if (!cause.equals(t)) {
				builder.innerError(Throwables.toErrorItem(cause));
			}
		}

		return builder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private Throwables() {
	}
}
