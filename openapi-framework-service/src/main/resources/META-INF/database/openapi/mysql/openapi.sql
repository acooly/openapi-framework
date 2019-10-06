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

CREATE TABLE `api_notify_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gid` varchar(40) DEFAULT NULL,
  `partner_id` varchar(40) DEFAULT NULL,
  `request_no` varchar(40) DEFAULT NULL,
  `merch_order_no` varchar(40) DEFAULT NULL,
  `message_type` varchar(16) NOT NULL,
  `protocol` varchar(40) DEFAULT NULL COMMENT '报文协议',
  `sign_type` VARCHAR(32) NULL COMMENT '签名类型',
  `sign` VARCHAR(512) NULL COMMENT '签名',
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


CREATE TABLE `api_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `partner_id` varchar(32) NOT NULL COMMENT '合作方编码',
  `partner_name` varchar(32) NOT NULL COMMENT '合作方名称',
  `secret_type` varchar(16) NOT NULL COMMENT '安全方案 {digest:摘要,cert:证书}',
  `sign_type` varchar(16) NOT NULL COMMENT '签名类型{MD5:MD5,RSA:RSA}',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_partner_id` (`partner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='租户管理';

CREATE TABLE `api_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `access_key` varchar(45) NOT NULL COMMENT '访问帐号',
  `secret_key` varchar(45) NOT NULL COMMENT '访问秘钥',
  `permissions` varchar(512) COMMENT '访问权限',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_access_key` (`access_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='认证授权信息管理';

