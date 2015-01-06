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

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * BackgroundService
 * @author Eric Martin
 */
public abstract class BackgroundService extends AbstractScheduledService 
{
	/**
	 * @see com.google.common.util.concurrent.AbstractScheduledService#executor()
	 */
	@Override
	protected ScheduledExecutorService executor() {

		ScheduledExecutorService executor = super.executor();
		
        // Add a listener to shutdown the executor after the service is stopped.
		// This is to work around a feature in Guava 13.0.1 that does not stop the executor when the service stops.
	       
		addListener(new ShutdownListener(executor), MoreExecutors.sameThreadExecutor());

        return executor;
	}
}
