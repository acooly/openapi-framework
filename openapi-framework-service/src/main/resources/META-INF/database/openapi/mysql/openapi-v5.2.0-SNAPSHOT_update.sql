ALTER TABLE `api_order_info` CHANGE COLUMN `access_key` `access_key` VARCHAR(45) NOT NULL COMMENT '访问key';
ALTER TABLE `api_auth` ADD COLUMN `expired_time` DATETIME NULL COMMENT '有效期' AFTER `whitelist`;
