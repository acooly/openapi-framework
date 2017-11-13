/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Bohr.Qiu <qiubo@qq.com>
 * @author zhangpu： 增加对部分隐私加密信息解密功能
 * @author zhangpu 重构接口抽象框架
 */
@Component
public class JsonApiRequestMarshall implements ApiRequestMarshall<ApiRequest, ApiContext> {

  private static JsonMarshallor jsonMarshallor = JsonMarshallor.INSTANCE;

  @Autowired private ApiMarshallCryptService apiMarshallCryptService;

  @Override
  public ApiRequest marshall(ApiContext apiContext) {
    String requestBody = apiContext.getRequestBody();
    ApiRequest parsed = jsonMarshallor.parse(requestBody, apiContext.getRequest().getClass());
    ObjectAccessor objectAccessor = ObjectAccessor.of(parsed);
    for (Map.Entry<String, Field> entry :
        objectAccessor.getClassMeta().getSecurityfieldMap().entrySet()) {
      String value = objectAccessor.getPropertyValue(entry.getKey());
      value = apiMarshallCryptService.decrypt(entry.getKey(), value, apiContext.getPartnerId());
      objectAccessor.setPropertyValue(entry.getKey(), value);
    }
    parsed.setPartnerId(apiContext.getPartnerId());
    return parsed;
  }

  @Override
  public ApiProtocol getProtocol() {
    return ApiProtocol.JSON;
  }
}
