package com.yflog.hc;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by root on 3/29/16.
 */
public class MutilthradedRequest {
    public static void main(String[] args) throws InterruptedException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        // URIs to perform GETs on
        String[] urisToGet = {
                "https://www.baidu.com",
                "http://www.qq.com",
                "http://yflog.com"
        };

        // create a thread for each URI
        GetThread[] threads = new GetThread[urisToGet.length];
        for (int i = 0; i < urisToGet.length; i++) {
            HttpGet httpGet = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient, httpGet);
        }

        // start the threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        // join the threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }

    private static class GetThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpGet;

        private GetThread(CloseableHttpClient httpClient, HttpGet httpGet) {
            this.httpClient = httpClient;
            this.httpGet = httpGet;
            this.context = HttpClientContext.create();
        }

        @Override
        public void run() {
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet, context);

                try {
                    HttpEntity entity = response.getEntity();
                }
                finally {
                    response.close();
                }
            }
            catch (IOException ioex) {
                ioex.printStackTrace();
            }
            finally {
                System.out.println("Get " + httpGet.getURI() + " finished");
            }
        }
    }
}
