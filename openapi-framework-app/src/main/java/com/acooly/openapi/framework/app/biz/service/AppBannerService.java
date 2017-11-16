package com.acooly.openapi.framework.app.biz.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.app.biz.domain.AppBanner;

import java.io.Serializable;

/**
 * app_banner Service
 * <p>
 * <p>Date: 2015-05-12 13:39:31
 *
 * @author Acooly Code Generator
 */
public interface AppBannerService extends EntityService<AppBanner> {


    void moveUp(Serializable id);


    void moveTop(Serializable id);
}
