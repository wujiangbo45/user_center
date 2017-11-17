package com.navinfo.opentsp.user.service.result;

public interface ReturnResult {

	public int httpCode();

	public int code();

	public String message();

}