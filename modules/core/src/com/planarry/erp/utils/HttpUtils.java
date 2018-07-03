package com.planarry.erp.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.planarry.erp.utils.HttpClientFactory.*;


public class HttpUtils {

    //region Class field

    private final String SHEMA = "http";

    private String resultStr;
    //endregion

    //region Getter
    public String getResultStr() {
        return resultStr;
    }
    //endregion

    public int post(String url, Map<String, Object> params) throws URISyntaxException, IOException {

        List<NameValuePair> postParameters = new ArrayList<>();
        params.forEach((key, value) -> postParameters.add(new BasicNameValuePair(key, value.toString())));

        //Build the server URI together with the parameters you wish to pass
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameters(postParameters);

        HttpPost postRequest = new HttpPost(uriBuilder.build());
        return doRequest(postRequest, uriBuilder.getScheme().toLowerCase().equals(SHEMA));
    }

    public int get(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        List<NameValuePair> postParameters = new ArrayList<>();
        params.forEach((key, value) -> {
            if (value instanceof List){
                List values = (List) value;
                values.forEach(v -> postParameters.add(new BasicNameValuePair(key, v.toString())));
            } else {
                postParameters.add(new BasicNameValuePair(key, value.toString()));
            }
        });

        //Build the server URI together with the parameters you wish to pass
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameters(postParameters);

        HttpGet postRequest = new HttpGet(uriBuilder.build());
        return doRequest(postRequest, uriBuilder.getScheme().toLowerCase().equals(SHEMA));
    }


    private int doRequest(HttpRequestBase request, boolean http) throws IOException {
        request.setHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON));
        request.setHeader("Charset", String.valueOf(Charset.forName("UTF-8")));

        // Build the http client.
        CloseableHttpClient httpclient;
        try {
            if (http) {
                httpclient = getHttpClient(SHEMA);
            } else {
                httpclient = getHttpsClient();
            }
            releaseInstance();
        } catch (Exception e) {
            e.printStackTrace();
            releaseInstance();
            return 500;
        }

        assert httpclient != null;
        CloseableHttpResponse response = httpclient.execute(request);

        //Read the response
        StringBuilder responseString = new StringBuilder();

        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity responseHttpEntity = response.getEntity();

        InputStream content = responseHttpEntity.getContent();

        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
        String line;

        while ((line = buffer.readLine()) != null) {
            responseString.append(line);
        }

        //release all resources held by the responseHttpEntity
        EntityUtils.consume(responseHttpEntity);

        //close the stream
        response.close();

        resultStr = responseString.toString();
        return statusCode;
    }
}