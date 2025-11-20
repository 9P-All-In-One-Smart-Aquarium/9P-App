package com.example.a9p.data.network;

import android.content.Context;

import com.example.a9p.R;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://192.168.136.219:443/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {
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

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                        .addInterceptor(logging)
                        .build();

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                        .setPrettyPrinting()
                        .create();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

            } catch (Exception e) {
                throw new RuntimeException("SSL 설정 실패: " + e.getMessage(), e);
            }
        }
        return retrofit.create(ApiService.class);
    }
}