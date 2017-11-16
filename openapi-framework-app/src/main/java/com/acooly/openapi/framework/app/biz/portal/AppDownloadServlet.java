package com.acooly.openapi.framework.app.biz.portal;

import com.acooly.core.common.web.servlet.AbstractSpringServlet;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;
import com.acooly.openapi.framework.app.biz.service.AppVersionService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * APP安装文件下载
 *
 * <p>url规划: http://host:port/app/download/${appCode}/${deviceType}/${fileName}
 * web.xml配置：/app/download/*
 *
 * @author zhangpu
 */
public class AppDownloadServlet extends AbstractSpringServlet {

  /** uid */
  private static final long serialVersionUID = 9196914741284048051L;

  private AppVersionService appVersionService;
  private Object object = new Object();

  @Override
  protected void doService(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    OutputStream out = null;
    InputStream in = null;
    try {
      String url = request.getRequestURI();
      String urlData = StringUtils.substringAfter(url, "/app/download/");
      String[] datas = urlData.split("/");
      String appCode = datas[0];
      String deviceType = datas[1];
      AppVersion appVersion = loadAppVersionService().getLatest(appCode, deviceType);
      if (appVersion == null) {
        throw new RuntimeException("没有对应的最新版本存在");
      }
      File file = new File(appVersion.getPath());
      if (!file.exists()) {
        throw new RuntimeException("文件不存在");
      }
      doDownloadHeader(file.getName(), response, deviceType);
      in = FileUtils.openInputStream(file);
      out = response.getOutputStream();
      IOUtils.copy(in, out);
      out.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(out);
      IOUtils.closeQuietly(in);
    }
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

  private AppVersionService loadAppVersionService() {
    if (appVersionService == null) {
      synchronized (object) {
        if (appVersionService == null) {
          appVersionService = getWebApplicationContext().getBean(AppVersionService.class);
        }
      }
    }
    return appVersionService;
  }
}
