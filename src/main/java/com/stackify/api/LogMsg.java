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
package com.stackify.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * LogMsg
 *
 * @author Eric Martin
 */

@Data
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogMsg {

    /**
     * The log message
     */
    @Setter
    @JsonProperty("Msg")
    private String msg;

    /**
     * Extra contextual data from the log message
     */
    @Setter
    @JsonProperty("data")
    private String data;

    /**
     * API Client library framework
     */
    @Setter
    @JsonProperty("Logger")
    private String apiClientName;

    /**
     * The error/exception details
     */
    @JsonProperty("Ex")
    private final StackifyError ex;

    /**
     * The thread name
     */
    @JsonProperty("Th")
    private final String th;

    /**
     * Unix timestamp of the log message
     */
    @JsonProperty("EpochMs")
    private final Long epochMs;

    /**
     * Log level of the message
     */
    @JsonProperty("Level")
    private final String level;

    /**
     * Transaction id
     */
    @JsonProperty("TransID")
    private final String transId;

    /**
     * Source method name
     */
    @JsonProperty("SrcMethod")
    private final String srcMethod;

    /**
     * Source line number
     */
    @JsonProperty("SrcLine")
    private final Integer srcLine;

    /**
     * The log message id
     */
    @JsonProperty("id")
    private final String id = UUID.randomUUID().toString();

    /**
     * List of Tags
     */
    @JsonProperty("Tags")
    private final List<String> tags;

}
