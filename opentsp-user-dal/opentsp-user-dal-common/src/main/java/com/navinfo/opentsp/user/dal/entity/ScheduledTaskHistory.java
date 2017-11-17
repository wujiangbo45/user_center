package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-09
 * @modify
 * @copyright Navi Tsp
 */
@Document(collection = "scheduled_task_history")
public class ScheduledTaskHistory implements Identified<String> {

    @Id
    private String id;

    private String taskClass;

    private String taskMethod;

    private Date createTime;

    private Date endTime;

    private String note;

    public String getId() {
        return id;
    }

    @Override
    public void generateID() {
        this.id = UUIDUtil.randomUUID();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getTaskMethod() {
        return taskMethod;
    }

    public void setTaskMethod(String taskMethod) {
        this.taskMethod = taskMethod;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
