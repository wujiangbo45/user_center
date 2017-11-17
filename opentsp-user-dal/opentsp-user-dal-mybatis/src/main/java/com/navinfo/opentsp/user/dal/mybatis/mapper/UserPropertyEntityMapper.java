package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserPropertyEntity;
import org.apache.ibatis.annotations.Param;

public interface UserPropertyEntityMapper extends MybatisMapper<UserPropertyEntity, String>{

    UserPropertyEntity findAttr(@Param("userId")String userId,
                               @Param("product")String product,
                               @Param("attrName")String attrName);
}