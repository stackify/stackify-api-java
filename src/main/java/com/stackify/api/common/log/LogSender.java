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
import com.stackify.api.ErrorItem;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpClient;
import com.stackify.api.common.http.HttpResponse;
import com.stackify.api.common.mask.Masker;
import lombok.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * LogSender
 *
 * @author Eric Martin
 */
public class LogSender {

    /**
     * REST path for log save
     */
    private static final String LOG_SAVE_PATH = "/api/v1/logs/%s";

    /**
     * The API configuration
     */
    private final ApiConfiguration apiConfig;

    /**
     * JSON object mapper
     */
    private final ObjectMapper objectMapper;

    /**
     * The queue of requests to be retransmitted (max of 20 batches of 100 messages)
     */
    // private final HttpResendQueue resendQueue = new HttpResendQueue(20);

    private final Masker masker;

    /**
     * Default constructor
     *
     * @param apiConfig    API configuration
     * @param objectMapper JSON object mapper
     * @param masker       Message Masker
     */
    public LogSender(@NonNull final ApiConfiguration apiConfig,
                     @NonNull final ObjectMapper objectMapper,
                     final Masker masker) {
        this.apiConfig = apiConfig;
        this.objectMapper = objectMapper;
        this.masker = masker;
    }

    /**
     * Applies masking to passed in LogMsgGroup.
     */
    private void mask(final LogMsgGroup group) {
        if (masker != null) {
            if (group.getMsgs().size() > 0) {
                for (LogMsg logMsg : group.getMsgs()) {
                    if (logMsg.getEx() != null) {
                        mask(logMsg.getEx().getError());
                    }
                    logMsg.setData(masker.mask(logMsg.getData()));
                    logMsg.setMsg(masker.mask(logMsg.getMsg()));
                    logMsg.setApiClientName(group.getApiClientName());
                }
            }
        }
    }

    private void mask(final ErrorItem errorItem) {
        if (errorItem != null) {
            errorItem.setMessage(masker.mask(errorItem.getMessage()));
            if (errorItem.getData() != null) {
                for (Map.Entry<String, String> entry : errorItem.getData().entrySet()) {
                    entry.setValue(masker.mask(entry.getValue()));
                }
            }
            mask(errorItem.getInnerError());
        }
    }

    /**
     * Sends a group of log messages to Stackify
     *
     * @param group The log message group
     */
    boolean send(@NonNull final LogMsgGroup group) throws IOException {

        mask(group);

        HttpClient httpClient = new HttpClient();

        // convert to json bytes

        byte[] jsonBytes = objectMapper.writer().writeValueAsBytes(new LogMsgGroup[]{group});

        // post to stackify

        HttpResponse httpResponse = httpClient.executePost(
                group.getAccessToken(),
                apiConfig.getApiUrl(),
                String.format(LOG_SAVE_PATH, group.getMsgs().size()),
                jsonBytes, true);

        return httpResponse.getStatusCode() == HttpURLConnection.HTTP_ACCEPTED;
    }
}
