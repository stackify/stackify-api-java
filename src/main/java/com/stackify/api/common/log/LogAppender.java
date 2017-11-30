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
package com.stackify.api.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackify.api.LogMsg;
import com.stackify.api.StackifyError;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.AppIdentityService;
import com.stackify.api.common.error.ErrorGovernor;
import com.stackify.api.common.mask.Masker;
import com.stackify.api.common.util.Preconditions;
import lombok.NonNull;

import java.io.Closeable;
import java.io.IOException;

/**
 * LogAppender
 * @author Eric Martin
 */
public class LogAppender<T> implements Closeable {

	/**
	 * Internal package prefix
	 */
	private static final String COM_DOT_STACKIFY = "com.stackify.";
	
	/**
	 * Logger project name
	 */
	private final String logger;

	/**
	 * Maps from specific log implementation events to our API
	 */
	private final EventAdapter<T> eventAdapter;

	/**
	 * Collector for LogMsg objects that need to be sent to Stackify
	 */
	private LogCollector collector = null;

	/**
	 * Background thread for sending log events to Stackify
	 */
	private LogBackgroundService backgroundService = null;

	/**
	 * Client side error governor to suppress duplicate errors
	 */
	private final ErrorGovernor errorGovernor = new ErrorGovernor();
 
	private final Masker masker;

	private final boolean skipJson;

	/**
	 * Constructor
	 * @param logger Logger project name
	 */
	public LogAppender(@NonNull final String logger,
					   @NonNull final EventAdapter<T> eventAdapter,
					   final Masker masker,
					   final boolean skipJson) {
		this.logger = logger;
		this.eventAdapter = eventAdapter;
		this.masker = masker;
		this.skipJson = skipJson;
	}

	/**
	 * Constructor
	 * @param logger Logger project name
	 */
	public LogAppender(@NonNull final String logger,
					   @NonNull final EventAdapter<T> eventAdapter,
					   final Masker masker) {
		this(logger, eventAdapter, masker, false);
	}

	/**
	 * Activates the appender
	 * @param apiConfig API configuration
	 */
	public void activate(final ApiConfiguration apiConfig) {
		Preconditions.checkNotNull(apiConfig);
		Preconditions.checkNotNull(apiConfig.getApiUrl());
		Preconditions.checkArgument(!apiConfig.getApiUrl().isEmpty());
		Preconditions.checkNotNull(apiConfig.getApiKey());
		Preconditions.checkArgument(!apiConfig.getApiKey().isEmpty());

		// Single JSON object mapper for all services

		ObjectMapper objectMapper = new ObjectMapper();

		// build the app identity service

		AppIdentityService appIdentityService = new AppIdentityService(apiConfig, objectMapper);

		// build the services for collecting and sending log messages

		this.collector = new LogCollector(logger, apiConfig.getEnvDetail(), appIdentityService);

		LogSender sender = new LogSender(apiConfig, objectMapper, this.masker, this.skipJson);

		// build the background service to asynchronously post errors to Stackify
		// startup the background service

		this.backgroundService = new LogBackgroundService(collector, sender);
		this.backgroundService.start();
	}

	/**
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (backgroundService != null) {
			backgroundService.stop();
		}
	}

	/**
	 * Adds the log message to the collector
	 * @param event
	 */
	public void append(final T event) {

		// make sure we can append the log message

		if (backgroundService == null) {
			return;
		}

		if (!backgroundService.isRunning()) {
			return;
		}

		// skip internal logging
		
		String className = eventAdapter.getClassName(event);
		
		if (className != null) {
			if (className.startsWith(COM_DOT_STACKIFY)) {
				return;
			}
		}
		
		// build the log message and queue it to be sent to Stackify

		Throwable exception = eventAdapter.getThrowable(event);

		StackifyError error = null;

		if ((exception != null) || (eventAdapter.isErrorLevel(event))) {
			StackifyError e = eventAdapter.getStackifyError(event, exception);

			if (errorGovernor.errorShouldBeSent(e)) {
				error = e;
			}
		}

		LogMsg logMsg = eventAdapter.getLogMsg(event, error);

		collector.addLogMsg(logMsg);
	}
}
