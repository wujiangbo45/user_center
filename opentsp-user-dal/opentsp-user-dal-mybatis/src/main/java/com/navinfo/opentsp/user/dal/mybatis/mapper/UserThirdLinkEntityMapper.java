package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserThirdLinkEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserThirdLinkEntityMapper extends MybatisMapper<UserThirdLinkEntity, String>{

    UserThirdLinkEntity findByTypeAndId(@Param("thirdType") String thirdType, @Param("openId") String openId);
    UserThirdLinkEntity findByTypeAndUnionId(@Param("thirdType") String thirdType, @Param("unionid") String unionid);
    UserThirdLinkEntity findByTypeAndOpenId(@Param("thirdType") String thirdType, @Param("openId") String openId);
    List<String>  selectBindByUserId(@Param("userId")String userId,@Param("isVaild")byte isValid);
    String  selectIdByUserIdAndType(@Param("userId")String userId,@Param("thirdType")String thridType,@Param("isVaild")byte isValid);
}