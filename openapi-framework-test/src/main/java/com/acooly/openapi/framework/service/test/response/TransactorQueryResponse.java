/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * faZheng 2016年2月22日 下午2:22:38 创建
 */

package com.acooly.openapi.framework.service.test.response;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.service.test.dto.TransactorInfo;

import java.util.List;

/**
 * 经营人查询 响应报文
 *
 * @author faZheng
 */
@OpenApiMessage(service = "transactorQuery", type = ApiMessageType.Response)
public class TransactorQueryResponse extends ApiResponse {

  @OpenApiField(desc = "经营人信息列表", constraint = "经营人信息列表")
  private List<TransactorInfo> transactorInfoList;

  public List<TransactorInfo> getTransactorInfoList() {
    return this.transactorInfoList;
  }

  public void setTransactorInfoList(List<TransactorInfo> transactorInfoList) {
    this.transactorInfoList = transactorInfoList;
  }
}
