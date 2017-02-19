package com.yiji.framework.openapi.common.convert.converter;

import com.acooly.core.utils.Dates;
import com.yiji.framework.openapi.common.ApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {

    private static Logger logger = LoggerFactory.getLogger(StringToDateConverter.class);

    @Override
    public Date convert(String source) {
        if (source == null || source.equals("")) {
            return null;
        }
        try {
            return Dates.parse(source);
        } catch (Exception e) {
            logger.warn("StringToDate转换失败,source:" + source, e);
            throw new RuntimeException("日期格式错误,需要的格式为:" + ApiConstants.DATA_FORMAT);
        }

    }

}
