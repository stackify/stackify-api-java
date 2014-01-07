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
package com.stackify.api.common.error;

import org.junit.Assert;
import org.junit.Test;

import com.stackify.api.ErrorItem;
import com.stackify.api.StackifyError;

/**
 * ErrorGovernorTest JUnit Test
 * 
 * @author Eric Martin
 */
public class ErrorGovernorTest {

	/**
	 * testErrorShouldBeSentWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testErrorShouldBeSentWithNull() {
		ErrorGovernor governor = new ErrorGovernor();
		governor.errorShouldBeSent(null);
	}
	
	/**
	 * testErrorShouldBeSent
	 */
	@Test
	public void testErrorShouldBeSent() {
		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.errorType("errorType");
		builder.errorTypeCode("errorTypeCode");
		builder.sourceMethod("sourceMethod");
		ErrorItem errorItem = builder.build();
		StackifyError error = StackifyError.newBuilder().error(errorItem).build();

		ErrorGovernor governor = new ErrorGovernor();
		boolean shouldBeSent = governor.errorShouldBeSent(error);
		
		Assert.assertEquals(true, shouldBeSent);
	}
}
