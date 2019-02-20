/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiQaQuestion;

import java.util.List;
import java.util.Map;

/**
 * 问题记录表 Service接口
 *
 * Date: 2019-02-20 17:09:59
 * @author zhike
 *
 */
public interface ApiQaQuestionService extends EntityService<ApiQaQuestion> {
    Map<String, List<ApiQaQuestion>> searchQaQuestion(String key);
}
