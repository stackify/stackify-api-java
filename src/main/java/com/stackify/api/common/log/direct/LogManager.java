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

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stackify.api.common.ApiClients;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.ApiConfigurations;
import com.stackify.api.common.log.LogAppender;

/**
 * LogManager
 * @author Eric Martin
 */
public class LogManager 
{
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogManager.class);

	/**
	 * True if the Log API has been initialized, false otherwise
	 */
	private static AtomicBoolean INITIALIZED = new AtomicBoolean(false);
	
	/**
	 * API configuration
	 */
	private static ApiConfiguration CONFIG = null;

	/**
	 * Generic log appender
	 */
	private static LogAppender<LogEvent> LOG_APPENDER = null;
		
	/**
	 * @return The log appender
	 */
	public static LogAppender<LogEvent> getAppender() {
		if (INITIALIZED.compareAndSet(false, true)) {
			startup();
		}
		
		return LOG_APPENDER;
	}
		
	/**
	 * Start up the background thread that is processing logs
	 */
	private static synchronized void startup() {
		try {
			CONFIG = ApiConfigurations.fromProperties();
			
			String clientName = ApiClients.getApiClient("/stackify-api-common.properties", "stackify-api-common");

			LOG_APPENDER = new LogAppender<LogEvent>(clientName, new LogEventAdapter(CONFIG.getEnvDetail()));			
			LOG_APPENDER.activate(CONFIG);
		} catch (Throwable t) {
			LOGGER.error("Exception starting Stackify Log API service", t);
		}
	}
	
	/**
	 * Shut down the background thread that is processing logs
	 */
	public static synchronized void shutdown() {
		if (LOG_APPENDER != null) {
			try {
				LOG_APPENDER.close();
			} catch (Throwable t) {
				LOGGER.error("Exception stopping Stackify Log API service", t);
			}
		}
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private LogManager() {
	}
}
