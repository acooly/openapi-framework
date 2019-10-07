/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Getter
@Setter
public class WithdrawApiRequest extends ApiAsyncRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，交易唯一标志", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotEmpty
    @Length(max = 20, min = 20, message = "会员编码，固定长度为20字节")
    @OpenApiField(desc = "会员编码", constraint = "提现用户ID", demo = "20198982938272827232", ordinal = 2)
    private String userId;

    @Length(max = 20, message = "账号最大长度为20字节")
    @OpenApiField(desc = "会员账号", constraint = "会员账号，如果为空则为主账号，与会员编码相同", demo = "20198982938272827232", ordinal = 3)
    private String accountNo;

    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    @OpenApiField(desc = "提现金额", constraint = "提现金额，格式为保留两位小数的元，如: 2.00,200.05", demo = "200.15", ordinal = 4)
    private Money amount;

    @OpenApiField(desc = "到账方式", constraint = "到账方式,默认:T1", demo = "T1", ordinal = 7)
    @Length(max = 1, min = 1, message = "长度为1字符")
    private DelayEnum delay = DelayEnum.T1;


    @Getter
    public enum DelayEnum implements Messageable {
        TO("T0", "工作日当天到账"),
        T1("T1", "第二个工作日到账"),
        DO("D0", "当天到账"),
        D1("D1", "第二天到账");

        private String code;
        private String name;

        /**
         * @param code
         * @param name
         */
        DelayEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String code() {
            return this.code;
        }

        @Override
        public String message() {
            return this.name;
        }
    }
}
