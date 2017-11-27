package com.acooly.openapi.framework.serviceimpl.meta.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.serviceimpl.meta.entity.ApiMetaServiceEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** @author qiubo */
public interface ApiMetaServiceEntityDao extends EntityMybatisDao<ApiMetaServiceEntity> {
  @Select("select * from api_meta_service  where service_name=#{serviceName} and version=#{version}")
  ApiMetaServiceEntity findByServiceNameAndVersion(@Param("serviceName")String serviceName, @Param("version")String version);
}
