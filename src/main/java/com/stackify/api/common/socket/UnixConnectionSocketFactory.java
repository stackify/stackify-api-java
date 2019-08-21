package com.stackify.api.common.socket;


import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * https://github.com/docker-java/docker-java
 * <p>
 * Provides a ConnectionSocketFactory for connecting Apache HTTP clients to Unix sockets.
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class UnixConnectionSocketFactory implements ConnectionSocketFactory {

    private File socketFile;

    public UnixConnectionSocketFactory(final String socketPath) {
        super();
        this.socketFile = new File(socketPath);
    }

    @Override
    public Socket createSocket(final HttpContext context) throws IOException {
        return new ApacheUnixSocket();
    }

    @Override
    public Socket connectSocket(final int connectTimeout, final Socket socket, final HttpHost host,
                                final InetSocketAddress remoteAddress, final InetSocketAddress localAddress, final HttpContext context)
            throws IOException {
        try {
            socket.connect(new AFUNIXSocketAddress(socketFile), connectTimeout);
        } catch (SocketTimeoutException e) {
            throw new ConnectTimeoutException(e, null, remoteAddress.getAddress());
        }

        return socket;
    }
}
