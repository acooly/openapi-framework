/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-20 13:46
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.Types;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.convert.ApiServiceConversionService;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * OpenApi相关的Annotation解析
 *
 * @author zhangpu
 * @date 2022-09-20 13:46
 */
@Slf4j
public class ApiAnnotations {

    public static int MAX_LIST_SIZE = 100;
    private static ConversionService conversionService = ApiServiceConversionService.INSTANCE;

    public static <T> T demoApiMessage(Class<T> tClazz) {
        Options options = Options.builder().listSize(5).build();
        return doDemoApiMessage(tClazz, options, true);
    }

    public static <T> T demoApiMessage(Class<T> tClazz, Options options) {
        return doDemoApiMessage(tClazz, options, true);
    }

    protected static <T> T doDemoApiMessage(Class<T> tClazz, Options options, boolean recursion) {
        int listSize = Math.min(options.listSize, MAX_LIST_SIZE);
        Map<String, Object> valueMapping = options.valueMapping;
        Class<?> clazz = tClazz;
        T object = Reflections.createObject(tClazz);
        while (clazz != null && clazz != ApiRequest.class && clazz != ApiResponse.class && clazz != ApiNotify.class && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
                if (openApiField == null) {
                    log.warn("报文:{}，属性:{} 没有标记@OpenApiField，忽略解析", clazz.getName(), field.getName());
                    continue;
                }
                Object value = null;
                if (DataTypeUtils.isCollectionStandard(field) && recursion) {
                    // 集合 [递归]
                    if (recursion) {
                        Class<?> genericClass = ApiPrivateUtils.getParameterGenericType(clazz, field);
                        if (genericClass != null && !DataTypeUtils.isSimpleType(genericClass)) {
                            Collection collection = null;
                            if (DataTypeUtils.isList(field)) {
                                collection = Lists.newArrayList();
                            } else if (DataTypeUtils.isSet(field)) {
                                collection = Sets.newLinkedHashSet();
                            }
                            for (int i = 1; i <= listSize; i++) {
                                collection.add(doDemoApiMessage(genericClass, options, genericClass != clazz));
                            }
                            value = collection;
                        }
                    }
                } else if (DataTypeUtils.isObject(field)) {
                    // 递归子对象
                    if (recursion) {
                        value = doDemoApiMessage(field.getType(), options, field.getType() != clazz);
                    }
                } else if (Types.isArray(field.getType())) {
                    // ignore
                } else if (Types.isEnum(field.getType())) {
                    // 枚举
                    Object[] objects = field.getType().getEnumConstants();
                    if (objects != null && objects.length > 0) {
                        // 设置了demo值，判断是否
                        String enumValue = openApiField.demo();
                        if (Strings.isNotBlank(enumValue)) {
                            for (Object o : objects) {
                                Enum e = (Enum) o;
                                if (Strings.equals(enumValue, e.name())) {
                                    value = e;
                                    break;
                                }
                            }
                        } else {
                            value = objects[0];
                        }
                    }
                } else {
                    // 正常类型直接设置值
                    value = parseDemoValue(field, openApiField);
                }
                Reflections.setFieldValue(object, field, value);
            }
            clazz = clazz.getSuperclass();
        }
        return object;
    }


    private static Object parseDemoValue(Field field, OpenApiField openApiField) {
        String value = openApiField.demo();
        if (Strings.isBlank(value)) {
            if (Types.isMoney(field.getType())) {
                value = "200.00";
            }
        }
        return conversionService.convert(value, field.getType());
    }

    @Data
    @Builder
    public static class Options {
        private int listSize = 5;
        /**
         * 字段的值自定义映射（支持非javabean对象和非集合类型）
         * key: fieldName, value对应的值（注意类型和格式匹配）
         */
        private Map<String, Object> valueMapping = Maps.newHashMap();
    }

}
