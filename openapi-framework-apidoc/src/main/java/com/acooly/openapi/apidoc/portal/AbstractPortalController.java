package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.openapi.apidoc.ApiDocProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * apidoc 开放平台 控制器基类
 *
 * @author zhangpu
 */
public abstract class AbstractPortalController extends AbstractStandardEntityController {

    {
        allowMapping = "";
    }

    @Autowired
    protected ApiDocProperties apiDocProperties;

}
