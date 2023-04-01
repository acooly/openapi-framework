/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author Administrator
 * @date 2023-04-01 10:50
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.validate.HibernateValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * OpenAPI专用bean验证工具
 * 1、如果是模板消息，则转换字段名称为中文名字输出
 * 2、如果自定义消息，则不输出字段名称，由开发人员完全自定义错误消息
 *
 * @author zhangpu@acooly.cn
 * @date 2023-04-01 10:50
 */
@Slf4j
public class ApiValidators {

    public static void validate(Object object, Class<?>... groups) {
        ValidatorFactory validatorFactory = HibernateValidatorFactory.getInstance();
        Set<ConstraintViolation<Object>> constraintViolations = validatorFactory.getValidator().validate(object, groups);

    }

    private static <T> void validateJsr303(Object object, Set<ConstraintViolation<T>> constraintViolations) {
        OrderCheckException exception = null;
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            exception = new OrderCheckException();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                exception.addError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

}
