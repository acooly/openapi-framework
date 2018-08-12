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
import org.apache.ibatis.annotations.Update;

import java.util.List;

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


    @Select("select * from api_doc_message where service_no = #{serviceNo}")
    List<ApiDocMessage> findByServiceNo(@Param("serviceNo") String serviceNo);

    @Update("delete from api_doc_message where message_no = #{messageNo}")
    void deleteByMessageNo(@Param("messageNo") String messageNo);

    @Update("delete from api_doc_message where service_no = #{serviceNo}")
    void deleteByServiceNo(@Param("serviceNo") String serviceNo);
}
