/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 服务方案 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 */
public interface ApiDocSchemeDao extends EntityMybatisDao<ApiDocScheme> {


    @Select("select * from api_doc_scheme where scheme_type = #{schemeType}  order by sort_time desc")
    List<ApiDocScheme> findBySchemeType(@Param("schemeType") SchemeTypeEnum schemeType);

    @Select("select * from api_doc_scheme order by sort_time desc")
    List<ApiDocScheme> findAll();

    @Select("select * from api_doc_scheme where sort_time >= #{sortTime} and id != #{id} limit 1")
    ApiDocScheme findBeforeOne(@Param("sortTime") Long sortTime, @Param("id") Long id);

    List<ApiDocScheme> treeQuery(@Param("category")String category,@Param("rootId") Long rootId, @Param("rootPath")String rootPath,@Param("status") DocStatusEnum status);
}
