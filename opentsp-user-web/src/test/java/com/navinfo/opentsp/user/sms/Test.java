package com.navinfo.opentsp.user.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-19
 * @modify
 * @copyright Navi Tsp
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        String url = "https://wdservice.mapbar.com/ssoapi/user/refreshToken";
        String json = "{\"token\":\"+FT1S6Mtq1BjIQwBlP3UHd9OGnM=\",\"loginName\":\"15311612804\"}";
        Map<String, String> header = new HashMap<>();
        header.put("device_type", "android");
        header.put("Host", "454545454545");
        String response = postJson(url, json, header);
        System.out.println(response);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        logger.info("post json request url : {}, param : {}", url, json);
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (!StringUtils.isEmpty(json)) {
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
        }

        conn.connect();

        int code = conn.getResponseCode();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String msg = null;
            while ((msg = br.readLine()) != null) {
                sb.append(msg).append("\n");
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } finally {
            if (br != null)
                br.close();
        }

        if (code != 200) {
            logger.error("request url [ " + url + " ] returns code [ " + code + " ]");
            throw new IOException("request url [ "
                    + url + " ] returns code [ " + code + " ], message [ " + sb.toString() + " ]");
        }

        logger.info("get request url : {}, response : {}", url, sb.toString());
        return sb.toString();
    }

}
