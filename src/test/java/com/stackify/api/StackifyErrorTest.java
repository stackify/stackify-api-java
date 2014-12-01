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

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * StackifyError JUnit Test
 *
 * @author Eric Martin
 */
public class StackifyErrorTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		EnvironmentDetail environment = EnvironmentDetail.newBuilder().build();
		Date timeStamp = new Date();
		ErrorItem errorDetail = ErrorItem.newBuilder().build();
		WebRequestDetail webRequestDetail = WebRequestDetail.newBuilder().build();
		Map<String, String> serverVariables = Collections.singletonMap("serverKey", "serverValue");
		String customerName = "customerName";
		String userName = "userName";

		StackifyError.Builder builder = StackifyError.newBuilder();
		builder.environmentDetail(environment);
		builder.occurredEpochMillis(timeStamp);
		builder.error(errorDetail);
		builder.webRequestDetail(webRequestDetail);
		builder.serverVariables(serverVariables);
		builder.customerName(customerName);
		builder.userName(userName);
		StackifyError stackifyError = builder.build();

		Assert.assertNotNull(stackifyError);

		Assert.assertEquals(environment, stackifyError.getEnvironmentDetail());
		Assert.assertEquals(timeStamp, stackifyError.getOccurredEpochMillis());
		Assert.assertEquals(errorDetail, stackifyError.getError());
		Assert.assertEquals(webRequestDetail, stackifyError.getWebRequestDetail());
		Assert.assertEquals(serverVariables, stackifyError.getServerVariables());
		Assert.assertEquals(customerName, stackifyError.getCustomerName());
		Assert.assertEquals(userName, stackifyError.getUserName());
	}
}
