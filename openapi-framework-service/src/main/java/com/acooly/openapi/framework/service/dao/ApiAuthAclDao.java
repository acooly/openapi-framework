/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
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
    @Select("select * from api_auth_acl  where auth_no =#{authNo}")
    List<ApiAuthAcl> findByAuthNo(@Param("authNo") String authNo);

    /**
     * 根据accessKey查询
     *
     * @param accessKey
     * @return
     */
    @Select("select * from api_auth_acl  where access_key =#{accessKey}")
    List<ApiAuthAcl> findByAccessKey(@Param("accessKey") String accessKey);


    /**
     * 删除authNo对应的ACL
     *
     * @param authNo
     */
    @Select("delete from api_auth_acl  where auth_no =#{authNo}")
    void removeByAuthNo(@Param("authNo") String authNo);

    /**
     * 查询授权列表
     * @param authNo
     * @return
     */
    @Select("SELECT ms.id,ms.service_name,ms.service_desc,ms.busi_type,ms.note,acl.create_time,acl.update_time " +
            " FROM api_auth_acl acl LEFT JOIN api_meta_service ms ON ms.service_name=acl.name AND ms.version=acl.version " +
            " WHERE acl.auth_no=#{authNo} order by acl.create_time desc ")
    List<ApiMetaService> findMetaServicesByAuthNo(@Param("authNo") String authNo);

}
