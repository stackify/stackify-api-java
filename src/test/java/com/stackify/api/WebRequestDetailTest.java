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
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * WebRequestDetail JUnit Test
 *
 * @author Eric Martin
 */
public class WebRequestDetailTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String userIpAddress = "userIpAddress";
		String httpMethod = "httpMethod";
		String requestProtocol = "requestProtocol";
		String requestUrl = "requestUrl";
		String requestUrlRoot = "requestUrlRoot";
		String referralUrl = "referralUrl";
		Map<String, String> headers = Collections.singletonMap("headersKey", "headersValue");
		Map<String, String> cookies = Collections.singletonMap("cookiesKey", "cookiesValue");
		Map<String, String> queryString = Collections.singletonMap("queryStringKey", "queryStringValue");
		Map<String, String> postData = Collections.singletonMap("postDataKey", "postDataValue");
		Map<String, String> sessionData = Collections.singletonMap("sessionDataKey", "sessionDataValue");
		String postDataRaw = "postDataRaw";
		String mvcAction = "mvcAction";
		String mvcController = "mvcController";
		String mvcArea = "mvcArea";

		WebRequestDetail.Builder builder = WebRequestDetail.newBuilder();
		builder.userIpAddress(userIpAddress);
		builder.httpMethod(httpMethod);
		builder.requestProtocol(requestProtocol);
		builder.requestUrl(requestUrl);
		builder.requestUrlRoot(requestUrlRoot);
		builder.referralUrl(referralUrl);
		builder.headers(headers);
		builder.cookies(cookies);
		builder.queryString(queryString);
		builder.postData(postData);
		builder.sessionData(sessionData);
		builder.postDataRaw(postDataRaw);
		builder.mvcAction(mvcAction);
		builder.mvcController(mvcController);
		builder.mvcArea(mvcArea);
		WebRequestDetail webRequest = builder.build();

		Assert.assertNotNull(webRequest);

		Assert.assertEquals(userIpAddress, webRequest.getUserIpAddress());
		Assert.assertEquals(httpMethod, webRequest.getHttpMethod());
		Assert.assertEquals(requestProtocol, webRequest.getRequestProtocol());
		Assert.assertEquals(requestUrl, webRequest.getRequestUrl());
		Assert.assertEquals(requestUrlRoot, webRequest.getRequestUrlRoot());
		Assert.assertEquals(referralUrl, webRequest.getReferralUrl());
		Assert.assertEquals(headers, webRequest.getHeaders());
		Assert.assertEquals(cookies, webRequest.getCookies());
		Assert.assertEquals(queryString, webRequest.getQueryString());
		Assert.assertEquals(postData, webRequest.getPostData());
		Assert.assertEquals(sessionData, webRequest.getSessionData());
		Assert.assertEquals(postDataRaw, webRequest.getPostDataRaw());
		Assert.assertEquals(mvcAction, webRequest.getMvcAction());
		Assert.assertEquals(mvcController, webRequest.getMvcController());
		Assert.assertEquals(mvcArea, webRequest.getMvcArea());
		
		WebRequestDetail webRequestCopy = webRequest.toBuilder().build();
		
		Assert.assertNotNull(webRequestCopy);

		Assert.assertEquals(userIpAddress, webRequestCopy.getUserIpAddress());
		Assert.assertEquals(httpMethod, webRequestCopy.getHttpMethod());
		Assert.assertEquals(requestProtocol, webRequestCopy.getRequestProtocol());
		Assert.assertEquals(requestUrl, webRequestCopy.getRequestUrl());
		Assert.assertEquals(requestUrlRoot, webRequestCopy.getRequestUrlRoot());
		Assert.assertEquals(referralUrl, webRequestCopy.getReferralUrl());
		Assert.assertEquals(headers, webRequestCopy.getHeaders());
		Assert.assertEquals(cookies, webRequestCopy.getCookies());
		Assert.assertEquals(queryString, webRequestCopy.getQueryString());
		Assert.assertEquals(postData, webRequestCopy.getPostData());
		Assert.assertEquals(sessionData, webRequestCopy.getSessionData());
		Assert.assertEquals(postDataRaw, webRequestCopy.getPostDataRaw());
		Assert.assertEquals(mvcAction, webRequestCopy.getMvcAction());
		Assert.assertEquals(mvcController, webRequestCopy.getMvcController());
		Assert.assertEquals(mvcArea, webRequestCopy.getMvcArea());
	}
}
