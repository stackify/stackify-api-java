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

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Encapsulates all details about an error that will be sent to Stackify
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * StackifyError.Builder builder = StackifyError.newBuilder();
 * builder.environmentDetail(environment);
 * ...
 * StackifyError stackifyError = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = StackifyError.Builder.class)
public class StackifyError {

	/**
	 * Environment
	 */
	@JsonProperty("EnvironmentDetail")
	private final EnvironmentDetail environmentDetail;

	/**
	 * Date/time of the error
	 */
	@JsonProperty("OccurredEpochMillis")
	private final Date occurredEpochMillis;

	/**
	 * Error details
	 */
	@JsonProperty("Error")
	private final ErrorItem error;

	/**
	 * Details of the web request
	 */
	@JsonProperty("WebRequestDetail")
	private final WebRequestDetail webRequestDetail;

	/**
	 * Server variables
	 */
	@JsonProperty("ServerVariables")
	private final Map<String, String> serverVariables;

	/**
	 * Customer name
	 */
	@JsonProperty("CustomerName")
	private final String customerName;

	/**
	 * User name
	 */
	@JsonProperty("UserName")
	private final String userName;

	/**
	 * @return the environmentDetail
	 */
	public EnvironmentDetail getEnvironmentDetail() {
		return environmentDetail;
	}

	/**
	 * @return the occurredEpochMillis
	 */
	public Date getOccurredEpochMillis() {
		return occurredEpochMillis;
	}

	/**
	 * @return the error
	 */
	public ErrorItem getError() {
		return error;
	}

	/**
	 * @return the webRequestDetail
	 */
	public WebRequestDetail getWebRequestDetail() {
		return webRequestDetail;
	}

	/**
	 * @return the serverVariables
	 */
	public Map<String, String> getServerVariables() {
		return serverVariables;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return an instance of builder based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.environmentDetail(this.environmentDetail)
			.occurredEpochMillis(this.occurredEpochMillis)
			.error(this.error)
			.webRequestDetail(this.webRequestDetail)
			.serverVariables(this.serverVariables)
			.customerName(this.customerName)
			.userName(this.userName);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private StackifyError(final Builder builder) {
		this.environmentDetail = builder.environmentDetail;
		this.occurredEpochMillis = builder.occurredEpochMillis;
		this.error = builder.error;
		this.webRequestDetail = builder.webRequestDetail;
		this.serverVariables = builder.serverVariables;
		this.customerName = builder.customerName;
		this.userName = builder.userName;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * StackifyError.Builder separates the construction of a StackifyError from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's environmentDetail
		 */
		@JsonProperty("EnvironmentDetail")
		private EnvironmentDetail environmentDetail;

		/**
		 * The builder's occurredEpochMillis
		 */
		@JsonProperty("OccurredEpochMillis")
		private Date occurredEpochMillis;

		/**
		 * The builder's error
		 */
		@JsonProperty("Error")
		private ErrorItem error;

		/**
		 * The builder's webRequestDetail
		 */
		@JsonProperty("WebRequestDetail")
		private WebRequestDetail webRequestDetail;

		/**
		 * The builder's serverVariables
		 */
		@JsonProperty("ServerVariables")
		private Map<String,String> serverVariables;

		/**
		 * The builder's customerName
		 */
		@JsonProperty("CustomerName")
		private String customerName;

		/**
		 * The builder's userName
		 */
		@JsonProperty("UserName")
		private String userName;

		/**
		 * Sets the builder's environmentDetail
		 * @param environmentDetail The environmentDetail to be set
		 * @return Reference to the current object
		 */
		public Builder environmentDetail(final EnvironmentDetail environmentDetail) {
			this.environmentDetail = environmentDetail;
			return this;
		}

		/**
		 * Sets the builder's occurredEpochMillis
		 * @param occurredEpochMillis The occurredEpochMillis to be set
		 * @return Reference to the current object
		 */
		public Builder occurredEpochMillis(final Date occurredEpochMillis) {
			this.occurredEpochMillis = occurredEpochMillis;
			return this;
		}

		/**
		 * Sets the builder's error
		 * @param error The error to be set
		 * @return Reference to the current object
		 */
		public Builder error(final ErrorItem error) {
			this.error = error;
			return this;
		}

		/**
		 * Sets the builder's webRequestDetail
		 * @param webRequestDetail The webRequestDetail to be set
		 * @return Reference to the current object
		 */
		public Builder webRequestDetail(final WebRequestDetail webRequestDetail) {
			this.webRequestDetail = webRequestDetail;
			return this;
		}

		/**
		 * Sets the builder's serverVariables
		 * @param serverVariables The serverVariables to be set
		 * @return Reference to the current object
		 */
		public Builder serverVariables(final Map<String,String> serverVariables) {
			this.serverVariables = serverVariables;
			return this;
		}

		/**
		 * Sets the builder's customerName
		 * @param customerName The customerName to be set
		 * @return Reference to the current object
		 */
		public Builder customerName(final String customerName) {
			this.customerName = customerName;
			return this;
		}

		/**
		 * Sets the builder's userName
		 * @param userName The userName to be set
		 * @return Reference to the current object
		 */
		public Builder userName(final String userName) {
			this.userName = userName;
			return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public StackifyError build() {
			return new StackifyError(this);
		}
	}
}
