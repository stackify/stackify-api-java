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

import java.io.InputStream;
import java.util.Properties;

import com.stackify.api.ApiClient;

/**
 * Utility class for api client details and building an ApiClient object
 * 
 * @author Eric Martin
 */
public class ApiClients {

	/**
	 * Creates an api client object with information from the specified properties file
	 * @param propertyFileName The file name of the properties file containing api client details (could be null)
	 * @return The ApiClient object
	 */
	public static ApiClient getApiClient(final Class<?> apiClass, final String propertyFileName) {
		
		String name = null;
		String version = null;
		
		// get the name and version from the properties stream
		
		InputStream propertiesStream = null;
		
		try {
			propertiesStream = apiClass.getResourceAsStream(propertyFileName);

			if (propertiesStream != null) {
				Properties props = new Properties();
				props.load(propertiesStream);
				name = (String) props.get("api-client.name");
				version = (String) props.get("api-client.version");
			}
		} catch (Throwable t) {
			// do nothing
		} finally {
			if (propertiesStream != null) {
				try {
					propertiesStream.close();
				} catch (Throwable t) {
					// do nothing
				}
			}
		}
		
		// build the api client
		
		ApiClient.Builder apiClientBuilder = ApiClient.newBuilder();
		apiClientBuilder.name(name);
		apiClientBuilder.version(version);
		
		return apiClientBuilder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private ApiClients() {
	}
}
