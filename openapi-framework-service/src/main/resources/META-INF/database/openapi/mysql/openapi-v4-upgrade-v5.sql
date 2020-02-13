-- upgrade partner
ALTER TABLE `api_tenant`
DROP COLUMN `sign_type`,
DROP COLUMN `secret_type`,
ADD COLUMN `merchant_no` VARCHAR(64) NULL DEFAULT NULL COMMENT '商户会员号' AFTER `partner_name`,
ADD COLUMN `tenant_no` VARCHAR(64) NULL DEFAULT NULL COMMENT '租户编码' AFTER `merchant_no`;

-- replace api_partner with api_tenant
DROP TABLE api_partner
ALTER TABLE `api_tenant` RENAME TO  `api_partner`;
-- update merchant_no and tenant_no with partner_id
update `api_partner` set `merchant_no` = `partner_id`, `tenant_no` = `partner_id` where partner_id = 'test';

-- upgrade api_auth
ALTER TABLE `api_auth`
ADD COLUMN `parent_id` BIGINT(20) NULL DEFAULT NULL AFTER `id`,
ADD COLUMN `auth_no` VARCHAR(45) NULL DEFAULT NULL COMMENT '认证编码' AFTER `parent_id`,
ADD COLUMN `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '认证名称' AFTER `auth_no`,
ADD COLUMN `partner_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '接入方ID' AFTER `title`,
ADD COLUMN `secret_type` VARCHAR(16) NULL DEFAULT NULL COMMENT '安全类型' AFTER `partner_id`,
ADD COLUMN `sign_type` VARCHAR(45) NULL DEFAULT NULL COMMENT '签名类型' AFTER `secret_type`;

update `api_auth` set `auth_no` = 'O200212004846016E0007',
 `partner_id` = 'test',`secret_type`='digest',`sign_type`='MD5' where access_key = 'test';

-- new table: acl
CREATE TABLE `api_auth_acl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auth_no` varchar(45) NOT NULL COMMENT '认证编码',
  `access_key` varchar(45) NOT NULL COMMENT '访问帐号',
  `service_no` varchar(64) NOT NULL COMMENT '服务编码',
  `name` varchar(45) DEFAULT NULL COMMENT '服务名',
  `version` varchar(8) DEFAULT NULL COMMENT '服务版本',
  `title` varchar(45) DEFAULT NULL COMMENT '服务中文名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='API认证ACL';

-- update manage menu
update `sys_resource` set `name`='接入管理',`VALUE`='/manage/openapi/apiPartner/index.html',`icon`='fa-circle-o' where id=201808211401;
update `sys_resource` set `VALUE`='/manage/openapi/apiAuth/index.html',`icon`='fa-circle-o' where id=201808211402;
update `sys_resource` set `icon`='fa-circle-o' where id=201808211403;
update `sys_resource` set `icon`='fa-circle-o' where id=201808211421;


