package com.yflog.ssl;

import sun.security.x509.X500Name;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by vincent on 4/21/16.
 */
public class SSLDiagnose {
    public static void main(String[] args) throws CertificateParsingException, IOException {
        if (args.length != 2) {
            System.err.println(_usage());
            System.exit(-1);
        }
        String host = args[0];
        int port = Integer.valueOf(args[1]);
        List<SSLCert> certs = sslcerts(host, port);

        for (SSLCert cert : certs) {
            System.out.println(cert.getSubjectCN());
        }

//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//            System.out.println(sslContext.getProtocol());
//            sslContext.init(null, null, null);
//            SSLSocketFactory sf = sslContext.getSocketFactory();
//            SSLSocket soc = (SSLSocket) sf.createSocket();
//            System.out.println(Arrays.toString(soc.getEnabledProtocols()));
//            System.out.println(Arrays.toString(soc.getSupportedProtocols()));
//        }
//        catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
    }

    public static List<String> sslProtocols(String host, int port) {
        return  null;
    }

    public static List<SSLCert> sslcerts(String host, int port) throws CertificateParsingException, IOException {
        List<SSLCert> certs = new ArrayList<>();
        EmptyX509TrustManager tm = new EmptyX509TrustManager();
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            TrustManager[] trust = new TrustManager[]{tm};
            context.init(null, trust, null);
            SSLSocketFactory factory = context.getSocketFactory();

            SSLSocket sock = (SSLSocket) factory.createSocket(host, 443);
            SSLParameters sslParameters = sock.getSSLParameters();
            sslParameters.setProtocols(new String[]{"TLSv1.1"});
            sock.setSSLParameters(sslParameters);

            String request = "GET / HTTP/1.1\r\nHost:" + host + ":" + port + "\r\nConnection:Close\r\nUser-Agent:SSLClient/1.0\r\n\r\n";

            OutputStreamWriter writer = new OutputStreamWriter(sock.getOutputStream(), "utf-8");
            writer.write(request);
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (tm.certificates == null) {
            System.err.println("Failed to retrieve certificates from host - " + host + ":" + port);
        }
        else {
            for (int i = 0; i < tm.certificates.length; i++) {
                final X509Certificate certificate = tm.certificates[i];
                SSLCert.Builder builder = new SSLCert.Builder();
                builder.notAfter(certificate.getNotAfter())
                        .notBefore(certificate.getNotBefore())
                        .issuerX500Name(new X500Name(certificate.getIssuerX500Principal().getName()))
                        .subJectX500Name(new X500Name(certificate.getSubjectX500Principal().getName()))
                        .publicKey(certificate.getPublicKey())
                        .version(certificate.getVersion())
                        .sigAlgName(certificate.getSigAlgName());
                List<String> alternateNames = new ArrayList<>();
                Collection<List<?>> names = certificate.getSubjectAlternativeNames();
                if (names != null) {
                    Iterator<List<?>> iterator = names.iterator();
                    while (iterator.hasNext()) {
                        alternateNames.add((String) iterator.next().get(1));
                    }
                    builder.subJectAlternames(alternateNames);
                }


                certs.add(builder.build());
            }
        }

        return certs;
    }

    public void protocols() {
    }


    private static String _usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Utility to diagnose Java connections to SSL servers").append("\n")
                .append("Usage: ").append("\n")
                .append("\tjava ").append(SSLDiagnose.class.getName()).append(" <host> <port>").append("\n")
                .append("or for more debugging: ").append("\n")
                .append("\tjava -Djavax,net.debug=ssl " ).append(SSLDiagnose.class.getName()).append(" <host> <port>").append("\n")
                .append("\n")
                .append("Eg. to test the SSL certificate at https://localhost, use").append("\n")
                .append("\tjava " ).append(SSLDiagnose.class.getName()).append(" localhost 443").append("\n");

        return sb.toString();
    }

    private static class EmptyX509TrustManager implements TrustManager, X509TrustManager {

        public X509Certificate[] certificates = null;

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            certificates = x509Certificates;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
