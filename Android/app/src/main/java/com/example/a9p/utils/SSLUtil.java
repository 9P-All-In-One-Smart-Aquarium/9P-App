package com.example.a9p.utils;

import android.content.Context;
import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;

import com.example.a9p.R;

public class SSLUtil {
    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            InputStream caInput = context.getResources().openRawResource(R.raw.root_ca);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(caInput);
            caInput.close();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException("SSL 설정 오류: " + e.getMessage(), e);
        }
    }
}
