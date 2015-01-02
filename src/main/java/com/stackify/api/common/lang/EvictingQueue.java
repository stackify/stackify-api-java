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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingQueue;

/**
 * EvictingQueue
 * @author Eric Martin
 */
public class EvictingQueue<E> extends ForwardingQueue<E> {

	/**
	 * Maximum size of the queue
	 */
	private final int maxSize;
	
	/**
	 * Deque for the evicting queue implementation
	 */
	private final Queue<E> deque;
	
	/**
	 * Constructor
	 * @param maxSize Maximum size of the queue
	 */
	public EvictingQueue(final int maxSize) {
		Preconditions.checkArgument(0 < maxSize);
		this.maxSize = maxSize;
		this.deque = new ArrayDeque<E>(maxSize);
	}

	/**
	 * @see com.google.common.collect.ForwardingQueue#delegate()
	 */
	@Override
	protected Queue<E> delegate() {
		return deque;
	}

	/**
	 * @see com.google.common.collect.ForwardingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(final E element) {
		return add(element);
	}

	/**
	 * @see com.google.common.collect.ForwardingCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(final E element) {
		Preconditions.checkNotNull(element);
		
		if (deque.size() == maxSize) {
			deque.remove();
		}
		
		deque.add(element);
		
		return true;
	}

	/**
	 * @see com.google.common.collect.ForwardingCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		Preconditions.checkNotNull(collection);
		
		for (E element : collection) {
			add(element);
		}
		
		return true;
	}
}
