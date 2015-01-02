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
package com.stackify.api.common.lang;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * EvictingQueue JUnit Test
 * @author Eric Martin
 */
public class EvictingQueueTest {
	
	/**
	 * testAdd
	 */
	@Test
	public void testAdd() {
		EvictingQueue<String> queue = new EvictingQueue<String>(3);
		Assert.assertEquals(0, queue.size());
		
		queue.add("a");		
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.add("b");		
		Assert.assertEquals(2, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.add("c");		
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.add("d");		
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("b", queue.peek());
	}
	
	/**
	 * testOffer
	 */
	@Test
	public void testOffer() {
		EvictingQueue<String> queue = new EvictingQueue<String>(3);
		Assert.assertEquals(0, queue.size());
		
		queue.offer("a");		
		Assert.assertEquals(1, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.offer("b");		
		Assert.assertEquals(2, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.offer("c");		
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("a", queue.peek());
		
		queue.offer("d");		
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("b", queue.peek());
	}
	
	/**
	 * testAddAll
	 */
	@Test
	public void testAddAll() {
		EvictingQueue<String> queue = new EvictingQueue<String>(3);
		Assert.assertEquals(0, queue.size());
		
		queue.addAll(Lists.newArrayList("a", "b", "c", "d"));
		Assert.assertEquals(3, queue.size());
		Assert.assertEquals("b", queue.peek());
	}
}
