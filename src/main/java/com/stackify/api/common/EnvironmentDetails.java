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

import com.stackify.api.EnvironmentDetail;

/**
 * Utility class for retrieving environment details and building an EnvironmentDetail object
 * 
 * @author Eric Martin
 */
public class EnvironmentDetails {

	/**
	 * Creates an environment details object with system information
	 * @param application The configured application name
	 * @param environment The configured application environment
	 * @return The EnvironmentDetail object
	 */
	public static EnvironmentDetail getEnvironmentDetail(final String application, final String environment) {
		
		// lookup the hostname

		String hostName = null;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Throwable t) {
			// Do nothing
		}
		
		// lookup the current path
		
		String currentPath = System.getProperty("user.dir");
		
		// build the environment details
		
		EnvironmentDetail.Builder environmentBuilder = EnvironmentDetail.newBuilder();
		environmentBuilder.deviceName(hostName);
		environmentBuilder.appLocation(currentPath);
		environmentBuilder.configuredAppName(application);
		environmentBuilder.configuredEnvironmentName(environment);
		
		return environmentBuilder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private EnvironmentDetails() {
	}
}
