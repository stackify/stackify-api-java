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

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * ApiConfigurations
 * @author Eric Martin
 */
public class ApiConfigurations {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiConfigurations.class);

	/**
	 * Explicitly configure the API
	 * @param apiUrl API URL
	 * @param apiKey API Key
	 * @param application Configured application name
	 * @param environment Configured environment name
	 * @return ApiConfiguration
	 */
	public static ApiConfiguration from(final String apiUrl, final String apiKey, final String application, final String environment) {
		Preconditions.checkNotNull(apiKey);
		Preconditions.checkArgument(!apiKey.isEmpty());
		
		ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();
		builder.apiUrl(apiUrl);
		builder.apiKey(apiKey);
		builder.application(application);
		builder.environment(environment);
		builder.envDetail(EnvironmentDetails.getEnvironmentDetail(application, environment));
		
		return builder.build();
	}
	
	/**
	 * @return ApiConfiguration read from the stackify-api.properties file
	 */
	public static ApiConfiguration fromProperties() {
		
		ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();

		FileReader confFileReader = null;
		
		try {
			URL confFileUrl = ClassLoader.getSystemResource("stackify-api.properties");
			File confFile = new File(confFileUrl.toURI());
						
			if (confFile.exists()) {
	
				confFileReader = new FileReader(confFile);
			
				Properties confProps = new Properties();
				confProps.load(confFileReader);
				
				String apiUrl = null;
				
				if (confProps.containsKey("stackify.apiUrl")) {
					apiUrl = confProps.getProperty("stackify.apiUrl");
				}
				
				String apiKey = confProps.getProperty("stackify.apiKey");
				String application = confProps.getProperty("stackify.application");
				String environment = confProps.getProperty("stackify.environment");
				
				builder.apiUrl(apiUrl);
				builder.apiKey(apiKey);
				builder.application(application);
				builder.environment(environment);
				builder.envDetail(EnvironmentDetails.getEnvironmentDetail(application, environment));
			}
			
		} catch (Throwable t) {
			LOGGER.error("Exception reading stackify-api.properties configuration file", t);
		} finally {
			if (confFileReader != null) {
				try {
					confFileReader.close();
				} catch (Throwable t) {
					LOGGER.info("Exception closing stackify-api.properties configuration file", t);
				}
			}
		}
		
		return builder.build();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private ApiConfigurations() {
	}
}
