/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonRedirectMarshall extends AbstractResponseMarshall<String, ApiResponse>
    implements ApiRedirectMarshall<String, ApiResponse> {

  protected String getLogLabel(ApiResponse apiResponse) {
    String labelPostfix =
        (StringUtils.isNotBlank(apiResponse.getService())
            ? "[" + apiResponse.getService() + "]:"
            : ":");
    return "服务跳转" + labelPostfix;
  }

  @Override
  protected String doMarshall(ApiResponse apiResponse) {
    return JsonMarshallor.INSTANCE.marshall(apiResponse);
  }
}
