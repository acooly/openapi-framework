/** create by zhangpu date:2015年9月11日 */
package com.acooly.openapi.framework.app.openapi.service;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;
import com.acooly.openapi.framework.app.biz.service.AppMessageService;
import com.acooly.openapi.framework.app.openapi.dto.AppMessageDto;
import com.acooly.openapi.framework.app.openapi.enums.ApiOwners;
import com.acooly.openapi.framework.app.openapi.message.AppMessageListRequest;
import com.acooly.openapi.framework.app.openapi.message.AppMessageListResponse;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 推送消息列表
 *
 * @author zhangpu
 * @date 2015年9月11日
 */
@OpenApiService(
  name = "appMessageList",
  desc = "推送消息列表",
  responseType = ResponseType.SYN,
  owner = ApiOwners.COMMON,
  busiType = ApiBusiType.Query
)
public class AppMessageListService
    extends BaseApiService<AppMessageListRequest, AppMessageListResponse> {

  @Autowired private AppMessageService appMessageService;

  @Override
  protected void doService(AppMessageListRequest request, AppMessageListResponse response) {
    PageInfo<AppMessage> pageInfo =
        appMessageService.query(buildPageInfo(request), request.getPartnerId());
    response.setTotalPages(pageInfo.getTotalPage());
    response.setTotalRows(pageInfo.getTotalCount());
    if (Collections3.isNotEmpty(pageInfo.getPageResults())) {
      for (AppMessage appMessage : pageInfo.getPageResults()) {
        response.append(convert(appMessage));
      }
    }
  }

  protected AppMessageDto convert(AppMessage appMessage) {
    AppMessageDto dto = new AppMessageDto();
    dto.setContent(appMessage.getContent());
    dto.setContext(appMessage.getContext());
    dto.setId(appMessage.getId());
    dto.setSender(appMessage.getSender());
    dto.setSendTime(appMessage.getSendTime());
    dto.setTitle(appMessage.getTitle());
    dto.setType(appMessage.getType());
    return dto;
  }

  protected PageInfo<AppMessage> buildPageInfo(AppMessageListRequest request) {
    return new PageInfo<AppMessage>(request.getLimit(), request.getStart());
  }
}
