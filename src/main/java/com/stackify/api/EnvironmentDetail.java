/*
 * Copyright 2013 Stackify
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
package com.stackify.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Contains details of the current application environment such as device name, application name, application
 * location and environment.
 * <p>
 * <p>
 * Example:
 * <pre>
 * {@code
 * EnvironmentDetail.Builder builder = EnvironmentDetail.newBuilder();
 * builder.deviceName("localhost");
 * ...
 * EnvironmentDetail environmentDetail = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@Data
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnvironmentDetail {

    /**
     * Device name
     */
    @JsonProperty("DeviceName")
    private final String deviceName;

    /**
     * Application name
     */
    @JsonProperty("AppName")
    private final String appName;

    /**
     * Application location
     */
    @JsonProperty("AppLocation")
    private final String appLocation;

    /**
     * Custom application name
     */
    @JsonProperty("ConfiguredAppName")
    private final String configuredAppName;

    /**
     * Custom application environment
     */
    @JsonProperty("ConfiguredEnvironmentName")
    private final String configuredEnvironmentName;

}
