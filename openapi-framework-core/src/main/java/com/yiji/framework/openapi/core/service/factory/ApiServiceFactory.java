package com.yiji.framework.openapi.core.service.factory;

import com.yiji.framework.openapi.core.service.base.ApiService;

public interface ApiServiceFactory {

	ApiService getApiService(String serviceName, String version);

}
