/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
 package com.acooly.openapi.framework.serviceimpl.manage.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.serviceimpl.manage.entity.ApiAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 认证授权信息管理 Mybatis Dao
 *
 * Date: 2018-08-21 14:31:06
 * @author qiubo
 */
public interface ApiAuthDao extends EntityMybatisDao<ApiAuth> {
 @Select("select * from api_auth  where access_key =#{accesskey}")
 ApiAuth findByAccesskey(@Param("accesskey") String accesskey);
}
