/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import org.springframework.stereotype.Component;

/**
 * 响应报文组装
 *
 * @author zhangpu
 */
@Component
public class JsonApiResponseMarshall extends AbstractResponseMarshall<String, ApiResponse>
    implements ApiResponseMarshall<String, ApiResponse> {

  @Override
  protected String doMarshall(ApiResponse response) {
    return JsonMarshallor.INSTANCE.marshall(response);
  }
}
