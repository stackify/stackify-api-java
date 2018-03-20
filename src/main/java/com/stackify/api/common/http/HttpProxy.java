/*
 * Copyright 2015 Stackify
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
package com.stackify.api.common.http;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * HttpProxy
 *
 * @author Eric Martin
 */
@Slf4j
@UtilityClass
public class HttpProxy {

    /**
     * @return Proxy from system settings
     */
    public static Proxy fromSystemProperties() {

        try {
            String proxyHost = System.getProperty("https.proxyHost");
            String proxyPort = System.getProperty("https.proxyPort");

            return build(proxyHost, Integer.parseInt(proxyPort));

        } catch (Throwable t) {
            log.info("Unable to read HTTP proxy information from system properties", t);
        }

        return Proxy.NO_PROXY;
    }

    public static Proxy build(@NonNull final String httpProxyHost,
                              @NonNull final int httpProxyPort) {

        try {
            if (!httpProxyHost.isEmpty()) {
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, httpProxyPort));
            }
        } catch (Throwable t) {
            log.info("Unable to read HTTP proxy information from system properties", t);
        }

        return Proxy.NO_PROXY;
    }

}
