
INSERT INTO `doc_scheme` (`id`, `author`, `english_name`, `name`, `name_type`, `note`, `scheme_type`, `visit_type`, `scheme_content_id`) VALUES ('1', 'zhike', 'common service', '通用服务', 'commonScheme', '通用解决方案', 'common', 'out', NULL);

INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('20151126', NULL, '开放平台', 'MENU', '0', '2016-11-21 16:27:31', '', '1', 'icons-resource-shouyeshouye', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511261', '20151126', '服务管理', 'URL', '0', '2016-09-28 09:42:22', '/manage/api/apiServiceDoc/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511262', '20151126', '方案管理', 'URL', '0', '2016-11-21 16:29:33', '/manage/api/apiDocScheme/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511263', '20151126', 'QA管理', 'URL', '0', '2016-11-21 16:29:49', '/manage/qa/qaQuestion/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511264', '201511263', '分类', 'URL', '1', '2016-11-21 16:30:42', '/manage/qa/qaClassify/index.html', '1', 'icons-resource-shezhi', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201511265', '201511263', '子类', 'URL', '1', '2016-11-21 16:31:06', '/manage/qa/subClassify/index.html', '1', 'icons-resource-shezhi1', NULL);


INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '20151126');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511261');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511262');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511263');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511264');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201511265');





