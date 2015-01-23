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
import com.stackify.api.AppIdentity;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.http.HttpException;
import com.stackify.api.common.util.Preconditions;

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

	/**
	 * Retrieves the application identity given the environment details
	 * @return The application identity
	 */
	private AppIdentity getAppIdentity(ApiConfiguration apiConfig) {
		final String applicationName = apiConfig.getApplication();

		if (applicationName == null)
			return null;

		// If there's no record create it.
		if (!applicationIdentityCache.containsKey(applicationName)) {
			applicationIdentityCache.put(applicationName, new AppIdentityState());
		}

		final AppIdentityState state = applicationIdentityCache.get(applicationName);
		final long now = System.currentTimeMillis();

		if ((state.lastModified() + FIVE_MINUTES_MILLIS) < now) {
			state.touch();
			try {
				final AppIdentity identity = identifyApp(apiConfig);
				applicationIdentityCache.put(applicationName, state.updateAppIdentity(identity));

				LOGGER.debug("Application identity: {}", identity);

			} catch (Throwable t) {
				LOGGER.info("Unable to determine application identity", t);
			}
		}

		return applicationIdentityCache.get(apiConfig.getApplication()).getAppIdentity();
	}

	/**
	 * Retrieves the application identity given the environment details
	 * @param applicationName - name of the application
	 * @return The application identity
	 */
	public AppIdentity getAppIdentity(final String applicationName) {
		if (isCached(applicationName)) {
			return applicationIdentityCache.get(applicationName).getAppIdentity();

		} else {
			// Update environment detail with new configured application name
			final EnvironmentDetail updatedEnvDetail =
				updateEnvironmentDetail(defaultApiConfig.getEnvDetail(), applicationName);

			// use existing apiConfig, with new application name
			final ApiConfiguration updatedApiConfig = defaultApiConfig.toBuilder()
				.application(applicationName)
				.envDetail(updatedEnvDetail)
				.build();

			return getAppIdentity(updatedApiConfig);
		}
	}

	/**
	 * getAppIdentity
	 * @return The application identity
	 */
 	public AppIdentity getAppIdentity() {
		if (isCached(defaultApiConfig.getApplication()))
			return applicationIdentityCache.get(defaultApiConfig.getApplication()).getAppIdentity();
		else
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


	private boolean isCached(final String applicationName) {
		Preconditions.checkNotNull(applicationName);

		return
			applicationIdentityCache.containsKey(applicationName) &&
			applicationIdentityCache.get(applicationName).getAppIdentity() != null;
	}


	private EnvironmentDetail updateEnvironmentDetail(final EnvironmentDetail envDetail, final String newConfAppName) {
		return EnvironmentDetail.newBuilder()
			.deviceName(envDetail.getDeviceName())
			.appName(envDetail.getAppName())
			.appLocation(envDetail.getAppLocation())
			.configuredAppName(newConfAppName)
			.configuredEnvironmentName(envDetail.getConfiguredEnvironmentName())
			.build();
	}


	/**
	 * This class contains appIdentity and it's modification date
	 */
	private class AppIdentityState {

		private AppIdentity mayBeAppIdentity;
		private long lastQueryTimeStamp;

		public AppIdentityState() {
			this.lastQueryTimeStamp = 0;
			this.mayBeAppIdentity = null;
		}

		public final AppIdentityState updateAppIdentity(final AppIdentity appIdentity) {
			mayBeAppIdentity = appIdentity;
			return this;
		}

		public final AppIdentity getAppIdentity() {
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
