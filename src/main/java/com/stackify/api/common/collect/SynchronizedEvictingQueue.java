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
package com.stackify.api.common.collect;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import com.stackify.api.common.util.Preconditions;

/**
 * SynchronizedEvictingQueue
 * @author Eric Martin
 */
public class SynchronizedEvictingQueue<E> implements Queue<E> {

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
	public SynchronizedEvictingQueue(final int maxSize) {
		Preconditions.checkArgument(0 < maxSize);
		this.maxSize = maxSize;
		this.deque = new ArrayDeque<E>(maxSize);
	}

	/**
	 * @see java.util.Collection#size()
	 */
	@Override
	public synchronized int size() {
		return deque.size();
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public synchronized boolean isEmpty() {
		return deque.isEmpty();
	}

	/**
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public synchronized boolean contains(Object o) {
		return deque.contains(o);
	}

	/**
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public synchronized Iterator<E> iterator() {
		return deque.iterator();
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public synchronized Object[] toArray() {
		return deque.toArray();
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return deque.toArray(a);
	}

	/**
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public synchronized boolean remove(Object o) {
		return deque.remove(o);
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return deque.containsAll(c);
	}

	/**
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(Collection<? extends E> c) {
		Preconditions.checkNotNull(c);
		
		for (E e : c) {
			add(e);
		}
		
		return true;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		return deque.removeAll(c);
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		return deque.retainAll(c);
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public synchronized void clear() {
		deque.clear();
	}

	/**
	 * @see java.util.Queue#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(E e) {
		Preconditions.checkNotNull(e);
		
		if (deque.size() == maxSize) {
			deque.remove();
		}
		
		deque.add(e);
		
		return true;
	}

	/**
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	@Override
	public synchronized boolean offer(E e) {
		return add(e);
	}

	/**
	 * @see java.util.Queue#remove()
	 */
	@Override
	public synchronized E remove() {
		return deque.remove();
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	@Override
	public synchronized E poll() {
		return deque.poll();
	}

	/**
	 * @see java.util.Queue#element()
	 */
	@Override
	public synchronized E element() {
		return deque.element();
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	@Override
	public synchronized E peek() {
		return deque.peek();
	}
}
