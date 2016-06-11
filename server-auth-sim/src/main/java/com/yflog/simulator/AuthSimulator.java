package com.yflog.simulator;

import com.sun.net.httpserver.*;
import com.sun.net.httpserver.spi.HttpServerProvider;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by vincent on 1/7/16.
 */
public class AuthSimulator {

    private static String _username = "vincent";
    private static String _password = "vincent";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        HttpContext cc  = server.createContext("/test", new MyHandler());
        cc.setAuthenticator(new BasicAuthenticator("test") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("test") && pwd.equals("test");
            }
        });
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main1(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Usage:\n\tHttpServer port username password");
            return;
        }

        int port = Integer.parseInt(args[0]);

        if (args.length >= 3 ) {
            _username = args[1];
            _password = args[2];
        }


        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(new InetSocketAddress("0.0.0.0", port), 100000);
        final AtomicLong handler = new AtomicLong(0);


        HttpContext context = server.createContext("/", new HttpHandler() {

            public void handle(HttpExchange httpExchange) throws IOException {

                System.out.println("handle the request");

                handler.incrementAndGet();

                Headers headers = httpExchange.getRequestHeaders();
                String authorization = headers.getFirst("Authorization");
                int status = 200;
                String body = "OK";
                if (authorization == null) {
                    Headers var11 = httpExchange.getResponseHeaders();
                    var11.set("WWW-Authenticate", "Basic realm=\"/\"");
                    body = "not auth";
                    status = 401;
                }
                else {
                    int basic = authorization.indexOf(32);
                    if (basic != -1 && authorization.substring(0, basic).equals("Basic")) {

                        byte[] buffer = new BASE64Decoder().decodeBuffer(authorization.substring(basic + 1));
                        String authStr = new String(buffer);
                        int var7 = authStr.indexOf(58);
                        String username = authStr.substring(0, var7);
                        String password = authStr.substring(var7 + 1);
                        System.out.printf("username=%s, password=%s\n", username, password);

                        boolean authOk = _username.equalsIgnoreCase(username) && _password.equalsIgnoreCase(password);
                        if (!authOk) {
                            Headers respHeaders = httpExchange.getResponseHeaders();
//                            respHeaders.set("WWW-Authenticate", "Basic realm=\"/\"");
                            status = 401;
                            body = "<!DOCTYPE html><html><head><title>Apache Tomcat/8.0.23 - Error report</title><style type=\"text/css\">H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}.line {height: 1px; background-color: #525D76; border: none;}</style> </head><body><h1>HTTP Status 401 - Unauthorized</h1><div class=\"line\"></div><p><b>type</b> Status report</p><p><b>message</b> <u>Unauthorized</u></p><p><b>description</b> <u>This request requires HTTP authentication.</u></p><hr class=\"line\"><h3>Apache Tomcat/8.0.23</h3></body></html>";
                        }
                    }
                    else {
                        status = 401;
                        body = "not authentication";
                    }
                }

                System.out.printf("Write back response, status=%d, body=%s\n", status, body);
                 byte[] response = body.getBytes();
                httpExchange.sendResponseHeaders(status, response.length);
                OutputStream out = httpExchange.getResponseBody();
                out.write(response);
                out.close();
                httpExchange.close();
            }
        });

//        context.setAuthenticator(new BasicAuthenticator("/") {
//            @Override
//            public boolean checkCredentials(String username, String password) {
//                System.out.printf("user=%s, passwd=%s\n", username, password);
////                return _username.equals(username) && _password.equals(password);
//                return true;
//            }
//        });

        server.setExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        server.start();
    }
}