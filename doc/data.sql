-- insert system configurations
TRUNCATE TABLE opentsp_config_props;

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'register', 'captcha_timeout', NULL, '注册时候，图形验证码有效期(秒)', '300', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'register', 'sms_timeout', NULL, '注册时候，短信验证码有效期(秒)', '1800', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'register', 'sms_code_length', NULL, '短信验证码长度', '6', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'session', 'token_expire_time', NULL, 'token有效期（分钟）', '43200', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'session', 'token_web_expire_time', NULL, 'token有效期（分钟）', '1440', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'session', 'session_expire_time', NULL, 'session有效期（分钟）', '30', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'register', 'email_timeout', NULL, '邮件验证码有效期（秒）', '172800', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'system', 'simple_aes_key', NULL, '简单的aes加密的key', 'EDi|<JD=_+}+lHuI', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'login', 'login_failed_time', NULL,
        '登录失败时间， 分钟 。 在 ${login_failed_time}分钟 内 登录失败 ${login_failed_count} 次，需要验证码', '10', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'login', 'login_failed_count', NULL,
        '登录失败次数。在 ${login_failed_time}分钟 内 登录失败 ${login_failed_count} 次，需要验证码', '3', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'sms', 'sms_ip_max', NULL, '每个ip每天最多发几条短信', '8', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'sms', 'sms_phone_max', NULL, '每个手机号每天最多发几条短信', '5', now());

INSERT INTO opentsp_config_props (id, product_id, type, prop_name, prop_value, description, default_val, create_time)
VALUES (replace(uuid(), '-', ''), '-1', 'sms', 'sms_phone_content_max', NULL, '每个手机号相同的内容每天最多发几条短信', '3', now());



-- insert product information
TRUNCATE TABLE opentsp_product_info;

INSERT INTO opentsp_product_info (product_id, product_name, description, create_time)
VALUES ('appstore', 'appstore', '应用商店', now());
INSERT INTO opentsp_product_info (product_id, product_name, description, create_time)
VALUES ('welink', 'welink', '应用商店', now());

-- insert opentsp appid appkey info
TRUNCATE TABLE opentsp_appid_key_info;
INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'qq', 'QQ登录', '1104359655', 'nciPPInqPRzcUT8h', '-1', now());

INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'weixin', '微信登录', 'wxa44cac64438a9924', 'e2a17017c2cb7e67a897b7a084a332df', '-1', now());

INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'qq', 'QQ登录', '1104874807', 'p3rC3MO2NUqtZHjS', 'appstore', now());

INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'weixin', '微信登录', 'wxd0ba3905a2b422e4', 'd4624c36b6795d1d99dcf0547af5443d', 'appstore', now());

INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'qq', 'QQ登录', '1104954062', 'M15hof8ouYIxTGYK', 'welink', now());

INSERT INTO opentsp_appid_key_info (id, third_type, third_name, appid, appkey, product_id, create_time)
VALUES (replace(uuid(), '-', ''), 'weixin', '微信登录', 'wxc7b715358fad6c5b', 'e3bdcdd81e92e0f53bd987f60cf8d744', 'welink', now());