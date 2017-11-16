package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.app.biz.dao.AppCustomerDao;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppCustomerService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("appCustomerService")
public class AppCustomerServiceImpl extends EntityServiceImpl<AppCustomer, AppCustomerDao>
    implements AppCustomerService {

  @Override
  public AppCustomer loadAppCustomer(String userName, EntityStatus status) {
    List<AppCustomer> appCustomers = getEntityDao().findByUserNameAndStatus(userName, status);
    if (Collections3.isEmpty(appCustomers)) {
      return null;
    } else {
      return Collections3.getFirst(appCustomers);
    }
  }

  @Override
  public AppCustomer createAppCustomer(AppCustomer appCustomer) {
    String accessKey = appCustomer.getAccessKey();
    if (Strings.isBlank(appCustomer.getAccessKey())) {
      accessKey = appCustomer.getUserName();
    }
    String secretKey = DigestUtils.sha1Hex(UUID.randomUUID().toString());
    appCustomer.setAccessKey(accessKey);
    appCustomer.setSecretKey(secretKey);
    appCustomer.setStatus(EntityStatus.Enable);
    appCustomer.setUpdateTime(appCustomer.getCreateTime());
    save(appCustomer);
    return appCustomer;
  }

  @Override
  public AppCustomer updateSecretKey(AppCustomer appCustomer) {
    String secretKey = DigestUtils.sha1Hex(UUID.randomUUID().toString());
    appCustomer.setSecretKey(secretKey);
    save(appCustomer);
    return appCustomer;
  }
}
