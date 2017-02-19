/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016-07-28
 *
 */
package com.yiji.framework.openapi.service.persistent.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.yiji.framework.openapi.domain.ApiPartnerService;

import java.util.List;

/**
 * api_partner_service JPA Dao
 * <p>
 * Date: 2016-07-28 15:33:42
 *
 * @author Acooly Code Generator
 */
public interface ApiPartnerServiceDao extends EntityJpaDao<ApiPartnerService, Long>, ApiPartnerServiceCustomDao {

    ApiPartnerService findByApipartneridAndApiserviceid(Long apipartnerid, Long apiserviceid);

    List<ApiPartnerService> findByApipartnerid(Long apipartnerid);

    Long countByApiserviceidIn(List<Long> ids);

}
