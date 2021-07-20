/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 *
 */
package com.acooly.openapi.framework.service.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.domain.ApiMetaService;

import java.util.List;

/**
 * 认证授权信息管理 Service接口
 * <p>
 *
 * @author zhangpu
 * @date 2020-2-2
 */
public interface ApiAuthAclService extends EntityService<ApiAuthAcl> {

    /**
     * 批量设置权限ACL
     *
     * @param apiAuthAcls
     */
    void merge(List<ApiAuthAcl> apiAuthAcls);

    /**
     * 获取ACL列表
     *
     * @param authNo
     * @return
     */
    List<ApiAuthAcl> loadAcls(String authNo);

    /**
     * 获取Acl
     *
     * @param accessKey
     * @return
     */
    List<ApiAuthAcl> queryAcls(String accessKey);

    /**
     * 获取已配置权限列表
     *
     * @param authNo
     * @return
     */
    List<ApiMetaService> loadMetaServices(String authNo);

    /**
     * 根据认证对象的编码删除所有ACLs
     *
     * @param authNo
     */
    void removeByAuthNo(String authNo);

}
