package com.yiji.framework.openapi.common.convert.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.acooly.core.utils.Money;

/**
 * money对象转换为字符串
 */
public class MoneyToStringConverter implements Converter<Money, String> {

	private static Logger logger = LoggerFactory.getLogger(MoneyToStringConverter.class);

	@Override
	public String convert(Money source) {
		if (source == null) {
			return null;
		}
		try {
			return source.getAmount().toString();
		} catch (Exception e) {
			logger.warn("MoneyToString转换失败,source:" + source);
			return null;
		}
	}
}