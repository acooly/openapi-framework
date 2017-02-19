-- ----------------------------
--  Table structure for `api_notify_message`
-- ----------------------------
DROP TABLE IF EXISTS `api_notify_message`;
CREATE TABLE `api_notify_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gid` varchar(40) DEFAULT NULL,
  `partner_id` varchar(40) NOT NULL,
  `message_type` varchar(16) NOT NULL,
  `service` varchar(32) DEFAULT NULL,
  `version` varchar(8) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `send_count` int(11) NOT NULL,
  `next_send_time` datetime DEFAULT NULL,
  `status` varchar(16) NOT NULL,
  `execute_status` varchar(16) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_NOTIFY_STATUS` (`status`,`execute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `api_order`
-- ----------------------------
DROP TABLE IF EXISTS `api_order`;
CREATE TABLE `api_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(40) NOT NULL COMMENT '全站式统一流水号',
  `request_no` varchar(40) NOT NULL COMMENT '流水号',
  `partner_id` varchar(40) NOT NULL COMMENT '合作伙伴id',
  `order_no` varchar(40) DEFAULT NULL COMMENT '商户订单号',
  `oid` varchar(40) DEFAULT NULL COMMENT '内部订单号',
  `service` varchar(32) NOT NULL,
  `version` varchar(8) NOT NULL,
  `sign_type` varchar(16) DEFAULT NULL COMMENT '签名类型',
  `charset` varchar(16) DEFAULT NULL COMMENT '请求编码',
  `protocol` varchar(40) DEFAULT NULL COMMENT '交易流水号',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '异步通知地址',
  `return_url` varchar(256) DEFAULT NULL COMMENT '同步返回地址',
  `business_info` varchar(1024) DEFAULT NULL COMMENT '请求业务信息',
  `context` varchar(128) DEFAULT NULL COMMENT '客户端会话',
  `raw_add_time` datetime DEFAULT NULL COMMENT '创建/更新时间',
  `raw_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PARTNER_REQUEST` (`partner_id`,`request_no`) USING BTREE,
  KEY `IK_GID` (`gid`) USING BTREE,
  KEY `IK_PID_MNO_SERVICE` (`partner_id`,`order_no`,`service`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='请求信息表';
