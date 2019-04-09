/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output.impl;

import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.generator.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by zhangpu on 2015/1/27.
 */
public class HtmlApiDocOutputer implements ApiDocOutputer<File> {

    private static final Logger logger = LoggerFactory.getLogger(HtmlApiDocOutputer.class);

    private String templatePath = "classpath:template/sample";

    private String outputPath = "file:///D:\\temp\\apidocs";

    @Override
    public File output(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext) {
        try {
            copyResources();
            doParseTop(apiServiceDocs, apidocContext);
            doParseApi(apiServiceDocs);
            doParseMenu(apiServiceDocs);

        } catch (Exception e) {
            logger.error("HTML Outputer 异常:{}", e.getMessage());
        }
        return null;
    }

    /**
     * @param apiServiceDocs
     * @param apidocContext
     */
    protected void doParseTop(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext) {
        if (apidocContext == null) {
            apidocContext = new ApiDocContext();
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("context", apidocContext);
        doParser("top.ftl", "top.htm", map);
    }

    protected void copyResources() {
        try {
            Resource targetDir = getOutputPathResource();
            String targetPath = targetDir.getURL().getPath();
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources(templatePath + "/**/*");

            FileOutputStream out = null;
            InputStream in = null;
            if (resources != null && resources.length > 0) {
                for (Resource resource : resources) {
                    String path = resource.getURL().getPath();
                    String targetFile = targetPath + StringUtils.substringAfterLast(path, getTemplateReletivePath());
                    if (StringUtils.endsWith(targetFile, "/") || StringUtils.endsWith(targetFile, ".ftl")) {
                        continue;
                    }
                    File t = new File(targetFile);
                    if (!t.getParentFile().exists()) {
                        t.getParentFile().mkdirs();
                    }

                    out = new FileOutputStream(t, false);
                    in = resource.getInputStream();
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected String getTemplateReletivePath() {
        if (StringUtils.containsIgnoreCase(templatePath, "classpath:")) {
            return StringUtils.substringAfter(templatePath, "classpath:");
        }
        return templatePath;
    }

    /**
     * 解析API文档
     *
     * @param apiServiceDocs
     */
    protected void doParseApi(List<ApiDocService> apiServiceDocs) {
        Map<String, Object> map = Maps.newHashMap();
        for (ApiDocService apiServiceDoc : apiServiceDocs) {
            map.put("api", apiServiceDoc);
            doParser("api.ftl", "api" + File.separator + getApiFileName(apiServiceDoc), map);
        }
    }

    /**
     * 解析菜单
     *
     * @param apiServiceDocs
     */
    protected void doParseMenu(List<ApiDocService> apiServiceDocs) {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> entity = null;
        String defaultOwner = "公共API";
        Map<String, List<Map<String, Object>>> apis = new TreeMap<String, List<Map<String, Object>>>();
        for (ApiDocService apiServiceDoc : apiServiceDocs) {
            if (StringUtils.isNotBlank(apiServiceDoc.getOwner())
                    && !StringUtils.equals("unknown", apiServiceDoc.getOwner())) {
                apis.put(apiServiceDoc.getOwner(), null);
            }
        }
        apis.put(defaultOwner, new LinkedList<Map<String, Object>>());
        // List<Map<String, Object>> entities = Lists.newArrayList();
        String owner = null;
        for (ApiDocService apiServiceDoc : apiServiceDocs) {
            entity = Maps.newHashMap();
            entity.put("fileName", getApiFileName(apiServiceDoc));
            entity.put("service", apiServiceDoc.getName());
            entity.put("version", apiServiceDoc.getVersion());
            entity.put("serviceName", apiServiceDoc.getTitle());
            // entities.add(entity);
            owner = apiServiceDoc.getOwner();
            if (StringUtils.isBlank(owner) || StringUtils.equals("unknown", owner)) {
                owner = defaultOwner;
            }
            if (apis.get(owner) == null) {
                apis.put(owner, new LinkedList<Map<String, Object>>());
            }
            apis.get(owner).add(entity);
        }
        map.put("apis", apis);
        doParser("menu.ftl", "menu.htm", map);
    }

//	public void  doParseScheme(List<ApiScheme> schemes){
//
//		Map<String, Object> map = Maps.newHashMap();
//		Map<String, Object> entity = null;
//
//		Map<String, List<Map<String, Object>>> apis = new TreeMap<String, List<Map<String, Object>>>();
//
//		for(ApiScheme scheme : schemes){
//			List<Map<String,Object>> serviceDocs = new LinkedList<Map<String,Object>>();
//			for (ApiServiceDoc apiServiceDoc : scheme.getServices()) {
//				entity = Maps.newHashMap();
//				entity.put("fileName", getApiFileName(apiServiceDoc));
//				entity.put("service", apiServiceDoc.getService());
//				entity.put("version", apiServiceDoc.getVersion());
//				entity.put("serviceName", apiServiceDoc.getServiceName());
//				serviceDocs.add(entity);
//			}
//
//			apis.put(scheme.getName(),serviceDocs);
//		}
//
//		map.put("apis", apis);
//		doParser("menu_scheme.ftl", "menu_scheme.htm", map);
//	}

    /**
     * 模板处理
     *
     * @param templateName
     * @param outFileName
     * @param map
     */
    public void doParser(String templateName, String outFileName, Map<String, Object> map) {
        Reader reader = null;
        Writer writer = null;
        try {
            Template template = buildConfiguration().getTemplate(templateName);
            writer = new FileWriter(new File(getApiOutputPath(), outFileName), false);
            template.process(map, writer);
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
    }

    protected String getApiFileName(ApiDocService apiServiceDoc) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(apiServiceDoc.getName()).append("_v")
                .append(apiServiceDoc.getVersion() == null ? "1.0" : apiServiceDoc.getVersion()).append(".htm");
        return fileName.toString();
    }

    protected Configuration buildConfiguration() {
        try {
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            String tpath = templatePath;
            if (StringUtils.containsIgnoreCase(tpath, "classpath:")) {
                tpath = StringUtils.substringAfter(tpath, "classpath:");
                if (!StringUtils.startsWith(tpath, "/")) {
                    tpath = "/" + tpath;
                }
                cfg.setClassForTemplateLoading(getClass(), tpath);
            } else {
                Resource path = new DefaultResourceLoader().getResource(tpath);
                cfg.setDirectoryForTemplateLoading(path.getFile());
            }
            return cfg;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected Resource getTemplatePathResource() {
        return new DefaultResourceLoader().getResource(templatePath);
    }

    protected Resource getOutputPathResource() {
        return new DefaultResourceLoader().getResource(outputPath);
    }

    protected String getApiOutputPath() {
        try {
            File file = getOutputPathResource().getFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getPath();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public ApiOutputerTypeEnum getType() {
        return ApiOutputerTypeEnum.html;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
