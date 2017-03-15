CREATE TABLE `api_partner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `partner_id` varchar(32) NOT NULL COMMENT '合作方编码',
  `partner_name` varchar(32) NOT NULL COMMENT '合作方名称',
  `secret_type` varchar(16) NOT NULL COMMENT '安全方案 {digest:摘要,cert:证书}',
  `sign_type` varchar(16) NOT NULL COMMENT '签名类型{MD5:MD5,RSA:RSA}',
  `secret_key` varchar(45) NOT NULL COMMENT '秘钥',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='合作方管理';

CREATE TABLE `api_partner_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `partner_id` varchar(32) NOT NULL COMMENT '接入方编码',
  `parner_name` varchar(32) DEFAULT NULL COMMENT '接入方名称',
  `apipartnerid` bigint(11) NOT NULL COMMENT '接入方主键',
  `apiserviceid` bigint(11) NOT NULL COMMENT '服务主键',
  `service_name` varchar(32) NOT NULL COMMENT '服务名称',
  `service_version` varchar(16) NOT NULL COMMENT '服务版本',
  `service_title` varchar(45) NOT NULL COMMENT '服务中文名',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PARTNER_SERVICE` (`apipartnerid`,`apiserviceid`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

CREATE TABLE `api_scheme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `name` varchar(32) NOT NULL,
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='服务方案';

CREATE TABLE `api_service_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(11) DEFAULT NULL COMMENT '父类型',
  `path` varchar(45) DEFAULT NULL COMMENT '路径',
  `sort_time` bigint(11) unsigned zerofill DEFAULT NULL COMMENT '排序',
  `name` varchar(32) NOT NULL COMMENT '类型名称',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(64) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='服务分类';

CREATE TABLE `api_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '服务编码',
  `name` varchar(32) NOT NULL COMMENT '服务名',
  `version` varchar(16) NOT NULL COMMENT '服务版本',
  `title` varchar(32) NOT NULL COMMENT '中文描述',
  `type_id` bigint(11) NOT NULL COMMENT '分类',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(64) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_SERVICE_KEY` (`name`,`version`) COMMENT '服务唯一索引',
  KEY `FK_8m0vhsfvwi48tbivdtcr8urb` (`type_id`),
  CONSTRAINT `FK_8m0vhsfvwi48tbivdtcr8urb` FOREIGN KEY (`type_id`) REFERENCES `api_service_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='服务分类';


