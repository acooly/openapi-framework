/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.common.convert;

import org.springframework.core.convert.converter.Converter;

/**
 * 框架专用类型转换接口
 * 
 * 用于框架外注入特定的类型转换，如：Money，Date等自定义数据类型转换处理，并与Spring的默认Coverter接口区别开
 * 
 * @author zhangpu
 */
public interface ApiServiceConverter<S, T> extends Converter<S, T> {
}
