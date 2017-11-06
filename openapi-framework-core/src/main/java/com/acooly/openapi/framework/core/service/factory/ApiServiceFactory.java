package com.acooly.openapi.framework.core.service.factory;

import com.acooly.openapi.framework.core.service.base.ApiService;

public interface ApiServiceFactory {

  ApiService getApiService(String serviceName, String version);
}
