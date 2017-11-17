package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.TokenDao;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-25
 * @modify
 * @copyright Navi Tsp
 */
//TODO implements
@Repository
@Mongo
public class TokenDaoImpl extends BaseMongoDaoImpl<TokenEntity, String> implements TokenDao {
    @Override
    public int removeToken(String token) {
        return 0;
    }

    @Override
    public int removeAllToken(String userId) {
        return 0;
    }

    @Override
    public int removeTokenByDeviceType(String userId, String deviceType) {
        return 0;
    }

    @Override
    public TokenEntity getToken(String token) {
        return null;
    }

    @Override
    public TokenEntity getLatestAvailableToken(String userId, String deviceType) {
        return null;
    }
}
