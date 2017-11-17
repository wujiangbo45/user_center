package com.navinfo.opentsp.user.sms;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-19
 * @modify
 * @copyright Navi Tsp
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://wdservice.mapbar.com/ssoapi/user/refreshToken");
        httpPost.setHeader("device_type", "android");

        String json = "{\"token\":\"p5mNjcP+iGs2eGsj8JkcdKelQrI=\",\"loginName\":\"13160881523\"}";
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println(response.getStatusLine().getStatusCode());

        //演示，不做try-catch-finally
        response.close();
        httpclient.close();
    }
}
