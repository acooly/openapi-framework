package com.acooly.openapi.framework.common.message.builtin;

import com.acooly.core.utils.Dates;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiRequest;
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
    @OpenApiField(desc = "访问key", constraint = "标准请求身份的访问码", demo = "ea416ed0759d46a8de58f63a59077499", ordinal = 1)
    private String accessKey;

    @NotEmpty
    @OpenApiField(desc = "对请求参数的签名", constraint = "对expiredBody的签名", demo = "a16ae3c399fbaa81b5d0b4d76ffe1fee", ordinal = 2)
    private String sign;

    @Valid
    @NotNull
    @OpenApiField(desc = "请求参数body", constraint = "请求进行签名的报文对象", ordinal = 3)
    private ExpiredBody expiredBody;

    @Getter
    @Setter
    public static class ExpiredBody {

        @NotEmpty
        @OpenApiField(desc = "请求参数", constraint = "明文报文", demo = "任意报文xxxxx数据", ordinal = 1)
        private String body;

        @NotNull
        @OpenApiField(desc = "有效期", constraint = "请求有效期，时间偏差不超过20分钟", demo = "2017-08-04 12:12:23", ordinal = 2)
        private Date expireDate;

        public String getSignBody() {
            return body + Dates.format(expireDate);
        }
    }
}
