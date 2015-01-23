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

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.stackify.api.WebRequestDetail;

/**
 * ServletLogContextTest
 * @author Eric Martin
 */
public class ServletLogContextTest {

	/**
	 * testTransactionId
	 */
	@Test
	public void testTransactionId() {
		
		Assert.assertNull(ServletLogContext.getTransactionId());
		
		ServletLogContext.clear();
		
		String id = UUID.randomUUID().toString();
		ServletLogContext.putTransactionId(id);
		
		Assert.assertEquals(id, ServletLogContext.getTransactionId());
		
		ServletLogContext.clear();

		Assert.assertNull(ServletLogContext.getTransactionId());
	}
	
	/**
	 * testUser
	 */
	@Test
	public void testUser() {
		
		Assert.assertNull(ServletLogContext.getUser());
		
		ServletLogContext.clear();
		
		String user = UUID.randomUUID().toString();
		ServletLogContext.putUser(user);
		
		Assert.assertEquals(user, ServletLogContext.getUser());
		
		ServletLogContext.clear();

		Assert.assertNull(ServletLogContext.getUser());
	}
	
	/**
	 * testWebRequest
	 */
	@Test
	public void testWebRequest() {
		
		Assert.assertNull(ServletLogContext.getWebRequest());
		
		ServletLogContext.clear();
		
		WebRequestDetail wrd = WebRequestDetail.newBuilder().build();
		ServletLogContext.putWebRequest(wrd);
		
		Assert.assertNotNull(ServletLogContext.getWebRequest());
		
		ServletLogContext.clear();

		Assert.assertNull(ServletLogContext.getWebRequest());
	}
}
