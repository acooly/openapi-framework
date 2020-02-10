/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
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
public interface ApiAuthAclDao extends EntityMybatisDao<ApiAuthAcl> {

    /**
     * 根据authNo获取权限ACL
     *
     * @param authNo
     * @return
     */
    @Select("select * from api_auth_service  where auth_no =#{authNo}")
    List<ApiAuthAcl> findByAuthNo(@Param("authNo") String authNo);

    /**
     * 删除authNo对应的ACL
     * @param authNo
     */
    @Select("delete * from api_auth_service  where auth_no =#{authNo}")
    void removeByAuthNo(@Param("authNo") String authNo);

}
