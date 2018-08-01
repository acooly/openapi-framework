-- ----------------------------
--  Table structure for `api_order_info`
-- ----------------------------
DROP TABLE IF EXISTS `api_order_info`;
CREATE TABLE `api_order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `access_key` varchar(40) NOT NULL COMMENT '访问key',
  `partner_id` varchar(40) NOT NULL COMMENT '商户ID',
  `gid` varchar(40) NOT NULL COMMENT '统一流水',
  `request_no` varchar(40) NOT NULL COMMENT '请求号',
  `service` varchar(32) NOT NULL COMMENT '服务名',
  `version` varchar(8) NOT NULL COMMENT '版本号',
  `sign_type` varchar(16) DEFAULT NULL COMMENT '签名类型',
  `protocol` varchar(40) DEFAULT NULL COMMENT '协议',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '通知地址',
  `return_url` varchar(256) DEFAULT NULL COMMENT '返回地址',
  `business_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `context` varchar(128) DEFAULT NULL COMMENT '会话信息',
  `raw_add_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `raw_update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PARTNER_REQUEST` (`request_no`) USING BTREE,
  KEY `IDX_ORDER_GID` (`gid`) USING BTREE,
  KEY `IDX_PARTNER_ORDER` (`partner_id`,`service`,`version`) USING BTREE,
  KEY `IDX_ADD_TIME` (`raw_add_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='请求信息表';