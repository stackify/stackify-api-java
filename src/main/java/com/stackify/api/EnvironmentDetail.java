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
package com.stackify.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Contains details of the current application environment such as device name, application name, application
 * location and environment.
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * EnvironmentDetail.Builder builder = EnvironmentDetail.newBuilder();
 * builder.deviceName("localhost");
 * ...
 * EnvironmentDetail environmentDetail = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = EnvironmentDetail.Builder.class)
public class EnvironmentDetail {

	/**
	 * Device name
	 */
	@JsonProperty("DeviceName")
	private final String deviceName;

	/**
	 * Application name
	 */
	@JsonProperty("AppName")
	private final String appName;

	/**
	 * Application location
	 */
	@JsonProperty("AppLocation")
	private final String appLocation;

	/**
	 * Custom application name
	 */
	@JsonProperty("ConfiguredAppName")
	private final String configuredAppName;

	/**
	 * Custom application environment
	 */
	@JsonProperty("ConfiguredEnvironmentName")
	private final String configuredEnvironmentName;

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @return the appLocation
	 */
	public String getAppLocation() {
		return appLocation;
	}

	/**
	 * @return the configuredAppName
	 */
	public String getConfiguredAppName() {
		return configuredAppName;
	}

	/**
	 * @return the configuredEnvironmentName
	 */
	public String getConfiguredEnvironmentName() {
		return configuredEnvironmentName;
	}

	/**
	 * @return a new instalce of builder based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.deviceName(this.deviceName)
			.appName(this.appName)
			.appLocation(this.appLocation)
			.configuredAppName(this.configuredAppName)
			.configuredEnvironmentName(this.configuredEnvironmentName);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private EnvironmentDetail(final Builder builder) {
		this.deviceName = builder.deviceName;
		this.appName = builder.appName;
		this.appLocation = builder.appLocation;
		this.configuredAppName = builder.configuredAppName;
		this.configuredEnvironmentName = builder.configuredEnvironmentName;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * EnvironmentDetail.Builder separates the construction of a EnvironmentDetail from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's deviceName
		 */
		@JsonProperty("DeviceName")
		private String deviceName;

		/**
		 * The builder's appName
		 */
		@JsonProperty("AppName")
		private String appName;

		/**
		 * The builder's appLocation
		 */
		@JsonProperty("AppLocation")
		private String appLocation;

		/**
		 * The builder's configuredAppName
		 */
		@JsonProperty("ConfiguredAppName")
		private String configuredAppName;

		/**
		 * The builder's configuredEnvironmentName
		 */
		@JsonProperty("ConfiguredEnvironmentName")
		private String configuredEnvironmentName;

		/**
		 * Sets the builder's deviceName
		 * @param deviceName The deviceName to be set
		 * @return Reference to the current object
		 */
		public Builder deviceName(final String deviceName) {
			this.deviceName = deviceName;
			return this;
		}

		/**
		 * Sets the builder's appName
		 * @param appName The appName to be set
		 * @return Reference to the current object
		 */
		public Builder appName(final String appName) {
			this.appName = appName;
			return this;
		}

		/**
		 * Sets the builder's appLocation
		 * @param appLocation The appLocation to be set
		 * @return Reference to the current object
		 */
		public Builder appLocation(final String appLocation) {
			this.appLocation = appLocation;
			return this;
		}

		/**
		 * Sets the builder's configuredAppName
		 * @param configuredAppName The configuredAppName to be set
		 * @return Reference to the current object
		 */
		public Builder configuredAppName(final String configuredAppName) {
			this.configuredAppName = configuredAppName;
			return this;
		}

		/**
		 * Sets the builder's configuredEnvironmentName
		 * @param configuredEnvironmentName The configuredEnvironmentName to be set
		 * @return Reference to the current object
		 */
		public Builder configuredEnvironmentName(final String configuredEnvironmentName) {
			this.configuredEnvironmentName = configuredEnvironmentName;
			return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public EnvironmentDetail build() {
			return new EnvironmentDetail(this);
		}
	}
}
