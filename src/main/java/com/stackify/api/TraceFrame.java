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

/**
 * Each TraceFrame object represents a single stack frame in a stack trace
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * TraceFrame.Builder builder = TraceFrame.newBuilder();
 * builder.codeFileName(stackTraceElement.getFileName());
 * ...
 * TraceFrame traceFrame = builder.build();
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
public class TraceFrame {

    /**
     * The file name
     */
    @JsonProperty("CodeFileName")
    private String codeFileName;

    /**
     * The line number
     */
    @JsonProperty("LineNum")
    private Integer lineNum;

    /**
     * The method name
     */
    @JsonProperty("Method")
    private String method;

}
