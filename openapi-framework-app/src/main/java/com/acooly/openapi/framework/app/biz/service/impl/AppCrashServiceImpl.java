package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppCrashDao;
import com.acooly.openapi.framework.app.biz.domain.AppCrash;
import com.acooly.openapi.framework.app.biz.service.AppCrashService;
import org.springframework.stereotype.Service;

@Service("appCrashService")
public class AppCrashServiceImpl extends EntityServiceImpl<AppCrash, AppCrashDao>
    implements AppCrashService {}
