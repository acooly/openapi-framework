/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 方案服务列表 Mybatis Dao
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocSchemeServiceDao extends EntityMybatisDao<ApiDocSchemeService> {


    @Select("select * from api_doc_scheme_service where scheme_no = #{schemeNo}")
    List<ApiDocSchemeService> findBySchemeNo(@Param("schemeNo") String schemeNo);

    @Select("select t2.id,t1.service_no,t1.name,t1.version,t1.title,t1.owner,t1.note,t1.manual_note,t1.service_type,t1.busi_type,t2.scheme_no," +
            "t1.comments,t1.create_time,t1.update_time,t2.sort_time from api_doc_service t1,api_doc_scheme_service t2 " +
            "where t1.service_no = t2.service_no and t2.scheme_no =  #{schemeNo} " +
            " order by t2.sort_time,t1.name desc")
    List<ApiDocService> findSchemeService(@Param("schemeNo") String schemeNo);

    @Select("select t1.*,t2.scheme_no from api_doc_service t1,api_doc_scheme_service t2 " +
            "where t1.service_no = t2.service_no " +
            " order by t2.sort_time,t1.name desc")
    List<ApiDocService> findAllSchemeService();

    @Select(value = "select * FROM api_doc_scheme_service WHERE scheme_no =#{schemeNo} AND service_no = #{serviceNo}")
    List<ApiDocSchemeService> findSchemeServicesBySchemeIdAndServiceNo(@Param("schemeNo") String schemeNo, @Param("serviceNo") String serviceNo);

    /**
     * 根据schemeNo获取这个scheme下的api列表
     *
     * @param schemeNo
     * @return
     */
    @Select("select t1.*,t3.scheme_no from api_doc_service as t1 join api_doc_scheme_service as t2 on t1.service_no=t2.service_no " +
            "join api_doc_scheme as t3 on t2.scheme_no= t3.scheme_no and t3.category='api' and t3.scheme_no!='SYSTEM' and t1.service_no " +
            "in (select service_no from api_doc_scheme_service where scheme_no = #{schemeNo}) order by t2.sort_time ")
    List<ApiDocService> findContentServices(String schemeNo);

    /**
     * 根据schemeNo获取这个scheme下的api列表
     *
     * @param
     * @return
     */
    @Select("select t1.*,t3.scheme_no from api_doc_service as t1 join api_doc_scheme_service as t2 on t1.service_no=t2.service_no " +
            "join api_doc_scheme as t3 on t2.scheme_no= t3.scheme_no and t3.category='api' and t3.scheme_no!='SYSTEM' " +
            "and (upper(t1.name) like CONCAT(CONCAT('%', #{keywords}),'%') or UPPER(t1.title) like CONCAT(CONCAT('%', #{keywords}),'%')) " +
            " order by t1.update_time desc,t1.name limit #{start},#{limit}")
    List<ApiDocService> findContentServicesByKey(@Param("keywords") String keywords, @Param("start") int start, @Param("limit") int limit);

    @Select("select count(t1.id) from api_doc_service as t1 join api_doc_scheme_service as t2 on t1.service_no=t2.service_no " +
            "join api_doc_scheme as t3 on t2.scheme_no= t3.scheme_no and t3.category='api' and t3.scheme_no!='SYSTEM' " +
            "and (upper(t1.name) like CONCAT(CONCAT('%', #{keywords}),'%') or UPPER(t1.title) like CONCAT(CONCAT('%', #{keywords}),'%')) ")
    int countServicesByKey(@Param("keywords") String keywords);


    /**
     * 找到前记录的上一条
     *
     * @param current
     * @param id
     * @return
     */
    @Select("select * from api_doc_scheme_service where scheme_no=#{schemeNo} and sort_time <=#{sortTime} and id != #{id}  order by sort_time desc limit 1")
    ApiDocSchemeService findBeforeOne(@Param("sortTime") long current, @Param("schemeNo") String schemeNo, @Param("id") Long id);

    /**
     * 找到当前记录的下一条
     *
     * @param current
     * @param id
     * @return
     */
    @Select("select * from api_doc_scheme_service where scheme_no=#{schemeNo} and sort_time >=#{sortTime} and id != #{id} order by sort_time limit 1")
    ApiDocSchemeService findAlfterOne(@Param("sortTime") long current, @Param("schemeNo") String schemeNo, @Param("id") Long id);

    /**
     * 找到当前第一条
     *
     * @return
     */
    @Select("select * from api_doc_scheme_service where scheme_no=#{schemeNo} order by sort_time limit 1")
    ApiDocSchemeService findTopOne(@Param("schemeNo") String schemeNo);
}
