/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 问题分类表 Mybatis Dao
 * <p>
 * Date: 2019-02-20 17:09:59
 *
 * @author zhike
 */
public interface ApiQaClassifyDao extends EntityMybatisDao<ApiQaClassify> {

    @Select(value = "select max(path) from api_qa_classify where parent_id = #{parentId}")
    String getMaxPath(@Param("parentId") Long parentId);

    @Select(value = "select max(path) from api_qa_classify where ISNULL(parent_id)")
    String getTopMaxPath();

    List<ApiQaClassify> findByPathStartingWith(String codePrefix);

    @Select("from api_qa_classify where parent_id is null")
    List<ApiQaClassify> getTops();

    @Select("from api_qa_classify where parent_id = #{parentId}")
    List<ApiQaClassify> getChildrens(@Param("parentId") Long parentId);
}
