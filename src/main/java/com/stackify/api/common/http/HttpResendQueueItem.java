/*
 * Copyright 2015 Stackify
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
 * HttpResendQueueItem
 * @author Eric Martin
 */
public class HttpResendQueueItem {

	/**
	 * JSON bytes
	 */
	private final byte[] jsonBytes;
	
	/**
	 * Number of failures for the item;
	 */
	private int numFailures;
	
	/**
	 * Constructor
	 * @param jsonBytes JSON bytes
	 */
	public HttpResendQueueItem(final byte[] jsonBytes) {
		this.jsonBytes = jsonBytes;
		this.numFailures = 1;
	}

	/**
	 * @return the jsonBytes
	 */
	public byte[] getJsonBytes() {
		return jsonBytes;
	}

	/**
	 * @return the numFailures
	 */
	public int getNumFailures() {
		return numFailures;
	}
	
	/**
	 * Increment the number of failures
	 */
	public void failed() {
		++numFailures;
	}
}
