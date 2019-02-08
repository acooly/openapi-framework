/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.parser.support;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Api服务扫描器
 *
 * @author zhangpu
 */
public interface ApiServiceScanner {

    List<Class<?>> scan(String packagePattern);

    List<Class<?>> scan(String packagePattern, Class<? extends Annotation> annotation);

}
