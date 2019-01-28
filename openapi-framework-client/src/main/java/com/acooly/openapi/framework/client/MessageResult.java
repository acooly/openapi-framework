package com.acooly.openapi.framework.client;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.utils.Encodes;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-01-28 18:17
 */
public class MessageResult {

    /**
     * 跳转或POST的目标地址
     */
    private String url;

    /**
     * 报文主体(对应http-body-json格式)
     */
    private String body;

    /**
     * 请求头或请求参数：x-api-*
     */
    private Map<String, String> headers = Maps.newHashMap();

    /**
     * 扩展参数
     */
    private Map<String, String> data = Maps.newHashMap();


    /**
     * 获取请求的所有参数
     * 可选通过POST或queryString方式
     *
     * @return
     */
    public Map<String, String> getAllParameters() {
        Map<String, String> params = Maps.newHashMap();
        params.putAll(headers);
        params.putAll(data);
        params.put(ApiConstants.BODY, this.body);
        return params;
    }

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : getAllParameters().entrySet()) {
            sb.append(entry.getKey()).append("=").append(Encodes.urlEncode(entry.getValue())).append("&");
        }
        if (sb.length() > 1) {
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }


    public void append(String key, String value) {
        data.put(key, value);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
