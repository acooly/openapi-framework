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
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    @Select(value = "SELECT ds.* FROM api_doc_service ds" +
            " LEFT JOIN api_doc_scheme_service dss ON ds.service_no = dss.service_no" +
            " WHERE dss.scheme_no = #{schemeNo}")
    List<ApiDocService> findServicesBySchemeNo(@Param("schemeNo") String schemeNo);

    @Select(value = "SELECT * FROM api_doc_service WHERE service_no" +
            " NOT IN(SELECT service_no FROM api_doc_scheme_service" +
            " WHERE scheme_no= #{schemeNo})")
    List<ApiDocService> getOtherSchemeServices(@Param("schemeNo") String schemeNo);

}
