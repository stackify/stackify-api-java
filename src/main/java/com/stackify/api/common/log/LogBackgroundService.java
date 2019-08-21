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
package com.stackify.api.common.log;

import com.stackify.api.common.concurrent.BackgroundService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * LogSenderService
 *
 * @author Eric Martin
 */
@Slf4j
public class LogBackgroundService extends BackgroundService {

    /**
     * The scheduler that determines delay timing after errors
     */
    private final LogBackgroundServiceScheduler scheduler = new LogBackgroundServiceScheduler();

    /**
     * The LogMsg collector
     */
    private final LogCollector collector;

    /**
     * Handles sending logs to destination
     */
    private final LogTransport logTransport;

    /**
     * Constructor
     *
     * @param collector    The LogMsg collector
     * @param logTransport Handles sending logs to destination
     */
    public LogBackgroundService(@NonNull final LogCollector collector,
                                @NonNull final LogTransport logTransport) {
        this.collector = collector;
        this.logTransport = logTransport;
    }

    @Override
    protected void startUp() {
    }

    @Override
    protected long getNextScheduleDelayMilliseconds() {
        return scheduler.getScheduleDelay();
    }

    @Override
    protected void runOneIteration() {
        try {
            int numSent = collector.flush(logTransport);
            scheduler.update(numSent);
        } catch (Throwable t) {
            log.info("Exception running Stackify_LogBackgroundService", t);
            scheduler.update(t);
        }
    }

    @Override
    protected void shutDown() {
        try {
            collector.flush(logTransport);
        } catch (Throwable t) {
            log.info("Exception flushing log collector during shut down", t);
        }
    }
}
