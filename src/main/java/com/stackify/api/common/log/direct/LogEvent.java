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
package com.stackify.api.common.log.direct;

import com.google.common.base.Objects;

/**
 * LogEvent
 * @author Eric Martin
 */
public class LogEvent {

	/**
	 * Log level
	 */
	private final String level;
	
	/**
	 * Log message
	 */
	private final String message;
	
	/**
	 * Exception
	 */
	private final Throwable exception;

	/**
	 * Timestamp
	 */
	private final long timestamp;
	
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param builder The Builder object that contains all of the values for initialization
	 */
	private LogEvent(final Builder builder) {
	    this.level = builder.level;
	    this.message = builder.message;
	    this.exception = builder.exception;
	    this.timestamp = System.currentTimeMillis();
	}

	/**
	 * @return A new instance of the Builder
	 */
	public static Builder newBuilder() {
	    return new Builder();
	}

	/**
	 * LogEvent.Builder separates the construction of a LogEvent from its representation
	 */
	public static class Builder {

		/**
		 * The builder's level
		 */
		private String level;
		
		/**
		 * The builder's message
		 */
		private String message;
		
		/**
		 * The builder's exception
		 */
		private Throwable exception;
		
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
		 * Sets the builder's message
		 * @param message The message to be set
		 * @return Reference to the current object
		 */
		public Builder message(final String message) {
		    this.message = message;
		    return this;
		}
		
		/**
		 * Sets the builder's exception
		 * @param exception The exception to be set
		 * @return Reference to the current object
		 */
		public Builder exception(final Throwable exception) {
		    this.exception = exception;
		    return this;
		}
		
		/**
		 * @return A new object constructed from this builder
		 */
		public LogEvent build() {
		    return new LogEvent(this);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return A string representation of the object
	 */
	@Override
	public String toString() {
	    return Objects.toStringHelper(this)
	                  .omitNullValues()
	                  .add("level", level)
	                  .add("message", message)
	                  .add("exception", exception)
	                  .add("timestamp", timestamp)
	                  .toString();
	}
}
