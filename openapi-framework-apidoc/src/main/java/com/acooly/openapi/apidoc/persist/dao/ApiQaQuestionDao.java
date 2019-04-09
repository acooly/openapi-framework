/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiQaQuestion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 问题记录表 Mybatis Dao
 * <p>
 * Date: 2019-02-20 17:10:00
 *
 * @author zhike
 */
public interface ApiQaQuestionDao extends EntityMybatisDao<ApiQaQuestion> {

    @Select(value = "from api_qa_question where problem like %#{key}% or solution like %#{key}%")
    List<ApiQaQuestion> search(@Param("key") String key);
}
