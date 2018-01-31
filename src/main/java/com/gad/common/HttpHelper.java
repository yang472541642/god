/*
 * @(#) HttpClientUtils.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.common;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class HttpHelper {

    private String method = "get";

    private Header[] responseHeaders;

    private HttpRequestBase request;

    private CloseableHttpResponse response;

    private List<NameValuePair> params;

    private String responseContent;

    private CloseableHttpClient client;



    public static HttpHelper create() {
        return new HttpHelper();
    }

    public HttpHelper get(String url, Boolean redirectsEnabled) {
        request = new HttpGet(url);
        setCommonHeaders(request);

        if (redirectsEnabled!=null && !redirectsEnabled) {
            RequestConfig config = RequestConfig.custom().setRedirectsEnabled(Boolean.FALSE).build();
            request.setConfig(config);
        }

        return this;
    }

    public HttpHelper get(String url) {
        request = new HttpGet(url);
        setCommonHeaders(request);
        return this;
    }

    public HttpHelper post(String url) {
        request = new HttpPost(url);
        this.method = "post";
        setCommonHeaders(request);
        return this;
    }

    public HttpHelper addParam(String name, String val) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(new BasicNameValuePair(name, val));
        return this;
    }

    public HttpHelper addParams(Map<String, String> map) {
        params = new ArrayList<>();
        for (String key : map.keySet()) {
            params.add(new BasicNameValuePair(key, map.get(key)));
        }
        return this;
    }


    public HttpHelper addHeader(String key, String val) {
        request.addHeader(key, val);
        return this;
    }

    public HttpHelper addHeaders(Header[] headers) {
        for (Header header : headers) {
            request.addHeader(header);
        }
        return this;
    }


    public HttpHelper execute() throws IOException {
        if (client == null) {
            client = HttpClientBuilder.create().build();
        }
        if (params != null && method.equals("post")) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
            HttpPost tmp = ((HttpPost) request);
            tmp.setEntity(entity);
            this.response = client.execute(tmp);
        } else {
            this.response = client.execute(request);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.setResponseHeaders(response.getAllHeaders());
        HttpEntity resEntity = response.getEntity();
        this.responseContent = new String(FileUtils.readInputStream(resEntity.getContent()));
        return this;
    }

    private static void setCommonHeaders(HttpRequest request) {
        request.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
        request.setHeader("Cache-Control", "max-age=0");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        // request.setHeader("Host", "www.cpxzs.com");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        request.setHeader("X-Requested-With", "XMLHttpRequest");
    }

    public String getResponseContent() {
        return responseContent;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
