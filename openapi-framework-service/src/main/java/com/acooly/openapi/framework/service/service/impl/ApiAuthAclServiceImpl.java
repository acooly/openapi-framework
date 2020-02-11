/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.module.event.EventBus;
import com.acooly.openapi.framework.service.dao.ApiAuthAclDao;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.event.ApiAuthUpdateEvent;
import com.acooly.openapi.framework.service.service.ApiAuthAclService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 认证授权信息管理 Service实现
 * <p>
 *
 * @author zhangpu
 * @date 2020-2-2
 */
@Service("apiAuthServiceAcl")
public class ApiAuthAclServiceImpl extends EntityServiceImpl<ApiAuthAcl, ApiAuthAclDao> implements ApiAuthAclService {

    /**
     * 发布时间，用于更新缓存
     */
    @Autowired(required = false)
    private EventBus eventBus;

    @Override
    public void merge(List<ApiAuthAcl> apiAuthAcls) {
        try {
            ApiAuthAcl first = Collections3.getFirst(apiAuthAcls);
            String authNo = first.getAuthNo();
            List<ApiAuthAcl> acls = loadAcls(authNo);
            // 新增: apiAuthAcls，在acls无的服务： 从apiAuthAcls有去除acls中存在的。
            List<ApiAuthAcl> saveAcls = Collections3.subtract(apiAuthAcls, acls);
            // 删除：acls存在，apiAuthAcls不存在的需要删除：从acls中去除apiAuthAcls存在的。
            List<ApiAuthAcl> deleteAcls = Collections3.subtract(acls, apiAuthAcls);
            if (Collections3.isNotEmpty(saveAcls)) {
                saves(saveAcls);
            }
            if (Collections3.isNotEmpty(deleteAcls)) {
                List<Long> ids = Lists.newArrayList();
                for (ApiAuthAcl apiAuthAcl : deleteAcls) {
                    ids.add(apiAuthAcl.getId());
                }
                Serializable[] idArray = ids.toArray(new Long[]{});
                removes(idArray);
            }
            cleanupCache(first);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("合并ACL失败", e, "API_ACL_MERGE_FAIL");
        }
    }

    @Override
    public List<ApiAuthAcl> loadAcls(String authNo) {
        return getEntityDao().findByAuthNo(authNo);
    }

    @Override
    public List<ApiAuthAcl> queryAcls(String accessKey) {
        return getEntityDao().findByAccessKey(accessKey);
    }

    private void cleanupCache(ApiAuthAcl apiAuthAcl) {
        eventBus.publish(new ApiAuthUpdateEvent(apiAuthAcl));
    }
}
