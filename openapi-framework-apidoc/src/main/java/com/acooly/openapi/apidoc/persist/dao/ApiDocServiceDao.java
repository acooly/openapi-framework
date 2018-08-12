/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 服务 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocServiceDao extends EntityMybatisDao<ApiDocService> {


    @Select("select * from api_doc_service where service_no = #{serviceNo}")
    ApiDocService findByServiceNo(@Param("serviceNo") String serviceNo);

    @Update("delete from api_doc_service where service_no = #{serviceNo}")
    void deleteByServiceNo(@Param("serviceNo") String serviceNo);

}
