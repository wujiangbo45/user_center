package com.navinfo.opentsp.user.service.file;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.navinfo.opentsp.user.common.util.http.HttpUtil;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.mongo.dao.GridFsDao;
import com.navinfo.opentsp.user.service.param.file.FileUploadParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传服务类
 *
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class UploadFileService {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    @Autowired
    private GridFsDao gridFsDao;

    @Autowired
    private TokenService tokenService;

    /**
     * c     * @param in 文件输入留
     *
     * @param fileUploadParam 传入参数
     * @param fileName        文件名称
     * @param contentType     文件类型
     * @return
     */
    public CommonResult<CommonResult> saveFile(InputStream in, FileUploadParam fileUploadParam, String fileName, String contentType) {
        TokenEntity tokenEntity = null;
        try {
            tokenEntity = tokenService.getToken(fileUploadParam.getToken());
            if (tokenEntity != null) {
                DBObject dbObject = new BasicDBObject();
                dbObject.put("fileName", fileName);
                gridFsDao.saveFile(in, tokenEntity.getUserId(), contentType, dbObject);
            } else {
                return CommonResult.newInstance(ResultCode.NO_RESULT, CommonResult.class);
            }
        } catch (IOException e) {
            logger.error("upload file error ! userId:" + tokenEntity.getUserId() + ",token:" + tokenEntity.getToken(), e);
        }
        return CommonResult.newInstance(ResultCode.SUCCESS, CommonResult.class);
    }

    /**
     * 查询文件结果流
     *
     * @param token
     * @return
     */
    public GridFSDBFile queryFile(String token) {
        TokenEntity tokenEntity = tokenService.getToken(token);
        if (tokenEntity != null) {
            return gridFsDao.getByFileName(tokenEntity.getUserId());
        }
        return null;
    }
    /**
     * 查询文件结果流
     *
     * @param userId
     * @return
     */
    public GridFSDBFile queryFileByUserId(String userId) {
        return gridFsDao.getByFileName(userId);
    }

    /**
     * 获取第三方平台的图片，存储到WeDrive
     * @param userId
     * @param url
     * @return
     */
    public CommonResult<String> saveThridPlatformImg(String userId, String url) {
        if (gridFsDao.getByFileName(userId) != null) {
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        }

        String contentType = "image/jpeg";
        try {
            InputStream in = HttpUtil.getImage(url);
            DBObject dbObject = new BasicDBObject();
            dbObject.put("fileName", userId);
            gridFsDao.saveFile(in, userId, contentType, dbObject);
        } catch (IOException e) {
            logger.error("upload file error ! userId:" +userId  , e);
        }
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }
}
