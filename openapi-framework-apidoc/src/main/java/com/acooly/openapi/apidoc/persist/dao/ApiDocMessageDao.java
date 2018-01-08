/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 服务报文 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocMessageDao extends EntityMybatisDao<ApiDocMessage> {

    @Select("select * from api_doc_message where message_no = #{messageNo}")
    ApiDocMessage findByMessageNo(@Param("messageNo") String messageNo);

}
