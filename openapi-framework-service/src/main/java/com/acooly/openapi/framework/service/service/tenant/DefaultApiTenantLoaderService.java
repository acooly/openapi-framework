/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-22 11:45
 */
package com.acooly.openapi.framework.service.service.tenant;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 框架内部默认空实现
 *
 * @author zhangpu
 * @date 2021-03-22 11:45
 */
@Slf4j
@Component
public class DefaultApiTenantLoaderService implements ApiTenantLoaderService {

    @Override
    public List<ApiTenant> load() {
        return Lists.newArrayList();
    }
}
