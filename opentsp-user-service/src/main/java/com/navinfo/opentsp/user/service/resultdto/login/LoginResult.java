package com.navinfo.opentsp.user.service.resultdto.login;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public class LoginResult {

    private String userId;
    private String nickname;
    private String token;

    public LoginResult() {
    }

    public LoginResult(String userId, String nickname, String token) {
        this.userId = userId;
        this.nickname = nickname;
        this.token = token;
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
