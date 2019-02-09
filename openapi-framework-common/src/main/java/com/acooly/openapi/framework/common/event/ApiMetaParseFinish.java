package com.acooly.openapi.framework.common.event;

import com.acooly.openapi.framework.common.executor.ApiService;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-28 00:32
 */
@Data
public class ApiMetaParseFinish extends SpringApplicationEvent {
    private List<ApiService> apiService;

    public ApiMetaParseFinish(SpringApplication application, String[] args, List<ApiService> apiService) {
        super(application, args);
        this.apiService = apiService;
    }
}
