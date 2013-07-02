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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Utility class for cleaning up an HttpURLConnection object
 * 
 * @author Eric Martin
 */
public class HttpUrlConnections {

	/**
	 * Reads and closes the input and error streams for a connection
	 * @param connection The connection
	 */
	public static void readAndCloseInputStreams(final HttpURLConnection connection) {
		
		if (connection != null) {
			
			// read and close the input stream
			
			try {
				readAndCloseInputStream(connection.getInputStream());
			} catch (Throwable t) {
				// do nothing
			}
			
			// read and close the error stream
			
			try {
				readAndCloseInputStream(connection.getErrorStream());
			} catch (Throwable t) {
				// do nothing
			}
		}
	}
	
	/**
	 * Reads all contents from the input stream and closes it
	 * @param stream The input stream
	 */
	public static void readAndCloseInputStream(final InputStream stream) {
		if (stream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			try {
				while (reader.readLine() != null) {
					// do nothing
				}
			} catch (Throwable t) {
				// do nothing
			} finally {
				try {
					reader.close();
				} catch (Throwable t) {
					// do nothing
				}
			}
		}
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private HttpUrlConnections() {
	}
}
