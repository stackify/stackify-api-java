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

/**
 * ApiConfigurations JUnit Test
 * @author Eric Martin
 */
public class ApiConfigurationsTest {

	/**
	 * fromPropertiesWithOverrides
	 */
	@Test
	public void fromPropertiesWithOverrides() {
		String apiUrl = "urlOverride";
		String apiKey = "keyOverride";
		String application = "appOverride";
		String environment = "envOverride";
				
		ApiConfiguration apiConfig = ApiConfigurations.fromPropertiesWithOverrides(apiUrl, apiKey, application, environment);
		
		Assert.assertNotNull(apiConfig);
		Assert.assertEquals(apiUrl, apiConfig.getApiUrl());
		Assert.assertEquals(apiKey, apiConfig.getApiKey());
		Assert.assertEquals(application, apiConfig.getApplication());
		Assert.assertEquals(environment, apiConfig.getEnvironment());
		Assert.assertNotNull(apiConfig.getEnvDetail());
	}
	
	/**
	 * testFromProperties
	 */
	@Test
	public void testFromProperties() {
		ApiConfiguration apiConfig = ApiConfigurations.fromProperties();
		
		Assert.assertNotNull(apiConfig);
		Assert.assertEquals("url", apiConfig.getApiUrl());
		Assert.assertEquals("key", apiConfig.getApiKey());
		Assert.assertEquals("app", apiConfig.getApplication());
		Assert.assertEquals("env", apiConfig.getEnvironment());
		Assert.assertNotNull(apiConfig.getEnvDetail());
	}
}
