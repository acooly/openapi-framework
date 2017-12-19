/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.output.impl;

import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.output.ApiDocumentOutputer;
import com.acooly.openapi.apidoc.output.ApiOutputerEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * JDBC输出实现
 * <p/>
 * Created by zhangpu on 2015/2/26.
 */
@Service("ApiDocumentJdbcOutputer")
public class ApiDocumentJdbcOutputer implements ApiDocumentOutputer<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(ApiDocumentJdbcOutputer.class);

    @Resource
    private ApiDocServiceService apiDocServiceService;

    @Override
    public Boolean output(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext) {
        try {
            //apiServiceDocService.reset(apiServiceDocs);
        } catch (Exception e) {
            logger.error("输出到数据库失败,", e);
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return ApiOutputerEnum.Jdbc.name();
    }
}
