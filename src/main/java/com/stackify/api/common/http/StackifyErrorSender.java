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
package com.stackify.api.common.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.Preconditions;
import com.stackify.api.StackifyError;
import com.stackify.api.json.StackifyErrorConverter;

/**
 * Utility class for sending a list of StackifyError objects to Stackify
 * 
 * @author Eric Martin
 */
public class StackifyErrorSender {

	/**
	 * The API URL
	 */
	private final String apiUrl;
	
	/**
	 * The API key
	 */
	private final String apiKey;

	/**
	 * Writes Stackify errors to an output stream
	 */
	private final StackifyErrorConverter errorConverter;
	
	/**
	 * Default constructor
	 * @param apiUrl The API URL
	 * @param apiKey The API key
	 * @param errorWriter StackifyError to JSON Writer
	 * @throws NullPointerException If errorWriter is null
	 */
	public StackifyErrorSender(final String apiUrl, final String apiKey, final StackifyErrorConverter errorWriter) {
		Preconditions.checkNotNull(errorWriter, "StackifyErrorConverter is null");
		Preconditions.checkNotNull(apiUrl, "API URL is null");
		Preconditions.checkArgument(!apiUrl.isEmpty(), "API URL is empty");
		Preconditions.checkNotNull(apiKey, "API key is null");
		Preconditions.checkArgument(!apiKey.isEmpty(), "API key is empty");

		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
		this.errorConverter = errorWriter;
	}
	
	/**
	 * Send the error to Stackify
	 * @param errors The errors
	 * @throws IOException If an I/O exception occurs.
	 * @throws NullPointerException If apiUrl, apiKey, or errors is null
	 * @throws IllegalArgumentException If apiUrl, apiKey, or errors is empty
	 */
	public int send(final List<StackifyError> errors) throws IOException {
		if (errors == null) {
			throw new NullPointerException("StackifyError list is null");
		}
		
		if (errors.isEmpty()) {
			throw new IllegalArgumentException("StackifyError list is empty");
		}
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(apiUrl);
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Encoding", "gzip");
			connection.setRequestProperty("X-Stackify-Key", apiKey);
			connection.setRequestProperty("X-Stackify-PV", "V1");
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
						
			OutputStream stream = new BufferedOutputStream(new GZIPOutputStream(connection.getOutputStream()));
			errorConverter.writeToStream(errors, stream);
			stream.flush();
			stream.close();
									
			return connection.getResponseCode();
		} finally {
			HttpUrlConnections.readAndCloseInputStreams(connection);
		}
	}	
}
