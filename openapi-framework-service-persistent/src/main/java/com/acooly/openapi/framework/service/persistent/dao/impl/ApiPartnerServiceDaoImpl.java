/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 01:36 创建
 */
package com.acooly.openapi.framework.service.persistent.dao.impl;

import com.acooly.module.ds.AbstractJdbcTemplateDao;
import com.google.common.collect.Lists;
import com.acooly.openapi.framework.common.dto.ApiServiceDto;
import com.acooly.openapi.framework.service.persistent.dao.ApiPartnerServiceCustomDao;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

/**
 * @author acooly
 */
public class ApiPartnerServiceDaoImpl extends AbstractJdbcTemplateDao implements ApiPartnerServiceCustomDao {

    @Override
    public List<ApiServiceDto> queryPartnerService(Long partnerId) {
        String sql = "select t1.id, t1.title, t1.name, t1.version,t2.path " +
                "from api_service t1, api_service_type t2, api_partner_service t3 " +
                "where t1.id = t3.apiserviceid and t1.type_id = t2.id and t3.apipartnerid=" + partnerId;
        return queryForList(sql, ApiServiceDto.class);
    }

    @Override
    public List<String> getAuthorizedServices(String partnerId) {
        String sql = "select service_name from api_partner_service where partner_id = '" + partnerId + "'";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        List<String> authedApis = Lists.newArrayList();
        while(rs.next()){
            authedApis.add(rs.getString(1));
        }
        return authedApis;
    }
}
