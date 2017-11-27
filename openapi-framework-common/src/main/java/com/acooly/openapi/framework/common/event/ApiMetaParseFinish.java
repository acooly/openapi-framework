package com.acooly.openapi.framework.common.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-28 00:32
 */
public class ApiMetaParseFinish extends SpringApplicationEvent {
    public ApiMetaParseFinish(SpringApplication application, String[] args) {
        super(application, args);
    }
}
