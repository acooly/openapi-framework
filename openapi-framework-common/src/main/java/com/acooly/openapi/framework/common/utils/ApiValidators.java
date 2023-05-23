/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author Administrator
 * @date 2023-04-01 10:50
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.validate.HibernateValidatorFactory;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.exception.ApiOrderCheckException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
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
        validateJsr303(object, constraintViolations);
    }

    private static <T> void validateJsr303(Object object, Set<ConstraintViolation<T>> constraintViolations) {
        ApiOrderCheckException exception = null;
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            exception = new ApiOrderCheckException();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                String messageTemplate = constraintViolation.getMessageTemplate();
                // 是采用message配置的原生消息,则通过反射找到找到Field的中文名字,返回错误消息为：中文名字+原生消息
                if (Strings.startsWith(messageTemplate, "{") && Strings.endsWith(messageTemplate, "}")) {
                    String propertyName = constraintViolation.getPropertyPath().toString();
                    Field field = null;
                    try {
                        field = BeanUtils.getDeclaredField(constraintViolation.getRootBeanClass(), propertyName);
                    } catch (Exception e) {
                        // ignore
                    }
                    if (field != null) {
                        OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
                        if (openApiField != null) {
                            String desc = openApiField.desc();
                            if (Strings.isNotBlank(desc)) {
                                exception.addError(propertyName, desc + constraintViolation.getMessage());
                                continue;
                            }
                        }
                    }
                }
                exception.addError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

}
