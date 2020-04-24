/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.persist.dao.ApiDocMessageDao;
import com.acooly.openapi.apidoc.persist.dto.MergeResult;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.enums.ApiDocMergeType;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务报文 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Slf4j
@Service("apiDocMessageService")
public class ApiDocMessageServiceImpl extends EntityServiceImpl<ApiDocMessage, ApiDocMessageDao> implements ApiDocMessageService {

    @Autowired
    private ApiDocItemService apiDocItemService;


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    @Override
    public void merge(String serviceNo, List<ApiDocMessage> apiDocMessages) {
        if (Collections3.isEmpty(apiDocMessages)) {
            return;
        }
        List<MergeResult> mergeResults = Lists.newArrayList();
        mergeResults.addAll(doMergeDelete(serviceNo, apiDocMessages));
        mergeResults.addAll(doMergeSave(apiDocMessages));
        if (Collections3.isNotEmpty(mergeResults)) {
            log.info("Message合并 [成功] serviceNo:{}, size: {}", serviceNo, mergeResults.size());
            for (MergeResult result : mergeResults) {
                log.debug("Message合并 {}", result.toString());
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void cascadeDelete(String serviceNo) {
        doMergeDelete(getEntityDao().findByServiceNo(serviceNo));
    }

    /**
     * 合并更新
     *
     * @param apiDocMessages
     */
    protected List<MergeResult> doMergeSave(List<ApiDocMessage> apiDocMessages) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        ApiDocMessage entity;
        String messageNo;
        for (ApiDocMessage apiDocMessage : apiDocMessages) {
            entity = getEntityDao().findByMessageNo(apiDocMessage.getMessageNo());
            messageNo = apiDocMessage.getMessageNo();
            if (entity != null) {
                // update
                if (!ApiDocs.equelsApiDocMessage(entity, apiDocMessage)) {
                    // 数据有变更
                    apiDocMessage.setId(entity.getId());
                    update(apiDocMessage);
                    mergeResults.add(MergeResult.messageOf(messageNo, ApiDocMergeType.UPDATE));
                }
            } else {
                // insert
                save(apiDocMessage);
                mergeResults.add(MergeResult.messageOf(messageNo, ApiDocMergeType.CREATE));
            }
            // 级联合并字段
            apiDocItemService.merge(messageNo, apiDocMessage.getApiDocItems());
        }
        return mergeResults;
    }

    /**
     * 合并删除
     * <p>
     * 生成的数据有，持久化数据中无的数据，从持久化中级联删除
     *
     * @param serviceNo
     * @param apiDocMessages
     */
    protected List<MergeResult> doMergeDelete(String serviceNo, List<ApiDocMessage> apiDocMessages) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        List<ApiDocMessage> persists = getEntityDao().findByServiceNo(serviceNo);
        if (Collections3.isEmpty(persists)) {
            return mergeResults;
        }
        List<ApiDocMessage> needRemoves = Lists.newArrayList();
        for (ApiDocMessage persist : persists) {
            // itemNo都匹配不上，则直接删除
            if (!apiDocMessages.contains(persist)) {
                needRemoves.add(persist);
                continue;
            }
        }
        if (needRemoves.size() > 0) {
            mergeResults.addAll(doMergeDelete(needRemoves));
        }
        return mergeResults;
    }

    /**
     * 级联删除
     *
     * @param apiDocMessages
     */
    protected List<MergeResult> doMergeDelete(List<ApiDocMessage> apiDocMessages) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        if (Collections3.isEmpty(apiDocMessages)) {
            return mergeResults;
        }
        for (ApiDocMessage message : apiDocMessages) {
            getEntityDao().deleteByMessageNo(message.getMessageNo());
            apiDocItemService.deleteByMessageNo(message.getMessageNo());
            mergeResults.add(MergeResult.messageOf(message.getMessageNo(), ApiDocMergeType.DELETE));
        }
        return mergeResults;
    }


    @Override
    public List<ApiDocMessage> loadApiDocMessages(String serviceNo) {
        List<ApiDocMessage> apiDocMessages = getEntityDao().findByServiceNo(serviceNo);
        for (ApiDocMessage apiDocMessage : apiDocMessages) {
            apiDocMessage.setApiDocItems(apiDocItemService.loadByMessageNo(apiDocMessage.getMessageNo()));
        }
        return apiDocMessages;
    }
}
