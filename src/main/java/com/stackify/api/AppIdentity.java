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
package com.stackify.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * AppIdentity
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = AppIdentity.Builder.class)
public class AppIdentity {

	/**
	 * Device id
	 */
	@JsonProperty("DeviceID")
	private final Integer deviceId;

	/**
	 * Device application id
	 */
	@JsonProperty("DeviceAppID")
	private final Integer deviceAppId;

	/**
	 * Application name id
	 */
	@JsonProperty("AppNameID")
	private final String appNameId;

	/**
	 * Environment id
	 */
	@JsonProperty("EnvID")
	private final Integer envId;

	/**
	 * Environment
	 */
	@JsonProperty("Env")
	private final String env;

	/**
	 * Application name
	 */
	@JsonProperty("AppName")
	private final String appName;

	/**
	 * Application-Environment id
	 */
	@JsonProperty("AppEnvID")
	private final String appEnvId;

	/**
	 * Device alias
	 */
	@JsonProperty("DeviceAlias")
	private final String deviceAlias;

	/**
	 * @return the deviceId
	 */
	public Integer getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the deviceAppId
	 */
	public Integer getDeviceAppId() {
		return deviceAppId;
	}

	/**
	 * @return the appNameId
	 */
	public String getAppNameId() {
		return appNameId;
	}

	/**
	 * @return the envId
	 */
	public Integer getEnvId() {
		return envId;
	}

	/**
	 * @return the env
	 */
	public String getEnv() {
		return env;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @return the appEnvId
	 */
	public String getAppEnvId() {
		return appEnvId;
	}

	/**
	 * @return the deviceAlias
	 */
	public String getDeviceAlias() {
		return deviceAlias;
	}

	/**
	 * @return An instance of Builder, based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.deviceId(this.deviceId)
			.deviceAppId(this.deviceAppId)
			.appNameId(this.appNameId)
			.envId(this.envId)
			.env(this.env)
			.appName(this.appName)
			.appEnvId(this.appEnvId)
			.deviceAlias(this.deviceAlias);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private AppIdentity(final Builder builder) {
		this.deviceId = builder.deviceId;
		this.deviceAppId = builder.deviceAppId;
		this.appNameId = builder.appNameId;
		this.envId = builder.envId;
		this.env = builder.env;
		this.appName = builder.appName;
		this.appEnvId = builder.appEnvId;
		this.deviceAlias = builder.deviceAlias;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
	    return new Builder();
	}

	/**
	 * AppIdentity.Builder separates the construction of a AppIdentity from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's deviceId
		 */
		@JsonProperty("DeviceID")
		private Integer deviceId;

		/**
		 * The builder's deviceAppId
		 */
		@JsonProperty("DeviceAppID")
		private Integer deviceAppId;

		/**
		 * The builder's appNameId
		 */
		@JsonProperty("AppNameID")
		private String appNameId;

		/**
		 * The builder's envId
		 */
		@JsonProperty("EnvID")
		private Integer envId;

		/**
		 * The builder's env
		 */
		@JsonProperty("Env")
		private String env;

		/**
		 * The builder's appName
		 */
		@JsonProperty("AppName")
		private String appName;

		/**
		 * The builder's appEnvId
		 */
		@JsonProperty("AppEnvID")
		private String appEnvId;

		/**
		 * The builder's deviceAlias
		 */
		@JsonProperty("DeviceAlias")
		private String deviceAlias;

		/**
		 * Sets the builder's deviceId
		 * @param deviceId The deviceId to be set
		 * @return Reference to the current object
		 */
		public Builder deviceId(final Integer deviceId) {
		    this.deviceId = deviceId;
		    return this;
		}

		/**
		 * Sets the builder's deviceAppId
		 * @param deviceAppId The deviceAppId to be set
		 * @return Reference to the current object
		 */
		public Builder deviceAppId(final Integer deviceAppId) {
		    this.deviceAppId = deviceAppId;
		    return this;
		}

		/**
		 * Sets the builder's appNameId
		 * @param appNameId The appNameId to be set
		 * @return Reference to the current object
		 */
		public Builder appNameId(final String appNameId) {
		    this.appNameId = appNameId;
		    return this;
		}

		/**
		 * Sets the builder's envId
		 * @param envId The envId to be set
		 * @return Reference to the current object
		 */
		public Builder envId(final Integer envId) {
		    this.envId = envId;
		    return this;
		}

		/**
		 * Sets the builder's env
		 * @param env The env to be set
		 * @return Reference to the current object
		 */
		public Builder env(final String env) {
		    this.env = env;
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
		 * Sets the builder's appEnvId
		 * @param appEnvId The appEnvId to be set
		 * @return Reference to the current object
		 */
		public Builder appEnvId(final String appEnvId) {
		    this.appEnvId = appEnvId;
		    return this;
		}

		/**
		 * Sets the builder's deviceAlias
		 * @param deviceAlias The deviceAlias to be set
		 * @return Reference to the current object
		 */
		public Builder deviceAlias(final String deviceAlias) {
		    this.deviceAlias = deviceAlias;
		    return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public AppIdentity build() {
		    return new AppIdentity(this);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppIdentity{deviceId=" + deviceId + ", deviceAppId="
				+ deviceAppId + ", appNameId=" + appNameId + ", envId=" + envId
				+ ", env=" + env + ", appName=" + appName + ", appEnvId="
				+ appEnvId + ", deviceAlias=" + deviceAlias + "}";
	}
}
