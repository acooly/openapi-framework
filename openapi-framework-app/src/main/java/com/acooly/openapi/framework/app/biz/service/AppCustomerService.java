package com.acooly.openapi.framework.app.biz.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;

/**
 * app_customer Service
 *
 * <p>Date: 2015-05-12 13:39:30
 *
 * @author Acooly Code Generator
 */
public interface AppCustomerService extends EntityService<AppCustomer> {

  AppCustomer loadAppCustomer(String usreName, EntityStatus status);

  AppCustomer createAppCustomer(AppCustomer appCustomer);

  AppCustomer updateSecretKey(AppCustomer appCustomer);
}
