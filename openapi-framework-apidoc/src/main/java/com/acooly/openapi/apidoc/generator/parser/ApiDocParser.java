package com.acooly.openapi.apidoc.generator.parser;

import com.acooly.openapi.apidoc.generator.ApiDocModule;

/**
 * 通用解析接口
 *
 * @author zhangpu
 * @date 2019-01-13 19:02
 */
public interface ApiDocParser<T> {

    /**
     * 解析
     *
     * @return
     */
    T parse();


    ApiDocModule getModule();
}
