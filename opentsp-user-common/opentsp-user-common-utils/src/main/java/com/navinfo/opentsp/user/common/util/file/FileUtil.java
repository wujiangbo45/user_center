package com.navinfo.opentsp.user.common.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final Map<String, String> FILE_CONTENT_CACHE = new ConcurrentHashMap<>();

    public static String replace(String json, Map<String, String> variables){
        for(Map.Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            json = json.replace("${"+key+"}", value);
        }

        return json;
    }

    public static synchronized String readString(String name) throws FileNotFoundException {
        if(FILE_CONTENT_CACHE.containsKey(name))
            return FILE_CONTENT_CACHE.get(name);

        String json = null;
        Resource resource = new ClassPathResource(name);
        if(!resource.exists())
            throw new FileNotFoundException("template file [ " + name + " ] not found !! ");

        InputStream is = null;
        BufferedReader br = null;
        try {
            is = resource.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String flag = null;
            while((flag = br.readLine()) != null) {
                sb.append(flag);
            }
            json = sb.toString();

            logger.info("read file : {} ,content : {}" , name, sb.toString());
            FILE_CONTENT_CACHE.put(name, json);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

            try {
                if(br != null)
                    br.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return json;
    }

}
