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

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * CharStreams
 * @author Eric Martin
 */
public class CharStreams {

	/**
	 * Read char stream to a string
	 * @param r Readable
	 * @return String
	 * @throws IOException
	 */
	public static String toString(final Readable r) throws IOException {
		Preconditions.checkNotNull(r);
		
	    StringBuilder sb = new StringBuilder();

	    CharBuffer buf = CharBuffer.allocate(0x800);

	    while (r.read(buf) != -1) {
	    	buf.flip();
	    	sb.append(buf);
	    	buf.clear();
	    }
	    
	    return sb.toString();
	}

	/**
	 * Hidden to prevent construction
	 */
	private CharStreams() {
	}
}
