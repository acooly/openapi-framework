/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-23 16:46
 */
package com.acooly.openapi.framework.service.test.ext;

import com.acooly.openapi.framework.service.service.tenant.ApiTenant;
import com.acooly.openapi.framework.service.service.tenant.ApiTenantLoaderService;
import com.acooly.openapi.framework.service.service.tenant.DefaultApiTenant;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangpu
 * @date 2021-03-23 16:46
 */
@Slf4j
@Component
@Primary
public class ApiTenantLoaderServiceTestImpl implements ApiTenantLoaderService {

    @Override
    public List<ApiTenant> load() {

        // 直接数据MOCK进行测试。
        // 实际应用场景中：由集成项目通过读取数据或则facade获取管理或配置的数据
        List<ApiTenant> apiTenants = Lists.newArrayList();
        apiTenants.add(new DefaultApiTenant("10000000001000000001", "车云宝"));
        apiTenants.add(new DefaultApiTenant("10000000001000000002", "心愿宝"));
        apiTenants.add(new DefaultApiTenant("10000000001000000003", "韦小宝"));
        return apiTenants;
    }
}
