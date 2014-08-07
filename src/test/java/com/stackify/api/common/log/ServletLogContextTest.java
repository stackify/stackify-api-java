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
		
		Assert.assertFalse(ServletLogContext.getTransactionId().isPresent());
		
		ServletLogContext.clear();
		
		String id = UUID.randomUUID().toString();
		ServletLogContext.putTransactionId(id);
		
		Assert.assertEquals(id, ServletLogContext.getTransactionId().get());
		
		ServletLogContext.clear();

		Assert.assertFalse(ServletLogContext.getTransactionId().isPresent());
	}
	
	/**
	 * testUser
	 */
	@Test
	public void testUser() {
		
		Assert.assertFalse(ServletLogContext.getUser().isPresent());
		
		ServletLogContext.clear();
		
		String user = UUID.randomUUID().toString();
		ServletLogContext.putUser(user);
		
		Assert.assertEquals(user, ServletLogContext.getUser().get());
		
		ServletLogContext.clear();

		Assert.assertFalse(ServletLogContext.getUser().isPresent());
	}
	
	/**
	 * testWebRequest
	 */
	@Test
	public void testWebRequest() {
		
		Assert.assertFalse(ServletLogContext.getWebRequest().isPresent());
		
		ServletLogContext.clear();
		
		WebRequestDetail wrd = WebRequestDetail.newBuilder().build();
		ServletLogContext.putWebRequest(wrd);
		
		Assert.assertNotNull(ServletLogContext.getWebRequest().get());
		
		ServletLogContext.clear();

		Assert.assertFalse(ServletLogContext.getWebRequest().isPresent());
	}
}
