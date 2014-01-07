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
package com.stackify.api.common.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * Utility class for functions on MessageDigest objects
 * 
 * @author Eric Martin
 */
public class MessageDigests {

    /**
     * Generates an MD5 hash hex string for the input string
     * @param input The input string
     * @return MD5 hash hex string
     */
    public static String md5Hex(final String input) {
		if (input == null) {
			throw new NullPointerException("String is null");
		}

    	MessageDigest digest = null;
    	
    	try {
    		digest = MessageDigest.getInstance("MD5");
    	} catch (NoSuchAlgorithmException e) {
    		// this should never happen
    		throw new RuntimeException(e);
    	}
        
        byte[] hash = digest.digest(input.getBytes());
        
        return DatatypeConverter.printHexBinary(hash);
    }
	
	/**
	 * Hidden to prevent construction
	 */
	private MessageDigests() {
		// do nothing
	}
}
