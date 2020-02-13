package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.facade.BizOrderBase;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.mapper.BeanCopier;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

@Getter
@Setter
public class ApiRequest extends ApiMessage {


    /**
     * 参数校验,校验失败请抛出RuntimeException
     */
    @Override
    public void check() throws RuntimeException {
    }


    public <T extends OrderBase> T toOrder(Class<T> clazz) {
        T t = Reflections.createObject(clazz);
        t.setGid(MDC.get("gid"));
        BeanCopier.copy(this, t);
        if (t instanceof BizOrderBase) {
            ((BizOrderBase) t).setBizOrderNo(Ids.oid());
        }
        return t;
    }
}