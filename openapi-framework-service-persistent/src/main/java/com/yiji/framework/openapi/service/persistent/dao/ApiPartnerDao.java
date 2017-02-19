/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.service.persistent.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.yiji.framework.openapi.domain.ApiPartner;
import org.springframework.data.jpa.repository.Query;


/**
 * 合作方管理 JPA Dao
 * <p>
 * Date: 2016-07-16 02:05:01
 *
 * @author Acooly Code Generator
 */
public interface ApiPartnerDao extends EntityJpaDao<ApiPartner, Long>, ApiPartnerCustomDao {

    @Query(value = "FROM ApiPartner WHERE partnerId=?1")
    ApiPartner queryByPartnerId(String partnerId);

    @Query(value = "FROM ApiPartner WHERE id != ?1 AND partnerId=?2")
    ApiPartner queryExceptIdByPartnerId(Long id,String partnerId);
}
