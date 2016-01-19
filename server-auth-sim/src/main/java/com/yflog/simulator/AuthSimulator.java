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

    public static void main(String[] args) throws Exception {

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
            @Override
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

                        byte[] var5 = new BASE64Decoder().decodeBuffer(authorization.substring(basic + 1));
                        String var6 = new String(var5);
                        int var7 = var6.indexOf(58);
                        String username = var6.substring(0, var7);
                        String password = var6.substring(var7 + 1);
                        System.out.printf("username=%s, password=%s\n", username, password);

                        boolean authOk = _username.equalsIgnoreCase(username) && _password.equalsIgnoreCase(password);
                        if (!authOk) {
                            Headers var10 = httpExchange.getResponseHeaders();
                            var10.set("WWW-Authenticate", "Basic realm=\"/\"");
                            status = 401;
                            body = "wrong username or password";
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

        context.setAuthenticator(new BasicAuthenticator("/") {
            @Override
            public boolean checkCredentials(String username, String password) {
                System.out.printf("user=%s, passwd=%s\n", username, password);
                return _username.equals(username) && _password.equals(password);
            }
        });

        server.setExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        server.start();
    }
}