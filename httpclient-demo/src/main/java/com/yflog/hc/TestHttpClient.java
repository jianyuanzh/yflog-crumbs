package com.yflog.hc;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
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

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials("test", "test"));
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        HttpGet httpget = new HttpGet("http://localhost:8888/test");
        CloseableHttpResponse response = httpClient.execute(httpget);
        System.out.println(response.getStatusLine());

    }

    public static void mains(String[] args) throws IOException, NoSuchAlgorithmException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpHost targetHost = new HttpHost("zhc2.logicmonitor.com");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials("admin", "admin"));

// Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
// Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

// Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpGet httpget = new HttpGet("/santaba/rest/service/groups/1");
        for (int i = 0; i < 3; i++) {
            CloseableHttpResponse response = httpclient.execute(
                    targetHost, httpget, context);
            System.out.println(response.getStatusLine());
            try {
                HttpEntity entity = response.getEntity();

            }
            finally {
                response.close();
            }
        }
    }
}
