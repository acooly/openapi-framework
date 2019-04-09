/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 方案服务列表 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocSchemeServiceDao extends EntityMybatisDao<ApiDocSchemeService> {


    @Select("select * from api_doc_scheme_service where scheme_no = #{schemeNo}")
    List<ApiDocSchemeService> findBySchemeNo(@Param("schemeNo") String schemeNo);


    @Select("select t1.* from api_doc_service t1,api_doc_scheme_service t2 " +
            "where t1.service_no = t2.service_no and t2.scheme_no =  #{schemeNo}" +
            " order by t2.sort_time desc")
    List<ApiDocService> findSchemeService(@Param("schemeNo") String schemeNo);


}
