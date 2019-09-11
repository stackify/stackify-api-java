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

import com.stackify.api.common.util.PropertyUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * ApiConfigurations
 *
 * @author Eric Martin
 */
@UtilityClass
@Slf4j
public class ApiConfigurations {

    /**
     * Explicitly configure the API
     *
     * @param apiUrl      API URL
     * @param apiKey      API Key
     * @param application Configured application name
     * @param environment Configured environment name
     * @return ApiConfiguration
     */
    public static ApiConfiguration fromPropertiesWithOverrides(final String apiUrl,
                                                               final String apiKey,
                                                               final String application,
                                                               final String environment) {
        return fromPropertiesWithOverrides(apiUrl, apiKey, application, environment, null, null);
    }

    /**
     * Explicitly configure the API
     *
     * @param apiUrl              API URL
     * @param apiKey              API Key
     * @param application         Configured application name
     * @param environment         Configured environment name
     * @param transport           api | agent_socket
     * @param allowComDotStackify Configured allow com.stackify.* logging
     * @return ApiConfiguration
     */
    public static ApiConfiguration fromPropertiesWithOverrides(final String apiUrl,
                                                               final String apiKey,
                                                               final String application,
                                                               final String environment,
                                                               final String transport,
                                                               final String allowComDotStackify) {
        ApiConfiguration props = ApiConfigurations.fromProperties();

        String mergedApiUrl = ((apiUrl != null) && (0 < apiUrl.length())) ? apiUrl : props.getApiUrl();
        String mergedApiKey = ((apiKey != null) && (0 < apiKey.length())) ? apiKey : props.getApiKey();
        String mergedApplication = ((application != null) && (0 < application.length())) ? application : props.getApplication();
        String mergedEnvironment = ((environment != null) && (0 < environment.length())) ? environment : props.getEnvironment();

        ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();
        builder.transport(transport);
        builder.apiUrl(mergedApiUrl);
        builder.apiKey(mergedApiKey);
        builder.application(mergedApplication);
        builder.environment(mergedEnvironment);
        builder.envDetail(EnvironmentDetails.getEnvironmentDetail(mergedApplication, mergedEnvironment));
        builder.allowComDotStackify(Boolean.valueOf(allowComDotStackify));

        return builder.build();
    }

    /**
     * @return ApiConfiguration read from the stackify-api.properties file
     */
    public static ApiConfiguration fromProperties() {

        ApiConfiguration.Builder builder = ApiConfiguration.newBuilder();

        try {

            Map<String, String> properties = PropertyUtil.read("/stackify-api.properties");

            String apiUrl = null;

            if (properties.containsKey("stackify.apiUrl")) {
                apiUrl = properties.get("stackify.apiUrl");
            }

            String httpProxyHost = properties.get("stackify.httpProxyHost");
            String httpProxyPort = properties.get("stackify.httpProxyPort");
            String apiKey = properties.get("stackify.apiKey");
            String application = properties.get("stackify.application");
            String environment = properties.get("stackify.environment");

            String transport = null;
            if (properties.containsKey("stackify.transport")) {
                transport = properties.get("stackify.transport");
            }

            boolean skipJson = false;
            if (properties.containsKey("stackify.skipJson")) {
                skipJson = Boolean.parseBoolean(properties.get("stackify.skipJson"));
            }

            builder.transport(transport);
            builder.httpProxyHost(httpProxyHost);
            builder.httpProxyPort(httpProxyPort);
            builder.apiUrl(apiUrl);
            builder.apiKey(apiKey);
            builder.application(application);
            builder.environment(environment);
            builder.envDetail(EnvironmentDetails.getEnvironmentDetail(application, environment));
            builder.skipJson(skipJson);

        } catch (Throwable t) {
            log.error("Exception reading stackify-api.properties configuration file", t);
        }

        return builder.build();
    }

}
