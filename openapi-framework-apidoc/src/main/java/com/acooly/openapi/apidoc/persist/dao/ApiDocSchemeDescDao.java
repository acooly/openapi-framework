/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 服务方案描述 Mybatis Dao
 * <p>
 * Date: 2019-02-20 15:39:15
 *
 * @author zhike
 */
public interface ApiDocSchemeDescDao extends EntityMybatisDao<ApiDocSchemeDesc> {

    @Select("select * from api_doc_scheme_desc where scheme_no = #{schemeNo}")
    ApiDocSchemeDesc findBySchemeNo(@Param("schemeNo") String schemeNo);
}
