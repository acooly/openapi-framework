/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-21 20:28 创建
 *
 */
package com.acooly.openapi.framework.common.utils.json;

import com.acooly.core.utils.Money;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * @author qiubo@qq.com
 */
public class MoneyDeserializer implements ObjectDeserializer {
    public final static MoneyDeserializer instance = new MoneyDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {

        try {
            int token = parser.getLexer().token();
            if (token == JSONToken.NULL) {
                parser.getLexer().nextToken(JSONToken.COMMA);
                return null;
            }
            if (token == JSONToken.COMMA) {
                JSONScanner lexer = (JSONScanner) parser.getLexer();
                String val;
                lexer.nextToken();
                lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                val = lexer.stringVal();
                lexer.nextToken(JSONToken.RBRACE);
                lexer.nextToken();
                Money money = new Money(val);
                return (T) money;

            } else {
                Object value = parser.parse();
                if (value == null) {
                    return null;
                }
                Money money = new Money(value.toString());
                return (T) money;
            }
        } catch (Exception e) {
            throw new RuntimeException("Money格式错误");
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_FLOAT;
    }

}