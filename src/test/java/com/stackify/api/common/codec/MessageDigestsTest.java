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

import org.junit.Assert;
import org.junit.Test;

/**
 * MessageDigests JUnit Test
 * 
 * @author Eric Martin
 */
public class MessageDigestsTest {

	/**
	 * testMd5HexWithNull
	 */
	@Test(expected = NullPointerException.class)
	public void testMd5HexWithNull() {
		MessageDigests.md5Hex(null);
	}
	
	/**
	 * testMd5HexWithNull
	 */
	@Test
	public void testMd5Hex() {
		Assert.assertEquals("9E107D9D372BB6826BD81D3542A419D6", MessageDigests.md5Hex("The quick brown fox jumps over the lazy dog"));
		Assert.assertEquals("E4D909C290D0FB1CA068FFADDF22CBD0", MessageDigests.md5Hex("The quick brown fox jumps over the lazy dog."));
		Assert.assertEquals("D41D8CD98F00B204E9800998ECF8427E", MessageDigests.md5Hex(""));
	}
}
