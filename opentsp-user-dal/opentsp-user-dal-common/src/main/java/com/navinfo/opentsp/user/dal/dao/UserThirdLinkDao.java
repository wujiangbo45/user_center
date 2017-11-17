package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.UserThirdLinkEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserThirdLinkDao extends BaseDAO<UserThirdLinkEntity, String>{

    public UserThirdLinkEntity findByTypeAndId(String thirdType, String openId);
    public UserThirdLinkEntity findByTypeAndUnionId(String thirdType, String unionid);
    public UserThirdLinkEntity findByTypeAndOpenId(String thirdType, String openId);
    public List<String> selectByUserId(String userId,byte isVaild);
    public String  selectIdByUserIdAndType(String userId,String thridType,byte isValid);
}
