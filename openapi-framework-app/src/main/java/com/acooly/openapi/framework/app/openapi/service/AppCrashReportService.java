/** create by zhangpu date:2015年9月11日 */
package com.acooly.openapi.framework.app.openapi.service;

import com.acooly.core.utils.mapper.BeanMapper;
import com.acooly.openapi.framework.app.biz.domain.AppCrash;
import com.acooly.openapi.framework.app.biz.service.AppCrashService;
import com.acooly.openapi.framework.app.openapi.enums.ApiOwners;
import com.acooly.openapi.framework.app.openapi.message.AppCrashReportRequest;
import com.acooly.openapi.framework.app.openapi.message.AppCrashReportResponse;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * APP崩溃上报
 *
 * @author zhangpu
 * @date 2015年9月11日
 */
@OpenApiService(
  name = "appCrashReport",
  desc = "崩溃上报",
  responseType = ResponseType.SYN,
  owner = ApiOwners.COMMON
)
public class AppCrashReportService
    extends BaseApiService<AppCrashReportRequest, AppCrashReportResponse> {

  @Autowired private AppCrashService appCrashService;

  @Override
  protected void doService(AppCrashReportRequest request, AppCrashReportResponse response) {
    try {
      AppCrash appCrash = new AppCrash();
      BeanMapper.copy(request, appCrash);
      appCrash.setCrashDate(new Date());
      appCrash.setStackTrace(new String(Base64.decodeBase64(appCrash.getStackTrace())));
      appCrashService.save(appCrash);
    } catch (ApiServiceException ae) {
      throw ae;
    } catch (Exception e) {
      throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e.getMessage());
    }
  }
}
