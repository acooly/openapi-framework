/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiDocItemDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 报文字段 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocItemService")
@Slf4j
public class ApiDocItemServiceImpl extends EntityServiceImpl<ApiDocItem, ApiDocItemDao> implements ApiDocItemService {


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void mergeSaves(List<ApiDocItem> apiDocItems) {
        doMergeSaves(apiDocItems, null);
    }

    public void doMergeSaves(List<ApiDocItem> apiDocItems, ApiDocItem parentItem) {
        for (ApiDocItem apiDocItem : apiDocItems) {
            if (Collections3.isNotEmpty(apiDocItem.getChildren())) {
                doMergeSaves(apiDocItem.getChildren(), apiDocItem);
            }
            if (parentItem != null && Strings.isNotBlank(parentItem.getItemNo())) {
                apiDocItem.setParentNo(parentItem.getItemNo());
            }
            doMergeSave(apiDocItem);
        }
        log.debug("合并保存ApiDocItems:{}" + apiDocItems.size());
    }


    protected ApiDocItem doMergeSave(ApiDocItem apiDocItem) {
        ApiDocItem entity = getEntityDao().findByItemNo(apiDocItem.getItemNo());
        // 暂时不处理已删除
        if (entity != null) {
            // update
            if (!ApiDocs.equelsApiDocItem(entity, apiDocItem)) {
                // 数据有变更
                apiDocItem.setId(entity.getId());
                update(apiDocItem);
            }
        } else {
            // insert
            save(apiDocItem);
        }
        return apiDocItem;
    }


    @Override
    public List<ApiDocItem> loadByMessageNo(String messageNo) {

        List<ApiDocItem> apiDocItems = getEntityDao().findByMessageNo(messageNo);
        if (Collections3.isEmpty(apiDocItems)) {
            return apiDocItems;
        }
        return doMarshalTree(apiDocItems);
    }


    private List<ApiDocItem> doMarshalTree(List<ApiDocItem> apiDocItems) {

        List<ApiDocItem> result = Lists.newLinkedList();
        try {
            Map<String, ApiDocItem> dtoMap = Maps.newHashMap();
            for (ApiDocItem apiDocItem : apiDocItems) {
                dtoMap.put(apiDocItem.getItemNo(), apiDocItem);
            }
            for (Map.Entry<String, ApiDocItem> entry : dtoMap.entrySet()) {
                ApiDocItem node = entry.getValue();
                if (Strings.isBlank(node.getParentNo())) {
                    result.add(node);
                } else {
                    if (dtoMap.get(node.getParentNo()) != null) {
                        dtoMap.get(node.getParentNo()).addChild(node);
                        Collections.sort(dtoMap.get(node.getParentNo()).getChildren(),
                                new ApiDocItem.ApiDocItemComparator());
                    }
                }
            }
            Collections.sort(result, new ApiDocItem.ApiDocItemComparator());
        } catch (Exception e) {
            throw new BusinessException("组装消息字段树失败：" + e.getMessage());
        }
        return result;
    }

}
