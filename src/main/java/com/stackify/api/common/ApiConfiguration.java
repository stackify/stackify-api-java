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
package com.stackify.api.common;

import com.stackify.api.EnvironmentDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * ApiConfiguration
 *
 * @author Eric Martin
 */
@ToString
@Getter
@Builder(builderClassName = "Builder", toBuilder = true, builderMethodName = "newBuilder")
public class ApiConfiguration {

    public static final String TRANSPORT_DIRECT = "direct";
    public static final String TRANSPORT_AGENT_SOCKET = "agent_socket";

    private static final String DEFAULT_TRANSPORT = TRANSPORT_DIRECT;
    private static final String DEFAULT_AGENT_SOCKET_PATH_UNIX = "/usr/local/stackify/stackify.sock";

    /**
     * Default API URL
     */
    private static final String DEFAULT_API_URL = "https://api.stackify.com";

    /**
     * API URL
     */
    private final String apiUrl;

    /**
     * API Key
     */
    private final String apiKey;

    /**
     * Application name
     */
    private final String application;

    /**
     * Environment
     */
    private final String environment;

    /**
     * Environment details
     */
    private final EnvironmentDetail envDetail;

    /**
     * Add #SKIPJSON tag to messages containing Json
     */
    private final Boolean skipJson;

    /**
     * Allow logging from com.stackify.*
     */
    private final Boolean allowComDotStackify;

    /**
     * Http Proxy Host ie) 10.20.30.40
     */
    private final String httpProxyHost;

    /**
     * Http Proxy Port ie) 8080
     */
    private final String httpProxyPort;

    private final String transport;

    /**
     * @return the apiUrl
     */
    public String getApiUrl() {
        return apiUrl != null ? apiUrl : DEFAULT_API_URL;
    }

    public String getTransport() {
        return transport != null ? transport : DEFAULT_TRANSPORT;
    }

    public String getAgentSocketPath() {
        return DEFAULT_AGENT_SOCKET_PATH_UNIX;
    }
}
