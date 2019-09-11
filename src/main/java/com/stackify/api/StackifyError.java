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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Encapsulates all details about an error that will be sent to Stackify
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * StackifyError.Builder builder = StackifyError.newBuilder();
 * builder.environmentDetail(environment);
 * ...
 * StackifyError stackifyError = builder.build();
 * }
 * </pre>
 *
 * @author Eric Martin
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StackifyError {

    /**
     * Environment
     */
    @JsonProperty("EnvironmentDetail")
    private EnvironmentDetail environmentDetail;

    /**
     * Date/time of the error
     */
    @JsonProperty("OccurredEpochMillis")
    private Date occurredEpochMillis;

    /**
     * Error details
     */
    @JsonProperty("Error")
    private ErrorItem error;

    /**
     * Details of the web request
     */
    @JsonProperty("WebRequestDetail")
    private WebRequestDetail webRequestDetail;

    /**
     * Server variables
     */
    @JsonProperty("ServerVariables")
    private Map<String, String> serverVariables;

    /**
     * Customer name
     */
    @JsonProperty("CustomerName")
    private String customerName;

    /**
     * User name
     */
    @JsonProperty("UserName")
    private String userName;

}
