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

/**
 * ApiClients
 * @author Eric Martin
 */
public class ApiClients {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiClients.class);

	/**
	 * Gets the client name from the properties file
	 * @param fileName Properties file name
	 * @param defaultClientName Default client name
	 * @return The client name
	 */
	public static String getApiClient(final String fileName, final String defaultClientName) {
		
		FileReader fileReader = null;
		
		try {
			URL fileUrl = ApiConfigurations.class.getResource(fileName);
			File file = new File(fileUrl.toURI());
						
			if (file.exists()) {
				fileReader = new FileReader(file);
			
				Properties props = new Properties();
				props.load(fileReader);
		
				String name = (String) props.get("api-client.name");
				String version = (String) props.get("api-client.version");

				return name + "-" + version;
			}
		} catch (Throwable t) {
			LOGGER.error("Exception reading {} configuration file", fileName, t);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Throwable t) {
					LOGGER.info("Exception closing {} configuration file", fileName, t);
				}
			}
		}
		
		return defaultClientName;
	}
}
