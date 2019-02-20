/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiQaQuestionDao;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;
import com.acooly.openapi.apidoc.persist.entity.ApiQaQuestion;
import com.acooly.openapi.apidoc.persist.service.ApiQaClassifyService;
import com.acooly.openapi.apidoc.persist.service.ApiQaQuestionService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 问题记录表 Service实现
 *
 * Date: 2019-02-20 17:10:00
 *
 * @author zhike
 *
 */
@Service("apiQaQuestionService")
public class ApiQaQuestionServiceImpl extends EntityServiceImpl<ApiQaQuestion, ApiQaQuestionDao> implements ApiQaQuestionService {

    private static final String DEFALUT_TYPE_NAME = "其他";

    @Autowired
    private ApiQaClassifyService apiQaClassifyService;
    @Override
    public Map<String, List<ApiQaQuestion>> searchQaQuestion(String key) {

        try {
            List<ApiQaClassify> topTypes = apiQaClassifyService.loadTopTypes();
            List<ApiQaQuestion> services = queryByKey(key);
            Map<String, List<ApiQaQuestion>> map = Maps.newLinkedHashMap();
            map.put(DEFALUT_TYPE_NAME, new ArrayList<ApiQaQuestion>());
            String topPath = null;
            ApiQaClassify topType = null;
            for (ApiQaQuestion qaQuestion : services) {
                topPath = Strings.substring(qaQuestion.getApiQaClassify().getPath(), 0, 3);
                topType = getTypeByPath(topTypes, topPath);
                if (topType == null) {
                    map.get(DEFALUT_TYPE_NAME).add(qaQuestion);
                    continue;
                }

                if (map.get(topType.getName()) == null) {
                    map.put(topType.getName(), new ArrayList<ApiQaQuestion>());
                }
                map.get(topType.getName()).add(qaQuestion);
            }
            return map;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private ApiQaClassify getTypeByPath(List<ApiQaClassify> topTypes, String path) {
        for (ApiQaClassify type : topTypes) {
            if (Strings.equals(type.getPath(), path)) {
                return type;
            }
        }
        return null;
    }


    protected List<ApiQaQuestion> queryByKey(String key) {
        if (Strings.isBlank(key)) {
            return getAll();
        } else {
            return getEntityDao().search("%"+key+"%");
        }

    }
}
