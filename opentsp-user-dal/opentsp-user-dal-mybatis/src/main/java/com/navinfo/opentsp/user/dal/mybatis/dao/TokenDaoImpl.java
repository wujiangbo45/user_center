package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.TokenDao;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.TokenEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class TokenDaoImpl extends BaseDAOImpl<TokenEntity, String> implements TokenDao {

    @Autowired
    private TokenEntityMapper mapper;

    @Override
    protected MybatisMapper<TokenEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public int removeToken(String token) {
        return this.mapper.removeToken(token);
    }

    @Override
    public int removeAllToken(String userId) {
        return this.mapper.removeAllToken(userId);
    }

    @Override
    public int removeTokenByDeviceType(String userId, String deviceType) {
        return mapper.removeTokenByDeviceType(userId, deviceType);
    }

    @Override
    public TokenEntity getToken(String token) {
        return mapper.getToken(token);
    }

    @Override
    public TokenEntity getLatestAvailableToken(String userId, String deviceType) {
        return mapper.getLatestAvailableToken(userId, deviceType);
    }
}
