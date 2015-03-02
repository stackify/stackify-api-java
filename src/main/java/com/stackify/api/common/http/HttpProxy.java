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

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpProxy
 * @author Eric Martin
 */
public class HttpProxy {

	/**
	 * The logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpProxy.class);

	/**
	 * @return Proxy from system settings
	 */
	public static Proxy fromSystemProperties() {
		try {			
			String proxyHost = System.getProperty("https.proxyHost");
			String proxyPort = System.getProperty("https.proxyPort");
			
			if ((proxyHost != null) && (!proxyHost.isEmpty())) {
				if ((proxyPort != null) && (!proxyPort.isEmpty())) {
					return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
				}
			}
		} catch (Throwable t) {
			LOGGER.info("Unable to read HTTP proxy information from system properties", t);
		}
		
		return Proxy.NO_PROXY;
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private HttpProxy() {
	}
}
