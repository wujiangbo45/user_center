SELECT concat('INSERT INTO `opentsp_user_info` (`id`,`nickname`,`mobile`,`email`,`password`,`salt`,`account_non_locked`,`account_non_expired`,`account_actived`,`enable`,`product_id`,`create_time`,`update_time`)
VALUES (''247982ee78234755b9d06d2d54254369'',''M.G.'',''18612302838'',''1121506496@qq.com'',''$2a$08$CoDVcCGJhyvQJNoiuB3uoOer/rtJY/WzAyILjeiNpXNH93mZUYEKS'',''½§÷Ç'',''1'',''1'',''1'',''1'',''appstore'',''2015-12-17 14:43:21'',
''2016-01-14 12:59:03'');') AS sql_statment
FROM opentsp_user.user_auth_details;


select concat('INSERT INTO `opentsp_user_info` (`id`,`nickname`,`mobile`,`email`,`password`,`salt`,`account_non_locked`,`account_non_expired`,`account_actived`,`enable`,`product_id`,`create_time`,`update_time`)
VALUES (''',id,''',''',username,''',''',mobile,''',''',email,''',''',password,''','''',1,1,1,1,''opentsp'',''',now(),''',
''',now(),''');') as sql_statment from opentsp_user.user_auth_details;