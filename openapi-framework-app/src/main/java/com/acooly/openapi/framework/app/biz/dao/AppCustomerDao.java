package com.acooly.openapi.framework.app.biz.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;

import java.util.List;

/**
 * app_customer JPA Dao
 *
 * <p>Date: 2015-05-12 13:39:30
 *
 * @author Acooly Code Generator
 */
public interface AppCustomerDao extends EntityJpaDao<AppCustomer, Long> {

  List<AppCustomer> findByUserNameAndStatus(String userName, EntityStatus status);
}
