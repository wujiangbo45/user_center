package com.navinfo.opentsp.user.dal.mongo.dao;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Repository
public class GridFsDao {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GridFsDao.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存文件
     * @param in
     * @param fileName
     * @throws IOException
     */
    public void saveFile(InputStream in ,String fileName ,String contentType , DBObject metadata) throws IOException {
        try {
            if(this.getByFileName(fileName) != null) {
                this.deleteByFileName(fileName);
            }
            GridFS gridFS = new GridFS(mongoTemplate.getDb());
            GridFSInputFile gridFSInputFile = gridFS.createFile(in);
            gridFSInputFile.setFilename(fileName);
            gridFSInputFile.setMetaData(metadata);
            gridFSInputFile.setContentType(contentType);
            gridFSInputFile.save();
        } catch (Exception e) {
            logger.error("save file error !",e);
            throw e;
        }finally{
            if(in !=null)//关闭流
                in.close();
        }
    }

    /**
     * 根据文件名称查询图片
     * @param fileName
     * @return
     */
    public GridFSDBFile getByFileName(String fileName){
        GridFSDBFile gridFSDBFile = null;
        try {
            GridFS gridFS = new GridFS(mongoTemplate.getDb());
            gridFSDBFile = gridFS.findOne(fileName);
        } catch (Exception e) {
            logger.error("query file error !",e);
        }
        return gridFSDBFile;
    }

    /**
     * 按照给定的collectionName写文件到mogodb中
     * @param in：文件输入流
     * @param id：id
     * @param collectionName：collection名称
     * @param fileName：文件名（需要唯一）
     * @throws IOException
     */
    public void saveFile(InputStream in, Object id,String collectionName,String fileName) throws IOException{
        try {
            GridFS gridFS = new GridFS(mongoTemplate.getDb(),collectionName);//获取一个gridFS的对象,同时指定collection
			/*DBObject query  = new BasicDBObject("_id", id);
			GridFSDBFile gridFSDBFile = gridFS.findOne(query);
			if(gridFSDBFile != null)
				return;*/
            GridFSInputFile gridFSInputFile = gridFS.createFile(in);//创建gridfs文件
            gridFSInputFile.setFilename(fileName);//指定唯一文件名称
            gridFSInputFile.setId(id);
            gridFSInputFile.save();//保存
        } catch (Exception e) {
            throw e;
        }finally{
            if(in !=null)//关闭流
                in.close();
        }
        return;
    }


    /**
     * 根据文件名提取文件(在指定collectionName中提取)
     * @param collectionName
     * @param fileName
     * @return
     */
    public GridFSDBFile getByFileName(String collectionName, String fileName) {
        GridFSDBFile gridFSDBFile = null;
        try {
            // 获取fs的根节点
            GridFS gridFS = new GridFS(mongoTemplate.getDb(), collectionName);
            gridFSDBFile = gridFS.findOne(fileName);
        } catch (Exception e) {
            logger.error("get file error !",e);
            throw e;
        }
        return gridFSDBFile;
    }

    /**
     * 删除文件
     * @param fileName
     */
    public void deleteByFileName(String fileName) {
        try {
            // 获取fs的根节点
            GridFS gridFS = new GridFS(mongoTemplate.getDb());
            gridFS.remove(fileName);
        } catch (Exception e) {
            logger.error("delete file error !" ,e);
        }
    }
}
