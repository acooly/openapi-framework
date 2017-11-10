-- ----------------------------
--  Table structure for `api_order_info`
-- ----------------------------
DROP TABLE IF EXISTS `api_order_info`;
CREATE TABLE `api_order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(40) NOT NULL COMMENT '统一流水',
  `request_no` varchar(40) NOT NULL COMMENT '请求号',
  `partner_id` varchar(40) NOT NULL COMMENT '商户ID',
  `order_no` varchar(40) DEFAULT NULL COMMENT '订单号',
  `oid` varchar(40) DEFAULT NULL COMMENT '内部订单号',
  `service` varchar(32) NOT NULL COMMENT '服务名',
  `version` varchar(8) NOT NULL COMMENT '版本号',
  `sign_type` varchar(16) DEFAULT NULL COMMENT '签名类型',
  `charset` varchar(16) DEFAULT NULL COMMENT '请求编码',
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
  KEY `IDX_PARTNER_ORDER` (`partner_id`,`order_no`,`service`,`version`) USING BTREE,
  KEY `IDX_ADD_TIME` (`raw_add_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='请求信息表';


-- ----------------------------
--  Table structure for `api_notify_message`
-- ----------------------------
DROP TABLE IF EXISTS `api_notify_message`;
CREATE TABLE `api_notify_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gid` varchar(40) DEFAULT NULL,
  `partner_id` varchar(40) DEFAULT NULL,
  `request_no` varchar(40) DEFAULT NULL,
  `merch_order_no` varchar(40) DEFAULT NULL,
  `message_type` varchar(16) NOT NULL,
  `service` varchar(32) DEFAULT NULL,
  `version` varchar(8) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `resp_info` varchar(128) DEFAULT NULL,
  `send_count` int(11) NOT NULL,
  `next_send_time` datetime DEFAULT NULL,
  `status` varchar(16) NOT NULL,
  `execute_status` varchar(16) NOT NULL,
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `IDX_NOTIFY_STATUS` (`status`,`execute_status`),
  KEY `IDX_NOTIFY_QUERY` (`gid`,`partner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

