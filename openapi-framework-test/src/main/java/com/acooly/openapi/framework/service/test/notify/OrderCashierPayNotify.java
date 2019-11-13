package com.acooly.openapi.framework.service.test.notify;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2016/2/12
 */
public class OrderCashierPayNotify extends ApiNotify {

    @Size(min = 16, max = 64)
    @OpenApiField(desc = "订单号", demo = "12312312312", ordinal = 1)
    private String merchOrderNo;

    @Size(min = 20, max = 20)
    @OpenApiField(desc = "交易号", demo = "123123123123", ordinal = 2)
    private String tradeNo;

    @NotNull
    @OpenApiField(desc = "付款金额", demo = "200.00", ordinal = 3)
    private Money amount;

    @NotNull
    @OpenApiField(desc = "收款金额", demo = "199.00", ordinal = 4)
    private Money amountIn;

    @OpenApiField(desc = "手续费", demo = "1.00", ordinal = 5)
    private Money fee;

    @NotNull
    private TradeStatus tradeStatus;


    public enum TradeStatus implements Messageable {
        /**
         * 成功
         */
        success("success", "成功"),
        /**
         * 处理中
         */
        processing("processing", "处理中"),
        /**
         * 失败
         */
        failure("failure", "失败");
        private final String code;
        private final String message;

        TradeStatus(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public String message() {
            return message;
        }


    }

}
