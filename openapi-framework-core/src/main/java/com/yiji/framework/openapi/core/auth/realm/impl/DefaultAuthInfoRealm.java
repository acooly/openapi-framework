/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.yiji.framework.openapi.core.auth.realm.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.core.OpenApiConstants;

import java.util.List;

/**
 * 框架AuthInfoRealm默认实现
 * <p/>
 * 常量实现.
 *
 * @author qzhanbo@yiji.com
 * @author acooly
 */
public class DefaultAuthInfoRealm extends CacheableAuthInfoRealm {

    /**
     * 获取商户安全校验码
     *
     * @param partnerId
     * @return
     */
    public String getSecretKey(String partnerId) {
        try {
            return OpenApiConstants.DEF_SECRETKEY;
        } catch (Exception e) {
            logger.error("获取用户安全校验码失败", e);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "获取用户安全校验码失败:" + e.getMessage());
        }
    }

    /**
     * 获取商户授权的权限列表.
     *
     * @param partnerId
     * @return
     */
    public List<String> getAuthorizedServices(String partnerId) {

        try {
            List<String> list = Lists.newArrayList();
            String productions = "*";
            parseProductions(list, productions);
            return list;
        } catch (Exception e) {
            logger.error("获取用户产品列表失败", e);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "获取用户产品列表失败:" + e.getMessage());
        }
    }

    private static void parseProductions(List<String> list, String productions) {
        if (!Strings.isNullOrEmpty(productions)) {
            String[] prods = productions.split(",");
            for (String prod : prods) {
                if (!Strings.isNullOrEmpty(prod)) {
                    list.add(prod);
                }
            }
        }
    }
}
