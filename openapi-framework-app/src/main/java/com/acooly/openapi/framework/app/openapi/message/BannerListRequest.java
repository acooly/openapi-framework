/** create by zhangpu date:2015年5月10日 */
package com.acooly.openapi.framework.app.openapi.message;


import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.AppRequest;

/**
 * 首页 banner 列表
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "bannerList", type = ApiMessageType.Request)
public class BannerListRequest extends AppRequest {}
