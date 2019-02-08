package com.acooly.openapi.apidoc.generator;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-02-08 17:37
 */
@Slf4j
public class ApiDocContext {

    private String title = "OpenApi自动文档";
    private String subTitle;
    private Map<String, String> links = Maps.newLinkedHashMap();

    public void appendLink(String title, String url) {
        links.put(title, url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
