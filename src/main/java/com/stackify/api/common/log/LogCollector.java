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

import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.collect.SynchronizedEvictingQueue;
import com.stackify.api.common.lang.Threads;
import com.stackify.api.common.oauth.OAuth2Service;
import com.stackify.api.common.oauth.OAuth2Token;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

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

    /**
     * Try posting message 3 times before skipping it
     */
    private static final int MAX_POST_ATTEMPTS = 3;

    /**
     * Environment details
     */
    private final ApiConfiguration apiConfiguration;

    private final OAuth2Service oAuth2Service;

    private final String apiClientName;

    /**
     * The queue of objects to be transmitted
     */
    private final Queue<LogMsg> queue = new SynchronizedEvictingQueue<LogMsg>(10000);

    final Queue<RetryQueueItem<LogMsgGroup>> retryQueue = new SynchronizedEvictingQueue<RetryQueueItem<LogMsgGroup>>(20);

    /**
     * Constructor
     *
     * @param apiClientName    API Client Name
     * @param apiConfiguration API Configuration
     * @param oAuth2Service    OAuth Service
     */
    public LogCollector(@NonNull final String apiClientName,
                        @NonNull final ApiConfiguration apiConfiguration,
                        @NonNull final OAuth2Service oAuth2Service) {
        this.apiClientName = apiClientName;
        this.apiConfiguration = apiConfiguration;
        this.oAuth2Service = oAuth2Service;
    }

    /**
     * Queues logMsg to be sent
     *
     * @param logMsg The log message
     */
    public void addLogMsg(@NonNull final LogMsg logMsg) {
        queue.offer(logMsg);
    }

    /**
     * Flushes the queue by sending all messages to Stackify
     *
     * @param sender The LogMsgGroup sender
     * @return The number of messages sent to Stackify
     */
    public int flush(final LogSender sender) throws IOException {

        int numSent = 0;
        int maxToSend = queue.size();

        if (0 < maxToSend) {

            while (numSent < maxToSend) {

                // get the next batch of messages
                int batchSize = Math.min(maxToSend - numSent, MAX_BATCH);

                List<LogMsg> batch = new ArrayList<LogMsg>(batchSize);

                for (int i = 0; i < batchSize; ++i) {
                    batch.add(queue.remove());
                }

                LogMsgGroup group = LogMsgGroup.newBuilder()
                        .msgs(batch)
                        .environmentDetail(apiConfiguration.getEnvDetail())
                        .apiClientName(apiClientName)
                        .build();

                boolean success = false;
                try {

                    // get access token
                    OAuth2Token oAuth2Token = oAuth2Service.getAccessToken(apiConfiguration.getEnvDetail());

                    if (oAuth2Token != null) {
                        group.setAccessToken(oAuth2Token.getAccessToken());
                    } else {
                        throw new Exception("Unable to retrieve auth token.");
                    }

                    success = sender.send(group);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

                // add to retry queue on failure
                if (!success) {
                    retryQueue.offer(new RetryQueueItem<LogMsgGroup>(group));
                }

                // next iteration
                numSent += batchSize;
            }
        }

        return numSent;
    }

    public void flushRetries(final LogSender sender) {

        if (!retryQueue.isEmpty()) {

            // queued items are available for retransmission

            // drain resend queue or 1st exception

            try {

                while (!retryQueue.isEmpty()) {

                    // get next item off queue

                    RetryQueueItem<LogMsgGroup> item = retryQueue.poll();

                    if (item != null) {

                        // add to retry queue on failure

                        LogMsgGroup logMsgGroup = item.getObject();

                        try {

                            // set new oauth token
                            OAuth2Token oAuth2Token = oAuth2Service.getAccessToken(apiConfiguration.getEnvDetail());
                            logMsgGroup.setAccessToken(oAuth2Token.getAccessToken());

                            sender.send(logMsgGroup);

                            Threads.sleepQuietly(250, TimeUnit.MILLISECONDS);

                        } catch (Throwable e) {

                            item.incrementRetryCount();

                            if (MAX_POST_ATTEMPTS > item.getRetryCount()) {
                                retryQueue.offer(item);
                            }

                            throw e;

                        }
                    }
                }

            } catch (Throwable t) {
                System.err.println("Failure retransmitting queued requests");
                t.printStackTrace();
            }
        }
    }

    @Getter
    static class RetryQueueItem<T> {

        private T object;
        private int retryCount;

        public RetryQueueItem(T object) {
            this.object = object;
        }

        public void incrementRetryCount() {
            this.retryCount++;
        }

    }

}
