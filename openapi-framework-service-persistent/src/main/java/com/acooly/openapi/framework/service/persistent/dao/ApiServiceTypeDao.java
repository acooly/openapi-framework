/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service.persistent.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.openapi.framework.domain.ApiServiceType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务分类 JPA Dao
 *
 * <p>Date: 2016-07-16 01:57:05
 *
 * @author Acooly Code Generator
 */
public interface ApiServiceTypeDao extends EntityJpaDao<ApiServiceType, Long> {

  @Query(nativeQuery = true, value = "select max(path) from api_service_type where parent_id = ?1")
  String getMaxPath(Long parentId);

  @Query(
    nativeQuery = true,
    value = "select max(path) from api_service_type where ISNULL(parent_id)"
  )
  String getTopMaxPath();

  List<ApiServiceType> findByPathStartingWith(String codePrefix);

  @Query("from ApiServiceType where parentId is null")
  List<ApiServiceType> getTops();
}
