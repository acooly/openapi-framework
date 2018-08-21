package com.acooly.openapi.framework.core.service.support.auth;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.Dates;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-20 15:53
 */
@Getter
@Setter
public class AuthRequest extends ApiRequest {
    public static final long EXPIRE = 20 * 60 * 1000;
    @NotEmpty
    @OpenApiField(desc = "访问key", constraint = "必填")
    private String accessKey;

    @NotEmpty
    @OpenApiField(desc = "对请求参数的签名", constraint = "必填")
    private String sign;

    @Valid
    @NotNull
    @OpenApiField(desc = "请求参数body", constraint = "必填")
    private ExpiredBody expiredBody;

    @Getter
    @Setter
    public static class ExpiredBody {
        @NotEmpty
        @OpenApiField(desc = "请求参数", constraint = "必填")
        private String body;
        @NotNull
        @OpenApiField(desc = "请求有效期，上线偏差不超过20分钟", constraint = "必填")
        private Date expireDate;

        public String getSignBody(){
            return body+ Dates.format(expireDate);
        }
    }
}
