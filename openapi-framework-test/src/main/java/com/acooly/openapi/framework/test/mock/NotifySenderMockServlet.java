/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.test.mock;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.utils.Servlets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;


/**
 * @author zhangpu
 */
public class NotifySenderMockServlet extends HttpServlet {

	private static final long serialVersionUID = 4730933269416485351L;
	protected final static Logger logger = LoggerFactory.getLogger(NotifySenderMockServlet.class);

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> requestData = Servlets.getParameters(req);
		logger.info("MOCK下层调用OpenApiArchServcie：{}", requestData);
		String partnerId = req.getParameter(ApiConstants.PARTNER_ID);
		String gid = req.getParameter(ApiConstants.GID);
		String type = req.getParameter("type");
		if (StringUtils.isBlank(type)) {
			type = "async";
		}
		requestData.remove(ApiConstants.PARTNER_ID);
		requestData.remove(ApiConstants.GID);

		Writer w = resp.getWriter();
		Object result = null;
		try {
			w.write("call :" + type);
			OpenApiRemoteService openApiRemoteService = WebApplicationContextUtils.getWebApplicationContext(getServletContext())
					.getBean(OpenApiRemoteService.class);

			if (StringUtils.equals(type, "async")) {
				ApiNotifyOrder order = new ApiNotifyOrder();
				order.setParameters(requestData);
				order.setPartnerId(partnerId);
				order.setGid(gid);
				openApiRemoteService.asyncNotify(order);
				w.write("success. result:" + result);
			} else {
				// ApiNotifyOrder order = new ApiNotifyOrder();
				// order.setData(requestData);
				// order.setPartnerId(partnerId);
				// order.setGid(gid);
				// order.setMerchOrderNo(merchOrderNo);
				// result = openApiArchServcie.syncReturn(order);
				// w.write("success. result:" + result);
			}
			logger.info("MOCK调用通知结果:{}", result);
		} catch (Exception e) {
			w.write("failure:" + e.getMessage());
			logger.info("MOCK调用通知失败, result:{},error:{}", result, e.getMessage());
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
