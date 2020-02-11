/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 认证授权信息管理 Mybatis Dao
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 */
public interface ApiAuthDao extends EntityMybatisDao<ApiAuth> {

    /**
     * 根据accesskey查询
     * @param accesskey
     * @return
     */
    @Select("select * from api_auth  where access_key =#{accesskey}")
    ApiAuth findByAccesskey(@Param("accesskey") String accesskey);

    /**
     * 根据编码查询
     * @param authNo
     * @return
     */
    @Select("select * from api_auth  where auth_no =#{authNo}")
    ApiAuth findByAuthNo(@Param("authNo") String authNo);
}
