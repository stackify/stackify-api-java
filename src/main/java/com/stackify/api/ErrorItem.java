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
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Contains the details of a single exception including the stack trace and any causes
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * ErrorItem.Builder builder = ErrorItem.newBuilder();
 * builder.message(throwable.getMessage());
 * builder.errorType(throwable.getClass().getCanonicalName());
 * ...
 * ErrorItem errorItem = builder.build();
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
public class ErrorItem {

    /**
     * The error message
     */
    @Setter
    @JsonProperty("Message")
    private String message;

    /**
     * The error's class name
     */
    @JsonProperty("ErrorType")
    private String errorType;

    /**
     * The error type code
     */
    @JsonProperty("ErrorTypeCode")
    private String errorTypeCode;

    /**
     * Custom data for the error
     */
    @JsonProperty("Data")
    private Map<String, String> data;

    /**
     * The method name
     */
    @JsonProperty("SourceMethod")
    private String sourceMethod;

    /**
     * The stack trace
     */
    @JsonProperty("StackTrace")
    private List<TraceFrame> stackTrace;

    /**
     * The cause of this error
     */
    @JsonProperty("InnerError")
    private ErrorItem innerError;

}
