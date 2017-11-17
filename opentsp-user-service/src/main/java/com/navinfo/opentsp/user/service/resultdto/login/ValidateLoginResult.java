package com.navinfo.opentsp.user.service.resultdto.login;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/3/8
 * @modify
 * @copyright Navi Tsp
 */
public class ValidateLoginResult {


    private String userId;
    private String nickname;
    private String token;
    private String email;
    private String mobile;

    public ValidateLoginResult() {
    }


    public ValidateLoginResult(String userId, String nickname, String token, String email, String mobile) {
        this.userId = userId;
        this.nickname = nickname;
        this.token = token;
        this.email = email;
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}



