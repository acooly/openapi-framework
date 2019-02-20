/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.dao.ApiQaClassifyDao;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;
import com.acooly.openapi.apidoc.persist.service.ApiQaClassifyService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 问题分类表 Service实现
 *
 * Date: 2019-02-20 17:09:59
 *
 * @author zhike
 *
 */
@Service("apiQaClassifyService")
public class ApiQaClassifyServiceImpl extends EntityServiceImpl<ApiQaClassify, ApiQaClassifyDao> implements ApiQaClassifyService {
    @Override
    public ApiQaClassify create(Long parentId, String name, String comments) {
        String path = null;
        String maxPath = getMaxPath(parentId);
        if (StringUtils.isNotBlank(maxPath)) {
            path = String.valueOf(Long.parseLong(maxPath) + 1);
        } else {
            if (parentId == null) {
                path = "100";
            } else {
                ApiQaClassify parent = get(parentId);
                path = parent.getPath() + "100";
            }
        }
        ApiQaClassify type = new ApiQaClassify();
        type.setPath(path);
        type.setComments(comments);
        type.setName(name);
        type.setParentId(parentId);
        type.setSortTime(System.currentTimeMillis());
        save(type);
        return type;
    }

    @Override
    public List<ApiQaClassify> loadTree(Long parentId) {
        List<ApiQaClassify> types = null;
        if (parentId == null) {
            types = getEntityDao().getAll();
        } else {
            ApiQaClassify parent = get(parentId);
            types = getEntityDao().findByPathStartingWith(parent.getPath());
        }
        List<ApiQaClassify> result = Lists.newLinkedList();
        try {
            Map<Long, ApiQaClassify> dtoMap = Maps.newHashMap();
            for (ApiQaClassify type : types) {
                dtoMap.put(type.getId(), type);
            }
            for (Map.Entry<Long, ApiQaClassify> entry : dtoMap.entrySet()) {
                ApiQaClassify node = entry.getValue();
                if (node.getParentId() == null || node.getParentId() == 0) {
                    result.add(node);
                } else {
                    if (dtoMap.get(node.getParentId()) != null) {
                        dtoMap.get(node.getParentId()).addChild(node);
                        Collections.sort(dtoMap.get(node.getParentId()).getChildren(),
                                new ApiQaClassify.NodeComparator());
                    }
                }
            }
            types.clear();
            types = null;
            dtoMap.clear();
            dtoMap = null;
            Collections.sort(result, new ApiQaClassify.NodeComparator());
        } catch (Exception e) {
            throw new BusinessException("获取授权资源列表失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ApiQaClassify> loadTopTypes() {
        return getEntityDao().getTops();
    }

    protected String getMaxPath(Long parentId) {
        if (parentId == null) {
            return getEntityDao().getTopMaxPath();
        } else {
            return getEntityDao().getMaxPath(parentId);
        }
    }
}
