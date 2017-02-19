/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.service.persistent.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.yiji.framework.openapi.domain.ApiService;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务分类 JPA Dao
 * <p>
 * Date: 2016-07-16 01:57:13
 *
 * @author Acooly Code Generator
 */
public interface ApiServiceDao extends EntityJpaDao<ApiService, Long> {

    @Query(value = "select * from api_service where name like ?1 or title like ?1", nativeQuery = true)
    List<ApiService> search(String key);

}
