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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

