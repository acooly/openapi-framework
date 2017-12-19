/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.utils;

import com.acooly.core.utils.Money;
import com.acooly.openapi.apidoc.enums.ApiDataTypeEnum;
import com.acooly.openapi.apidoc.parser.dto.ApiDataSize;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Api文档类型判断工具
 *
 * @author zhangpu
 */
public class ApiDataTypeUtils {


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

    public static String getStringType(Field field) {
        Annotation[] Annotations = field.getAnnotations();
        int start = Integer.MIN_VALUE;
        int end = Integer.MAX_VALUE;
        for (Annotation a : Annotations) {
            if (Size.class.isAssignableFrom(a.getClass())) {
                Size s = (Size) a;
                start = s.min();
                end = s.max();
            } else if (Length.class.isAssignableFrom(a.getClass())) {
                Length s = (Length) a;
                start = s.min();
                end = s.max();
            }
        }
        if (start == Integer.MIN_VALUE && end == Integer.MAX_VALUE) {
            return ApiDataTypeEnum.S.getCode();
        } else {
            StringBuilder sb = new StringBuilder();
            if (start == end) {
                sb.append("FS(").append(start).append(")");
            } else {
                sb.append("S(").append(start).append("-").append(end).append(")");
            }
            return sb.toString();
        }
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

    public static String getNumberType(Field field) {
        int minLength = 1;
        int maxLength = String.valueOf(Integer.MAX_VALUE).length();
        boolean setting = false;
        Min min = (Min) field.getAnnotation(Min.class);
        if (min != null) {
            minLength = String.valueOf(min.value()).length();
            setting = true;
        }
        Max max = (Max) field.getAnnotation(Max.class);
        if (max != null) {
            maxLength = String.valueOf(max.value()).length();
            setting = true;
        }

        if (setting) {
            return minLength == maxLength ? "FN(" + maxLength + ")" : "N(" + minLength + "-" + maxLength + ")";
        } else {
            return "N";
        }
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

    public static boolean isMap(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    public static boolean isMoney(Field field) {
        return Money.class.isAssignableFrom(field.getType());
    }

    /**
     * @param field
     * @return
     */
    public static ApiDataTypeEnum getApiDataType(Field field) {
        if (ApiDataTypeUtils.isString(field)) {
            Size size = field.getAnnotation(Size.class);
            Length length = field.getAnnotation(Length.class);

            if (size != null && size.max() == size.min()) {
                return ApiDataTypeEnum.FS;
            }

            if (length != null && length.max() == length.min()) {
                return ApiDataTypeEnum.FS;
            }

            return ApiDataTypeEnum.S;
        }

        if (ApiDataTypeUtils.isNumber(field)) {
            return ApiDataTypeEnum.N;
        }

        if (ApiDataTypeUtils.isMoney(field)) {
            return ApiDataTypeEnum.M;
        }

        if (ApiDataTypeUtils.isCollection(field)) {
            return ApiDataTypeEnum.A;
        }

        return ApiDataTypeEnum.O;
    }

    /**
     * 解析对应字段的长度
     *
     * @param field
     * @return 返回对应字段的长度, 如果为非String类型返回null.
     */
    public static ApiDataSize getApiDataSize(Field field) {
        if (ApiDataTypeUtils.isString(field)) {
            Integer min = null;
            Integer max = null;
            Size size = field.getAnnotation(Size.class);
            Length length = field.getAnnotation(Length.class);
            if (size != null) {
                min = size.min();
                max = size.max();
            } else if (length != null) {
                min = length.min();
                max = length.max();
            }

            return new ApiDataSize(min, max);
        }

        return new ApiDataSize();
    }


    public static String getApiDataLength(ApiDataSize apiDataSize) {
        Integer min = apiDataSize.getMin();
        Integer max = apiDataSize.getMax();
        if (min == 0 || max == 0 || max == Integer.MAX_VALUE || max == min) {
            return "(" + (min != 0 ? min : max) + ")";
        }
        return "(" + min + DATA_LENGTH_SPLITER + max + ")";

    }
}
