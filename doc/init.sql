--   -------------------------------------------------- 
--   Generated by Enterprise Architect Version 11.0.1106
--   Created On : 星期五, 25 九月, 2015 
--   DBMS       : MySql 
--   -------------------------------------------------- 



--  Drop Tables, Stored Procedures and Views 

DROP TABLE IF EXISTS opentsp_config_props CASCADE;
DROP TABLE IF EXISTS opentsp_product_info CASCADE;
DROP TABLE IF EXISTS opentsp_user_token_info CASCADE;
DROP TABLE IF EXISTS opentsp_user_car_info CASCADE;
DROP TABLE IF EXISTS user_register_statics CASCADE;
DROP TABLE IF EXISTS opentsp_send_email_log CASCADE;
DROP TABLE IF EXISTS opentsp_send_sms_log CASCADE;
DROP TABLE IF EXISTS opentsp_user_profile CASCADE;
DROP TABLE IF EXISTS opentsp_appid_key_info CASCADE;
DROP TABLE IF EXISTS opentsp_user_login_log CASCADE;
DROP TABLE IF EXISTS opentsp_user_props CASCADE;
DROP TABLE IF EXISTS opentsp_third_link CASCADE;
DROP TABLE IF EXISTS opentsp_user_info CASCADE;

--  Create Tables 
CREATE TABLE opentsp_config_props
(
	id VARCHAR(32) NOT NULL,
	product_id VARCHAR(255) NOT NULL COMMENT '产品标识，默认值为-1，表示全部产品都使用这个配置。',
	type VARCHAR(255) NOT NULL COMMENT '类型/功能点， 比如： captcha, session...',
	prop_name VARCHAR(255) NOT NULL COMMENT '参数名称',
	prop_value VARCHAR(255) COMMENT '参数值',
	description VARCHAR(255) NOT NULL COMMENT '描述',
	default_val VARCHAR(255) NOT NULL,
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id),
	UNIQUE uq_pro_type_name(product_id, type, prop_name)
) ENGINE=InnoDB COMMENT='系统配置表';


CREATE TABLE opentsp_product_info
(
	product_id VARCHAR(255) NOT NULL,
	product_name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	create_time DATETIME NOT NULL,
	status INT  DEFAULT 0,
	PRIMARY KEY (product_id)

)  ENGINE=InnoDB COMMENT='产品线信息';


CREATE TABLE opentsp_user_token_info
(
	id VARCHAR(32) NOT NULL,
	user_id VARCHAR(32) NOT NULL,
	login_name VARCHAR(255) NOT NULL,
	op_product_id VARCHAR(255) NOT NULL COMMENT '哪个产品操作（登录）的',
	token VARCHAR(255) NOT NULL,
	create_time DATETIME NOT NULL,
	device_id VARCHAR(255),
	device_type VARCHAR(255) NOT NULL,
	app_version VARCHAR(255),
	client_ip VARCHAR(50) COMMENT '客户端ip',
	is_valid BIT(1) NOT NULL DEFAULT 1 COMMENT '是否有效，1有效，0无效',
	PRIMARY KEY (id),
	UNIQUE UQ_opentsp_user_token_info_token(token)

)  ENGINE=InnoDB COMMENT='用户token表';

CREATE INDEX opentsp_user_token_info_uid ON opentsp_user_token_info(user_id);

