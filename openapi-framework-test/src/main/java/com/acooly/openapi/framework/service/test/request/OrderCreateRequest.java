package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.validate.jsr303.CertNo;
import com.acooly.core.utils.validate.jsr303.MobileNo;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiFieldCondition;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 创建订单 请求报文
 *
 * <p>Created by zhangpu on 2016/2/12.
 */
@Getter
@Setter
public class OrderCreateRequest extends ApiRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "标题", constraint = "标题", demo = "特色牛肉干", ordinal = 2)
    @ToString.Maskable
    private String title;

    @OpenApiField(desc = "金额", constraint = "单笔金额最小1元，最大10000元", demo = "120.00", ordinal = 3)
    private Money amount;

    @Size(min = 20, max = 20)
    @OpenApiField(desc = "付款用户ID", demo = "201603080912340001", ordinal = 4)
    private String payerUserId;

    @OpenApiFieldCondition("单付款而用户ID(payerUserId)为空时,该字段必选")
    @Size(min = 20, max = 20)
    @OpenApiField(desc = "买家用户ID", demo = "201603080912340001", ordinal = 5)
    private String buyerUserId;

    @MobileNo
    @OpenApiField(desc = "买家手机号码", demo = "13676455680", ordinal = 6)
    private String buyeryMobileNo;

    @Email
    @OpenApiField(desc = "买家电子邮件", demo = "wangsa@gooogle.com", ordinal = 7)
    private String buyeryEmail;

    @CertNo
    @OpenApiField(desc = "买家身份证号码", demo = "510232987409587463", ordinal = 8)
    private String buyerCertNo;

    @NotEmpty
    @ToString.Invisible
    @Size(min = 20, max = 20)
    @OpenApiField(desc = "卖家用户ID", demo = "201603080912340002", ordinal = 9)
    private String payeeUserId;

    @NotEmpty
    @Length(min = 6)
    @OpenApiField(desc = "交易密码", demo = "!QAZ@WSX", security = true, ordinal = 10)
    private String password;

    @OpenApiField(desc = "单个商品", ordinal = 11)
    private GoodInfo goodsInfo;

    @OpenApiField(desc = "商品信息", ordinal = 12)
    private List<GoodInfo> goodsInfos;

}
