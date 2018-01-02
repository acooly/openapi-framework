/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-11 00:07 创建
 */
package com.acooly.openapi.apidoc.utils;

import com.acooly.core.utils.BeanUtils;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Signed;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangpu 2017-12-11 00:07
 */
@Slf4j
public class ApiDocs {

    private static final String API_DOC_COMMON_SPLIT_CHAR = "_";

    public static String getServiceNo(String serviceName, String serviceVersion) {
        return serviceName + API_DOC_COMMON_SPLIT_CHAR + serviceVersion;
    }

    public static String signApiDocItem(ApiDocItem apiDocItem){

        StringBuilder waitToSign = new StringBuilder();






        return null;


    }





    private static List<Field> getDeclaredFileds(Class<?> clazz, Class annotationClazz){
        List<Field> list = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getAnnotation(annotationClazz) != null) {
                list.add(field);
            }
        }
        return list;
    }


    private static List<Field> getDeclaredProperty(Object bean, Class annotationClazz){
        List<Field> list = new ArrayList<Field>();
        Field[] fields = bean.getClass().getDeclaredFields();

        try {
            Object value = null;
            for (Field field : fields) {
                if (field.getAnnotation(annotationClazz) == null) {
                    continue;
                }

                value = BeanUtils.getDeclaredProperty(bean,field);
                list.add(field);
            }
        } catch (Exception e) {
            throw new RuntimeException("对象数据签名解析失败："+e.getMessage());
        }

        return list;
    }

}
