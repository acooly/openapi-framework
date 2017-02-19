package com.yiji.framework.openapi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基于Spring服务的Servlet基础类
 * 
 * <li>1、整合Spring容器到Servlet中，方便子类直接获取Spring容器内的服务 <li>2、提Servlet处理基础模板框架 <li>
 * 3、提供对Servlet配置参数的获取封装
 * 
 * 
 * @author zhangpu
 * 
 */
public abstract class AbstractSpringServlet extends HttpServlet {

	/** UID */
	private static final long serialVersionUID = 902161018414718234L;

	private static final Logger logger = LoggerFactory.getLogger(AbstractSpringServlet.class);

	/** WebApplicationContext for this servlet */
	private WebApplicationContext webApplicationContext;

	@Override
	public void init() throws ServletException {
		logger.debug("Initializing Spring-based Servlet '" + getServletName() + "'");
		this.webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		doInit();
	}

	protected <T> T getBean(String name, Class<T> requestType) {
		return webApplicationContext.getBean(name, requestType);
	}

	protected Object getBean(String name) {
		return webApplicationContext.getBean(name);
	}

	protected String getStringParameter(String name) {
		return getInitParameter(name);
	}

	protected long getLongParameter(String name) {
		try {
			return Long.parseLong(getStringParameter(name));
		} catch (Exception e) {
			throw new RuntimeException("Parse init parameter to long failure.[" + name + ":" + getStringParameter(name)
					+ "]");
		}
	}

	protected int getIntParameter(String name) {
		try {
			return Integer.parseInt(getStringParameter(name));
		} catch (Exception e) {
			throw new RuntimeException("Parse init parameter to int failure.[" + name + ":" + getStringParameter(name)
					+ "]");
		}
	}

	protected List<String> getParameter(String name) {
		String value = getStringParameter(name);
		if (value == null || "".equals(value)) {
			return new ArrayList<String>(0);
		}
		String[] array = value.split(",");
		return Arrays.asList(array);
	}

	protected void doInit() {

	}

	protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	public void destroy() {
		webApplicationContext = null;
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

}
