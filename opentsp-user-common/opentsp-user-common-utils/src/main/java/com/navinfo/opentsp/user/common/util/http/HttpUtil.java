package com.navinfo.opentsp.user.common.util.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String get(String url, String param, String token) throws IOException {
        Map<String, String> headers = new HashMap<>();
        if (!StringUtils.isEmpty(token)) {
            headers.put("token", token);
        }
        return get(url, param, headers);
    }


    public static String get(String url, String param, Map<String, String> headers) throws IOException {
        logger.info("get request url : {}, param : {}", url, param);
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("contentType", "utf-8");
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (!StringUtils.isEmpty(param)) {
            OutputStream os = conn.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.close();
        }

        conn.connect();

        StringBuilder sb = new StringBuilder();
        int code = conn.getResponseCode();
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
            if (br != null) {
                br.close();
            }
        }

        if (code != 200) {
            logger.error("request url [ " + url + " ] returns code [ " + code + " ]");
            throw new IOException("request url [ "
                    + url + " ] returns code [ " + code + " ], message [ " + sb.toString() + " ]");
        }

        logger.info("get request url : {}, response : {}", url, sb.toString());
        return sb.toString();
    }

    public static InputStream getImage(String url) throws IOException {
        logger.info("get request url : {}, param : {}", url);
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setReadTimeout(30000);
        conn.connect();
        StringBuilder sb = new StringBuilder();
        int code = conn.getResponseCode();
        InputStream inputStream = null;
        if (code == 200) {
            // 从服务器获得一个输入流
            inputStream = conn.getInputStream();
        }
        logger.info("get request url : {}, response : {}", url, sb.toString());
        return inputStream;
    }


    public static String post(String url, String param, String token) throws IOException {
        Map<String, String> headers = new HashMap<>();
        if (!StringUtils.isEmpty(token)) {
            headers.put("token", token);
        }
        return post(url, param, headers);
    }

    public static String mapToParams(Map<String, Object> params) {
        if (params == null || params.size() == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(String.valueOf(entry.getValue()));
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            return sb.toString();
        }

        return "";
    }

    public static String post(String url, String param, Map<String, String> headers) throws IOException {
        logger.info("post request url : {}, param : {}", url, param);
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("contentType", "utf-8");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (!StringUtils.isEmpty(param)) {
            OutputStream os = conn.getOutputStream();
            os.write(param.getBytes("UTF-8"));
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

    public static String postJson(String url, String json, String token) throws IOException {
        Map<String, String> headers = new HashMap<>();
        if (!StringUtils.isEmpty(token)) {
            headers.put("token", token);
        }
        return postJson(url, json, headers);
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

    /**
     * 文件上传
     *
     * @param contentByte 文件内容
     * @param fileName    文件名称
     * @param mimetype    文件类型
     * @param uuid
     * @param type        类型
     * @return
     */
    public static String uploadFile(String requestUrl, byte[] contentByte, String fileName, String mimetype, String uuid, String type) {
        String httpResult = null;
        String url = null;
        Map<String, String> mapParam = new HashMap<>();
        try {
            //{uuid}/create
            String uploadUrl = requestUrl + uuid + "/" + type;
            mapParam.put("data", Base64Utils.encodeToString(contentByte));
            Map<String, String> attributesParam = new HashMap<>();
            attributesParam.put("filename", fileName);
            Map<String, String> headerpParam = new HashMap<>();
            headerpParam.put("Accept", "application/json");
            headerpParam.put("mimetype", mimetype);
            httpResult = HttpUtil.postJson(uploadUrl, JsonUtil.toJson(mapParam), headerpParam);
            Map<String, Object> mapResult = JsonUtil.toMap(httpResult);
            if (mapResult.containsKey("resultCode") && (int) mapResult.get("resultCode") == 200) {
                url = (String) mapResult.get("url");
                if (StringUtils.isEmpty(url)) {
                    httpResult = url;
                    logger.debug(fileName + " upload fail!" + httpResult);
                } else {
                    url = url + "?mimetype=" + mimetype;
                    logger.debug(fileName + " upload success!" + httpResult);
                }
            } else {
                logger.debug(fileName + " upload fail!" + httpResult);
            }
            logger.debug(JsonUtil.toJson(mapParam));
        } catch (Exception e) {
            try {
                logger.error(JsonUtil.toJson(mapParam));
            } catch (JsonProcessingException e1) {
                logger.error(fileName + " json formatter error!", e);
            }
            logger.error(fileName + " upload fail!" + httpResult, e);
        }
        return url;
    }

}
