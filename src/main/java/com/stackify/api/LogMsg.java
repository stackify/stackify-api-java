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
 * LogMsg
 * @author Eric Martin
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = LogMsg.Builder.class)
public class LogMsg {

	/**
	 * The log message
	 */
	@JsonProperty("Msg")
	private final String msg;

	/**
	 * Extra contextual data from the log message
	 */
	@JsonProperty("data")
	private final String data;

	/**
	 * The error/exception details
	 */
	@JsonProperty("Ex")
	private final StackifyError ex;

	/**
	 * The thread name
	 */
	@JsonProperty("Th")
	private final String th;

	/**
	 * Unix timestamp of the log message
	 */
	@JsonProperty("EpochMs")
	private final Long epochMs;

	/**
	 * Log level of the message
	 */
	@JsonProperty("Level")
	private final String level;

	/**
	 * Transaction id
	 */
	@JsonProperty("TransID")
	private final String transId;

	/**
	 * Source method name
	 */
	@JsonProperty("SrcMethod")
	private final String srcMethod;

	/**
	 * Source line number
	 */
	@JsonProperty("SrcLine")
	private final Integer srcLine;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return the ex
	 */
	public StackifyError getEx() {
		return ex;
	}

	/**
	 * @return the th
	 */
	public String getTh() {
		return th;
	}

	/**
	 * @return the epochMs
	 */
	public Long getEpochMs() {
		return epochMs;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @return the transId
	 */
	public String getTransId() {
		return transId;
	}

	/**
	 * @return the srcMethod
	 */
	public String getSrcMethod() {
		return srcMethod;
	}

	/**
	 * @return the srcLine
	 */
	public Integer getSrcLine() {
		return srcLine;
	}

	/**
	 * @return An instance of Builder, based on current class state
	 */
	public Builder toBuilder() {
		return newBuilder()
			.msg(this.msg)
			.data(this.data)
			.ex(this.ex)
			.th(this.th)
			.epochMs(this.epochMs)
			.level(this.level)
			.transId(this.transId)
			.srcMethod(this.srcMethod)
			.srcLine(this.srcLine);
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private LogMsg(final Builder builder) {
		this.msg = builder.msg;
		this.data = builder.data;
		this.ex = builder.ex;
		this.th = builder.th;
		this.epochMs = builder.epochMs;
		this.level = builder.level;
		this.transId = builder.transId;
		this.srcMethod = builder.srcMethod;
		this.srcLine = builder.srcLine;
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
	    return new Builder();
	}

	/**
	 * LogMsg.Builder separates the construction of a LogMsg from its representation
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Builder {

		/**
		 * The builder's msg
		 */
		@JsonProperty("Msg")
		private String msg;

		/**
		 * The builder's data
		 */
		@JsonProperty("data")
		private String data;

		/**
		 * The builder's ex
		 */
		@JsonProperty("Ex")
		private StackifyError ex;

		/**
		 * The builder's th
		 */
		@JsonProperty("Th")
		private String th;

		/**
		 * The builder's epochMs
		 */
		@JsonProperty("EpochMs")
		private Long epochMs;

		/**
		 * The builder's level
		 */
		@JsonProperty("Level")
		private String level;

		/**
		 * The builder's transId
		 */
		@JsonProperty("TransID")
		private String transId;

		/**
		 * The builder's srcMethod
		 */
		@JsonProperty("SrcMethod")
		private String srcMethod;

		/**
		 * The builder's srcLine
		 */
		@JsonProperty("SrcLine")
		private Integer srcLine;

		/**
		 * Sets the builder's msg
		 * @param msg The msg to be set
		 * @return Reference to the current object
		 */
		public Builder msg(final String msg) {
		    this.msg = msg;
		    return this;
		}

		/**
		 * Sets the builder's data
		 * @param data The data to be set
		 * @return Reference to the current object
		 */
		public Builder data(final String data) {
		    this.data = data;
		    return this;
		}

		/**
		 * Sets the builder's ex
		 * @param ex The ex to be set
		 * @return Reference to the current object
		 */
		public Builder ex(final StackifyError ex) {
		    this.ex = ex;
		    return this;
		}

		/**
		 * Sets the builder's th
		 * @param th The th to be set
		 * @return Reference to the current object
		 */
		public Builder th(final String th) {
		    this.th = th;
		    return this;
		}

		/**
		 * Sets the builder's epochMs
		 * @param epochMs The epochMs to be set
		 * @return Reference to the current object
		 */
		public Builder epochMs(final Long epochMs) {
		    this.epochMs = epochMs;
		    return this;
		}

		/**
		 * Sets the builder's level
		 * @param level The level to be set
		 * @return Reference to the current object
		 */
		public Builder level(final String level) {
		    this.level = level;
		    return this;
		}

		/**
		 * Sets the builder's transId
		 * @param transId The transId to be set
		 * @return Reference to the current object
		 */
		public Builder transId(final String transId) {
		    this.transId = transId;
		    return this;
		}

		/**
		 * Sets the builder's srcMethod
		 * @param srcMethod The srcMethod to be set
		 * @return Reference to the current object
		 */
		public Builder srcMethod(final String srcMethod) {
		    this.srcMethod = srcMethod;
		    return this;
		}

		/**
		 * Sets the builder's srcLine
		 * @param srcLine The srcLine to be set
		 * @return Reference to the current object
		 */
		public Builder srcLine(final Integer srcLine) {
		    this.srcLine = srcLine;
		    return this;
		}

		/**
		 * @return A new object constructed from this builder
		 */
		public LogMsg build() {
		    return new LogMsg(this);
		}
	}
}
