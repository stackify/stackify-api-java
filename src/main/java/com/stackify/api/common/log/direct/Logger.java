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

import org.slf4j.LoggerFactory;

import com.stackify.api.common.log.LogAppender;

/**
 * Logger
 * @author Eric Martin
 */
public class Logger {

	/**
	 * Logger
	 */
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

	/**
	 * Queues a log message to be sent to Stackify
	 * @param level The log level
	 * @param message The log message
	 */
	public static void queueMessage(final String level, final String message) {
		try {
			LogAppender<LogEvent> appender = LogManager.getAppender();

			if (appender != null) {
				LogEvent.Builder builder = LogEvent.newBuilder();
				builder.level(level);
				builder.message(message);
				
				if ((level != null) && ("ERROR".equals(level.toUpperCase()))) {
					StackTraceElement[] stackTrace = new Throwable().getStackTrace();

					if ((stackTrace != null) && (1 < stackTrace.length)) {
						StackTraceElement caller = stackTrace[1];
						builder.className(caller.getClassName());
						builder.methodName(caller.getMethodName());
						builder.lineNumber(caller.getLineNumber());						
					}
				}
				
				appender.append(builder.build());
			}
		} catch (Throwable t) {
			LOGGER.info("Unable to queue message to Stackify Log API service: {} {}", level, message, t);
		}
	}
	
	/**
	 * Queues an exception to be sent to Stackify
	 * @param e The exception
	 */
	public static void queueException(final Throwable e) {
		if (e != null) {
			try {
				LogAppender<LogEvent> appender = LogManager.getAppender();
	
				if (appender != null) {
					appender.append(LogEvent.newBuilder().level("ERROR").message(e.getMessage()).exception(e).build());
				}
			} catch (Throwable t) {
				LOGGER.info("Unable to queue exception to Stackify Log API service: {}", e, t);
			}
		}
	}
	
	/**
	 * Queues an exception to be sent to Stackify
	 * @param level The log level
	 * @param message The log message
	 * @param e The exception
	 */
	public static void queueException(final String level, final String message, final Throwable e) {
		try {
			LogAppender<LogEvent> appender = LogManager.getAppender();

			if (appender != null) {
				appender.append(LogEvent.newBuilder().level(level).message(message).exception(e).build());
			}
		} catch (Throwable t) {
			LOGGER.info("Unable to queue exception to Stackify Log API service: {} {} {}", level, message, e, t);
		}
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private Logger() {
	}
}
