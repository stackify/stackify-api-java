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
package com.stackify.api.common.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Maps
 * @author Eric Martin
 */
public class Maps {

	/**
	 * Converts properties to a String-String map
	 * @param props Properties
	 * @return String-String map of properties
	 */
	public static Map<String, String> fromProperties(final Properties props) {
		Map<String, String> propMap = new HashMap<String, String>();
		
		for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			propMap.put(key, props.getProperty(key));
		}
				
		return Collections.unmodifiableMap(propMap);
	}

	/**
	 * Hidden to prevent construction
	 */
	private Maps() {
	}
}
