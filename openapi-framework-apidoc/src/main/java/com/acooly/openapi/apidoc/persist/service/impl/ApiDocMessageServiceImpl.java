/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.dao.ApiDocMessageDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import org.springframework.stereotype.Service;

/**
 * 服务报文 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocMessageService")
public class ApiDocMessageServiceImpl extends EntityServiceImpl<ApiDocMessage, ApiDocMessageDao> implements ApiDocMessageService {


    @Override
    public void mergeSave(ApiDocMessage apiDocMessage) {
        ApiDocMessage entity = getEntityDao().findByMessageNo(apiDocMessage.getMessageNo());
        // 暂时不处理已删除
        if (entity != null) {
            // update
            if (!ApiDocs.equelsApiDocMessage(entity, apiDocMessage)) {
                // 数据有变更
                apiDocMessage.setId(entity.getId());
                update(apiDocMessage);
            }
        } else {
            // insert
            save(apiDocMessage);
        }


    }
}
