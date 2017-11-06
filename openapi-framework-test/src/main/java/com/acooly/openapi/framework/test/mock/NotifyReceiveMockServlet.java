/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.test.mock;

import com.acooly.openapi.framework.common.utils.Servlets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/** @author zhangpu */
public class NotifyReceiveMockServlet extends HttpServlet {

  protected static final Logger logger = LoggerFactory.getLogger(NotifyReceiveMockServlet.class);
  /** UID */
  private static final long serialVersionUID = 4730933269416485351L;
  String key = "c9cef22553af973d4b04a012f9cb8ea8";

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.info("商户接收通知MOCK：{}", Servlets.getParameters(req));

    String sign = req.getParameter("sign");
    String signType = req.getParameter("signType");
    Writer w = resp.getWriter();
    try {
      // SignerFactory sf =
      // WebApplicationContextUtils.getWebApplicationContext(getServletContext())
      // .getBean(SignerFactory.class);
      // Signer signer = sf.getSigner(signType);
      // signer.verify(sign, key, Servlets.getParamMap(req));
      w.write("success");
      logger.info("success. signType:{}", signType);
    } catch (Exception e) {
      w.write("failure:" + e.getMessage());
      logger.info("failure.signType:{},error:{}", signType, e.getMessage());
    } finally {
      IOUtils.closeQuietly(w);
    }
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
}
