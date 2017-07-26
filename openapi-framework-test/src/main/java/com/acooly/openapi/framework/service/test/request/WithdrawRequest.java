/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
public class WithdrawRequest extends ApiRequest {

    public static enum busiTypeEnum {
        T0("0", "T+0提现"),
        T1("1", "T+2提现");
        private String code;
        private String name;

        /**
         * @param code
         * @param name
         */
        private busiTypeEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * 提现用户ID
     */
    @OpenApiField(desc = "提现用户ID")
    @Length(max = 20, min = 20, message = "提现用户ID为必选项，长度为20字符")
    private String userId;
    @OpenApiField(desc = "提现金额，格式为保留两位小数的元，如: 2.00,200.05")
    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    private Money amount;
    @OpenApiField(desc = "银行编码，请参考附录的银行编码列表")
    @Length(max = 15, message = "银行编码不能为空，最大长度为15字符")
    private String bankCode;
    @OpenApiField(desc = "银行卡号")
    @Length(max = 30, min = 15, message = "长度为15-30字符")
    private String bankCardNo;
    @OpenApiField(desc = "到账方式:<li>0: T+0</li><li>1: T+1</li><li>2: T+2</li>")
    @Length(max = 1, min = 1, message = "长度为1字符")
    private String delay;
    @OpenApiField(desc = "业务类型")
    private busiTypeEnum busiType = busiTypeEnum.T0;

    /**
     * @param userId
     * @param amount
     * @param bankCode
     * @param bankCardNo
     * @param delay
     */
    public WithdrawRequest(String userId, Money amount, String bankCode, String bankCardNo,
                           String delay) {
        super();
        this.userId = userId;
        this.amount = amount;
        this.bankCode = bankCode;
        this.bankCardNo = bankCardNo;
        this.delay = delay;
    }

    public WithdrawRequest() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public busiTypeEnum getBusiType() {
        return busiType;
    }

    public void setBusiType(busiTypeEnum busiType) {
        this.busiType = busiType;
    }

}
