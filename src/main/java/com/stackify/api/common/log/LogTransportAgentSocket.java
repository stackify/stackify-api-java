/*
 * Copyright 2019 Stackify
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
import com.stackify.api.common.mask.Masker;
import com.stackify.api.common.proto.LogMsgGroupConverter;
import com.stackify.api.common.proto.StackifyProto;
import com.stackify.api.common.socket.HttpSocketClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log Transport - Agent Socket
 * Send log messages to Stackify Agent via Domain Socket
 *
 * @author Darin Howard
 */
@Slf4j
public class LogTransportAgentSocket implements LogTransport {

    private static final String URI_PREFIX = "unix://localhost:80";

    /**
     * The API configuration
     */
    private final ApiConfiguration apiConfig;

    private final LogTransportPreProcessor logTransportPreProcessor;

    private final HttpSocketClient httpSocketClient;

    /**
	 * The transport logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogTransportAgentSocket.class);

    public LogTransportAgentSocket(@NonNull final ApiConfiguration apiConfig,
                                   Masker masker,
                                   boolean skipJson) {
        this.apiConfig = apiConfig;
        this.logTransportPreProcessor = new LogTransportPreProcessor(masker, skipJson);
        this.httpSocketClient = new HttpSocketClient(apiConfig.getAgentSocketPath());
    }

    /**
     * Sends a group of log messages to Stackify
     *
     * @param group The log message group
     */
    public void send(@NonNull final LogMsgGroup group) throws Exception {

        try {

            // run pre-processor
            logTransportPreProcessor.execute(group);

            // convert to protobuf model
            StackifyProto.LogGroup logGroup = LogMsgGroupConverter.convert(group);

            // post to stackify
            HttpPost httpPost = new HttpPost(URI_PREFIX + "/log");
            httpPost.setHeader("Content-Type", "application/x-protobuf");
            httpPost.setEntity(new ByteArrayEntity(logGroup.toByteArray()));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                    "#Log #Transport #Socket Sending request to {} - Body: {}",
                    httpPost.getURI(),
                    (new ObjectMapper())
                        .writeValueAsString(group)
                );
            }

            httpSocketClient.send(httpPost);
        } catch (Throwable e) {
            log.info("Queueing logs for retransmission due to Exception");
            log.debug(e.getMessage(), e);
            throw e;
        }

    }

}
