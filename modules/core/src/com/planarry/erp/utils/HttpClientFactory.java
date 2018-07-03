/*
 * Copyright(c) 2017 Planarry
 */

package com.planarry.erp.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class HttpClientFactory {

    private static CloseableHttpClient client;

    @SuppressWarnings("deprecation")
    static CloseableHttpClient getHttpsClient() throws KeyManagementException, NoSuchAlgorithmException {

        if (client != null) {
            return client;
        }
        SSLContext sslcontext = SSLContexts.custom().useSSL().build();
        sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        client = HttpClients.custom().setSSLSocketFactory(factory).build();

        return client;
    }

    static CloseableHttpClient getHttpClient(String shema) {

        if (client != null) {
            return client;
        }

        PlainConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> connSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(shema, plainSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(connSocketFactoryRegistry);

        connManager.setMaxTotal(20);
        connManager.setDefaultMaxPerRoute(20);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .build();

        client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        return client;
    }

    static void releaseInstance() {
        client = null;
    }
}