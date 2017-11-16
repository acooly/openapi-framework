package com.acooly.openapi.framework.app.biz.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;

/**
 * 群发消息 JPA Dao
 *
 * <p>Date: 2015-11-04 13:30:36
 *
 * @author Acooly Code Generator
 */
public interface AppMessageDao extends EntityJpaDao<AppMessage, Long>, AppMessageDaoCustom {}
