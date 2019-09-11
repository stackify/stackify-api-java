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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * LogMsg
 *
 * @author Eric Martin
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@Data
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
     * The error/exception details
     */
    @JsonProperty("Ex")
    private StackifyError ex;

    /**
     * The thread name
     */
    @JsonProperty("Th")
    private String th;

    /**
     * Unix timestamp of the log message
     */
    @JsonProperty("EpochMs")
    private Long epochMs;

    /**
     * Log level of the message
     */
    @JsonProperty("Level")
    private String level;

    /**
     * Transaction id
     */
    @JsonProperty("TransID")
    private String transId;

    /**
     * Source method name
     */
    @JsonProperty("SrcMethod")
    private String srcMethod;

    /**
     * Source line number
     */
    @JsonProperty("SrcLine")
    private Integer srcLine;

    /**
     * The log message id
     */
    @lombok.Builder.Default
    @JsonProperty("id")
    private String id = UUID.randomUUID().toString();

    /**
     * List of Tags
     */
    @JsonProperty("Tags")
    private List<String> tags;


}
