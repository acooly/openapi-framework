/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiDocServiceDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocServiceService")
public class ApiDocServiceServiceImpl extends EntityServiceImpl<ApiDocService, ApiDocServiceDao> implements ApiDocServiceService {


    @Autowired
    private ApiDocMessageService apiDocMessageService;

    @Autowired
    private ApiDocItemService apiDocItemService;


    @Override
    public void merge(List<ApiDocService> apiDocServices) {
        List<ApiDocService> persists = getAll();
        List<Long> needRemoves = Lists.newArrayList();
        for (ApiDocService persist : persists) {
            // 生成没有，数据库存在，合并删除
            if (!apiDocServices.contains(persist)) {
                needRemoves.add(persist.getId());
                continue;
            }
        }

        for (ApiDocService entity : apiDocServices) {
            mergeOne(entity);
        }

        // do remove
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mergeOne(ApiDocService apiDocService) {
        mergeSave(apiDocService);

        List<ApiDocMessage> apiDocMessages = apiDocService.getApiDocMessages();
        if (Collections3.isEmpty(apiDocMessages)) {
            return;
        }

        for (ApiDocMessage apiDocMessage : apiDocMessages) {
            apiDocMessageService.mergeSave(apiDocMessage);

            if (Collections3.isEmpty(apiDocMessage.getApiDocItems())) {
                continue;
            }

            for (ApiDocItem apiDocItem : apiDocMessage.getApiDocItems()) {
                apiDocItemService.mergeSave(apiDocItem);
                // 简化设计和逻辑：只支持两层报文
                if(Collections3.isNotEmpty(apiDocItem.getChildren())){

                }
            }
        }
    }


    @Override
    public void mergeSave(ApiDocService apiDocService) {

        String serviceNo = getServiceNo(apiDocService);
        ApiDocService entity = getEntityDao().findByServiceNo(serviceNo);
        // 暂时不处理已删除
        if (entity != null) {
            // update
            if (!ApiDocs.equelsApiDocService(entity, apiDocService)) {
                // 数据有变更
                apiDocService.setId(entity.getId());
                update(apiDocService);
            }
        } else {
            // insert
            save(apiDocService);
        }

    }


    protected String getServiceNo(ApiDocService entity) {
        String serviceNo = entity.getServiceNo();
        if (Strings.isBlank(serviceNo)) {
            serviceNo = ApiDocs.getServiceNo(entity.getName(), entity.getVersion());
        }
        return serviceNo;
    }
}
