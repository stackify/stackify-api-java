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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.mask.Masker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Log Transport - Direct
 * Send log messages directly to Stackify
 *
 * @author Eric Martin
 */
@Slf4j
public class LogTransportDirect implements LogTransport {

    /**
     * REST path for log save
     */
    private static final String LOG_SAVE_PATH = "/Log/Save";

    /**
     * The API configuration
     */
    private final ApiConfiguration apiConfig;

    /**
     * JSON object mapper
     */
    private final ObjectMapper objectMapper;

    private final LogTransportPreProcessor logTransportPreProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTransportAgentSocket.class);

    /**
     * Default constructor
     *
     * @param apiConfig    API configuration
     * @param objectMapper JSON object mapper
     */
    public LogTransportDirect(@NonNull final ApiConfiguration apiConfig,
                              @NonNull final ObjectMapper objectMapper,
                              Masker masker,
                              boolean skipJson) {
        this.apiConfig = apiConfig;
        this.objectMapper = objectMapper;
        this.logTransportPreProcessor = new LogTransportPreProcessor(masker, skipJson);
    }

    /**
     * Sends a group of log messages to Stackify
     *
     * @param group The log message group
     */
    public void send(@NonNull final LogMsgGroup group) throws Exception {

        HttpClient httpClient = new HttpClient(apiConfig);

        // run pre-processor

        logTransportPreProcessor.execute(group);

        // convert to json bytes

        byte[] jsonBytes = objectMapper.writer().writeValueAsBytes(group);

        // post to stackify

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                    "#Log #Transport #Direct Sending request to {} - Body: {}",
                    LOG_SAVE_PATH,
                    objectMapper
                        .writeValueAsString(group)
                );
            }
            httpClient.post(LOG_SAVE_PATH, jsonBytes, true);
        } catch (Exception e) {
            log.info("Queueing logs for retransmission due to Exception");
            log.debug(e.getMessage(), e);
            throw e;
        }
    }
}
