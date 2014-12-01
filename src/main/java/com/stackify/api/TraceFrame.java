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
 * Each TraceFrame object represents a single stack frame in a stack trace
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * TraceFrame.Builder builder = TraceFrame.newBuilder();
 * builder.codeFileName(stackTraceElement.getFileName());
 * ...
 * TraceFrame traceFrame = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = TraceFrame.Builder.class)
public class TraceFrame {

	/**
	 * The file name
	 */
	@JsonProperty("CodeFileName")
	private final String codeFileName;

	/**
	 * The line number
	 */
	@JsonProperty("LineNum")
	private final Integer lineNum;

	/**
	 * The method name
	 */
	@JsonProperty("Method")
	private final String method;

	/**
	 * @return the codeFileName
	 */
	public String getCodeFileName() {
		return codeFileName;
	}

	/**
	 * @return the lineNum
	 */
	public Integer getLineNum() {
		return lineNum;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return an instance of Builder based on current state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.codeFileName(this.codeFileName)
			.lineNum(this.lineNum)
			.method(this.method);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private TraceFrame(final Builder builder) {
		this.codeFileName = builder.codeFileName;
		this.lineNum = builder.lineNum;
		this.method = builder.method;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * TraceFrame.Builder separates the construction of a TraceFrame from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's codeFileName
		 */
		@JsonProperty("CodeFileName")
		private String codeFileName;

		/**
		 * The builder's lineNum
		 */
		@JsonProperty("LineNum")
		private Integer lineNum;

		/**
		 * The builder's method
		 */
		@JsonProperty("Method")
		private String method;

		/**
		 * Sets the builder's codeFileName
		 * @param codeFileName The codeFileName to be set
		 * @return Reference to the current object
		 */
		public Builder codeFileName(final String codeFileName) {
			this.codeFileName = codeFileName;
			return this;
		}

		/**
		 * Sets the builder's lineNum
		 * @param lineNum The lineNum to be set
		 * @return Reference to the current object
		 */
		public Builder lineNum(final Integer lineNum) {
			this.lineNum = lineNum;
			return this;
		}

		/**
		 * Sets the builder's method
		 * @param method The method to be set
		 * @return Reference to the current object
		 */
		public Builder method(final String method) {
			this.method = method;
			return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public TraceFrame build() {
			return new TraceFrame(this);
		}
	}
}
