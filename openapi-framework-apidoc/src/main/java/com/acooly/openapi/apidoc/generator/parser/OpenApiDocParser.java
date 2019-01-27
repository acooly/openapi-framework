package com.acooly.openapi.apidoc.generator.parser;

/**
 * 通用解析接口
 *
 * @author zhangpu
 * @date 2019-01-13 19:02
 */
public interface OpenApiDocParser<T, R> {

    R parse(T t);

}
