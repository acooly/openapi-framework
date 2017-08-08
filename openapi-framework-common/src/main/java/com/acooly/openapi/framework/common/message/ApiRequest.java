package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;

public class ApiRequest extends ApiMessage {

    @OpenApiField(desc = "通知地址", constraint = "使用异步服务时必填")
    private String notifyUrl;

    @OpenApiField(desc = "回调地址", constraint = "需要回跳到商户时必填")
    private String returnUrl;

    /**
     * 参数校验,校验失败请抛出RuntimeException
     */
    public void check() throws RuntimeException {

    }


    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public <T extends OrderBase> T toOrder(Class<T> clazz){
       T t= BeanUtils.instantiate(clazz);
       t.setGid(MDC.get("gid"));
       BeanCopier.copy(this,t);
       return t;
    }

}
