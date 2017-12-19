package com.acooly.openapi.apidoc.utils;

import org.springframework.core.env.AbstractEnvironment;

import com.acooly.core.utils.ConfigurableConstants;

public class Constants extends ConfigurableConstants {
	
	static {
		String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		if (activeProfile != null && !"".equals(activeProfile)) {
			init("application." + activeProfile + ".properties");
		} else {
			init("application.properties");
		}
	}
	public static final String GATEWAY_URL  = getProperty("gateWay.Url", "");
	public static final String TESTWAY_URL = getProperty("testWay.Url", "");
}
