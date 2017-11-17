package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserThirdLinkDao;
import com.navinfo.opentsp.user.dal.entity.UserThirdLinkEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserThirdLinkEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class UserThirdLinkDaoImpl extends BaseDAOImpl<UserThirdLinkEntity, String> implements UserThirdLinkDao {

    @Autowired
    private UserThirdLinkEntityMapper mapper;

    @Override
    protected MybatisMapper<UserThirdLinkEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public UserThirdLinkEntity findByTypeAndId(String thirdType, String openId) {
        return mapper.findByTypeAndId(thirdType, openId);
    }
    @Override
    public UserThirdLinkEntity findByTypeAndUnionId(String thirdType, String unionid) {
        return mapper.findByTypeAndUnionId(thirdType, unionid);
    }

    @Override
    public UserThirdLinkEntity findByTypeAndOpenId(String thirdType, String openId) {
        return mapper.findByTypeAndOpenId(thirdType, openId);
    }
   public   List<String> selectByUserId(String userId,byte isVaild){
       return mapper.selectBindByUserId(userId, isVaild);
   }
   public String  selectIdByUserIdAndType(String userId,String thridType,byte isValid){
        return mapper.selectIdByUserIdAndType(userId,thridType,isValid);
    }
}
