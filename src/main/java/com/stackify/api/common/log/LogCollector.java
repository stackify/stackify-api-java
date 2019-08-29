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

import com.stackify.api.AppIdentity;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.AppIdentityService;
import com.stackify.api.common.collect.SynchronizedEvictingQueue;
import com.stackify.api.common.util.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * LogCollector
 *
 * @author Eric Martin
 */
@Slf4j
public class LogCollector {

    /**
     * Max batch size of log messages to be sent in a single request
     */
    private static final int MAX_BATCH = 100;

    private static final String DEFAULT_PLATFORM = "java";

    /**
     * The logger (project) name
     */
    private final String logger;

    /**
     * The logger platform (log type)
     */
    private final String platform;

    /**
     * Environment details
     */
    private final EnvironmentDetail envDetail;

    /**
     * Application identity service
     */
    private final AppIdentityService appIdentityService;

    /**
     * The queue of objects to be transmitted
     */
    private final Queue<LogMsg> queue = new SynchronizedEvictingQueue<LogMsg>(10000);

    private final RetryPolicy<LogMsgGroup> retryPolicy = new RetryPolicy<LogMsgGroup>()
            .withDelay(Duration.ofSeconds(10))
            .withMaxRetries(3);

    /**
     * Constructor
     *
     * @param platform  Logger platform (log type)
     * @param logger    The logger (project) name
     * @param envDetail Environment details
     */
    public LogCollector(@NonNull final String platform,
                        @NonNull final String logger,
                        @NonNull final EnvironmentDetail envDetail,
                        @NonNull final AppIdentityService appIdentityService) {
        this.platform = platform;
        this.logger = logger;
        this.envDetail = envDetail;
        this.appIdentityService = appIdentityService;
    }

    /**
     * Constructor
     *
     * @param logger    The logger (project) name
     * @param envDetail Environment details
     */
    public LogCollector(final String logger,
                        final EnvironmentDetail envDetail,
                        final AppIdentityService appIdentityService) {
        this(DEFAULT_PLATFORM, logger, envDetail, appIdentityService);
    }

    /**
     * Queues logMsg to be sent
     *
     * @param logMsg The log message
     */
    public void addLogMsg(final LogMsg logMsg) {
        Preconditions.checkNotNull(logMsg);
        queue.offer(logMsg);
    }

    /**
     * Flushes the queue by sending all messages to Stackify
     *
     * @param logTransport The LogMsgGroup sender
     * @return The number of messages sent to Stackify
     * @throws Exception
     */
    public int flush(final LogTransport logTransport) throws Exception {

        int numSent = 0;
        int maxToSend = queue.size();

        if (0 < maxToSend) {
            AppIdentity appIdentity = appIdentityService.getAppIdentity();

            while (numSent < maxToSend && queue.size() > 0) {

                // get the next batch of messages
                int batchSize = Math.min(maxToSend - numSent, MAX_BATCH);

                List<LogMsg> batch = new ArrayList<LogMsg>(batchSize);

                for (int i = 0; i < batchSize; ++i) {
                    batch.add(queue.remove());
                }

                if (batch.size() > 0) {
                    // build the log message group
                    LogMsgGroup group = createLogMessageGroup(batch, platform, logger, envDetail, appIdentity);

                    send(logTransport, group);

                    // next iteration
                    numSent += batchSize;
                }
            }
        }

        return numSent;
    }

    /**
     * Send group to transport - with retry policy configured
     */
    private void send(final LogTransport logTransport,
                      final LogMsgGroup group) {
        Failsafe.with(retryPolicy).runAsync(() -> logTransport.send(group));
    }

    /**
     * @param batch       - a bunch of messages that should be sent over the wire
     * @param platform    - platform (log type)
     * @param logger      - logger (project) name
     * @param envDetail   - environment details
     * @param appIdentity - application identity
     * @return LogMessage group object with
     */
    private LogMsgGroup createLogMessageGroup(final List<LogMsg> batch,
                                              final String platform,
                                              final String logger,
                                              final EnvironmentDetail envDetail,
                                              final AppIdentity appIdentity) {
        final LogMsgGroup.Builder groupBuilder = LogMsgGroup.newBuilder();

        groupBuilder
                .platform(platform)
                .logger(logger)
                .serverName(envDetail.getDeviceName())
                .env(envDetail.getConfiguredEnvironmentName())
                .appName(envDetail.getConfiguredAppName())
                .appLoc(envDetail.getAppLocation());

        if (appIdentity != null) {
            groupBuilder
                    .cdId(appIdentity.getDeviceId())
                    .cdAppId(appIdentity.getDeviceAppId())
                    .appNameId(appIdentity.getAppNameId())
                    .appEnvId(appIdentity.getAppEnvId())
                    .envId(appIdentity.getEnvId())
                    .env(appIdentity.getEnv());

            if ((appIdentity.getAppName() != null) && (0 < appIdentity.getAppName().length())) {
                groupBuilder.appName(appIdentity.getAppName());
            }
        }

        groupBuilder.msgs(batch);

        return groupBuilder.build();
    }
}
