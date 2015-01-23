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

import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.spi.MDCAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackify.api.WebRequestDetail;

/**
 * ServletLogContext
 * @author Eric Martin
 */
public class ServletLogContext {

	/**
	 * STACKIFY_TRANSACTIONID
	 */
	private static final String TRANSACTION_ID = "STACKIFY_TRANSACTIONID";

	/**
	 * STACKIFY_USER
	 */
	private static final String USER = "STACKIFY_USER";

	/**
	 * STACKIFY_WEBREQUEST
	 */
	private static final String WEB_REQUEST = "STACKIFY_WEBREQUEST";

	/**
	 * JSON object mapper
	 */
	private static final ObjectMapper JSON = new ObjectMapper();

	/**
	 * SLF4J's basic MDC implementation
	 */
	private static final MDCAdapter MDC = new BasicMDCAdapter();

	/**
	 * @return The transaction id from the logging context
	 */
	public static String getTransactionId() {
		
		String value = MDC.get(TRANSACTION_ID);
		
		if ((value != null) && (0 < value.length())) {
			return value;
		}
		
		return null;
	}
	
	/**
	 * Sets the transaction id in the logging context
	 * @param transactionId The transaction id
	 */
	public static void putTransactionId(final String transactionId) {
		if ((transactionId != null) && (0 < transactionId.length())) {
			MDC.put(TRANSACTION_ID, transactionId);
		}
	}
	
	/**
	 * @return The user from the logging context
	 */
	public static String getUser() {
		
		String value = MDC.get(USER);
		
		if ((value != null) && (0 < value.length())) {
			return value;
		}
		
		return null;
	}
	
	/**
	 * Sets the user in the logging context
	 * @param user The user
	 */
	public static void putUser(final String user) {
		if ((user != null) && (0 < user.length())) {
			MDC.put(USER, user);
		}
	}
	
	/**
	 * @return The web request from the logging context
	 */
	public static WebRequestDetail getWebRequest() {
		
		String value = MDC.get(WEB_REQUEST);
		
		if ((value != null) && (0 < value.length())) {
			try {
				return JSON.readValue(value, WebRequestDetail.class);
			} catch (Throwable t) {
				// do nothing
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the web request in the logging context
	 * @param webRequest The web request
	 */
	public static void putWebRequest(final WebRequestDetail webRequest) {
		if (webRequest != null) {
			try {
				String value = JSON.writeValueAsString(webRequest);
				MDC.put(WEB_REQUEST, value);
			} catch (Throwable t) {
				// do nothing
			}
		}
	}
	
	/**
	 * Removes our properties from the MDC
	 */
	public static void clear() {
		MDC.clear();
	}
}
