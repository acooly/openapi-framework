/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.servlet;

import com.acooly.openapi.framework.core.executer.HttpApiServiceExecuter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 服务框架入口
 *
 * @author zhangpu
 * @date 2014年8月3日
 */
public class OpenAPIDispatchServlet extends AbstractSpringServlet {

  /** UID */
  private static final long serialVersionUID = -2915513005298196286L;

  private HttpApiServiceExecuter httpapiServiceExecuter;

  @Override
  protected void doInit() {
    httpapiServiceExecuter =
        (HttpApiServiceExecuter) getWebApplicationContext().getBean("httpApiServiceExecuter");
  }

  @Override
  protected void doService(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    httpapiServiceExecuter.execute(request, response);
  }
}
