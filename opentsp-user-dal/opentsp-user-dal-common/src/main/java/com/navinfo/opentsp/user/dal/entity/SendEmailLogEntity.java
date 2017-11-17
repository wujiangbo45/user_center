package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_send_email_log")
@Entity(table = "opentsp_send_email_log")
public class SendEmailLogEntity implements Identified<String> {

    private String id;

    /**
     * 哪个产品操作的
     */
    @Column(name = "op_product_id")
    private String opProductId;

    private String email;

    @Column(name = "user_id")
    private String userId;

    private String type;

    private String response;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "create_time")
    private Date createTime = new Date();

    private String content;

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOpProductId() {
        return opProductId;
    }

    public void setOpProductId(String opProductId) {
        this.opProductId = opProductId == null ? null : opProductId.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response == null ? null : response.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}