INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('20151126', NULL, '开放平台', 'MENU', '0', '2016-11-21 16:27:31', '', '1', 'icons-resource-shouyeshouye', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511261', '20151126', '服务管理', 'URL', '0', '2016-09-28 09:42:22', '/manage/apidoc/apiDocService/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511262', '20151126', '方案管理', 'URL', '0', '2016-11-21 16:29:33', '/manage/apidoc/apiDocScheme/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511263', '20151126', 'QA管理', 'URL', '0', '2016-11-21 16:29:49', '/manage/apidoc/apiQaQuestion/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511264', '201511263', '分类', 'URL', '1', '2016-11-21 16:30:42', '/manage/qa/qaClassify/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511265', '201511263', '子类', 'URL', '1', '2016-11-21 16:31:06', '/manage/apidoc/apiQaClassify/index.html', '1', 'icons-resource-shezhi1', NULL);


INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '20151126');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511261');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511262');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511263');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511264');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511265');