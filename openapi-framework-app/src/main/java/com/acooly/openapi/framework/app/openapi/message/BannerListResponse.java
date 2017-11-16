/** create by zhangpu date:2015年5月10日 */
package com.acooly.openapi.framework.app.openapi.message;

import com.acooly.openapi.framework.app.openapi.dto.MediaInfo;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 首页 banner 列表
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "bannerList", type = ApiMessageType.Response)
public class BannerListResponse extends ApiResponse {

  @OpenApiField(desc = "媒体列表")
  private List<MediaInfo> banners = Lists.newArrayList();

  public void append(MediaInfo dto) {
    banners.add(dto);
  }

  public List<MediaInfo> getBanners() {
    return banners;
  }

  public void setBanners(List<MediaInfo> banners) {
    this.banners = banners;
  }
}
