package com.navinfo.opentsp.user.service.location;

import com.navinfo.opentsp.user.dal.dao.LocationDao;
import com.navinfo.opentsp.user.dal.entity.LocationEntity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class LocationServiceImpl implements LocationService, InitializingBean, Runnable {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationDao locationDao;
    @Autowired
    private LocationDataLoader dataLoader;

    @Autowired
    private ExecutorService executorService;

    @Override
    public List<LocationEntity> findCity(String parentId, String name) {
        return this.locationDao.findCity(parentId, name);
    }

    @Override
    public void reload() {
        this.dataLoader.loadData();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!dataLoader.isDataLoaded()) {
            logger.warn("no province/city/area data found ! system will loading to database !");
            this.executorService.submit(this);
        }
    }

    @Override
    public void run() {
        this.dataLoader.loadData();
    }
}
