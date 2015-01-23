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

import com.stackify.api.EnvironmentDetail;

/**
 * ApiConfiguration
 * @author Eric Martin
 */
public class ApiConfiguration {

	/**
	 * Default API URL
	 */
	private static final String DEFAULT_API_URL = "https://api.stackify.com";

	/**
	 * API URL
	 */
	private final String apiUrl;

	/**
	 * API Key
	 */
	private final String apiKey;

	/**
	 * Application name
	 */
	private final String application;

	/**
	 * Environment
	 */
	private final String environment;

	/**
	 * Environment details
	 */
	private final EnvironmentDetail envDetail;

	/**
	 * @return the apiUrl
	 */
	public String getApiUrl() {
		return apiUrl != null ? apiUrl : DEFAULT_API_URL;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @return the envDetail
	 */
	public EnvironmentDetail getEnvDetail() {
		return envDetail;
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private ApiConfiguration(final Builder builder) {
		this.apiUrl = builder.apiUrl;
		this.apiKey = builder.apiKey;
		this.application = builder.application;
		this.environment = builder.environment;
		this.envDetail = builder.envDetail;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
	    return new Builder();
	}


	/**
	 * @return a Builder object based on current instance
	 */
	public Builder toBuilder() {
		return newBuilder()
				.apiUrl(apiUrl)
				.apiKey(apiKey)
				.application(application)
				.environment(environment)
				.envDetail(envDetail);
	}

	/**
	 * ApiConfiguration.Builder separates the construction of a ApiConfiguration from its representation
	 */
	public static class Builder {

		/**
		 * The builder's apiUrl
		 */
		private String apiUrl;

		/**
		 * The builder's apiKey
		 */
		private String apiKey;

		/**
		 * The builder's application
		 */
		private String application;

		/**
		 * The builder's environment
		 */
		private String environment;

		/**
		 * The builder's envDetail
		 */
		private EnvironmentDetail envDetail;

		/**
		 * Sets the builder's apiUrl
		 * @param apiUrl The apiUrl to be set
		 * @return Reference to the current object
		 */
		public Builder apiUrl(final String apiUrl) {
			this.apiUrl = apiUrl;
			return this;
		}

		/**
		 * Sets the builder's apiKey
		 * @param apiKey The apiKey to be set
		 * @return Reference to the current object
		 */
		public Builder apiKey(final String apiKey) {
			this.apiKey = apiKey;
			return this;
		}

		/**
		 * Sets the builder's application
		 * @param application The application to be set
		 * @return Reference to the current object
		 */
		public Builder application(final String application) {
			this.application = application;
			return this;
		}

		/**
		 * Sets the builder's environment
		 * @param environment The environment to be set
		 * @return Reference to the current object
		 */
		public Builder environment(final String environment) {
			this.environment = environment;
			return this;
		}

		/**
		 * Sets the builder's envDetail
		 * @param envDetail The envDetail to be set
		 * @return Reference to the current object
		 */
		public Builder envDetail(final EnvironmentDetail envDetail) {
			this.envDetail = envDetail;
			return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public ApiConfiguration build() {
			return new ApiConfiguration(this);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApiConfiguration [apiUrl=" + apiUrl + ", apiKey=" + apiKey
				+ ", application=" + application + ", environment="
				+ environment + ", envDetail=" + envDetail + "]";
	}
}
