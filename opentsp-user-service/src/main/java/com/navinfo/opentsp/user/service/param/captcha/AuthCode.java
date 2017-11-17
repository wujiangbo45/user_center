package com.navinfo.opentsp.user.service.param.captcha;

import java.io.Serializable;

public class AuthCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String img64;

	public AuthCode() {

	}

	public AuthCode(String code, String img64) {
		this.code = code;
		this.img64 = img64;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImg64() {
		return img64;
	}

	public void setImg64(String img64) {
		this.img64 = img64;
	}
}