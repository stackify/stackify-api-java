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
package com.stackify.api.common;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.stackify.api.EnvironmentDetail;

/**
 * ApiConfiguration JUnit Test
 * @author Eric Martin
 */
public class ApiConfigurationTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String apiUrl = "url";
		String apiKey = "key";
		String application = "app";
		String environment = "env";
		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		
		ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();
		builder.apiUrl(apiUrl);
		builder.apiKey(apiKey);
		builder.application(application);
		builder.environment(environment);
		builder.envDetail(envDetail);
		
		ApiConfiguration apiConfig = builder.build();
		
		Assert.assertNotNull(apiConfig);
		Assert.assertEquals(apiUrl, apiConfig.getApiUrl());
		Assert.assertEquals(apiKey, apiConfig.getApiKey());
		Assert.assertEquals(application, apiConfig.getApplication());
		Assert.assertEquals(environment, apiConfig.getEnvironment());
		Assert.assertEquals(envDetail, apiConfig.getEnvDetail());
		
		Assert.assertNotNull(apiConfig.toString());
	}
	
	/**
	 * testBuilderWithDefaultUrl
	 */
	@Test
	public void testBuilderWithDefaultUrl() {
		Assert.assertEquals("https://api.stackify.com", ApiConfiguration.newBuilder().build().getApiUrl());
	}
	
	/**
	 * testToBuilder
	 */
	@Test
	public void testToBuilder() {
		String apiUrl = "url";
		String apiKey = "key";
		String application = "app";
		String environment = "env";
		EnvironmentDetail envDetail = Mockito.mock(EnvironmentDetail.class);
		
		ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();
		builder.apiUrl(apiUrl);
		builder.apiKey(apiKey);
		builder.application(application);
		builder.environment(environment);
		builder.envDetail(envDetail);
		
		ApiConfiguration apiConfig = builder.build();
		
		ApiConfiguration.Builder toBuilder = apiConfig.toBuilder();
		
		Assert.assertNotNull(toBuilder);
		
		ApiConfiguration toApiConfig = toBuilder.build();
		
		Assert.assertNotNull(toApiConfig);
		Assert.assertEquals(apiUrl, toApiConfig.getApiUrl());
		Assert.assertEquals(apiKey, toApiConfig.getApiKey());
		Assert.assertEquals(application, toApiConfig.getApplication());
		Assert.assertEquals(environment, toApiConfig.getEnvironment());
		Assert.assertEquals(envDetail, toApiConfig.getEnvDetail());
	}
}
