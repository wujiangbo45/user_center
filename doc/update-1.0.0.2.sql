ALTER TABLE opentsp_user_login_log DROP index uid_ulogin;
CREATE INDEX uid_ulogin on opentsp_user_login_log(user_id, login_time desc);

ALTER TABLE opentsp_product_info add status INT  DEFAULT 0;