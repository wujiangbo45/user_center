package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.service.file.UploadFileService;
import com.navinfo.opentsp.user.service.param.file.FileUploadParam;
import com.navinfo.opentsp.user.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class FileUploadControllerTest {

    @Autowired
    private UploadFileService uploadFileService;

    @Test
    public void saveFile() {
        try {
            InputStream inputStream = new FileInputStream(this.getClass().getResource("/").getPath() + "meizi.png");

            FileUploadParam fileUploadParam = new FileUploadParam();

            fileUploadParam.setToken("ng5xHY2si8SVrHMNsyvo9RXDSXk=");

            uploadFileService.saveFile(inputStream, fileUploadParam, "meizi.png", "image/png");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveImage() {
        try {
            uploadFileService.saveThridPlatformImg("ng5xHY2si8SVrHMNsyvo9RXDSXk=", "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
