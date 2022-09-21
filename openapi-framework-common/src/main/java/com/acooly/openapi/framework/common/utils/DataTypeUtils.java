/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Money;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Api文档类型判断工具
 *
 * @author zhangpu
 */
public class DataTypeUtils {


    public static final String DATA_LENGTH_SPLITER = "-";

    public static boolean isSimpleType(Class<?> clazz) {
        return isString(clazz) || isNumber(clazz);
    }

    /**
     * 判断是否API文档字符串类型
     * <p>
     * 包括JAVA类型:String, Date和枚举
     *
     * @param field
     * @return
     */
    public static boolean isString(Field field) {
        return isString(field.getType());
    }

    public static boolean isString(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || clazz.isEnum()
                || (clazz.isPrimitive() && "boolean,char".contains(clazz.toString()));
    }

    /**
     * 判断是否数字类型
     *
     * @param field
     * @return
     */
    public static boolean isNumber(Field field) {
        return isNumber(field.getType());
    }

    public static boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz)
                || (clazz.isPrimitive() && "long,int,byte,short".contains(clazz.toString()));
    }


    /**
     * 判断是否数组
     * <p>
     * 包括类型：集合和数组
     *
     * @param field
     * @return
     */
    public static boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray();
    }

    public static boolean isCollectionStandard(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    public static boolean isList(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    public static boolean isSet(Field field) {
        return Set.class.isAssignableFrom(field.getType());
    }


    public static boolean isArray(Field field) {
        return field.getType().isArray();
    }

    public static boolean isMap(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    public static boolean isMoney(Field field) {
        return Money.class.isAssignableFrom(field.getType());
    }

    public static boolean isMoney(Class<?> clazz) {
        return Money.class.isAssignableFrom(clazz);
    }


    public static boolean isObject(Field field) {
        if (isNumber(field) || isString(field) || isMoney(field)) {
            return false;
        }
        return true;
    }

    public static boolean isObject(Class<?> clazz) {
        if (isNumber(clazz) || isString(clazz) || isMoney(clazz)) {
            return false;
        }
        return true;
    }


    public static Class<?> getCollectionGenericType(Field field) {
        Type type = field.getGenericType();
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            for (Type t : ((ParameterizedType) type).getActualTypeArguments()) {
                return (Class<?>) t;
            }
        }
        return null;
    }

}
