package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.framework.app.biz.domain.AppCrash;
import com.acooly.openapi.framework.app.biz.service.AppCrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage/module/app/appCrash")
public class AppCrashManagerController
    extends AbstractJQueryEntityController<AppCrash, AppCrashService> {

  @Autowired private AppCrashService appCrashService;
}
