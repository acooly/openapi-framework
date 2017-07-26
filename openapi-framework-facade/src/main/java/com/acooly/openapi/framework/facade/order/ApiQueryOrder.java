/*
 * 修订记录:
 * zhike@yiji.com 2017-06-01 11:25 创建
 *
 */
package com.acooly.openapi.framework.facade.order;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Parameterize;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */
public class ApiQueryOrder extends OrderBase implements Parameterize<String, String> {

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
}
