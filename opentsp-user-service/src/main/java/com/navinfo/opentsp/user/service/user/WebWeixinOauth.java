package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.param.login.WebOauthLoginParam;
import com.navinfo.opentsp.user.service.resultdto.login.OauthResult;
import com.navinfo.opentsp.user.service.resultdto.login.WebWeixinTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/3/16
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class WebWeixinOauth{

    private static final Logger logger = LoggerFactory.getLogger(WeixinOauth.class);

    public static final String type = "weixin";

    @Value("${opentsp.weixin.accesstoken.url:}")
    private String accesstokenUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String oauthType() {
        return type;
    }

    /**
     *
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316518&token=&lang=zh_CN
     *
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     *
     * @param code
     * @param appIDKeyEntity
     * @return
     * @throws IllegalAccessException
     */
    public WebWeixinTokenInfo getOauthResult(String code, AppIDKeyEntity appIDKeyEntity) {

        String url = accesstokenUrl + "?secret="+appIDKeyEntity.getAppkey()+"&code=" + code + "&appid=" +appIDKeyEntity.getAppid()+"&grant_type=authorization_code";

        String str = restTemplate.getForObject(url, String.class);
        try {
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("convert encoding error !");
            logger.error(e.getMessage(), e);
        }
        logger.info("user wexin login, return json : {}", str);

        if(str.contains("errcode")){
            logger.error("invalid code, server return json : {}, error !", str);
            return null;
        }

        WebWeixinTokenInfo  webWeixinTokenInfo = JsonUtil.fromJson(str, WebWeixinTokenInfo.class);
        return webWeixinTokenInfo;
    }
}
