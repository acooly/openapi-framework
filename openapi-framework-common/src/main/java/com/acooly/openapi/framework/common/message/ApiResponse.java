package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class ApiResponse extends ApiMessage {

    @NotBlank
    @OpenApiField(desc = "服务响应编码", constraint = "必填", demo = "PARAM_FORMAT_ERROR", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String code = ApiServiceResultCode.SUCCESS.getCode();

    @NotBlank
    @OpenApiField(desc = "服务响应信息", demo = "参数格式错误", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String message = ApiServiceResultCode.SUCCESS.getMessage();

    @OpenApiField(desc = "服务响应信息详情", demo = "手机号码格式错误", ordinal = ApiConstants.ORDINAL_MAX + 3)
    private String detail;

    @OpenApiField(desc = "服务响应状态", demo = "true", ordinal = ApiConstants.ORDINAL_MAX + 4)
    private boolean success = true;

    public void setResult(ApiServiceResultCode apiServiceResultCode) {
        setResult(apiServiceResultCode, null);
    }

    public void setResult(ApiServiceResultCode apiServiceResultCode, String detail) {
        setCode(apiServiceResultCode.code());
        setMessage(apiServiceResultCode.message());
        setDetail(detail);
    }

    public void setCode(String code) {
        if (code == null) {
            this.code = code;
            return;
        }
        if (code.equals(ApiServiceResultCode.SUCCESS.code())
                || code.equals(ApiServiceResultCode.PROCESSING.code())) {
            success = true;
        } else {
            success = false;
        }
        this.code = code;
    }
}
