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
package com.stackify.api.common.util;

import org.junit.Test;

/**
 * Preconditions JUnit Test
 * @author Eric Martin
 */
public class PreconditionsTest {

	/**
	 * testCheckNotNull
	 */
	@Test
	public void testCheckNotNull() {
		Preconditions.checkNotNull("x");
	}
	
	/**
	 * testCheckNotNullWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testCheckNotNullWithNull() {
		Preconditions.checkNotNull(null);
	}
	
	/**
	 * testCheckArgument
	 */
	@Test
	public void testCheckArgument() {
		Preconditions.checkArgument(true);
	}
	
	/**
	 * testCheckArgumentFalse
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCheckArgumentFalse() {
		Preconditions.checkArgument(false);
	}
}
