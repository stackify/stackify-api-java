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
package com.stackify.api.common.http;

/**
 * HttpException
 * @author Eric Martin
 */
public class HttpException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -6349485370575426368L;
	
	/**
	 * HTTP status code
	 */
	private final int statusCode;
	
	/**
	 * Constructor
	 * @param statusCode HTTP status code
	 */
	public HttpException(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
