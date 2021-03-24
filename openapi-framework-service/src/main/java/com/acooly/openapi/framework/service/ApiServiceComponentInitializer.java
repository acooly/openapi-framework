/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-18 00:37 创建
 */
package com.acooly.openapi.framework.service;

import com.acooly.core.common.boot.component.ComponentInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhangpu
 * @date 2020-05-27
 */
@Slf4j
public class ApiServiceComponentInitializer implements ComponentInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        setPropertyIfMissing("acooly.framework.custom-scripts.openapi[0]", "/manage/assert/openapi/openapi-manage.js");

        /** 升级增加apiAuth白名单 */
        setPropertyIfMissing("acooly.ds.dbPatchs.api_auth[0].columnName", "whitelist");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_auth[0].patchSql", "ALTER TABLE `api_auth` \n" +
                "ADD COLUMN `whitelist_check` VARCHAR(16) NULL COMMENT '白名单验证' AFTER `permissions`,\n" +
                "ADD COLUMN `whitelist` VARCHAR(127) NULL COMMENT '白名单' AFTER `whitelist_check`;");

        /** 升级增加apiAuth的状态 */
        setPropertyIfMissing("acooly.ds.dbPatchs.api_auth[1].columnName", "status");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_auth[1].patchSql", "ALTER TABLE `api_auth`" +
                " ADD COLUMN `status` VARCHAR(16) NULL DEFAULT 'enable' COMMENT '状态' AFTER `whitelist`;");

        // change tenant_no to tenant_id
        setPropertyIfMissing("acooly.ds.dbPatchs.api_partner[0].columnName", "tenant_id");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_partner[0].patchSql", "ALTER TABLE `api_partner`" +
                " CHANGE COLUMN `tenant_no` `tenant_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '租户编码' ;");

        // add tenant_name
        setPropertyIfMissing("acooly.ds.dbPatchs.api_partner[1].columnName", "tenant_name");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_partner[1].patchSql", "ALTER TABLE `api_partner` \n" +
                "ADD COLUMN `tenant_name` VARCHAR(32) NULL COMMENT '租户名称' AFTER `comments`;\n");

        // add request_id in orderInfo
        setPropertyIfMissing("acooly.ds.dbPatchs.api_order_info[0].columnName", "request_ip");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_order_info[0].patchSql", "ALTER TABLE `api_order_info`\n" +
                " ADD COLUMN `request_ip` VARCHAR(16) NULL COMMENT '请求IP' AFTER `return_url`;");


    }
}
