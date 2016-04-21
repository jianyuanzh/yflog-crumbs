package com.yflog.hc;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.crypto.Cipher;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Created by root on 4/5/16.
 */
public class TestHttpClient {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
//        Security.setProperty("jdk.tls.disabledAlgorithms", "" /*disabledAlgorithms */);
//        CloseableHttpClient client = HttpClients.createDefault();

        HttpsURLConnection connection = (HttpsURLConnection) new URL("https://yflog.logicmonitor.com:9443").openConnection();
        connection.connect();

        System.out.println(connection.getResponseMessage());;
//        CloseableHttpResponse response = client.execute(new HttpGet("https://www.baidu.com"));
//
//        System.out.println("resp: " + response.getStatusLine());

    }
}
