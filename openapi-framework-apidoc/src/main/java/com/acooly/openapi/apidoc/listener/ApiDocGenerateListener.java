///*
// * www.yiji.com Inc.
// * Copyright (c) 2015 All Rights Reserved.
// */
//
//package com.acooly.openapi.apidoc.listener;
//
//import com.acooly.core.utils.Dates;
//import com.acooly.openapi.apidoc.builder.ApiDocBuilder;
//import com.acooly.openapi.apidoc.persistence.domain.ApiGenDoc;
//import com.acooly.openapi.apidoc.ApiDocProperties;
//import com.acooly.openapi.apidoc.persistence.service.ApiGenDocService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
///**
// * 修订记录： liubin 2015-07-31 11:38 创建
// * <p>
// * 需要使用文档自动生成的系统,配置该listener,servletContext启动的时候就会去检索相关信息,生成文档。 <br>
// * 使用步骤：
// * <li>1. 将该<code>ApiDocGenerateListener</code>配置进<bold>web.xml</bold></li>
// * <li>2. 配置服务扫描目录:配置 context-param参数,参数名: <code>packagePartern</code></li>
// * <p>
// * 2016-07-22 废弃servletLinstener，使用新的ApplicationListener实现文档监听生成 by
// * hgeshu@yiji.com
// */
//@Component
//public class ApiDocGenerateListener implements ApplicationListener<ApplicationReadyEvent> {
//
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	@Autowired
//	private ApiDocProperties apiDocProperties;
//	@Autowired
//	private ApiDocBuilder apiDocBuilder;
//	@Autowired
//	private ApiGenDocService apiGenDocService;
//
//	@Override
//	public void onApplicationEvent(ApplicationReadyEvent event) {
//		if (StringUtils.equals(apiDocProperties.getGenIndex(), ApiDocProperties.NONE)) {
//			logger.info("Api文档生成标志为[{}]将不生产Api文档...", ApiDocProperties.NONE);
//			return;
//		} else {
//			Thread docGenThread=new Thread(this::docGen);
//			docGenThread.setDaemon(true);
//			docGenThread.setName("apidocGeneratorThread");
//			docGenThread.start();
//		}
//	}
//
//	private void docGen() {
//		try {
//			logger.info("生成Api文档开始....");
//			String packagePartern = apiDocProperties.getScanPackagePartern();
//			if (StringUtils.isBlank(apiDocProperties.getGenIndex())) {//没有配置生成标志的情况
//				ApiGenDoc apiGenDoc = apiGenDocService.findByVersion(Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//				if (apiGenDoc == null || StringUtils.equals("0", apiGenDoc.getGenIndex())) {
//					try {
//						//文档持久进数据库
//						apiDocBuilder.JdbcBuilder(packagePartern);
//						saveOrUpdateGenInfo(apiGenDoc, apiGenDocService, Dates.format(Dates.CHINESE_DATE_FORMAT_LINE),
//							"1", Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//					} catch (Exception e) {
//						saveOrUpdateGenInfo(apiGenDoc, apiGenDocService, Dates.format(Dates.CHINESE_DATE_FORMAT_LINE),
//							"0", Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//						logger.error("当前版本[{}]生成Api文档失败,异常信息:{}", Dates.format(Dates.CHINESE_DATE_FORMAT_LINE),
//							e.getMessage());
//					}
//				} else {
//					logger.info("当前版本[{}]Api文档已生成成功，不再重复生成", Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//					return;
//				}
//			} else if (StringUtils.equals(apiDocProperties.getGenIndex(), ApiDocProperties.ALWAYS)) {//总是生成
//				try {
//					//文档持久进数据库
//					apiDocBuilder.JdbcBuilder(packagePartern);
//					apiGenDocService.saveApiGenInfo(Dates.format(Dates.DATETIME_NOT_SEPARATOR), "1",
//						Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//				} catch (Exception e) {
//					apiGenDocService.saveApiGenInfo(Dates.format(Dates.DATETIME_NOT_SEPARATOR), "0",
//						Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//					logger.error("当前版本[{}]生成Api文档失败,异常信息:{}", apiDocProperties.getGenIndex(), e.getMessage());
//				}
//			} else {//其它情况
//				ApiGenDoc apiGenDoc = apiGenDocService.findByVersion(apiDocProperties.getGenIndex());
//				if (apiGenDoc == null || StringUtils.equals("0", apiGenDoc.getGenIndex())) {
//					try {
//						//文档持久进数据库
//						apiDocBuilder.JdbcBuilder(packagePartern);
//						saveOrUpdateGenInfo(apiGenDoc, apiGenDocService, apiDocProperties.getGenIndex(), "1",
//							Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//					} catch (Exception e) {
//						saveOrUpdateGenInfo(apiGenDoc, apiGenDocService, apiDocProperties.getGenIndex(), "0",
//							Dates.format(Dates.CHINESE_DATE_FORMAT_LINE));
//						logger.error("当前版本[{}]生成Api文档失败,异常信息:{}", apiDocProperties.getGenIndex(), e.getMessage());
//					}
//				} else {
//					logger.info("当前版本[{}]Api文档已生成成功，不再重复生成", apiDocProperties.getGenIndex());
//					return;
//				}
//			}
//
//			logger.info("生成Api文档成功!");
//		} catch (Exception e) {
//			logger.error("生成Api文档失败,异常信息:{}", e.getMessage());
//		}
//	}
//
//	/**
//	 * 保存或更新生成记录
//	 * @param apiGenDoc
//	 * @param apiGenDocService
//	 * @param genVersion
//	 * @param genIndex
//	 * @param genTime
//	 */
//	private void saveOrUpdateGenInfo(	ApiGenDoc apiGenDoc, ApiGenDocService apiGenDocService, String genVersion,
//										String genIndex, String genTime) {
//		if (apiGenDoc == null) {
//			apiGenDocService.saveApiGenInfo(genVersion, genIndex, genTime);
//		} else {
//			apiGenDoc.setGenIndex(genIndex);
//			apiGenDoc.setGenTime(genTime);
//			apiGenDocService.update(apiGenDoc);
//		}
//	}
//
//}
