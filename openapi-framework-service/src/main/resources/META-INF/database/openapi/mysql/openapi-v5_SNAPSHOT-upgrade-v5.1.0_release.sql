ALTER TABLE `api_auth` ADD COLUMN `status` VARCHAR(16) NULL DEFAULT 'enable' COMMENT '状态' AFTER `whitelist`;
ALTER TABLE `api_partner` CHANGE COLUMN `tenant_no` `tenant_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '租户编码';
ALTER TABLE `api_partner` ADD COLUMN `tenant_name` VARCHAR(32) NULL COMMENT '租户名称' AFTER `comments`;
ALTER TABLE `api_order_info` ADD COLUMN `request_ip` VARCHAR(16) NULL COMMENT '请求IP' AFTER `return_url`