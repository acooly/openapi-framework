
insert into `sys_resource` ( `ID`, `PARENTID`, `SHOW_MODE`, `ICON`, `DESCN`, `VALUE`, `TYPE`, `ORDER_TIME`, `NAME`, `SHOW_STATE`) values ( '2018082114', null, '1', 'icons-resource-jiekuanbiaoxinxi', null, '', 'MENU', '2018-08-21 11:30:47', 'OpenAPI', '0');
insert into `sys_resource` ( `ID`, `PARENTID`, `SHOW_MODE`, `ICON`, `DESCN`, `VALUE`, `TYPE`, `ORDER_TIME`, `NAME`, `SHOW_STATE`) values ( '201808211401', '2018082114', '1', 'icons-resource-kehuguanli', null, '/manage/module/openapi/apiTenant/index.html', 'URL', '2018-08-21 11:30:47', '租户管理', '0');
insert into `sys_resource` ( `ID`, `PARENTID`, `SHOW_MODE`, `ICON`, `DESCN`, `VALUE`, `TYPE`, `ORDER_TIME`, `NAME`, `SHOW_STATE`) values ( '201808211402', '2018082114', '1', 'icons-resource-dingdan', null, '/manage/module/openapi/apiAuth/index.html', 'URL', '2018-08-21 11:30:48', '认证管理', '0');
insert into `sys_resource` ( `ID`, `PARENTID`, `SHOW_MODE`, `ICON`, `DESCN`, `VALUE`, `TYPE`, `ORDER_TIME`, `NAME`, `SHOW_STATE`) values ( '201808211403', '2018082114', '1', 'icons-resource-dingdan1', null, '/manage/openapi/orderInfo/index.html', 'URL', '2018-08-21 11:30:49', '订单查询', '0');
insert into `sys_resource` ( `ID`, `PARENTID`, `SHOW_MODE`, `ICON`, `DESCN`, `VALUE`, `TYPE`, `ORDER_TIME`, `NAME`, `SHOW_STATE`) values ( '201808211421', '2018082114', '1', 'icons-resource-jiekuanbiaoxinxi', null, '/manage/openapi/notifyMessage/index.html', 'URL', '2018-08-21 11:30:50', '通知记录', '0');

insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '2018082114');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211401');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211402');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211403');
insert into `SYS_ROLE_RESC` ( `ROLE_ID`, `RESC_ID`) values ( '1', '201808211421');
