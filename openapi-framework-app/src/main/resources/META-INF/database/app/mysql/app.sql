-- ----------------------------
-- Table structure for app_banner
-- ----------------------------
DROP TABLE IF EXISTS `app_banner`;
CREATE TABLE `app_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `content` varchar(256) DEFAULT NULL,
  `media_url` varchar(128) NOT NULL COMMENT '广告栏图片',
  `link` varchar(128) DEFAULT NULL COMMENT '内容链接',
  `sort_time` datetime DEFAULT NULL COMMENT '排序时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_customer
-- ----------------------------
DROP TABLE IF EXISTS `app_customer`;
CREATE TABLE `app_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `access_key` varchar(64) NOT NULL COMMENT '访问码',
  `secret_key` varchar(64) NOT NULL COMMENT '安全码',
  `device_type` varchar(16) DEFAULT NULL COMMENT '设备类型',
  `device_model` varchar(64) DEFAULT NULL COMMENT '设备型号',
  `device_id` varchar(64) NOT NULL COMMENT '设备标识',
  `status` varchar(255) NOT NULL COMMENT '状态{enable:有效,disable:无效}',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_start_guide
-- ----------------------------
DROP TABLE IF EXISTS `app_start_guide`;
CREATE TABLE `app_start_guide` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sort_order` bigint(20) NOT NULL COMMENT '排序（大小顺序）',
  `image_default` varchar(255) NOT NULL COMMENT '默认图片(640*1136)',
  `image_iphone4` varchar(255) DEFAULT NULL COMMENT 'IPHONE4(640*960)',
  `image_iphone6` varchar(255) DEFAULT NULL COMMENT 'iphone5/6: 1242*2208',
  `image_android` varchar(255) DEFAULT NULL COMMENT 'android: 1080 * 1920',
  `status` varchar(255) NOT NULL COMMENT '状态{enable:可用,disable:禁用}',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_version
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APP_CODE` varchar(32) DEFAULT NULL COMMENT 'APP编码',
  `APP_NAME` varchar(32) DEFAULT NULL COMMENT 'APP名称',
  `DEVICE_TYPE` varchar(32) DEFAULT NULL COMMENT '设备类型 {Android, IPhone}',
  `VERSION_NAME` varchar(16) NOT NULL COMMENT '版本号 如：1.0.0, 用于显示',
  `VERSION_CODE` int(11) NOT NULL DEFAULT '0' COMMENT '版本编码 通过这个的最大值判断最新版本',
  `SUBJECT` varchar(255) NOT NULL COMMENT '版本说明',
  `URL` varchar(255) DEFAULT NULL COMMENT '下载URL，全URL',
  `PUB_TIME` datetime DEFAULT NULL COMMENT '发布时间',
  `FORCE_UPDATE` int(11) DEFAULT NULL COMMENT '是否强制更新 {0:否,1:是}',
  `COMMENTS` varchar(255) DEFAULT NULL COMMENT '备注',
  `PATH` varchar(255) DEFAULT NULL COMMENT '物理存储路径',
  `APPLE_URL` varchar(255) DEFAULT NULL COMMENT '苹果安装地址',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_DEVICE_VCODE` (`DEVICE_TYPE`,`VERSION_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='手机客户端版本';

-- ----------------------------
-- Table structure for app_welcome
-- ----------------------------
DROP TABLE IF EXISTS `app_welcome`;
CREATE TABLE `app_welcome` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sort_order` bigint(20) NOT NULL COMMENT '排序（大小顺序）',
  `image_default` varchar(255) NOT NULL COMMENT '默认图片(640*1136)',
  `image_iphone4` varchar(255) DEFAULT NULL COMMENT 'IPHONE4(640*960)',
  `image_iphone6` varchar(255) DEFAULT NULL COMMENT 'iphone5/6: 1242*2208',
  `image_android` varchar(255) DEFAULT NULL COMMENT 'android: 1080 * 1920',
  `status` varchar(255) NOT NULL COMMENT '状态{enable:可用,disable:禁用}',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `app_crash`;
CREATE TABLE `app_crash` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(32) DEFAULT NULL COMMENT '应用名称',
  `user_name` varchar(16) DEFAULT NULL COMMENT '用户名',
  `platform_id` varchar(32) DEFAULT NULL COMMENT '平台ID',
  `android_version` varchar(16) DEFAULT NULL COMMENT 'Android版本号',
  `app_version_code` varchar(16) DEFAULT NULL COMMENT '应用的版本代码',
  `app_version_name` varchar(16) DEFAULT NULL COMMENT '应用的版本名称',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `model` varchar(32) DEFAULT NULL COMMENT '手机/平板的模型',
  `brand` varchar(32) DEFAULT NULL COMMENT '品牌',
  `product` varchar(255) DEFAULT NULL COMMENT 'Android产品信息',
  `crash_date` datetime DEFAULT NULL COMMENT '崩溃的时间点',
  `package_name` varchar(128) DEFAULT NULL COMMENT '应用的包名',
  `stack_trace` text DEFAULT NULL COMMENT '崩溃的堆栈信息',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `app_message`;
CREATE TABLE `app_message` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `TITLE` varchar(32) NOT NULL COMMENT '标题(用于管理)',
  `SEND_TIME` datetime NOT NULL COMMENT '发送时间',
  `TYPE` varchar(16) NOT NULL COMMENT '群发类型 {broadcast:广播,group:群发}',
  `SCHEDULER_TIME` datetime DEFAULT NULL COMMENT '定时发送时间(预留，暂不开发)',
  `CONTENT_TYPE` varchar(16) NOT NULL COMMENT '内容类型',
  `CONTENT` varchar(1024) NOT NULL COMMENT '发送内容',
  `CONTEXT` varchar(256) DEFAULT NULL COMMENT '会话参数',
  `SENDER` varchar(16) DEFAULT NULL COMMENT '发送人',
  `RECEIVERS` varchar(256) DEFAULT NULL COMMENT '类型为group时有效。多个用户使用逗号分隔',
  `STATUS` varchar(16) DEFAULT NULL COMMENT '状态.{apply:提交,sending:发送中,success:成功,fail:发送失败}',
  `COMMENTS` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='群发消息';

