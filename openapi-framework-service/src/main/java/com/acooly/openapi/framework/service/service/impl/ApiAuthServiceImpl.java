/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.module.event.EventBus;
import com.acooly.openapi.framework.service.dao.ApiAuthDao;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.event.ApiUpdateEvent;
import com.acooly.openapi.framework.service.service.ApiAuthAclService;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 认证授权信息管理 Service实现
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 * @author zhangpu : 调整缓存更新为事件模式
 */
@Slf4j
@Service("apiAuthService")
public class ApiAuthServiceImpl extends EntityServiceImpl<ApiAuth, ApiAuthDao> implements ApiAuthService {

    /**
     * 发布时间，用于更新缓存
     */
    @Autowired(required = false)
    private EventBus eventBus;

    @Autowired
    private ApiAuthAclService apiAuthAclService;


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeByParentId(Long parentId) {
        getEntityDao().deleteByParentId(parentId);
    }

    @Override
    public ApiAuth findByAccesskey(String accesskey) {
        return this.getEntityDao().findByAccesskey(accesskey);
    }

    @Override
    public ApiAuth findByAuthNo(String authNo) {
        return getEntityDao().findByAuthNo(authNo);
    }

    @Override
    public void save(ApiAuth o) throws BusinessException {
        ApiAuth apiAuth = findByAccesskey(o.getAccessKey());
        if (apiAuth != null) {
            log.warn("认证对象的AccessKey已存在: {}", o.getAccessKey());
            throw new BusinessException(CommonErrorCodes.OBJECT_NOT_UNIQUE, "认证对象的AccessKey已存在");
        }
        if (Strings.isBlank(o.getAuthNo())) {
            o.setAuthNo(Ids.did());
        }
        super.save(o);
    }

    @Override
    public void update(ApiAuth o) throws BusinessException {
        ApiAuth oldApiAuth = this.get(o.getId());
        if (Strings.isBlank(o.getAuthNo())) {
            o.setAuthNo(Ids.did());
        }
        super.update(o);
        eventBus.publish(new ApiUpdateEvent(oldApiAuth));
    }


    @Override
    public void remove(ApiAuth o) throws BusinessException {
        super.remove(o);
        eventBus.publish(new ApiUpdateEvent(o));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removes(Serializable... ids) throws BusinessException {
        for (Serializable id : ids) {
            removeById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeById(Serializable id) throws BusinessException {
        ApiAuth apiAuth = get(id);
        if (apiAuth != null) {
            // 级联删除子，目前只设计2层结构
            removeByParentId(apiAuth.getId());
            // 调整逻辑为级联删除ACLs
            apiAuthAclService.removeByAuthNo(apiAuth.getAuthNo());
        }
        super.removeById(id);
        eventBus.publish(new ApiUpdateEvent(apiAuth));
    }

    @Override
    public List<ApiAuth> findByParent(Long parentId) {
        if (parentId == null) {
            return getEntityDao().findTops();
        }
        return getEntityDao().findByParentId(parentId);
    }

    @Override
    public List<ApiAuth> findByPartnerId(String partnerId) {
        return getEntityDao().findByPartnerId(partnerId);
    }

    @Override
    public PageInfo<ApiAuth> query(PageInfo<ApiAuth> pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap, String serviceCode) {

        PageInfo<ApiAuth> innerPageInfo = this.query(pageInfo, map, sortMap);

        if (Strings.isNoneBlank(serviceCode)) {
            List<ApiAuth> apiAuthList = innerPageInfo.getPageResults();

            List<ApiAuth> hasPermList = new ArrayList<>(apiAuthList.size());
            //先查询所有配置了*.*的接入方
            List<ApiAuth> allPermitList = this.getEntityDao().findAllPermitAuth();
            log.info(JSONObject.toJSONString(allPermitList));
            HashSet<String> allPertmitSet = new HashSet();
            allPermitList.forEach(apiAuth -> {
                allPertmitSet.add(apiAuth.getAccessKey());
            });

            //通过具体的服务码查询,接入方
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("EQ_name", serviceCode);

            List<ApiAuthAcl> authAclList = this.apiAuthAclService.query(queryMap, null);
            HashSet<String> authAclSet = new HashSet();
            authAclList.forEach(apiAuthAcl -> {
                authAclSet.add(apiAuthAcl.getAccessKey());
            });

            for (ApiAuth apiPartner : apiAuthList) {
                //先判断是否配置了*:*或存在对应的权限
                if (allPertmitSet.contains(apiPartner.getAccessKey()) || authAclSet.contains(apiPartner.getAccessKey())) {
                    hasPermList.add(apiPartner);
                    continue;
                }
            }
            innerPageInfo.setPageResults(hasPermList);
        }

        return innerPageInfo;
    }
}
