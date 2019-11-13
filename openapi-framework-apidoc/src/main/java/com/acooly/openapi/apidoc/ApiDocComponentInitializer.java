/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-14 00:56 创建
 */
package com.acooly.openapi.apidoc;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class ApiDocComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setPropertyIfMissing("acooly.mybatis.daoScanPackages.apidoc", "com.acooly.openapi.apidoc.persist.dao");
        setPropertyIfMissing("acooly.mybatis.typeAliasesPackage.apidoc", "com.acooly.openapi.apidoc.persist.entity");

        // 兼容方式修改列长度
        // api_meta_service和api_doc_service的note字段增长到4000
        setPropertyIfMissing("acooly.ds.dbPatchs.api_meta_service[0].columnName", "note_for_modify");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_meta_service[0].patchSql", "ALTER TABLE `api_meta_service` CHANGE COLUMN `note` `note` VARCHAR(4000) NULL DEFAULT NULL COMMENT '服务介绍';");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_service[0].columnName", "note_for_modify");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_service[0].patchSql", "ALTER TABLE `api_doc_service` CHANGE COLUMN `note` `note` VARCHAR(4000) NULL DEFAULT NULL COMMENT '服务介绍';");

        // api_doc_item的descn和demo字段增长到1024
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_item[0].columnName", "descn_for_modify");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_item[0].patchSql", "ALTER TABLE `api_doc_item` CHANGE COLUMN `descn` `descn` VARCHAR(1024) NULL DEFAULT NULL COMMENT '字段描述';");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_item[1].columnName", "demo_for_modify");
        setPropertyIfMissing("acooly.ds.dbPatchs.api_doc_item[1].patchSql", "ALTER TABLE `api_doc_item` CHANGE COLUMN `demo` `demo` VARCHAR(1024) NULL DEFAULT NULL COMMENT '字段示例';");
    }
}
