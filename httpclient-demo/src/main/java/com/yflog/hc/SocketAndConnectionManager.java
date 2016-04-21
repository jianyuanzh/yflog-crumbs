package com.yflog.hc;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


/**
 * Created by root on 3/29/16.
 * HTTP connections make use of java.net.Socket Object internally to handle transmission
 * of data across the wire. However they rely on ConnectionSocketFactory interface to create,
 * initialize and connect sockets. This enables the users of HttpClient to provide application
 * socket initialization code at runtime.
 *
 */
public class SocketAndConnectionManager {
    public void buildConnMgrWithSockFactory() {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

        Registry<ConnectionSocketFactory> rgst = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", plainsf).build();

        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(rgst);
        HttpClients.custom().setConnectionManager(cm).build();
    }
    public static void main(String[] args) {

    }
}