CREATE TABLE opentsp_user_car_info
(
	id VARCHAR(32) NOT NULL,
	user_id VARCHAR(32) NOT NULL COMMENT '用户id',
	op_product_id VARCHAR(50) NOT NULL COMMENT '哪个产品操作的',
	car_model_id VARCHAR(255) NOT NULL,
	car_icon VARCHAR(500) COMMENT '车标图片地址',
	car_no VARCHAR(50) COMMENT '车牌号',
	engine_no VARCHAR(255) COMMENT '发动机号',
	buy_date DATETIME,
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time DATETIME NOT NULL COMMENT '更新时间',
	is_valid BIT(1) NOT NULL DEFAULT 1 COMMENT '是否可用，0不可用，1可用',
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='用户关联车信息';

CREATE INDEX uid_ucar on opentsp_user_car_info(user_id);

CREATE TABLE user_register_statics
(
	id VARCHAR(32) NOT NULL,
	register_year INT NOT NULL,
	register_month VARCHAR(50) NOT NULL,
	register_week INT NOT NULL,
	register_date VARCHAR(50) NOT NULL COMMENT '注册日期， eg. 2015-05-12',
	register_src VARCHAR(255) NOT NULL COMMENT '注册来源， web, mobile, car_box',
	register_num BIGINT NOT NULL DEFAULT 0,
	product VARCHAR(255) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE uq_ur_statics(product, register_date, register_src)

) ENGINE=InnoDB COMMENT='注册用户统计表';

CREATE TABLE opentsp_send_email_log
(
	id VARCHAR(32) NOT NULL,
	op_product_id VARCHAR(255) NOT NULL COMMENT '哪个产品操作的',
	subject VARCHAR(500) NOT NULL,
	email VARCHAR(255) NOT NULL COMMENT 'email',
	user_id VARCHAR(32),
	type VARCHAR(50) NOT NULL COMMENT '类型，例如：注册，找回密码贩',
	content TEXT NOT NULL,
	response VARCHAR(255) NOT NULL,
	client_ip VARCHAR(50) COMMENT '客户端ip',
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='发邮件表';

CREATE INDEX idx_osel_ctime ON opentsp_send_email_log(create_time);

CREATE TABLE opentsp_send_sms_log
(
	id VARCHAR(32) NOT NULL,
	op_product_id VARCHAR(255) NOT NULL COMMENT '通过哪个产品操作的',
	user_id VARCHAR(32),
	mobile VARCHAR(32) NOT NULL,
	type VARCHAR(50) NOT NULL COMMENT '类型， 取值范围：register, find_password, quick_login',
	content TEXT NOT NULL,
	response VARCHAR(255) NOT NULL,
	client_ip VARCHAR(50),
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='发送短信记录';

CREATE INDEX idx_ossl_ctime ON opentsp_send_sms_log(create_time);


CREATE TABLE opentsp_user_profile
(
	user_id VARCHAR(32) NOT NULL,
	img_url VARCHAR(255),
	gender BIT(1) COMMENT '性别，1男，0女',
	birthday DATETIME,
	province VARCHAR(255) COMMENT '省份、 名称',
	city VARCHAR(255),
	district VARCHAR(255),
	mobile_actived BIT(1) NOT NULL DEFAULT 0 COMMENT '手机号已激活 , 0 否， 1是',
	email_actived BIT(1) NOT NULL DEFAULT 0 COMMENT '邮箱已激活，0 未激活，1已激活',
	nickname_modifiable BIT(1) NOT NULL DEFAULT 0 COMMENT '昵称是否可修改，0不可，1可以',
	register_src VARCHAR(255) NOT NULL COMMENT '注册来源，取值范围： web, mobile, car_pad ',
	register_device_id VARCHAR(255),
	register_device_type VARCHAR(255) NOT NULL,
	register_app_version VARCHAR(255),
	PRIMARY KEY (user_id)

)  ENGINE=InnoDB COMMENT='用户其他资料';


CREATE TABLE opentsp_appid_key_info
(
	id VARCHAR(32) NOT NULL,
	third_type VARCHAR(50) NOT NULL COMMENT '第三方类型， 取值范围： weixin， qq',
	third_name VARCHAR(255) NOT NULL,
	appid VARCHAR(255) NOT NULL,
	appkey VARCHAR(255) NOT NULL,
	product_id VARCHAR(255) NOT NULL,
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='第三方平台的appid appkey 管理';


CREATE TABLE opentsp_user_login_log
(
	id VARCHAR(32) NOT NULL,
	user_id VARCHAR(32),
	product_id VARCHAR(255) NOT NULL COMMENT '注册时候的product',
	op_product_id VARCHAR(255) NOT NULL COMMENT '哪个产品操作（登录）的',
	login_name VARCHAR(255) NOT NULL COMMENT '登录名，可能是邮箱、手机号、openid',
	login_time DATETIME NOT NULL,
	login_ip VARCHAR(50),
	login_result VARCHAR(255) NOT NULL COMMENT '登录结果',
	token VARCHAR(255) COMMENT '登录后的token',
	device_type VARCHAR(255) NOT NULL,
	device_id VARCHAR(255),
	app_version VARCHAR(255),
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='用户登陆历史记录';

-- CREATE INDEX uid_ulogin on opentsp_user_login_log(user_id);
CREATE INDEX uid_ulogin on opentsp_user_login_log(user_id, login_time desc);

CREATE TABLE opentsp_user_props
(
	id VARCHAR(32) NOT NULL,
	user_id VARCHAR(32) NOT NULL,
	product_id VARCHAR(255) NOT NULL COMMENT '哪个产品用这个属性',
	attr_name VARCHAR(255) NOT NULL,
	attr_value VARCHAR(255),
	description VARCHAR(255) NOT NULL COMMENT '描述',
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id)

)  ENGINE=InnoDB COMMENT='用户属性表， 需要包含已下固定属性：
 	- 其他属性视情况扩展      ';

CREATE INDEX uid_uprops on opentsp_user_props(user_id);


CREATE TABLE opentsp_third_link
(
	id VARCHAR(32) NOT NULL COMMENT '主键',
	user_id VARCHAR(32) NOT NULL COMMENT '用户id',
	op_product_id VARCHAR(255) COMMENT '哪个产品操作的',
	third_type VARCHAR(50) NOT NULL COMMENT '第三方类型， 取值范围： weixin， qq',
	third_auth_info VARCHAR(50) NOT NULL COMMENT 'opentsp_appid_key_info 的主键',
	third_open_id VARCHAR(255) NOT NULL COMMENT '第三方唯一id， 对于qq和微信就是openId',
	access_token VARCHAR(255) COMMENT '第三方的access_token， 先存着',
	refresh_token VARCHAR(255) COMMENT '第三方平台的refresh_token, 可能没用 先存着',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL COMMENT '更新时间，自动更新',
	is_valid BIT(1) NOT NULL DEFAULT 1 COMMENT '是否可用，0不可用，1可用',
	PRIMARY KEY (id),
	UNIQUE UQ_opentsp_third_link_third_open_id(third_type, third_open_id)

)  ENGINE=InnoDB COMMENT='用户第三方账户绑定';

CREATE INDEX uid_uthird on opentsp_third_link(user_id);

CREATE TABLE opentsp_user_info
(
	id VARCHAR(32) NOT NULL COMMENT '用户id',
	nickname VARCHAR(255),
	mobile VARCHAR(32) COMMENT '手机号',
	email VARCHAR(100),
	password VARCHAR(255) NOT NULL COMMENT '密码',
	salt VARCHAR(255) COMMENT 'salt',
	account_non_locked BIT(1) NOT NULL DEFAULT 1 COMMENT '账户是否被锁定， 0锁定，1未锁定',
	account_non_expired BIT(1) NOT NULL DEFAULT 1 COMMENT '账户是否已过期，0已过期，1未过期',
	account_actived BIT(1) NOT NULL DEFAULT 0 COMMENT '账户是否已经激活， 1已激活，0未激活',
	enable BIT(1) NOT NULL DEFAULT 1 COMMENT '账户是否启用， 1启用，0未启用',
	product_id VARCHAR(255) NOT NULL,
	create_time DATETIME NOT NULL COMMENT '注册时间',
	update_time TIMESTAMP NOT NULL COMMENT '更新时间， 自动更新',
	PRIMARY KEY (id),
	UNIQUE UQ_opentsp_user_info_mobile(mobile),
	UNIQUE UQ_opentsp_user_info_email(email)

)  ENGINE=InnoDB COMMENT='用户基本信息表';

CREATE INDEX idx_oui_ctime ON opentsp_user_info(create_time);

-- ===================== create view ====================
create or replace view opentsp_user_info_view
as
	select t1.id, t1.create_time, t1.product_id, t2.register_src
	from opentsp_user_info t1
		inner join opentsp_user_profile t2
			on t2.user_id = t1.id;