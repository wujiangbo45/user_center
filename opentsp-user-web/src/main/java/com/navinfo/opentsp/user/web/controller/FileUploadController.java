package com.navinfo.opentsp.user.web.controller;

import com.mongodb.gridfs.GridFSDBFile;
import com.navinfo.opentsp.user.service.file.UploadFileService;
import com.navinfo.opentsp.user.service.param.file.FileUploadParam;
import com.navinfo.opentsp.user.service.param.file.QueryFileParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 上传文件controller
     * @param request
     * @param fileUploadParam
     * @return
     */
    @RequestMapping(value = "/uploadPic", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<CommonResult>  upload(@RequestParam("file") MultipartFile file ,HttpServletRequest request,FileUploadParam fileUploadParam){
        CommonResult<CommonResult> commonResult = null;
        try {
            commonResult = uploadFileService.saveFile(file.getInputStream() , fileUploadParam ,file.getOriginalFilename() , file.getContentType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonResult;
    }

    /**
     * 查看头像图片
     * @param request
     * @param response
     * @param fileUploadParam
     */
    @RequestMapping(value = "/queryPic", method = {RequestMethod.GET,RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public void queryFile(HttpServletRequest request,HttpServletResponse response,FileUploadParam fileUploadParam){
        try {
            GridFSDBFile gridFSDBFile = uploadFileService.queryFile(fileUploadParam.getToken());
            if (gridFSDBFile != null) {
                response.setContentType(gridFSDBFile.getContentType());
                OutputStream os = response.getOutputStream();
                InputStream inputStream = gridFSDBFile.getInputStream();
                try{
                    int count = 0;
                    byte[] buffer = new byte[8096];
                    while ((count = inputStream.read(buffer)) != -1)
                        os.write(buffer, 0, count);
                    os.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }finally{
                    if (os != null)
                        os.close();
                    if (inputStream != null)
                        inputStream.close();
                }
            }else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("query file error !",e);
        }
    }

    @RequestMapping(value = "/queryPicById", method = {RequestMethod.GET,RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public void queryFile2(HttpServletRequest request,HttpServletResponse response,QueryFileParam queryFileParam){
        try {
            GridFSDBFile gridFSDBFile = uploadFileService.queryFileByUserId(queryFileParam.getUserId());
            if (gridFSDBFile != null) {
                response.setContentType(gridFSDBFile.getContentType());
                OutputStream os = response.getOutputStream();
                InputStream inputStream = gridFSDBFile.getInputStream();
                try{
                    int count = 0;
                    byte[] buffer = new byte[8096];
                    while ((count = inputStream.read(buffer)) != -1)
                        os.write(buffer, 0, count);
                    os.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }finally{
                    if (os != null)
                        os.close();
                    if (inputStream != null)
                        inputStream.close();
                }
            }else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("query file error !",e);
        }
    }
}
