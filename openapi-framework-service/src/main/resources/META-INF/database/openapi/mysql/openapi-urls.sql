INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`, `create_time`, `update_time`)
VALUES
	(2018082114, NULL, 'OpenAPI', 'MENU', 0, '2018-08-21 11:30:47', '', 1, 'fa fa-jsfiddle', NULL, '2019-01-23 16:03:56', '2019-01-23 16:03:56'),
	(201808211401, 2018082114, '接入管理', 'URL', 0, '2019-02-03 00:26:32', '/manage/openapi/apiPartner/index.html', 1, 'fa-circle-o', NULL, '2019-01-23 16:03:56', '2019-02-03 00:27:52'),
	(201808211402, 2018082114, '认证授权', 'URL', 0, '2019-02-03 00:26:31', '/manage/openapi/apiAuth/index.html', 1, 'fa-circle-o', NULL, '2019-01-23 16:03:56', '2019-02-03 00:27:46'),
	(201808211403, 2018082114, '请求订单', 'URL', 0, '2019-02-03 00:26:30', '/manage/openapi/orderInfo/index.html', 1, 'fa-circle-o', NULL, '2019-01-23 16:03:56','2019-02-03 00:27:37'),
	(201808211421, 2018082114, '异步通知', 'URL', 0, '2019-02-03 00:26:29', '/manage/openapi/notifyMessage/index.html', 1, 'fa-circle-o', NULL,'2019-02-03 00:26:29', '2019-02-03 00:26:29'),
    (201808211422, 2018082114, '动态秘钥', 'URL', 0, '2019-02-03 00:26:30', '/manage/openapi/apiAuth/dynamic.html', 1, 'fa-circle-o', NULL, '2023-07-10 16:23:54', '2023-07-10 16:34:31');

insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '2018082114');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211401');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211402');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211403');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211421');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211422');
