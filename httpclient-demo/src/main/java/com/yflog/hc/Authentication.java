package com.yflog.hc;

/**
 * Created by root on 3/29/16.
 * The AuthScheme interface represents an abstract challenge-response oriented
 * authentication scheme. Following functions should be supported:
 *  * Parse and process the chanllege sent by the target server in response to request
 *  for protected resource.
 *  * Provide properties of the processed challenge: the authentication scheme type and its parameters, such the realm this authentication
 *  scheme is applicable to, if available.
 *  * Generate the authentication string for the given set of credentials and the HTTP
 *  request in response to the actual authorization challege.
 *
 *  HttpClient ships with several AuthScheme implementation:
 *  1. Basic: RFC 2617. Is insecure. But is perfectly adequate is used in combination with TLS/SSL encryption.
 *  2. Digest: RFC 2617. More secure than Basic.
 *  3. NTLM: NTLM is a proprietaty authentication scheme developed by Microsoft and optimized for Windows Platforms.
 *  Believed to be more secure than Digest.
 *  4. SPNEGO:
 *  5. Kerberos:
 */
public class Authentication {
}
