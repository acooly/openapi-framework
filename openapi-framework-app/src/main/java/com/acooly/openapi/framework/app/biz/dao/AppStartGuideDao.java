package com.acooly.openapi.framework.app.biz.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.openapi.framework.app.biz.domain.AppStartGuide;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;

import java.util.List;

/**
 * app_start_guide JPA Dao
 *
 * <p>Date: 2015-05-22 14:44:16
 *
 * @author Acooly Code Generator
 */
public interface AppStartGuideDao extends EntityJpaDao<AppStartGuide, Long> {
  List<AppStartGuide> findByStatusOrderBySortOrderAsc(EntityStatus status);
}
