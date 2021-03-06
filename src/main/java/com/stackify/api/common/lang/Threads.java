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
package com.stackify.api.common.lang;

import java.util.concurrent.TimeUnit;

/**
 * Threads
 * @author Eric Martin
 */
public class Threads {
	
	/**
	 * Sleeps and eats any exceptions
	 * @param sleepFor Sleep for value
	 * @param unit Unit of measure
	 */
	public static void sleepQuietly(final long sleepFor, final TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(sleepFor));
		} catch (Throwable t) {
			// do nothing
		}
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private Threads() {
	}
}
