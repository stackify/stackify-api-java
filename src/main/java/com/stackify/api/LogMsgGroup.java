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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * LogMsgGroup
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = LogMsgGroup.Builder.class)
public class LogMsgGroup {

	/**
	 * Client device id
	 */
	@JsonProperty("CDID")
	private final Integer cdId;

	/**
	 * Client device application id
	 */
	@JsonProperty("CDAppID")
	private final Integer cdAppId;

	/**
	 * Application name id
	 */
	@JsonProperty("AppNameID")
	private final String appNameId;

	/**
	 * Application/environment id
	 */
	@JsonProperty("AppEnvID")
	private final String appEnvId;

	/**
	 * Environment id
	 */
	@JsonProperty("EnvID")
	private final Integer envId;

	/**
	 * Environment name
	 */
	@JsonProperty("Env")
	private final String env;

	/**
	 * Device name
	 */
	@JsonProperty("ServerName")
	private final String serverName;

	/**
	 * Application name
	 */
	@JsonProperty("AppName")
	private final String appName;

	/**
	 * Application path
	 */
	@JsonProperty("AppLoc")
	private final String appLoc;

	/**
	 * Logger project
	 */
	@JsonProperty("Logger")
	private final String logger;

	/**
	 * Logger platform ("java")
	 */
	@JsonProperty("Platform")
	private final String platform;

	/**
	 * Log messages
	 */
	@JsonProperty("Msgs")
	private final List<LogMsg> msgs;

	/**
	 * @return the cdId
	 */
	public Integer getCdId() {
		return cdId;
	}

	/**
	 * @return the cdAppId
	 */
	public Integer getCdAppId() {
		return cdAppId;
	}

	/**
	 * @return the appNameId
	 */
	public String getAppNameId() {
		return appNameId;
	}

	/**
	 * @return the appEnvId
	 */
	public String getAppEnvId() {
		return appEnvId;
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
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @return the appLoc
	 */
	public String getAppLoc() {
		return appLoc;
	}

	/**
	 * @return the logger
	 */
	public String getLogger() {
		return logger;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @return the msgs
	 */
	public List<LogMsg> getMsgs() {
		return msgs;
	}

	/**
 	 * @return An instance of Builder, based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.cdId(this.cdId)
			.cdAppId(this.cdAppId)
			.appNameId(this.appNameId)
			.appEnvId(this.appEnvId)
			.envId(this.envId)
			.env(this.env)
			.serverName(this.serverName)
			.appName(this.appName)
			.appLoc(this.appLoc)
			.logger(this.logger)
			.platform(this.platform)
			.msgs(this.msgs);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private LogMsgGroup(final Builder builder) {
		this.cdId = builder.cdId;
		this.cdAppId = builder.cdAppId;
		this.appNameId = builder.appNameId;
		this.appEnvId = builder.appEnvId;
		this.envId = builder.envId;
		this.env = builder.env;
		this.serverName = builder.serverName;
		this.appName = builder.appName;
		this.appLoc = builder.appLoc;
		this.logger = builder.logger;
		this.platform = builder.platform;
		this.msgs = builder.msgs;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
	    return new Builder();
	}

	/**
	 * LogMsgGroup.Builder separates the construction of a LogMsgGroup from its representation
	 */
	public static class Builder {

		/**
		 * The builder's cdId
		 */
		@JsonProperty("CDID")
		private Integer cdId;

		/**
		 * The builder's cdAppId
		 */
		@JsonProperty("CDAppID")
		private Integer cdAppId;

		/**
		 * The builder's appNameId
		 */
		@JsonProperty("AppNameID")
		private String appNameId;

		/**
		 * The builder's appEnvId
		 */
		@JsonProperty("AppEnvID")
		private String appEnvId;

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
		 * The builder's serverName
		 */
		@JsonProperty("ServerName")
		private String serverName;

		/**
		 * The builder's appName
		 */
		@JsonProperty("AppName")
		private String appName;

		/**
		 * The builder's appLoc
		 */
		@JsonProperty("AppLoc")
		private String appLoc;

		/**
		 * The builder's logger
		 */
		@JsonProperty("Logger")
		private String logger;

		/**
		 * The builder's platform
		 */
		@JsonProperty("Platform")
	    private String platform;

		/**
		 * The builder's msgs
		 */
		@JsonProperty("Msgs")
		private List<LogMsg> msgs;

		/**
		 * Sets the builder's cdId
		 * @param cdId The cdId to be set
		 * @return Reference to the current object
		 */
		public Builder cdId(final Integer cdId) {
		    this.cdId = cdId;
		    return this;
		}

		/**
		 * Sets the builder's cdAppId
		 * @param cdAppId The cdAppId to be set
		 * @return Reference to the current object
		 */
		public Builder cdAppId(final Integer cdAppId) {
		    this.cdAppId = cdAppId;
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
		 * Sets the builder's appEnvId
		 * @param appEnvId The appEnvId to be set
		 * @return Reference to the current object
		 */
		public Builder appEnvId(final String appEnvId) {
		    this.appEnvId = appEnvId;
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
		 * Sets the builder's serverName
		 * @param serverName The serverName to be set
		 * @return Reference to the current object
		 */
		public Builder serverName(final String serverName) {
		    this.serverName = serverName;
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
		 * Sets the builder's appLoc
		 * @param appLoc The appLoc to be set
		 * @return Reference to the current object
		 */
		public Builder appLoc(final String appLoc) {
		    this.appLoc = appLoc;
		    return this;
		}

		/**
		 * Sets the builder's logger
		 * @param logger The logger to be set
		 * @return Reference to the current object
		 */
		public Builder logger(final String logger) {
		    this.logger = logger;
		    return this;
		}

		/**
		 * Sets the builder's platform
		 * @param platform The platform to be set
		 * @return Reference to the current object
		 */
		public Builder platform(final String platform) {
		    this.platform = platform;
		    return this;
		}

		/**
		 * Sets the builder's msgs
		 * @param msgs The msgs to be set
		 * @return Reference to the current object
		 */
		public Builder msgs(final List<LogMsg> msgs) {
		    this.msgs = msgs;
		    return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public LogMsgGroup build() {
		    return new LogMsgGroup(this);
		}
	}
}
