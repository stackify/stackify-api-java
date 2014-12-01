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

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Contains the details of a single exception including the stack trace and any causes
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * ErrorItem.Builder builder = ErrorItem.newBuilder();
 * builder.message(throwable.getMessage());
 * builder.errorType(throwable.getClass().getCanonicalName());
 * ...
 * ErrorItem errorItem = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = ErrorItem.Builder.class)
public class ErrorItem {

	/**
	 * The error message
	 */
	@JsonProperty("Message")
	private final String message;

	/**
	 * The error's class name
	 */
	@JsonProperty("ErrorType")
	private final String errorType;

	/**
	 * The error type code
	 */
	@JsonProperty("ErrorTypeCode")
	private final String errorTypeCode;

	/**
	 * Custom data for the error
	 */
	@JsonProperty("Data")
	private final Map<String, String> data;

	/**
	 * The method name
	 */
	@JsonProperty("SourceMethod")
	private final String sourceMethod;

	/**
	 * The stack trace
	 */
	@JsonProperty("StackTrace")
	private final List<TraceFrame> stackTrace;

	/**
	 * The cause of this error
	 */
	@JsonProperty("InnerError")
	private final ErrorItem innerError;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the errorType
	 */
	public String getErrorType() {
		return errorType;
	}

	/**
	 * @return the errorTypeCode
	 */
	public String getErrorTypeCode() {
		return errorTypeCode;
	}

	/**
	 * @return the data
	 */
	public Map<String, String> getData() {
		return data;
	}

	/**
	 * @return the sourceMethod
	 */
	public String getSourceMethod() {
		return sourceMethod;
	}

	/**
	 * @return the stackTrace
	 */
	public List<TraceFrame> getStackTrace() {
		return stackTrace;
	}

	/**
	 * @return the innerError
	 */
	public ErrorItem getInnerError() {
		return innerError;
	}

	/**
	 * @return an instance of Builder, based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.message(this.message)
			.errorType(this.errorType)
			.errorTypeCode(this.errorTypeCode)
			.data(this.data)
			.sourceMethod(this.sourceMethod)
			.stackTrace(this.stackTrace)
			.innerError(this.innerError);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private ErrorItem(final Builder builder) {
		this.message = builder.message;
		this.errorType = builder.errorType;
		this.errorTypeCode = builder.errorTypeCode;
		this.data = builder.data;
		this.sourceMethod = builder.sourceMethod;
		this.stackTrace = builder.stackTrace;
		this.innerError = builder.innerError;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * ErrorItem.Builder separates the construction of a ErrorItem from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's message
		 */
		@JsonProperty("Message")
		private String message;

		/**
		 * The builder's errorType
		 */
		@JsonProperty("ErrorType")
		private String errorType;

		/**
		 * The builder's errorTypeCode
		 */
		@JsonProperty("ErrorTypeCode")
		private String errorTypeCode;

		/**
		 * The builder's data
		 */
		@JsonProperty("Data")
		private Map<String,String> data;

		/**
		 * The builder's sourceMethod
		 */
		@JsonProperty("SourceMethod")
		private String sourceMethod;

		/**
		 * The builder's stackTrace
		 */
		@JsonProperty("StackTrace")
		private List<TraceFrame> stackTrace;

		/**
		 * The builder's innerError
		 */
		@JsonProperty("InnerError")
		private ErrorItem innerError;

		/**
		 * Sets the builder's message
		 * @param message The message to be set
		 * @return Reference to the current object
		 */
		public Builder message(final String message) {
			this.message = message;
			return this;
		}

		/**
		 * Sets the builder's errorType
		 * @param errorType The errorType to be set
		 * @return Reference to the current object
		 */
		public Builder errorType(final String errorType) {
			this.errorType = errorType;
			return this;
		}

		/**
		 * Sets the builder's errorTypeCode
		 * @param errorTypeCode The errorTypeCode to be set
		 * @return Reference to the current object
		 */
		public Builder errorTypeCode(final String errorTypeCode) {
			this.errorTypeCode = errorTypeCode;
			return this;
		}

		/**
		 * Sets the builder's data
		 * @param data The data to be set
		 * @return Reference to the current object
		 */
		public Builder data(final Map<String,String> data) {
			this.data = data;
			return this;
		}

		/**
		 * Sets the builder's sourceMethod
		 * @param sourceMethod The sourceMethod to be set
		 * @return Reference to the current object
		 */
		public Builder sourceMethod(final String sourceMethod) {
			this.sourceMethod = sourceMethod;
			return this;
		}

		/**
		 * Sets the builder's stackTrace
		 * @param stackTrace The stackTrace to be set
		 * @return Reference to the current object
		 */
		public Builder stackTrace(final List<TraceFrame> stackTrace) {
			this.stackTrace = stackTrace;
			return this;
		}

		/**
		 * Sets the builder's innerError
		 * @param innerError The innerError to be set
		 * @return Reference to the current object
		 */
		public Builder innerError(final ErrorItem innerError) {
			this.innerError = innerError;
			return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public ErrorItem build() {
			return new ErrorItem(this);
		}
	}
}
