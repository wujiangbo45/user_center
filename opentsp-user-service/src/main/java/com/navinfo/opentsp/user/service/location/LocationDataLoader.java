package com.navinfo.opentsp.user.service.location;

import com.navinfo.opentsp.user.dal.dao.LocationDao;
import com.navinfo.opentsp.user.dal.entity.LocationEntity;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class LocationDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(LocationDataLoader.class);

    @Autowired
    private LocationDao locationDao;

    public void loadData() {
        logger.info("drop collections !");
        locationDao.deleteAll();
        logger.info("drop collections success! loading data ...");

        InputStream is = null;
        Resource resource = null;
        try {
            resource = new ClassPathResource("city/city.xml");
            is = resource.getInputStream();
            Document root = new SAXReader().read(is);
            this.loadAndSave(root);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }

        logger.info("loading data success ! total num : {}", this.locationDao.count());
    }

    public boolean isDataLoaded() {
        return this.locationDao.count() > 0;
    }

    private void loadAndSave(Document document) {
        Element root = document.getRootElement();
        this.doInIterate(root, "/country/zone/zoneitem/province/*", new ElementIterateCallback() {
            @Override
            public void forEach(Element element) {
                logger.info("loading province : {}", element.elementText("cname"));
                LocationEntity locationEntity = saveLocationEntity(element, null);
                saveCity(element, locationEntity);
                logger.info("loading province {} success !", element.elementText("cname"));
            }
        });
    }

    private LocationEntity saveLocationEntity(Element element, LocationEntity parent) {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setCityCode(element.elementText("citycode"));
        locationEntity.setName(element.elementText("cname"));
        locationEntity.setCreateTime(new Date());
        locationEntity.setEname(element.elementText("ename"));
        locationEntity.setParentCode(parent == null ? null : parent.getCityCode());
        locationEntity.setLevel(parent == null ? 0 : parent.getLevel() + 1);
        locationEntity.setPath((parent == null ? "" : parent.getPath()) + "/" + locationEntity.getCityCode());
        this.locationDao.save(locationEntity);
        return locationEntity;
    }

    private void saveCity(Element province, LocationEntity parentEntity) {
        this.doInIterate(province, "city/*", new ElementIterateCallback() {
            @Override
            public void forEach(Element element) {
                logger.info("loading city : {}", element.elementText("cname"));
                LocationEntity locationEntity = saveLocationEntity(element, parentEntity);
                saveArea(element, locationEntity);
                logger.info("loading city {} success !", element.elementText("cname"));
            }
        });
    }

    public void saveArea(Element city, LocationEntity parentEntity) {
        this.doInIterate(city, "area/*", new ElementIterateCallback() {
            @Override
            public void forEach(Element element) {
                logger.info("loading area : {}", element.elementText("cname"));
                saveLocationEntity(element, parentEntity);
            }
        });
    }

    protected void doInIterate(Element parentElement, String xPath, ElementIterateCallback callback) {
        List<Element> elementList = parentElement.selectNodes(xPath);
        for (Element element : elementList){
            callback.forEach(element);
        }
    }

    interface ElementIterateCallback {
        public void forEach(Element element);
    }

}
