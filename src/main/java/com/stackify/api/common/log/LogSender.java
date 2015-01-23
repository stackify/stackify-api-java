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

import java.io.IOException;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.http.HttpException;
import com.stackify.api.common.util.Preconditions;

/**
 * LogSender
 * @author Eric Martin
 */
public class LogSender {
	
	/**
	 * The API configuration
	 */
	private final ApiConfiguration apiConfig;

	/**
	 * JSON object mapper
	 */
	private final ObjectMapper objectMapper;
	
	/**
	 * Default constructor
	 * @param apiConfig API configuration
	 * @param objectMapper JSON object mapper
	 */
	public LogSender(final ApiConfiguration apiConfig, final ObjectMapper objectMapper) {
		Preconditions.checkNotNull(apiConfig);
		Preconditions.checkNotNull(objectMapper);

		this.apiConfig = apiConfig;
		this.objectMapper = objectMapper;
	}
	
	/**
	 * Sends a group of log messages to Stackify
	 * @param group The log message group
	 * @return The HTTP status code returned from the HTTP POST
	 * @throws IOException
	 */
	public int send(final LogMsgGroup group) throws IOException {
		Preconditions.checkNotNull(group);
		
		// convert to json bytes
		
		byte[] jsonBytes = objectMapper.writer().writeValueAsBytes(group);
		
		// post to stackify
		
		HttpClient httpClient = new HttpClient(apiConfig);
		
		int statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		
		try {
			httpClient.post("/Log/Save", jsonBytes, true);
			statusCode = HttpURLConnection.HTTP_OK;
		} catch (HttpException e) {
			statusCode = e.getStatusCode();
		}
		
		return statusCode;
	}
}
