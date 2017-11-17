package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * not used in mongodb
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@Document(collection = "location_link_info")
@CompoundIndexes({
        @CompoundIndex(name = "lli_parentId", def = "{'parentId':1}"),
        @CompoundIndex(name = "lli_sonId", def = "{'sonId':1}")
})
@Deprecated
public class LocationLinkEntity implements Identified<String> {

    public static final int DIRECT_Y = 1;
    public static final int DIRECT_N = 0;

    @Id
    private String id;

    private String parentId;

    private String sonId;

    private int direct;

    public String getId() {
        return id;
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSonId() {
        return sonId;
    }

    public void setSonId(String sonId) {
        this.sonId = sonId;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
}
