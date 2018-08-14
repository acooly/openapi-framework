/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by shuijing
 * date:2018-08-14
 */
 package com.acooly.openapi.mock.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.mock.entity.ApiMock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 服务类 Mybatis Dao
 *
 * Date: 2018-08-14 17:35:43
 * @author shuijing
 */
public interface ApiMockDao extends EntityMybatisDao<ApiMock> {
 @Select("select * from api_mock  where service_name=#{serviceName} and version=#{version}")
 List<ApiMock> findByServiceNameAndVersion(@Param("serviceName") String serviceName, @Param("version") String version);
}
