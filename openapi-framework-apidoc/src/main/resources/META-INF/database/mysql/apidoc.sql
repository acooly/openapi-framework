CREATE TABLE `api_doc_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_no` varchar(150) NOT NULL COMMENT '服务编号(name+version)',
  `name` varchar(128) NOT NULL COMMENT '服务名称',
  `version` varchar(32) DEFAULT '1.0' COMMENT '服务版本',
  `title` varchar(256) NOT NULL DEFAULT '' COMMENT '服务标题',
  `owner` varchar(64) DEFAULT NULL COMMENT '所属系统',
  `note` varchar(4000) DEFAULT '' COMMENT '服务说明',
  `manual_note` varchar(512) DEFAULT '' COMMENT '手工说明',
  `service_type` varchar(64) NOT NULL DEFAULT '' COMMENT '服务类型{SYNC:同步}',
  `busi_type` varchar(32) DEFAULT NULL COMMENT '业务类型{Query:查询}',
  `sort_time` bigint(20) DEFAULT NULL COMMENT '排序值',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `signature` varchar(128) DEFAULT NULL COMMENT '签名(MD5(service_no+title+owner+note+service_type+busi_type))',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_API_DOC_SERVICE_NO` (`service_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='服务信息';

CREATE TABLE `api_doc_scheme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `scheme_no` varchar(64) NOT NULL DEFAULT '' COMMENT '方案编码',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '标题',
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `note` varchar(128) DEFAULT NULL COMMENT '说明',
  `scheme_type` varchar(16) DEFAULT '' COMMENT '方案类型{auto:通用,custom:自定义}',
  `sort_time` bigint(20) DEFAULT NULL COMMENT '排序值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='服务方案';

CREATE TABLE `api_doc_scheme_desc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `scheme_no` varchar(64) NOT NULL DEFAULT '' COMMENT '方案编码',
  `scheme_desc` text DEFAULT NULL COMMENT '方案描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_API_SCHEME_DESC` (`scheme_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='服务方案描述';

CREATE TABLE `api_doc_scheme_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `scheme_no` varchar(64) NOT NULL DEFAULT '' COMMENT '方案编码',
  `service_no` varchar(64) NOT NULL DEFAULT '' COMMENT '服务编码',
  `sort_time` bigint(20) DEFAULT NULL COMMENT '排序值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_API_SCHEME_SERVICE` (`scheme_no`,`service_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='方案服务列表';

CREATE TABLE `api_doc_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `message_no` varchar(64) NOT NULL DEFAULT '' COMMENT '消息编号(服务编号+message_type)',
  `service_no` varchar(64) NOT NULL COMMENT '服务编码',
  `message_type` varchar(32) NOT NULL DEFAULT '' COMMENT '消息类型{Requst:请求}',
  `note` varchar(255) DEFAULT NULL COMMENT '自动说明',
  `manual_note` varchar(255) DEFAULT NULL COMMENT '人工说明',
  `sort_time` bigint(11) DEFAULT NULL COMMENT '排序值',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `signatrue` varchar(128) DEFAULT NULL COMMENT '签名(message_no+message_type+note)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_API_DOC_MESSAGE_NO` (`message_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='服务报文';

CREATE TABLE `api_doc_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父ID',
  `message_no` varchar(64) NOT NULL COMMENT '报文编码',
  `parent_no` varchar(64) DEFAULT NULL COMMENT '父编号',
  `item_no` varchar(64) DEFAULT NULL COMMENT '唯一编号(message_no+parent_item_no+name)',
  `name` varchar(128) NOT NULL COMMENT '字段名称',
  `title` varchar(128) NOT NULL COMMENT '字段标题',
  `min` int(11) DEFAULT '0' COMMENT '最小长度',
  `max` int(11) DEFAULT NULL COMMENT '最大长度',
  `data_type` varchar(16) NOT NULL COMMENT '数据类型',
  `descn` varchar(1024) DEFAULT NULL COMMENT '字段描述',
  `demo` varchar(1024) DEFAULT NULL COMMENT '字段示例',
  `status` varchar(16) NOT NULL DEFAULT '' COMMENT '可选状态',
  `encryptStatus` varchar(16) DEFAULT NULL COMMENT '是否加密{yes:需要,no:无需}',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort_time` bigint(20) DEFAULT NULL COMMENT '排序辅助值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `signatrue` varchar(128) DEFAULT NULL COMMENT '签名(item_no+name+title+data_length+data_type+descn+demo+status+encryptStatus)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_api_doc_item` (`item_no`),
  KEY `FK_apidoc_item_self` (`parent_id`),
  CONSTRAINT `FK_apidoc_item_self` FOREIGN KEY (`parent_id`) REFERENCES `api_doc_item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='报文字段';

CREATE TABLE `api_meta_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(128) NOT NULL COMMENT '服务名称',
  `version` varchar(32) DEFAULT NULL COMMENT '版本号',
  `service_desc` varchar(256) DEFAULT NULL COMMENT '服务描述',
  `response_type` varchar(128) DEFAULT NULL COMMENT '服务响应类型',
  `owner` varchar(64) DEFAULT NULL COMMENT '所属系统',
  `busi_type` varchar(64) DEFAULT NULL COMMENT '服务名称',
  `note` varchar(4000) DEFAULT NULL COMMENT '服务介绍',
  `service_class` varchar(128) DEFAULT NULL COMMENT '服务类',
  `request_class` varchar(128) DEFAULT NULL COMMENT '请求类',
  `response_class` varchar(128) DEFAULT NULL COMMENT '同步响应类',
  `notify_class` varchar(128) DEFAULT NULL COMMENT '异步响应类',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name_version` (`service_name`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='服务类';

CREATE TABLE `api_qa_classify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父类ID',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `sort_time` bigint(20) NOT NULL COMMENT '排序辅助值',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='问题分类表';

-- ----------------------------
-- Table structure for doc_qa_question
-- ----------------------------
CREATE TABLE `api_qa_question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem` varchar(255) DEFAULT NULL COMMENT '问题',
  `solution` longtext NOT NULL COMMENT '解决方案',
  `weight` int(11) NOT NULL COMMENT '权重',
  `classify_id` bigint(20) NOT NULL COMMENT '类ID',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='问题记录表';