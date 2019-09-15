package com.acooly.openapi.framework.service.test.request;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.service.test.enums.TransactorType;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

/**
 * 查询经营人 请求报文
 *
 * @author faZheng
 */
public class TransactorQueryRequest extends ApiRequest {

    /**
     * 所属企业用户ID
     */
    @NotEmpty
    @Length(max = 32)
    @OpenApiField(desc = "所属企业用户ID", constraint = "所属企业用户ID", demo = "12121212", ordinal = 1)
    private String userId;

    /**
     * 经营人类型 *
     */
    @OpenApiField(desc = "经营人类型", constraint = "经营人类型", ordinal = 2)
    private TransactorType transactorType;

    /**
     * 经营人ID *
     */
    @Length(max = 32)
    @OpenApiField(desc = "经营人ID", constraint = "经营人ID", demo = "acooly", ordinal = 3)
    private String transactorId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TransactorType getTransactorType() {
        return this.transactorType;
    }

    public void setTransactorType(TransactorType transactorType) {
        this.transactorType = transactorType;
    }

    public String getTransactorId() {
        return this.transactorId;
    }

    public void setTransactorId(String transactorId) {
        this.transactorId = transactorId;
    }
}
