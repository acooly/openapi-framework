package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author qiubo
 */
public interface ApiMetaServiceDao extends EntityMybatisDao<ApiMetaService> {

    @Select("select * from api_meta_service  where service_name=#{serviceName} and version=#{version}")
    ApiMetaService findByServiceNameAndVersion(@Param("serviceName") String serviceName, @Param("version") String version);

    @Delete("delete from api_meta_service  where service_name=#{serviceName} and version=#{version}")
    void deleteByServiceNameAndVersion(@Param("serviceName") String serviceName, @Param("version") String version);

}
