package com.stackify.api.common.socket;

import lombok.NonNull;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Client Utility for sending http requests via unix domain socket.
 */
public class HttpSocketClient {

    private final HttpClientConnectionManager httpClientConnectionManager;

    public HttpSocketClient(@NonNull final String socketPath) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("unix", new UnixConnectionSocketFactory(socketPath));
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        httpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
    }

    public void send(@NonNull final HttpPost httpPost) throws Exception {

        CloseableHttpClient client = HttpClients
                .custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();

        try {
            CloseableHttpResponse response = client.execute(httpPost);
        } finally {
            client.close();
        }
    }

    public void send(@NonNull final HttpGet httpGet) throws Exception {

        CloseableHttpClient client = HttpClients
                .custom()
                .setConnectionManager(httpClientConnectionManager)
                .build();

        try {
            CloseableHttpResponse response = client.execute(httpGet);
        } finally {
            client.close();
        }
    }

}
