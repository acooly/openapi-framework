/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.openapi.framework.facade.order;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Parameterize;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangpu
 */
public class ApiNotifyOrder extends OrderBase implements Parameterize<String, String> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7850941772007013885L;
    /**
     * 通知报文
     */
    private ApiNotify notifyMessage;

    /**
     * 覆盖原始请求订单的notifyUrl
     */
    private String url;


    /**
     * 扩展参数（无订单通知时无效：GID）
     */
    private Map<String, String> parameters = Maps.newLinkedHashMap();


    @Override
    public void setParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    @Override
    public String getParameter(String key) {
        return this.parameters.get(key);
    }

    @Override
    public void removeParameter(String key) {
        this.parameters.remove(key);
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Set<String> keySet() {
        return this.parameters.keySet();
    }

    @Override
    public Collection<String> values() {
        return this.parameters.values();
    }

    @Override
    public void clear() {
        this.parameters.clear();
    }

    public ApiNotify getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(ApiNotify notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
