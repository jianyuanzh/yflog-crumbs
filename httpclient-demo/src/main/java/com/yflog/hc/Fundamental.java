package com.yflog.hc;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 3/29/16.
 */
public class Fundamental {
    public void basicUse() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.baidu.com");
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(httpGet);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            System.out.println(httpResponse.getStatusLine().getReasonPhrase());
            System.out.println(httpResponse.getStatusLine().getProtocolVersion());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (httpResponse != null)
                try {
                    httpResponse.close();
                }
                catch (IOException e) {

                }
            if (client != null) {
                try {
                    client.close();
                }
                catch (IOException e) {

                }
            }

        }

    }

    public void managedConnection() throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
        HttpClientContext context = HttpClientContext.create();
        HttpClientConnectionManager conMrg = new BasicHttpClientConnectionManager();
        // Request new connection. This can be a long process
        HttpRoute route = new HttpRoute(new HttpHost("www.baidu.com", 80));
        ConnectionRequest connRequest = conMrg.requestConnection(route, null);
        // wait for the connection un to 10 seconds
        HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);

        try {
            // If no open
            if (!conn.isOpen()) {
                // establis connnection based on its route info
                conMrg.connect(conn, route, 1000, context);
                // and mark it as route complete
                conMrg.routeComplete(conn, route, context);
            }
            // do useful things with this connecetion ....

            System.out.println(context.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conMrg.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
        }

    }
    public static void main(String[] args) {
        new Fundamental().basicUse();
        try {
            new Fundamental().managedConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
