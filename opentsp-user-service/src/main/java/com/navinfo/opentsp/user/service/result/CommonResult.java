package com.navinfo.opentsp.user.service.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommonResult<T> {

    @JsonIgnore
    private int httpCode;
    private int code;
    private String message;
    private T data;

    public CommonResult() {
        super();
    }

    public CommonResult(int httpCode, int code, String message, T data) {
        super();
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult<T> fillResult(ReturnResult result) {
        this.code = result.code();
        this.message = result.message();
        this.httpCode = result.httpCode();
        return this;
    }

    public static <T> CommonResult<T> newInstance(ReturnResult result, Class<T> clazz) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.fillResult(result == null ? ResultCode.SUCCESS : result);
        return commonResult;
    }

    public int getCode() {
        return code;
    }

    public CommonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public T getData() {
        return data;
    }

    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

}