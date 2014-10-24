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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.stackify.api.AppIdentity;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.common.http.HttpClient;

/**
 * AppIdentityService JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AppIdentityService.class, HttpClient.class})
public class AppIdentityServiceTest {

	/**
	 * testGetAppIdentity
	 * @throws Exception
	 */
	@Test
	public void testGetAppIdentity() throws Exception {

		final EnvironmentDetail envDetail =
			EnvironmentDetail.newBuilder().deviceName("device").appName("app").build();

		final AppIdentity appIdentity =
			AppIdentity.newBuilder().deviceId(123).appNameId("456").appName("app").build();

		final ObjectMapper objectMapper = new ObjectMapper();

		String appIdentityResponse = objectMapper.writer().writeValueAsString(appIdentity);

		final HttpClient httpClient = PowerMockito.mock(HttpClient.class);
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(httpClient);
		PowerMockito.when(httpClient.post(Mockito.anyString(), (byte[]) Mockito.any())).thenReturn(appIdentityResponse);

		ApiConfiguration apiConfig =
			ApiConfiguration.newBuilder().apiUrl("url").apiKey("key").application("app").envDetail(envDetail).build();

		AppIdentityService service = new AppIdentityService(apiConfig, objectMapper);

		Optional<AppIdentity> rv = service.getAppIdentity();

		Assert.assertNotNull(rv);
		Assert.assertTrue(rv.isPresent());
		Assert.assertEquals(Integer.valueOf(123), rv.get().getDeviceId());
		Assert.assertEquals("456", rv.get().getAppNameId());
	}
	
	/**
	 * testGetAppIdentityWithoutName
	 */
	@Test
	public void testGetAppIdentityWithoutName() {
		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
		ApiConfiguration apiConfig = ApiConfigurations.fromProperties();
		AppIdentityService service = new AppIdentityService(apiConfig, objectMapper);

		Optional<AppIdentity> absent = service.getAppIdentity("");
		
		Assert.assertNotNull(absent);
		Assert.assertFalse(absent.isPresent());
	}
}
