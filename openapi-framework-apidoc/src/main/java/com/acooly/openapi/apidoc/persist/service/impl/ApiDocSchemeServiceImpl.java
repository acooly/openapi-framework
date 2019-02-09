/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.acooly.openapi.framework.common.utils.Exceptions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 服务方案 Service实现
 * <p>
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 */
@Slf4j
@Service("apiDocSchemeService")
public class ApiDocSchemeServiceImpl extends EntityServiceImpl<ApiDocScheme, ApiDocSchemeDao> implements ApiDocSchemeService {


    @Override
    public ApiDocScheme createDefault() {
        List<ApiDocScheme> apiDocSchemes = getEntityDao().findBySchemeType(SchemeTypeEnum.auto);
        ApiDocScheme def = Collections3.getFirst(apiDocSchemes);
        if (def == null) {
            def = new ApiDocScheme();
            def.setAuthor("system");
            def.setSchemeNo("system" + ApiDocs.API_DOC_COMMON_SPLIT_CHAR + SchemeTypeEnum.auto.code());
            def.setSchemeType(SchemeTypeEnum.auto);
            def.setTitle("系统");
            save(def);
        }
        return def;
    }

    @Override
    public List<ApiDocScheme> findBySchemeType(SchemeTypeEnum schemeType) {
        return getEntityDao().findBySchemeType(schemeType);
    }

    /**
     * 注意：合并操作不加全局事务，降低事务粒度。
     * 如果失败不一致，重新生成一次即可
     *
     * @param apiDocSchemes
     */
    @Override
    public void merge(List<ApiDocScheme> apiDocSchemes) {

        try {
            // 删除自动生成已持久化的未匹配的scheme
            List<ApiDocScheme> persists = findBySchemeType(SchemeTypeEnum.auto);
            List<ApiDocScheme> needRemoves = Lists.newArrayList();
            for (ApiDocScheme persist : persists) {
                if (!apiDocSchemes.contains(persist)) {
                    needRemoves.add(persist);
                }
            }
            List<Serializable> ids = Lists.newArrayList();
            needRemoves.forEach(e -> {
                ids.add(e.getId());
            });
            if(Collections3.isNotEmpty(ids)){
                removes(ids.toArray(new Serializable[]{}));
            }

            // 保存：传入有，数据库无的schemes
            persists = getAll();
            List<ApiDocScheme> needSaves = Lists.newArrayList();
            for (ApiDocScheme input : apiDocSchemes) {
                if (!persists.contains(input)) {
                    needSaves.add(input);
                }
            }
            saves(needSaves);
            log.info("合并服务方案 成功。 新增: {}， 删除冗余:{}", needSaves.size(), needRemoves.size());
        } catch (Exception e) {
            throw Exceptions.runtimeException("合并方案数据库失败：" + e.getMessage());
        }


    }
}
