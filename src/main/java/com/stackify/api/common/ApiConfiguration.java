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
import lombok.Data;

/**
 * ApiConfiguration
 *
 * @author Eric Martin
 */
@Data
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
public class ApiConfiguration {

    /**
     * Default API URL
     */
    private static final String DEFAULT_API_URL = "https://api.stackify.com";

    /**
     * Default AUTH URL
     */
    private static final String DEFAULT_AUTH_URL = "https://auth.stackify.net";

    /**
     * API URL
     */
    private final String apiUrl;

    /**
     * Auth API URL
     */
    private final String authUrl;

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
     * @return the apiUrl
     */
    public String getApiUrl() {
        return apiUrl != null ? apiUrl : DEFAULT_API_URL;
    }

    /**
     * @return the authUrl
     */
    public String getAuthUrl() {
        return authUrl != null ? authUrl : DEFAULT_AUTH_URL;
    }

}
