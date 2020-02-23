package com.acooly.openapi.framework.common.message;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;

import javax.validation.constraints.NotBlank;

/**
 * 响应报文基类
 *
 * @author zhangpu
 * @date 2020-02-21
 */
public class ApiResponse extends ApiMessage {

    @NotBlank
    @OpenApiField(desc = "服务响应编码", constraint = "必填", demo = "PARAM_FORMAT_ERROR", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String code = ApiConstants.SUCCESS_RESULT_CODE;

    @NotBlank
    @OpenApiField(desc = "服务响应信息", demo = "参数格式错误", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String message = ApiConstants.SUCCESS_RESULT_MESSAGE;

    @OpenApiField(desc = "服务响应信息详情", demo = "手机号码格式错误", ordinal = ApiConstants.ORDINAL_MAX + 3)
    private String detail;

    @OpenApiField(desc = "服务响应状态", demo = "true", ordinal = ApiConstants.ORDINAL_MAX + 4)
    private boolean success = true;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setResult(String code, String message) {
        setResult(code, message, null);
    }

    public void setResult(String code, String message, String detail) {
        setCode(code);
        setMessage(message);
        setDetail(detail);
    }

    public void setResult(Messageable messageable) {
        setResult(messageable, null);
    }

    public void setResult(Messageable messageable, String detail) {
        setCode(messageable.code());
        setMessage(messageable.message());
        setDetail(detail);
    }

    public void setCode(String code) {
        if (code == null) {
            this.code = code;
            return;
        }
        if (code.equals(ApiConstants.SUCCESS_RESULT_CODE)
                || code.equals(ApiConstants.PROCESSING_RESULT_CODE)) {
            success = true;
        } else {
            success = false;
        }
        this.code = code;
    }
}
