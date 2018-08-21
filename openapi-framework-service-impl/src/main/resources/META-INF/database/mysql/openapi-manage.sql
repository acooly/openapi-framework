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