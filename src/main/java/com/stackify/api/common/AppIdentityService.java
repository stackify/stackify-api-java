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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.stackify.api.AppIdentity;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.http.HttpException;

/**
 * AppIdentityService
 * @author Eric Martin
 */
public class AppIdentityService {

	/**
	 * The internal logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AppIdentityService.class);

	/**
	 * Five minutes (in milliseconds)
	 */
	private static long FIVE_MINUTES_MILLIS = 300000;
	
	/**
	 * Timestamp of the last query
	 */
	private long lastQuery = 0;
	
	/**
	 * The cached app identity
	 */
	private Optional<AppIdentity> appIdentity = Optional.absent();
			
	/**
	 * The API configuration
	 */
	private final ApiConfiguration apiConfig;

	/**
	 * Jackson object mapper
	 */
	private final ObjectMapper objectMapper;
	
	/**
	 * Constructor
	 * @param apiConfig The API configuration
	 * @param objectMapper Jackson object mapper
	 */
	public AppIdentityService(final ApiConfiguration apiConfig, final ObjectMapper objectMapper) {
		Preconditions.checkNotNull(apiConfig);
		Preconditions.checkNotNull(objectMapper);
		
		this.apiConfig = apiConfig;
		this.objectMapper = objectMapper;
	}
		
	/**
	 * Retrieves the application identity given the environment details
	 * @return The application identity
	 */
	public Optional<AppIdentity> getAppIdentity() {
		if (!appIdentity.isPresent()) {
			long currentTimeMillis = System.currentTimeMillis();
			
			if (lastQuery + FIVE_MINUTES_MILLIS < currentTimeMillis) {
				try {
					lastQuery = currentTimeMillis;
					appIdentity = Optional.fromNullable(identifyApp());
					LOGGER.debug("Application identity: {}", appIdentity.get());
				} catch (Throwable t) {
					LOGGER.info("Unable to determine application identity", t);
				}
			}
		}
		
		return appIdentity;
	}
	
	/**
	 * Retrieves the application identity given the environment details
	 * @return The application identity
	 * @throws IOException
	 * @throws HttpException 
	 */
	private AppIdentity identifyApp() throws IOException, HttpException {
		
		// convert to json bytes
		
		byte[] jsonBytes = objectMapper.writer().writeValueAsBytes(apiConfig.getEnvDetail());
		
		// post to stackify
		
		HttpClient httpClient = new HttpClient(apiConfig);
		String responseString = httpClient.post("/Metrics/IdentifyApp", jsonBytes);
		
		// deserialize the response and return the app identity
		
		ObjectReader jsonReader = objectMapper.reader(new TypeReference<AppIdentity>(){});
		AppIdentity appIdentity = jsonReader.readValue(responseString);
		
		return appIdentity;
	}
}
