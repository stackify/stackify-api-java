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

import java.util.List;

/**
 * LogMsgGroup
 *
 * @author Eric Martin
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogMsgGroup {

    /**
     * Client device id
     */
    @JsonProperty("CDID")
    private Integer cdId;

    /**
     * Client device application id
     */
    @JsonProperty("CDAppID")
    private Integer cdAppId;

    /**
     * Application name id
     */
    @JsonProperty("AppNameID")
    private String appNameId;

    /**
     * Application/environment id
     */
    @JsonProperty("AppEnvID")
    private String appEnvId;

    /**
     * Environment id
     */
    @JsonProperty("EnvID")
    private Integer envId;

    /**
     * Environment name
     */
    @JsonProperty("Env")
    private String env;

    /**
     * Device name
     */
    @JsonProperty("ServerName")
    private String serverName;

    /**
     * Application name
     */
    @JsonProperty("AppName")
    private String appName;

    /**
     * Application path
     */
    @JsonProperty("AppLoc")
    private String appLoc;

    /**
     * Logger project
     */
    @JsonProperty("Logger")
    private String logger;

    /**
     * Logger platform ("java")
     */
    @JsonProperty("Platform")
    private String platform;

    /**
     * Log messages
     */
    @JsonProperty("Msgs")
    private List<LogMsg> msgs;

    @JsonProperty("Container")
    private Container container;

    @JsonProperty("Kubernetes")
    private Kubernetes kubernetes;

}
