/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 13:55 创建
 *
 */
package com.acooly.openapi.framework.common.utils.json;

import com.acooly.core.utils.Money;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Object <--> json
 *
 * @author qiubo@qq.com
 */
public class JsonMarshallor {

    public static final JsonMarshallor INSTANCE = new JsonMarshallor();
    private final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();
    private Feature[] features;
    private ParserConfig parserConfig;
    private int featureValues = 0;

    private JsonMarshallor() {
        afterPropertiesSet();
    }

    public void afterPropertiesSet() {
        SERIALIZE_CONFIG.put(Money.class, MoneySerializer.INSTANCE);
        parserConfig = new ParserConfig();
        parserConfig.putDeserializer(Money.class, MoneyDeserializer.instance);
        features = new Feature[0];
        // 取消SortFeidFastMatch特性.
        featureValues = (~Feature.SortFeidFastMatch.getMask()) & JSON.DEFAULT_PARSER_FEATURE;
    }

    /**
     * 转换字符串为对象
     *
     * @param source 字符串
     * @param clazz  类型
     * @param <T>    返回对象
     * @return
     */
    public <T> T parse(String source, Type clazz) {
        return JSON.parseObject(source, clazz, parserConfig, featureValues, features);
    }

    /**
     * 转化字符串为jsonlist对象
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        ParserConfig.getGlobalInstance().putDeserializer(Money.class, MoneyDeserializer.instance);
        return JSON.parseArray(text, clazz);
    }

    /**
     * 转换对象为json字符串
     *
     * @param object 原对象
     * @return json字符串
     */
    public String marshall(Object object) {
        if (object == null) {
            return "{}";
        }
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = getJsonSerializer(out);
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }


    public String marshallForClient(Object object) {
        if (object == null) {
            return "{}";
        }
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = getJsonSerializer(out);
            serializer.config(SerializerFeature.WriteNullStringAsEmpty, false);
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }

    private JSONSerializer getJsonSerializer(SerializeWriter out) {
        JSONSerializer serializer = new JSONSerializer(out, SERIALIZE_CONFIG);
        serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
        serializer.setDateFormat("yyyy-MM-dd HH:mm:ss");
        serializer.config(SerializerFeature.QuoteFieldNames, true);
        serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
        serializer.config(SerializerFeature.WriteNullStringAsEmpty, true);
        return serializer;
    }

    public void addSerializer(Class<?> clazz, ObjectSerializer objectSerializer) {
        SERIALIZE_CONFIG.put(clazz, objectSerializer);
    }
}
