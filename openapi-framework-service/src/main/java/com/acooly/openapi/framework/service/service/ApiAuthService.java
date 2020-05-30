/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 *
 */
package com.acooly.openapi.framework.service.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.service.domain.ApiAuth;

import java.util.List;

/**
 * 认证授权信息管理 Service接口
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 */
public interface ApiAuthService extends EntityService<ApiAuth> {
    /**
     * 根据AccessKey查询认证对象
     *
     * @param accesskey
     * @return
     */
    ApiAuth findByAccesskey(String accesskey);

    /**
     * 根据编码查询
     * @param authNo
     * @return
     */
    ApiAuth findByAuthNo(String authNo);

    List<ApiAuth> findByParent(Long parentId);
}
