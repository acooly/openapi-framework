package com.acooly.openapi.framework.serviceimpl.persistent.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.domain.ApiMetaService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author qiubo
 */
public interface ApiMetaServiceDao extends EntityMybatisDao<ApiMetaService> {

    @Select("select * from api_meta_service  where service_name=#{serviceName} and version=#{version}")
    ApiMetaService findByServiceNameAndVersion(@Param("serviceName") String serviceName, @Param("version") String version);

}
