/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.dao.ApiDocItemDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import org.springframework.stereotype.Service;

/**
 * 报文字段 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocItemService")
public class ApiDocItemServiceImpl extends EntityServiceImpl<ApiDocItem, ApiDocItemDao> implements ApiDocItemService {


    @Override
    public void mergeSave(ApiDocItem apiDocItem) {

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

    }
}
