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

import java.util.List;

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
     *
     * @param accesskey
     * @return
     */
    @Select("select * from api_auth  where access_key =#{accesskey}")
    ApiAuth findByAccesskey(@Param("accesskey") String accesskey);

    /**
     * 根据编码查询
     *
     * @param authNo
     * @return
     */
    @Select("select * from api_auth  where auth_no =#{authNo}")
    ApiAuth findByAuthNo(@Param("authNo") String authNo);

    /**
     * 根据父节点ID查询子列表
     *
     * @param parentId
     * @return
     */
    @Select("select * from api_auth  where parent_id = #{parentId} order by id desc")
    List<ApiAuth> findByParentId(@Param("parentId") Long parentId);

    @Select("select * from api_auth  where parent_id is null order by id desc")
    List<ApiAuth> findTops();

    @Select("select * from api_auth aa where aa.permissions='*:*' and not exists (select 1 from api_auth_acl aaa where aaa.auth_no = aa.auth_no) ")
    List<ApiAuth> findAllPermitAuth();

}
