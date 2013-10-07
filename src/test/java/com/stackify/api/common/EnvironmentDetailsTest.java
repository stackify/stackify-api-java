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
package com.stackify.api.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.EnvironmentDetail;

/**
 * EnvironmentDetails JUnit Test
 * 
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnvironmentDetails.class, InetAddress.class})
public class EnvironmentDetailsTest {

	/**
	 * testGetEnvironmentDetail
	 * @throws UnknownHostException 
	 */
	@Test
	public void testGetEnvironmentDetail() throws UnknownHostException {
		String application = "application";
		String environment = "environment";
		String hostName = "hostName";
		
		InetAddress inetAddress = PowerMockito.mock(InetAddress.class);
		PowerMockito.when(inetAddress.getHostName()).thenReturn(hostName);
		
		PowerMockito.mockStatic(InetAddress.class);
		PowerMockito.when(InetAddress.getLocalHost()).thenReturn(inetAddress);
		
		EnvironmentDetail env = EnvironmentDetails.getEnvironmentDetail(application, environment);
		
		Assert.assertNotNull(env);
		
		Assert.assertEquals(hostName, env.getDeviceName());
		Assert.assertNotNull(env.getAppLocation());
		Assert.assertNull(env.getAppName());
		Assert.assertEquals(application, env.getConfiguredAppName());
		Assert.assertEquals(environment, env.getConfiguredEnvironmentName());
	}
	
	/**
	 * testGetEnvironmentDetailHostnameException
	 * @throws UnknownHostException 
	 */
	@Test
	public void testGetEnvironmentDetailHostnameException() throws UnknownHostException {
		String application = "application";
		String environment = "environment";
				
		PowerMockito.mockStatic(InetAddress.class);
		PowerMockito.when(InetAddress.getLocalHost()).thenThrow(new RuntimeException());
		
		EnvironmentDetail env = EnvironmentDetails.getEnvironmentDetail(application, environment);
		
		Assert.assertNotNull(env);
		
		Assert.assertNull(env.getDeviceName());
		Assert.assertNotNull(env.getAppLocation());
		Assert.assertNull(env.getAppName());
		Assert.assertEquals(application, env.getConfiguredAppName());
		Assert.assertEquals(environment, env.getConfiguredEnvironmentName());
	}
}
