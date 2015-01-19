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
@PrepareForTest({EnvironmentDetails.class, System.class, InetAddress.class})
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
				
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getenv("HOSTNAME")).thenReturn(hostName);
		PowerMockito.when(System.getProperty("user.dir")).thenReturn("/some/dir/");
		
		EnvironmentDetail env = EnvironmentDetails.getEnvironmentDetail(application, environment);
		
		Assert.assertNotNull(env);
		
		Assert.assertEquals(hostName, env.getDeviceName());
		Assert.assertNotNull(env.getAppLocation());
		Assert.assertNull(env.getAppName());
		Assert.assertEquals(application, env.getConfiguredAppName());
		Assert.assertEquals(environment, env.getConfiguredEnvironmentName());
	}
	
	/**
	 * testGetEnvironmentDetailWithoutHostnameEnvVar
	 * @throws UnknownHostException 
	 */
	@Test
	public void testGetEnvironmentDetailWithoutHostnameEnvVar() throws UnknownHostException {
		String application = "application";
		String environment = "environment";
		String hostName = "hostName";
		
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getenv("HOSTNAME")).thenReturn(null);
		PowerMockito.when(System.getProperty("user.dir")).thenReturn("/some/dir/");

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
}
