/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.utils;

import com.acooly.core.utils.GenericsUtils;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 专用工具
 *
 * @author zhangpu
 */
public class ApiDocPrivateUtils {
    private static final Logger logger = LoggerFactory
            .getLogger(ApiDocPrivateUtils.class);

    /**
     * 获取集合对象的泛型参数类型
     *
     * @param clazz
     * @param field
     * @return
     */
    public static Class<?> getParameterGenericType(Class<?> clazz, Field field) {
        TypeToken<?> t = TypeToken.of(field.getGenericType());
        if (t.getType() instanceof ParameterizedType) {
            Type type = ((ParameterizedType) t.getType())
                    .getActualTypeArguments()[0];
            if (type.toString().length() > 1) {
                return ((Class<?>) type);
            }

            // 获取基础POJO的泛型
            Class<?> cc = clazz;
            Class<?> tt = null;
            do {
                tt = GenericsUtils.getSuperClassGenricType(cc);
                if (tt != Object.class) {
                    break;
                }
                cc = cc.getSuperclass();
            } while (cc != ApiRequest.class && cc != ApiResponse.class
                    && cc != Object.class);
            return tt;

        }
        return null;
    }


    private static String getClassSourcePath(Class<?> clazz) {
        return Strings.replace(clazz.getName(), ".", "/") + ".java";
    }

}
