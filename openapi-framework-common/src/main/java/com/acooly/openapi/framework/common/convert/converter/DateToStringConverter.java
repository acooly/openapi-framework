package com.acooly.openapi.framework.common.convert.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.acooly.openapi.framework.common.ApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;


public class DateToStringConverter implements Converter<Date, String> {
	
	private static Logger logger = LoggerFactory.getLogger(DateToStringConverter.class);
	
	@Override
	public String convert(Date source) {
		if (source == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(ApiConstants.DATA_FORMAT).format(source);
		} catch (Exception e) {
			logger.warn("StringToDate转换失败,source:" + source,e);
			return null;
		}
	}
}
