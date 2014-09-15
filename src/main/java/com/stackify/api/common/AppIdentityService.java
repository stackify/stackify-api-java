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
import java.util.Hashtable;
import java.util.Map;

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
	 * Map<ApplicationName, AppIdentityState> The cached app identity
	 */
	private Map<String, AppIdentityState> applicationIdentityCache = new Hashtable<String, AppIdentityState>();

	/**
	 * The API configuration
	 */
	private final ApiConfiguration defaultApiConfig;

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

		this.defaultApiConfig = apiConfig;
		this.objectMapper = objectMapper;
	}

	private boolean isCached(final String applicationName) {
		Preconditions.checkNotNull(applicationName);
		return applicationIdentityCache.containsKey(applicationName);
	}

	/**
	 * Retrieves the application identity given the environment details
	 * @return The application identity
	 */
	private Optional<AppIdentity> getAppIdentity(ApiConfiguration apiConfig) {
		final String applicationName = apiConfig.getApplication();

		if (applicationName == null)
			return Optional.absent();

		// new state with current timestamp
		final AppIdentityState state = new AppIdentityState();

		final long now = System.currentTimeMillis();

		if (state.lastModified() + FIVE_MINUTES_MILLIS < now) {
			try {
				final AppIdentity identity = identifyApp(apiConfig);
				// Obtain AppIdentity for current app
				applicationIdentityCache.put(
					applicationName, state.updateAppIdentity(identity)
				);

				LOGGER.debug("Application identity: {}", identity);

			} catch (Throwable t) {
				LOGGER.info("Unable to determine application identity", t);
			}
		}

		return applicationIdentityCache.get(apiConfig.getApplication()).getAppIdentity();
	}



	public Optional<AppIdentity> getAppIdentity(final String applicationName) {
		if (isCached(applicationName)) {
			return applicationIdentityCache.get(applicationName).getAppIdentity();

		} else {
			// use existing apiConfig, with new application name
			final ApiConfiguration updatedApiConfig = defaultApiConfig.toBuilder().application(applicationName).build();
			return getAppIdentity(updatedApiConfig);
		}
	}


 	public Optional<AppIdentity> getAppIdentity() {
		return getAppIdentity(defaultApiConfig);
 	}

	/**
	 * Retrieves the application identity given the environment details
	 * @return The application identity
	 * @throws IOException
	 * @throws HttpException
	 */
	private AppIdentity identifyApp(ApiConfiguration apiConfig) throws IOException, HttpException {
		// convert to json bytes
		byte[] jsonBytes = objectMapper.writer().writeValueAsBytes(apiConfig.getEnvDetail());

		// post to stackify
		final HttpClient httpClient = new HttpClient(apiConfig);
		final String responseString = httpClient.post("/Metrics/IdentifyApp", jsonBytes);

		// deserialize the response and return the app identity
		ObjectReader jsonReader = objectMapper.reader(new TypeReference<AppIdentity>(){});
		return jsonReader.readValue(responseString);
	}

	/**
	 * This class contains appIdentity and it's modification date
	 */
	private class AppIdentityState {

		private Optional<AppIdentity> mayBeAppIdentity = Optional.absent();
		private long lastQueryTimeStamp;

		public AppIdentityState() {
			this.lastQueryTimeStamp = 0;
			this.mayBeAppIdentity = Optional.absent();
		}

		public AppIdentityState(final AppIdentity appIdentity) {
			this.touch();
			this.mayBeAppIdentity = Optional.fromNullable(appIdentity);
		}

		public AppIdentityState(final AppIdentity appIdentity, long timestamp) {
			this.lastQueryTimeStamp = timestamp;
			this.mayBeAppIdentity = Optional.fromNullable(appIdentity);
		}

		public final AppIdentityState updateAppIdentity(final AppIdentity appIdentity) {
			mayBeAppIdentity = Optional.fromNullable(appIdentity);
			return this;
		}

		public final Optional<AppIdentity> getAppIdentity() {
			this.touch();
			return mayBeAppIdentity;
		}

		public final long lastModified() {
			return lastQueryTimeStamp;
		}

		/**
		 * Changes last modified date
		 */
		public final void touch() {
			this.lastQueryTimeStamp = System.currentTimeMillis();
		}
	}
}
