package com.acooly.openapi.framework.app.biz.portal;

import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;
import com.acooly.openapi.framework.app.biz.service.AppVersionService;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Android应用下载
 *
 * @author zhangpu
 */
@Controller
@RequestMapping(value = "/app")
public class AppPortalController
    extends AbstractStandardEntityController<AppVersion, AppVersionService> {

  private static final String DEF_APP_CODE = "loan";
  @Resource private AppVersionService appVersionService;

  @RequestMapping(value = "index")
  @Override
  public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
    String appCode = getAppCode(request);
    AppVersion iphone = appVersionService.getLatest(appCode, AppVersion.DEVICE_TYPE_IPHONE);
    AppVersion android = appVersionService.getLatest(appCode, AppVersion.DEVICE_TYPE_ANDROID);
    String profiles = System.getProperty("spring.profiles.active");
    if (profiles == null) {
      profiles = "dev";
    }
    String profileName = profiles.equals("dev") ? "开发" : (profiles.equals("test") ? "测试" : "");
    model.addAttribute("android", android);
    model.addAttribute("iphone", iphone);
    model.addAttribute("profileName", profileName);
    model.addAttribute("profiles", profiles);
    model.addAttribute("viewType", request.getParameter("viewType"));
    return "index";
  }

  @RequestMapping(value = "json")
  @ResponseBody
  public AppDownload json(HttpServletRequest request, HttpServletResponse response) {
    String appCode = getAppCode(request);
    AppDownload appDownload = new AppDownload();
    appDownload.setProfile(System.getProperty("spring.profiles.active"));
    appDownload.setIphone(appVersionService.getLatest(appCode, AppVersion.DEVICE_TYPE_IPHONE));
    appDownload.setAndroid(appVersionService.getLatest(appCode, AppVersion.DEVICE_TYPE_ANDROID));
    return appDownload;
  }

  @RequestMapping(value = "{appCode}/{deviceType}")
  @ResponseBody
  public Object download(
      @PathVariable("appCode") String appCode,
      @PathVariable("deviceType") String deviceType,
      HttpServletRequest request,
      HttpServletResponse response) {
    OutputStream out = null;
    InputStream in = null;
    try {
      AppVersion appVersion = appVersionService.getLatest(appCode, deviceType);
      if (appVersion == null) {
        throw new RuntimeException("没有对应的最新版本存在");
      }
      File file = new File(appVersion.getPath());
      if (!file.exists()) {
        throw new RuntimeException("文件不存在");
      }
      doDownloadHeader(
          appVersion.getAppName()
              + "v"
              + appVersion.getVersionName()
              + "."
              + Files.getFileExtension(file.getName()),
          response,
          deviceType);
      in = FileUtils.openInputStream(file);
      out = response.getOutputStream();
      IOUtils.copy(in, out);
      out.flush();
    } catch (Exception e) {
      return "ERROR:" + e.getMessage();
    } finally {
      IOUtils.closeQuietly(out);
      IOUtils.closeQuietly(in);
    }
    return null;
  }

  protected void doDownloadHeader(
      String fileName, HttpServletResponse response, String deviceType) {
    if (deviceType.equalsIgnoreCase("android")) {
      response.setContentType("application/vnd.android.package-archive");
    } else {
      response.setContentType("application/octet-stream");
    }
    response.setHeader("Content-Disposition", "attachment");
    response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
  }

  private String getAppCode(HttpServletRequest request) {
    String appCode = request.getParameter("appCode");
    if (Strings.isBlank(appCode)) {
      appCode = DEF_APP_CODE;
    }
    return appCode;
  }
}
