package com.navinfo.opentsp.user.service.enums;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public enum Functions {

    REGISTER("register", "WeDrive账号-账号激活"),
    resentRegister("register", "WeDrive账号-账号激活"),
    LOGIN("login", ""),
    FIND_PASSWORD("findPassword","WeDrive账号-重置密码"),
    resent_findPassword("findPassword","WeDrive账号-重置密码"),
    BIND("bind","WeDrive账号-账号绑定"),
    resent_bind("bind", "WeDrive账号-账号绑定"),
    UNBIND("unbind","WeDrive账号-账号解绑"),
    resent_unbind("unbind","WeDrive账号-账号解绑"),
    CHANGEBIND("changeBind","WeDrive账号-更改绑定邮箱"),
    resent_changeBind("changeBind", "WeDrive账号-更改绑定邮箱"),
    ADMIN_RESET_PASSWD("admin_reset_passwd", "WeDrive账号-重置密码"),
    QUIK_LOGIN("quickLogin","");

    private String func;

    /**
     * 如果需要发邮件，这是邮件的主题
     */
    private String emailSubject;

    private Functions(String name, String subject){
        this.func = name;
        this.emailSubject = subject;
    }

    public String func(){
        return func;
    }

    public String emailSubject(){
        return this.emailSubject;
    }

    @Override
    public String toString() {
        return func;
    }

    public static Functions valuesOf(String name) {
        for (Functions status : values()) {
            if (status.func().equals(name)) {
                return status;
            }
        }

        Functions func = Enum.valueOf(Functions.class, name);
        if (func != null)
            return func;

        throw new IllegalArgumentException("No matching constant for [" + name + "]");
    }
}
