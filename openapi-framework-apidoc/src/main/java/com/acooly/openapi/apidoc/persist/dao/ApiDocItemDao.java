/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 报文字段 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocItemDao extends EntityMybatisDao<ApiDocItem> {

    @Select("select * from api_doc_item where item_no = #{itemNo}")
    ApiDocItem findByItemNo(@Param("itemNo") String itemNo);

    @Select("select * from api_doc_item where message_no = #{messageNo}")
    List<ApiDocItem> findByMessageNo(@Param("messageNo") String messageNo);

    @Select("select * from api_doc_item where message_no = #{messageNo} and parent_no is null")
    List<ApiDocItem> findByMessageNoTop(@Param("messageNo") String messageNo);

    @Select("select * from api_doc_item where message_no = #{messageNo} and parent_no = #{parentNo}")
    List<ApiDocItem> findByMessageNoAndParentNo(@Param("messageNo") String messageNo, @Param("parentNo") String parentNo);

    @Select("select * from api_doc_item where id in #{itemIds}")
    List<ApiDocItem> findByItemIds(@Param("itemIds") List<Long> itemIds);


    @Update("delete from api_doc_item where item_no = #{itemNo}")
    void deleteByItemNo(@Param("itemNo") String itemNo);

    @Update("delete from api_doc_item where message_no = #{messageNo}")
    void deleteByMessageNo(@Param("messageNo") String messageNo);

}
