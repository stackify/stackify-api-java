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

import com.stackify.api.TraceFrame;

/**
 * Utility class for converting a StackTraceElement object to an TraceFrame object
 * 
 * @author Eric Martin
 */
public class StackTraceElements {

	/**
	 * Converts a StackTraceElement to a TraceFrame
	 * @param element The StackTraceElement to be converted
	 * @return The TraceFrame
	 */
	public static TraceFrame toTraceFrame(final StackTraceElement element) {
		TraceFrame.Builder builder = TraceFrame.newBuilder();
		builder.codeFileName(element.getFileName());
		
		if (0 < element.getLineNumber()) {
			builder.lineNum(element.getLineNumber());
		}
		
		builder.method(element.getClassName() + "." + element.getMethodName());
		
		return builder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private StackTraceElements() {
	}
}
