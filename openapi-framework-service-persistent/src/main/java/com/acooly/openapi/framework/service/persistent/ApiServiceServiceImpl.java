/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service.persistent;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.service.ApiServiceService;
import com.acooly.openapi.framework.service.ApiServiceTypeService;
import com.acooly.openapi.framework.service.persistent.dao.ApiServiceDao;
import com.google.common.collect.Maps;
import com.acooly.openapi.framework.domain.ApiService;
import com.acooly.openapi.framework.domain.ApiServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务分类 Service实现
 * <p>
 * Date: 2016-07-16 01:57:13
 *
 * @author acooly
 */
@Service("apiServiceService")
public class ApiServiceServiceImpl extends EntityServiceImpl<ApiService, ApiServiceDao> implements ApiServiceService {

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceServiceImpl.class);

    @Resource
    private ApiServiceTypeService apiServiceTypeService;


    private static final String DEFALUT_TYPE_NAME = "其他";

    @Transactional(readOnly = true)
    @Override
    public Map<String, List<ApiService>> searchApiService(String key) {

        try {
            List<ApiServiceType> topTypes = apiServiceTypeService.loadTopTypes();
            List<ApiService> services = queryByKey(key);
            Map<String, List<ApiService>> map = Maps.newLinkedHashMap();
            map.put(DEFALUT_TYPE_NAME, new ArrayList<ApiService>());
            String topPath = null;
            ApiServiceType topType = null;
            for (ApiService apiService : services) {
                topPath = Strings.substring(apiService.getApiServiceType().getPath(), 0, 3);
                topType = getTypeByPath(topTypes, topPath);
                if (topType == null) {
                    map.get(DEFALUT_TYPE_NAME).add(apiService);
                    continue;
                }

                if (map.get(topType.getName()) == null) {
                    map.put(topType.getName(), new ArrayList<ApiService>());
                }
                map.get(topType.getName()).add(apiService);
            }
            return map;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    private ApiServiceType getTypeByPath(List<ApiServiceType> topTypes, String path) {
        for (ApiServiceType type : topTypes) {
            if (Strings.equals(type.getPath(), path)) {
                return type;
            }
        }
        return null;
    }


    protected List<ApiService> queryByKey(String key) {
        if (Strings.isBlank(key)) {
            return getAll();
        } else {
            return getEntityDao().search("%" + key + "%");
        }

    }

}
