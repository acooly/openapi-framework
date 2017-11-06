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
import com.acooly.openapi.framework.service.persistent.dao.ApiPartnerCustomDao;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/** @author acooly */
public class ApiPartnerDaoImpl extends AbstractJdbcTemplateDao implements ApiPartnerCustomDao {

  @Override
  public String getPartnerSercretKey(String partnerId) {
    String sql = "select secret_key from api_partner t1 where t1.partner_id = '" + partnerId + "'";
    SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
    if (rs != null && rs.next()) {
      return rs.getString(1);
    } else {
      return null;
    }
  }
}
