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

import org.junit.Assert;
import org.junit.Test;

/**
 * EnvironmentDetail JUnit Test
 *
 * @author Eric Martin
 */
public class EnvironmentDetailTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String deviceName = "device";
		String appName = "app";
		String appLocation = "location";
		String configuredAppName = "configuredAppName";
		String configuredEnvironmentName = "configuredEnvironmentName";

		EnvironmentDetail.Builder builder = EnvironmentDetail.newBuilder();
		builder.deviceName(deviceName);
		builder.appName(appName);
		builder.appLocation(appLocation);
		builder.configuredAppName(configuredAppName);
		builder.configuredEnvironmentName(configuredEnvironmentName);
		EnvironmentDetail environment = builder.build();

		Assert.assertNotNull(environment);

		Assert.assertEquals(deviceName, environment.getDeviceName());
		Assert.assertEquals(appName, environment.getAppName());
		Assert.assertEquals(appLocation, environment.getAppLocation());
		Assert.assertEquals(configuredAppName, environment.getConfiguredAppName());
		Assert.assertEquals(configuredEnvironmentName, environment.getConfiguredEnvironmentName());
	}
}
