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
public class WithdrawRequest extends ApiAsyncRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf")
    private String merchOrderNo;

    @NotEmpty
    @Length(max = 20, min = 20, message = "提现用户ID为必选项，长度为20字符")
    @OpenApiField(desc = "提现用户ID", constraint = "提现用户ID", demo = "20198982938272827232")
    private String userId;

    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    @OpenApiField(desc = "提现金额", constraint = "提现金额，格式为保留两位小数的元，如: 2.00,200.05", demo = "200.15")
    private Money amount;

    @Length(max = 15, message = "银行编码不能为空，最大长度为15字符")
    @OpenApiField(desc = "银行编码", constraint = "请参考附录的银行编码列表", demo = "ABC")
    private String bankCode;

    @Length(max = 30, min = 15)
    @OpenApiField(desc = "银行卡号", demo = "6226998032873746")
    private String bankCardNo;

    @OpenApiField(desc = "到账方式", constraint = "可选值：<li>0: T+0</li><li>1: T+1</li><li>2: T+2</li>", demo = "1")
    @Length(max = 1, min = 1, message = "长度为1字符")
    private String delay;

    @OpenApiField(desc = "业务类型", demo = "BUSI2")
    private BusiTypeEnum busiType = BusiTypeEnum.BUSI2;

    public enum BusiTypeEnum implements Messageable {
        BUSI1("BUSI1", "业务1"),
        BUSI2("BUSI2", "业务2");
        private String code;
        private String name;

        /**
         * @param code
         * @param name
         */
        BusiTypeEnum(String code, String name) {
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
}
