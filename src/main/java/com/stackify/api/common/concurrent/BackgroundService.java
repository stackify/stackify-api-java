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

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BackgroundService
 * @author Eric Martin
 */
public abstract class BackgroundService
{
	/**
	 * The service logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundService.class);

	/**
	 * Executor service
	 */
	private ScheduledExecutorService executorService;

	/**
	 * Next iteration
	 */
	private ScheduledFuture<Void> currentFuture;

	/**
	 * Lock to protect start/stop/iteration/reschedule
	 */
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Start the background service
	 * @throws Exception
	 */
	protected abstract void startUp() throws Exception;

	/**
	 * Run one iteration of the background service
	 * @throws Exception
	 */
	protected abstract void runOneIteration() throws Exception;

	/**
	 * @return The next schedule delay (between iterations) in milliseconds
	 */
	protected abstract long getNextScheduleDelayMilliseconds();

	/**
	 * Shut down the background service
	 * @throws Exception
	 */
	protected abstract void shutDown() throws Exception;

	/**
	 * Start the background service/thread
	 */
	public void start() {

		lock.lock();

		try {
			executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("StackifyLoggingBackgroundThread");
                    thread.setDaemon(true);
                    return thread;
                }
            });

			try {
				startUp();
			} catch (Throwable t) {
				LOGGER.info("Exception in service start up", t);
			}

			currentFuture = executorService.schedule(new RunOneIterationAndReschedule(), 0, TimeUnit.MILLISECONDS);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @return True if the background service is running, false otherwise
	 */
	public boolean isRunning() {
		if (executorService != null) {
			if (!executorService.isShutdown() && !executorService.isTerminated()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Stops the background service/thread
	 */
	public void stop() {

		lock.lock();

		try {
			currentFuture.cancel(false);

			try {
				shutDown();
			} catch (Throwable t) {
				LOGGER.info("Exception in service shut down", t);
			}

			try {
				executorService.shutdown();
				executorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (Throwable t) {
				LOGGER.info("Exception in service termination", t);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * RunOneIterationAndReschedule
	 */
	private class RunOneIterationAndReschedule implements Callable<Void> {

		/**
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Void call() throws Exception {

			// run one iteration

			lock.lock();

			try {
				runOneIteration();
			} catch (Throwable t) {
				LOGGER.info("Exception in iteration", t);
			} finally {
				lock.unlock();
			}

			// reschedule

			lock.lock();

			try {
				if (!currentFuture.isCancelled()) {
					long nextDelay = getNextScheduleDelayMilliseconds();
					currentFuture = executorService.schedule(this, nextDelay, TimeUnit.MILLISECONDS);
				}
			} catch (Throwable t) {
				LOGGER.info("Exception rescheduling iteration", t);
			} finally {
				lock.unlock();
			}

			// done

			return null;
		}
	}
}
