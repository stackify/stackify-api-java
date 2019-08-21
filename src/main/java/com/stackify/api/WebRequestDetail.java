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

import java.util.Map;

/**
 * Contains details about the web request associated to the error
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * WebRequestDetail.Builder builder = WebRequestDetail.newBuilder();
 * builder.userIpAddress("127.0.0.1");
 * builder.httpMethod("GET");
 * builder.requestProtocol("HTTPS");
 * ...
 * WebRequestDetail webRequestDetail = builder.build();
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
public class WebRequestDetail {

    /**
     * User IP address
     */
    @JsonProperty("UserIPAddress")
    private String userIpAddress;

    /**
     * HTTP method
     */
    @JsonProperty("HttpMethod")
    private String httpMethod;

    /**
     * Request protocol
     */
    @JsonProperty("RequestProtocol")
    private String requestProtocol;

    /**
     * Request URL
     */
    @JsonProperty("RequestUrl")
    private String requestUrl;

    /**
     * Request URL root
     */
    @JsonProperty("RequestUrlRoot")
    private String requestUrlRoot;

    /**
     * Referral URL
     */
    @JsonProperty("ReferralUrl")
    private String referralUrl;

    /**
     * Headers
     */
    @JsonProperty("Headers")
    private Map<String, String> headers;

    /**
     * Cookies
     */
    @JsonProperty("Cookies")
    private Map<String, String> cookies;

    /**
     * Query string parameters
     */
    @JsonProperty("QueryString")
    private Map<String, String> queryString;

    /**
     * Form post data
     */
    @JsonProperty("PostData")
    private Map<String, String> postData;

    /**
     * Session data
     */
    @JsonProperty("SessionData")
    private Map<String, String> sessionData;

    /**
     * Raw post data
     */
    @JsonProperty("PostDataRaw")
    private String postDataRaw;

    /**
     * MVC action
     */
    @JsonProperty("MVCAction")
    private String mvcAction;

    /**
     * MVC controller
     */
    @JsonProperty("MVCController")
    private String mvcController;

    /**
     * MVC area
     */
    @JsonProperty("MVCArea")
    private String mvcArea;

}
