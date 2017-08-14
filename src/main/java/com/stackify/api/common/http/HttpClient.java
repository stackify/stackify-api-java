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
package com.stackify.api.common.http;

import com.stackify.api.common.util.CharStreams;
import com.stackify.api.common.util.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * HttpClient
 *
 * @author Eric Martin
 */
@Slf4j
public class HttpClient {

    /**
     * CONNECT_TIMEOUT
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * READ_TIMEOUT
     */
    private static final int READ_TIMEOUT = 15000;

    /**
     * HTTP proxy
     */
    private final Proxy proxy;

    /**
     * Constructor
     */
    public HttpClient() {
        this.proxy = HttpProxy.fromSystemProperties();
    }

    /**
     * Reads all remaining contents from the stream and closes it
     *
     * @param stream The stream
     * @return The contents of the stream
     * @throws IOException
     */
    private String readAndClose(final InputStream stream) throws IOException {
        String contents = null;

        if (stream != null) {
            contents = CharStreams.toString(new InputStreamReader(new BufferedInputStream(stream), "UTF-8"));
            stream.close();
        }

        return contents;
    }

    public HttpResponse executePost(@NonNull final String accessToken,
                                    @NonNull final String postUrl,
                                    @NonNull final String path,
                                    @NonNull final byte[] jsonBytes,
                                    @NonNull final boolean gzip) throws IOException {

        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("content-type", "application/json");
        requestProperties.put("authorization", "Bearer " + accessToken);

        return executePost(postUrl, path, jsonBytes, gzip, requestProperties);
    }

    public HttpResponse executePost(@NonNull final String postUrl,
                                    @NonNull final String path,
                                    @NonNull final byte[] bodyBytes,
                                    final boolean gzip,
                                    final Map<String, String> requestProperties) throws IOException {

        Preconditions.checkArgument(!path.isEmpty());
        Preconditions.checkArgument(0 < bodyBytes.length);

        HttpURLConnection connection = null;

        try {

            URL url = new URL(postUrl + path);

            // request properties

            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (gzip) {
                connection.setRequestProperty("Content-Encoding", "gzip");
            }

            if (requestProperties != null) {
                for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            // write the post body

            OutputStream stream = null;

            if (gzip) {
                stream = new BufferedOutputStream(new GZIPOutputStream(connection.getOutputStream()));
            } else {
                stream = new BufferedOutputStream(connection.getOutputStream());
            }

            stream.write(bodyBytes);
            stream.flush();
            stream.close();

            // read the response
            HttpResponse httpResponse =  new HttpResponse(connection.getResponseCode(), readAndClose(connection.getInputStream()));

            return httpResponse;

        } finally {

            if (connection != null) {

                // read and close the input stream

                try {
                    readAndClose(connection.getInputStream());
                } catch (Throwable t) {
                    // do nothing
                }

                // read and close the error stream

                try {
                    readAndClose(connection.getErrorStream());
                } catch (Throwable t) {
                    // do nothing
                }
            }
        }
    }

}
