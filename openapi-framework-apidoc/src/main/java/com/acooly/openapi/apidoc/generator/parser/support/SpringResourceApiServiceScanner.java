/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.parser.support;

import com.acooly.core.common.boot.Apps;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Api服务扫描 Spring工具实现
 *
 * @author zhangpu
 */
@Service("apiServiceScanner")
public class SpringResourceApiServiceScanner implements ApiServiceScanner {
    private Logger logger = LoggerFactory.getLogger(getClass());
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    @Override
    public List<Class<?>> scan(String packagePattern, Class<? extends Annotation> annotation) {

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Set<Class<?>> classes = Sets.newHashSet();
        Splitter.on(",").trimResults().omitEmptyStrings().split(packagePattern).forEach(s -> {
            classes.addAll(doScan(s, annotation, resourcePatternResolver));
        });
        return Lists.newArrayList(classes.iterator());
    }

    @Override
    public List<Class<?>> scan(String packagePattern) {
        return scan(packagePattern, OpenApiService.class);
    }


    protected Set<Class<?>> doScan(String patten, Class<? extends Annotation> annotation, ResourcePatternResolver resourcePatternResolver) {
        Set<Class<?>> classes = Sets.newHashSet();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(patten) + '/' + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            ClassLoader loader = this.getClass().getClassLoader();
            String binaryName = null;
            for (Resource resource : resources) {
                binaryName = getCanonicalClassPath(resource);
                try {
                    Class<?> clazz = loader.loadClass(binaryName);
                    if (Modifier.isAbstract(clazz.getModifiers())) {
                        continue;
                    }
                    if (clazz.getAnnotation(annotation) != null) {
                        classes.add(clazz);
                    }
                } catch (Exception e) {
                    logger.info("加载类{}失败:{}", binaryName, e);
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("扫描ApiService失败:" + e.getMessage());
        }
        return classes;
    }


    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(Apps.getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    protected String getCanonicalClassPath(Resource resource) {
        try {
            URL classFileUrl = resource.getURL();
            String path = classFileUrl.getPath();
            if (StringUtils.contains(path, "classes/")) {
                path = StringUtils.substringAfterLast(path, "classes/");
            }
            if (StringUtils.contains(path, "jar!/")) {
                path = StringUtils.substringAfterLast(path, "jar!/");
            }
            path = path.replaceAll("/", ".").replace(".class", "");
            return path;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
