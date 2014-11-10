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
package com.stackify.api.common.log;

import com.google.common.base.Optional;
import com.stackify.api.LogMsg;
import com.stackify.api.StackifyError;

/**
 * EventAdapter
 * @author Eric Martin
 */
public interface EventAdapter<T> {

	/**
	 * Gets the Throwable (optional) from the logging event
	 * @param event The logging event
	 * @return The Throwable (optional)
	 */
	Optional<Throwable> getThrowable(final T event);

	/**
	 * Builds a StackifyError from the logging event
	 * @param event The logging event
	 * @param exception The exception
	 * @return The StackifyError
	 */
	StackifyError getStackifyError(final T event, final Throwable exception);

	/**
	 * Builds a LogMsg from the logging event
	 * @param event The logging event
	 * @param error The exception (optional)
	 * @return The LogMsg
	 */
	LogMsg getLogMsg(final T event, final Optional<StackifyError> error);	
	
	/**
	 * Returns true if the event was logged at an error level
	 * @param event The logging event
	 * @return True if the event was logged at error level
	 */
	boolean isErrorLevel(final T event);
}
