/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package org.yiji.framework.openapi.facade.order;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Parameterize;
import com.google.common.collect.Maps;

/**
 * @author zhangpu
 */
public class ApiNotifyOrder extends OrderBase implements Parameterize<String, String> {

	/** serialVersionUID */
	private static final long serialVersionUID = 7850941772007013885L;

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
