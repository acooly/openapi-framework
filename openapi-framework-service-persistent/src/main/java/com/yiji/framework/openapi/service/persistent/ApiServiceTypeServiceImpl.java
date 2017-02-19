/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.service.persistent;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.framework.openapi.service.persistent.dao.ApiServiceTypeDao;
import com.yiji.framework.openapi.domain.ApiServiceType;
import com.yiji.framework.openapi.service.ApiServiceTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 服务分类 Service实现
 * <p>
 * Date: 2016-07-16 01:57:05
 *
 * @author acooly
 */
@Service("apiServiceTypeService")
public class ApiServiceTypeServiceImpl extends EntityServiceImpl<ApiServiceType, ApiServiceTypeDao> implements ApiServiceTypeService {

    @Override
    public ApiServiceType create(Long parentId, String name, String comments) {
        String path = null;
        String maxPath = getMaxPath(parentId);
        if (StringUtils.isNotBlank(maxPath)) {
            path = String.valueOf(Long.parseLong(maxPath) + 1);
        } else {
            if (parentId == null) {
                path = "100";
            } else {
                ApiServiceType parent = get(parentId);
                path = parent.getPath() + "100";
            }
        }
        ApiServiceType type = new ApiServiceType();
        type.setPath(path);
        type.setComments(comments);
        type.setName(name);
        type.setParentId(parentId);
        type.setSortTime(System.currentTimeMillis());
        save(type);
        return type;
    }

    @Override
    public List<ApiServiceType> loadTree(Long parentId) {
        List<ApiServiceType> types = null;
        if (parentId == null) {
            types = getEntityDao().getAll();
        } else {
            ApiServiceType parent = get(parentId);
            types = getEntityDao().findByPathStartingWith(parent.getPath());
        }
        List<ApiServiceType> result = Lists.newLinkedList();
        try {
            Map<Long, ApiServiceType> dtoMap = Maps.newHashMap();
            for (ApiServiceType type : types) {
                dtoMap.put(type.getId(), type);
            }
            for (Map.Entry<Long, ApiServiceType> entry : dtoMap.entrySet()) {
                ApiServiceType node = entry.getValue();
                if (node.getParentId() == null || node.getParentId() == 0) {
                    result.add(node);
                } else {
                    if (dtoMap.get(node.getParentId()) != null) {
                        dtoMap.get(node.getParentId()).addChild(node);
                        Collections.sort(dtoMap.get(node.getParentId()).getChildren(),
                                new ApiServiceType.NodeComparator());
                    }
                }
            }
            types.clear();
            types = null;
            dtoMap.clear();
            dtoMap = null;
            Collections.sort(result, new ApiServiceType.NodeComparator());
        } catch (Exception e) {
            throw new BusinessException("获取授权资源列表失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<ApiServiceType> loadTopTypes() {
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
