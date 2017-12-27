/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.builder;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.output.ApiDocumentOutputerFactory;
import com.acooly.openapi.apidoc.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.output.impl.ApiDocumentJdbcOutputer;
import com.acooly.openapi.apidoc.parser.ApiDocParser;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by zhangpu on 2015/1/29.
 */
@Service("apiDocBuilder")
public class ApiDocBuilderImpl implements ApiDocBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocBuilderImpl.class);
    List<String> packageList = Lists.newArrayList();

    @Autowired
    private ApiDocParser apiDocumentParser;

    @Autowired
    private ApiDocumentOutputerFactory apiDocumentOutputerFactory;

    @Autowired
    private ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocProperties apiDocProperties;

    @Override
    public void JdbcBuilder(String packagePartern) {
        List<ApiDocService> docs = getApiDocs(packagePartern);
        ApiDocumentJdbcOutputer outputer = (ApiDocumentJdbcOutputer) apiDocumentOutputerFactory
                .getOutputer(ApiOutputerTypeEnum.Jdbc);
        doOutput(outputer, docs, null);
        //将服务放入默认解决方案中
        if (apiDocProperties.isIntoCommon()) {
            saveDefaulSchemeService(docs);
        }
    }

    /**
     * 将服务放入到默认解决方案中
     *
     * @param apiServiceDocs
     */
    private void saveDefaulSchemeService(List<ApiDocService> apiServiceDocs) {
//        try {
//            ApiDocScheme commonScheme = apiDocSchemeService.getEntityBySchemeType(SchemeTypeEnum.common.code());
//            if (commonScheme == null) {
//                commonScheme = new ApiDocScheme();
//                commonScheme.setName(SchemeTypeEnum.common.message());
//                commonScheme.setAuthor("system");
////                commonScheme.setNameType(SchemeTypeEnum.common.code());
////                commonScheme.setEnglishName(SchemeTypeEnum.common.code());
//                apiSchemeService.save(commonScheme);
//            }
//            Long schemeId = commonScheme.getId();
//            List<ApiDocSchemeService> apiSchemeServiceDocs = apiSchemeServiceService.findSchemeServicesBySchemeId(schemeId);
//            List<ApiDocSchemeService> saveApiSchemeServiceDocs = Lists.newArrayList();
//            for (ApiDocService apiServiceDoc : apiServiceDocs) {
//                putInSchemeService(schemeId, apiServiceDoc.getServiceNo(), saveApiSchemeServiceDocs, apiSchemeServiceDocs);
//            }
//            if (Collections3.isNotEmpty(saveApiSchemeServiceDocs)) {
//                apiSchemeServiceService.saves(saveApiSchemeServiceDocs);
//            }
//            logger.info("服务写入默认解决方案成功...");
//        } catch (Exception e) {
//            logger.warn("服务写入默认解决方案失败,异常信息：{}", e);
//        }
    }

    /**
     * 将没有放入默认解决方案的服务放入解决方案
     *
     * @param schemeId
     * @param serviceNo
     * @param saveApiSchemeServiceDocs
     * @param apiSchemeServiceDocs
     */
    private void putInSchemeService(Long schemeId, String serviceNo, List<ApiDocSchemeService> saveApiSchemeServiceDocs, List<ApiDocSchemeService> apiSchemeServiceDocs) {
        if (!isInSchemeService(schemeId, serviceNo, apiSchemeServiceDocs)) {
            ApiDocSchemeService apiSchemeServiceDoc = new ApiDocSchemeService();
//            apiSchemeServiceDoc.setSchemeId(schemeId);
            apiSchemeServiceDoc.setServiceNo(serviceNo);
            saveApiSchemeServiceDocs.add(apiSchemeServiceDoc);
        }
    }


    /**
     * 判断当前服务是否在默认解决方案中
     *
     * @param shcemeId
     * @param serviceNo
     * @param apiSchemeServiceDocs
     * @return
     */
    private boolean isInSchemeService(Long shcemeId, String serviceNo, List<ApiDocSchemeService> apiSchemeServiceDocs) {
        boolean isIn = false;
        for (ApiDocSchemeService apiSchemeServiceDoc : apiSchemeServiceDocs) {
            if (shcemeId == apiSchemeServiceDoc.getId() && Strings.equals(serviceNo, apiSchemeServiceDoc.getServiceNo())) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    protected List<ApiDocService> getApiDocs(String packagePartern) {
        return apiDocumentParser.parse();
    }

    public void getChildrenPackage(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] tempList = file.listFiles();
            if (tempList.length > 0) {
                for (int i = 0; i < tempList.length; i++) {
                    if (tempList[i].isDirectory()) {
                        getChildrenPackage(tempList[i].getPath());
                    } else if (tempList[i].isFile() && !packageList.contains(file.getPath())) {
                        packageList.add(file.getPath());
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Directory " + path + " does not exist or is not a" +
                    " directory");
        }

    }

    protected String getRelativePackage(String path, String packagePartern) {
        if (Strings.contains(path, "\\api")) {
            path = Strings.substringAfter(path, "\\api");
            path = packagePartern + path;
            path = path + "/*";
            path = path.replaceAll("\\\\", "/");
        }
        return path;
    }

    private void doOutput(ApiDocOutputer outputer, List<ApiDocService> docs, ApiDocContext apidocContext) {
        try {
            outputer.output(docs, apidocContext);
        } catch (Exception e) {
            logger.error("ApiOutpuer处理异常: outpuer:{},message:{}", outputer.getType(), e.getMessage());
        }
    }

    public ApiDocParser getApiDocumentParser() {
        return apiDocumentParser;
    }

    public void setApiDocumentParser(ApiDocParser apiDocumentParser) {
        this.apiDocumentParser = apiDocumentParser;
    }

    public ApiDocumentOutputerFactory getApiDocumentOutputerFactory() {
        return apiDocumentOutputerFactory;
    }

    public void setApiDocumentOutputerFactory(ApiDocumentOutputerFactory apiDocumentOutputerFactory) {
        this.apiDocumentOutputerFactory = apiDocumentOutputerFactory;
    }
}
