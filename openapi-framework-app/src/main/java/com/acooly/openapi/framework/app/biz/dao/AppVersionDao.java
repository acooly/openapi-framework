package com.acooly.openapi.framework.app.biz.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;
import org.springframework.data.jpa.repository.Query;

/**
 * 手机客户端版本 JPA Dao
 *
 * <p>Date: 2014-10-25 23:16:15
 *
 * @author Acooly Code Generator
 */
public interface AppVersionDao extends EntityJpaDao<AppVersion, Long> {

  @Query(
    nativeQuery = true,
    value =
        "select * from app_version where APP_CODE=?1 and DEVICE_TYPE = ?2 and VERSION_CODE = (select max(t1.VERSION_CODE) from app_version t1 where t1.APP_CODE=?1 and t1.DEVICE_TYPE = ?2)"
  )
  AppVersion getLatest(String appCode, String deviceType);
}
