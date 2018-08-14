CREATE TABLE `api_mock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(128) NOT NULL COMMENT '服务名称',
  `version` varchar(32) DEFAULT NULL COMMENT '版本号',
  `expect` varchar(256) DEFAULT NULL COMMENT '期望参数',
  `response` varchar(1024) DEFAULT NULL COMMENT '同步响应',
  `notify` varchar(1024) DEFAULT NULL COMMENT '异步响应',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1512 DEFAULT CHARSET=utf8mb4 COMMENT='服务mock';