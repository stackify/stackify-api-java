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
package com.stackify.api.common.concurrent;

import java.util.concurrent.ScheduledExecutorService;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

/**
 * ShutdownListener
 * @author Eric Martin
 */
public class ShutdownListener implements Listener {

	/**
	 * Executor
	 */
	private final ScheduledExecutorService executor;
	
	/**
	 * Constructor
	 * @param executor Executor
	 */
	public ShutdownListener(final ScheduledExecutorService executor) {
		Preconditions.checkNotNull(executor);
		this.executor = executor;
	}
	
	/**
	 * @see com.google.common.util.concurrent.Service.Listener#starting()
	 */
	@Override
	public void starting() {
	}

	/**
	 * @see com.google.common.util.concurrent.Service.Listener#running()
	 */
	@Override
	public void running() {
	}

	/**
	 * @see com.google.common.util.concurrent.Service.Listener#stopping(com.google.common.util.concurrent.Service.State)
	 */
	@Override
	public void stopping(final State from) {
	}

	/**
	 * @see com.google.common.util.concurrent.Service.Listener#terminated(com.google.common.util.concurrent.Service.State)
	 */
	@Override
	public void terminated(final State from) {
		executor.shutdown();
	}

	/**
	 * @see com.google.common.util.concurrent.Service.Listener#failed(com.google.common.util.concurrent.Service.State, java.lang.Throwable)
	 */
	@Override
	public void failed(final State from, final Throwable failure) {
		executor.shutdown();
	}
}
