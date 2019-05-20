/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-05-18 22:23
 */
package com.com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-05-18 22:23
 */
@Slf4j
public class ApiJavaBeanSerializer extends JavaBeanSerializer {

    public ApiJavaBeanSerializer(Class<?> beanType) {
        super(beanType);
    }

    public ApiJavaBeanSerializer(Class<?> beanType, String... aliasList) {
        super(beanType, aliasList);
    }

    public ApiJavaBeanSerializer(Class<?> beanType, Map<String, String> aliasMap) {
        super(beanType, aliasMap);
    }

    public ApiJavaBeanSerializer(SerializeBeanInfo beanInfo) {
        super(beanInfo);
        this.beanInfo = beanInfo;
    }


}
