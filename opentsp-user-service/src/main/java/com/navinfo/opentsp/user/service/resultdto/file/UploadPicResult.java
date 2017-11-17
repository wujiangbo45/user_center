package com.navinfo.opentsp.user.service.resultdto.file;

/**
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
public class UploadPicResult {

    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UploadPicResult(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
