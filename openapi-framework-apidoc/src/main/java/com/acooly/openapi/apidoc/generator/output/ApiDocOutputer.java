/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output;

import com.acooly.openapi.apidoc.generator.ApiDocModule;

/**
 * API文档输出接口 Created by zhangpu on 2015/1/27.
 */
public interface ApiDocOutputer<T> {


    void output(T t);

    /**
     * 输出类型
     *
     * @return
     */
    default ApiOutputerTypeEnum getType() {
        return ApiOutputerTypeEnum.database;
    }


    /**
     * 标记生成的文档模块
     *
     * @return
     */
    ApiDocModule getModule();


}
