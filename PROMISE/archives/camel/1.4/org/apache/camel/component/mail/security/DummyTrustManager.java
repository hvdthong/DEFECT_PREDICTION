package org.apache.camel.component.mail.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  DummyTrustManager that accepts any given certificate - <b>NOT SECURE</b>.
 */
public class DummyTrustManager implements X509TrustManager {

    private static final transient Log LOG = LogFactory.getLog(DummyTrustManager.class);

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        logCertificateChain("Client", chain);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        logCertificateChain("Server", chain);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private static void logCertificateChain(String type, X509Certificate[] chain) {
        if (LOG.isDebugEnabled()) {
            for (X509Certificate certificate : chain) {
                LOG.debug(type + " certificate is trusted: " + certificate);
            }
        }
    }

}

