/** create by zhangpu date:2015年10月18日 */
package com.acooly.openapi.framework.app.openapi.message;

import com.acooly.openapi.framework.app.openapi.enums.QueryType;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.AppRequest;

/**
 * 内容信息详情 请求
 *
 * @author zhangpu
 * @date 2015年10月18日
 */
public class AppContentInfoTempRequest extends AppRequest {

  @OpenApiField(desc = "查询方式", constraint = "默认按唯一标识方式查询")
  private QueryType queryType = QueryType.byId;

  /** 内容信息唯一标识(可以是数字或字符编码，由服务器端确定并转型) */
  @OpenApiField(desc = "标识", constraint = "内容信息唯一标识")
  private String id;
}
