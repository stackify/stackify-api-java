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
package com.stackify.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.stackify.api.ErrorItem;
import com.stackify.api.TraceFrame;

/**
 * ErrorItem JUnit Test
 *
 * @author Eric Martin
 */
public class ErrorItemTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String message = "message";
		String className = "className";
		String methodName = "methodName";
		List<TraceFrame> stackTrace = new ArrayList<TraceFrame>();
		ErrorItem cause = ErrorItem.newBuilder().build();
		String errorTypeCode = "errorTypeCode";

		Map<String, String> data = new HashMap<String, String>();
		data.put("key1", "value1");
		data.put("key2", "value2");

		ErrorItem.Builder builder = ErrorItem.newBuilder();
		builder.message(message);
		builder.errorType(className);
		builder.sourceMethod(methodName);
		builder.stackTrace(stackTrace);
		builder.innerError(cause);
		builder.errorTypeCode(errorTypeCode);
		builder.data(data);
		ErrorItem errorDetail = builder.build();

		Assert.assertNotNull(errorDetail);

		Assert.assertEquals(message, errorDetail.getMessage());
		Assert.assertEquals(className, errorDetail.getErrorType());
		Assert.assertEquals(methodName, errorDetail.getSourceMethod());
		Assert.assertEquals(stackTrace, errorDetail.getStackTrace());
		Assert.assertEquals(cause, errorDetail.getInnerError());
		Assert.assertEquals(errorTypeCode, errorDetail.getErrorTypeCode());
		Assert.assertEquals(data, errorDetail.getData());
	}
}
