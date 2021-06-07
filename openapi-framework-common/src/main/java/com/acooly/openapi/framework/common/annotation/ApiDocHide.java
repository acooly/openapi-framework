/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 15:00 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * opendoc对外隐藏
 * 隐藏的APIDOC只是在前端不显示，数据和内容仍然生成
 *
 * @author zhangpu@acooly.cn
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDocHide {
}
