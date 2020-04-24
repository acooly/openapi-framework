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
import com.acooly.openapi.apidoc.persist.dto.MergeResult;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.enums.ApiDocEntityType;
import com.acooly.openapi.apidoc.persist.enums.ApiDocMergeType;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
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


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    @Override
    public void merge(String messageNo, List<ApiDocItem> apiDocItems) {
        if (Collections3.isEmpty(apiDocItems)) {
            return;
        }
        List<MergeResult> mergeResults = Lists.newArrayList();
        mergeResults.addAll(doMergeDelete(messageNo, apiDocItems, null));
        mergeResults.addAll(doMergeSave(messageNo, apiDocItems, null));
        if (Collections3.isNotEmpty(mergeResults)) {
            log.info("Item合并 [成功] messageNo:{}, size: {}", messageNo, mergeResults.size());
            for (MergeResult result : mergeResults) {
                log.debug("Item合并 {}", result.toString());
            }
        }
    }


    @Override
    public List<ApiDocItem> loadByMessageNo(String messageNo) {
        List<ApiDocItem> apiDocItems = getEntityDao().findByMessageNo(messageNo);
        if (Collections3.isEmpty(apiDocItems)) {
            return apiDocItems;
        }
        return doMarshalTree(apiDocItems);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteByMessageNo(String messageNo) {
        getEntityDao().deleteByMessageNo(messageNo);
    }

    /**
     * 合并删除
     * <p>
     * 生成的数据有，持久化数据中无的数据，从持久化中级联删除
     *
     * @param messageNo
     * @param apiDocItems
     * @param parentItem
     */
    protected List<MergeResult> doMergeDelete(String messageNo, List<ApiDocItem> apiDocItems, ApiDocItem parentItem) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        String parentNo = parentItem != null ? parentItem.getItemNo() : null;
        List<ApiDocItem> persists = loadByMessageNoAndParentNo(messageNo, parentNo);
        if (Collections3.isEmpty(persists)) {
            return mergeResults;
        }
        List<ApiDocItem> needRemoves = Lists.newArrayList();
        for (ApiDocItem persist : persists) {
            // itemNo都匹配不上，则直接删除
            if (!apiDocItems.contains(persist)) {
                needRemoves.add(persist);
                continue;
            }


            for (ApiDocItem item : apiDocItems) {
                // itemNo相同，但类型改变了(对象 <-> 非对象)
                if (Strings.equals(item.getItemNo(), persist.getItemNo())
                        && !ApiDocs.equelsNoAndTypeApiDocItem(item, persist)) {
                    needRemoves.add(persist);
                    continue;
                } else {
                    // check children
                    if (Collections3.isNotEmpty(item.getChildren())) {
                        // 都存在子对象的情况, 检查下一层
                        mergeResults.addAll(doMergeDelete(messageNo, item.getChildren(), item));
                    }
                }
            }

        }
        if (needRemoves.size() > 0) {
            mergeResults.addAll(doMergeDelete(needRemoves));
        }
        return mergeResults;
    }

    /**
     * 级联删除Item
     *
     * @param items
     * @return
     */
    protected List<MergeResult> doMergeDelete(List<ApiDocItem> items) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        if (Collections3.isEmpty(items)) {
            return mergeResults;
        }
        for (ApiDocItem item : items) {
            if (Collections3.isNotEmpty(item.getChildren())) {
                mergeResults.addAll(doMergeDelete(item.getChildren()));
            }
            getEntityDao().deleteByItemNo(item.getItemNo());
            mergeResults.add(new MergeResult(ApiDocEntityType.ITEM, item.getName(), ApiDocMergeType.DELETE));
        }
        return mergeResults;
    }


    protected List<MergeResult> doMergeSave(String messageNo, List<ApiDocItem> apiDocItems, ApiDocItem parentItem) {
        // 合并更新（save or update）
        List<MergeResult> mergeResults = Lists.newArrayList();
        for (ApiDocItem apiDocItem : apiDocItems) {

            if (Collections3.isNotEmpty(apiDocItem.getChildren())) {
                mergeResults.addAll(doMergeSave(apiDocItem.getMessageNo(), apiDocItem.getChildren(), apiDocItem));
            }
            if (parentItem != null && Strings.isNotBlank(parentItem.getItemNo())) {
                apiDocItem.setParentNo(parentItem.getItemNo());
            }
            ApiDocMergeType mergeType = doMergeSave(apiDocItem);
            if (mergeType != ApiDocMergeType.NONE) {
                mergeResults.add(new MergeResult(ApiDocEntityType.ITEM, apiDocItem.getName(), mergeType));
            }
        }
        return mergeResults;
    }

    protected ApiDocMergeType doMergeSave(ApiDocItem apiDocItem) {
        ApiDocItem entity = getEntityDao().findByItemNo(apiDocItem.getItemNo());
        ApiDocMergeType apiDocMergeType = ApiDocMergeType.NONE;
        if (entity != null) {
            // update
            if (!ApiDocs.equelsApiDocItem(entity, apiDocItem)) {
                // 数据有变更
                apiDocItem.setId(entity.getId());
                update(apiDocItem);
                apiDocMergeType = ApiDocMergeType.UPDATE;
            }
        } else {
            // insert
            save(apiDocItem);
            apiDocMergeType = ApiDocMergeType.CREATE;
        }
        return apiDocMergeType;
    }

    protected List<ApiDocItem> loadByMessageNoAndParentNo(String messageNo, String parentNo) {
        if (Strings.isBlank(parentNo)) {
            return getEntityDao().findByMessageNoTop(messageNo);
        }
        return getEntityDao().findByMessageNoAndParentNo(messageNo, parentNo);
    }


    private List<ApiDocItem> doMarshalTree(List<ApiDocItem> apiDocItems) {

        List<ApiDocItem> result = Lists.newLinkedList();
        Comparator<ApiDocItem> comparator = Comparator.nullsLast(Comparator.comparing(ApiDocItem::getSortTime));
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
                        Collections.sort(dtoMap.get(node.getParentNo()).getChildren(), comparator);
                    }
                }
            }
            Collections.sort(result, comparator);
        } catch (Exception e) {
            throw new BusinessException("组装消息字段树失败：" + e.getMessage());
        }
        return result;
    }

}
