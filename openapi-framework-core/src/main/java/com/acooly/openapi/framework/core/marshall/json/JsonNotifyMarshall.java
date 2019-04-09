/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiNotifyMarshall;
import org.springframework.stereotype.Component;

@Component
public class JsonNotifyMarshall extends AbstractResponseMarshall<String, ApiNotify>
    implements ApiNotifyMarshall<String, ApiNotify> {

  @Override
  protected String doMarshall(ApiResponse apiResponse) {
    return JsonMarshallor.INSTANCE.marshall(apiResponse);
  }
}
