INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151001', NULL, 'APP组件', 'MENU', '0', '2015-09-28 18:59:46', '', '1', 'icons-resource-app', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151002', '151001', '版本管理', 'URL', '0', '2015-10-01 03:10:00', '/manage/module/app/appVersion/index.html', '1', 'icons-resource-fangdaozhuomian', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151003', '151001', '崩溃报告', 'URL', '0', '2015-10-01 03:20:00', '/manage/module/app/appCrash/index.html', '1', 'icons-resource-mac', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151004', '151001', '首页滑窗', 'URL', '0', '2015-10-01 03:30:00', '/manage/module/app/appBanner/index.html', '1', 'icons-resource-anli1', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151005', '151001', '设备绑定', 'URL', '0', '2015-10-01 03:40:00', '/manage/module/app/appCustomer/index.html', '1', 'icons-resource-app1', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151006', '151001', '启动图片', 'URL', '0', '2015-10-01 04:50:00', '/manage/module/app/appWelcome/index.html', '1', 'icons-resource-ipad', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151007', '151001', '引导图片', 'URL', '0', '2015-10-01 04:10:00', '/manage/module/app/appStartGuide/index.html', '1', 'icons-resource-dingdan', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('151008', '151001', '消息推送', 'URL', '0', '2015-10-01 05:10:00', '/manage/module/app/appMessage/index.html', '1', 'icons-resource-jiekuanhetongguanli', NULL);


INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151001');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151002');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151003');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151004');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151005');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151006');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151007');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '151008');
