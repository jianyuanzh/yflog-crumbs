package com.yflog.hc;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;

/**
 * Created by root on 3/29/16.
 * In additional to the trust verification and client authentication performed on the SSL/TLS protocol level,
 * HttpClient can optionally verify whether the target hostname matches the names stored inside the server's X.509
 * certificate, once the connection has been established. This verification can provide additional guarantees of
 * authenticity of the server trust material. The javax.net.ssl.HostnameVerifier interface represents a strategy
 * for hostname verification. HttpClient ships with two javax.net.ssl.HostnameVerifier implementations.
 * Important:
 *  Hostname verification should not be confused with SSL trust verification.
 * two host name verifiers:
 *      1. DefaultHostnameVerifier: the default implementation used by HttpClient is expected to be compliant with RFC2181.
 *      The hostname must match any of the alternative names specified by the certificate, or in
 *      case no alternative names are given the most specific CN of the certificate subject. A wildcard can occur in the CN,
 *      and in any of the subject-alts. [Used By Default]
 *      2. NoopHostnameVerifier: This hostname verifier essentially turns the hostname verification off. It accepts any SSL
 *      session as valid and matching the target host.
 */
public class HostnameVerification {
    public void useNoopHostnameVerifier() {
        SSLContext sslContext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }


}
