package com.yiji.framework.openapi.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.jsr303.CertNo;
import com.acooly.core.utils.validate.jsr303.MobileNo;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.annotation.OpenApiFieldCondition;
import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.service.test.dto.GoodInfo;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 创建订单 请求报文
 * <p/>
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiMessage(service = "createOrder", type = ApiMessageType.Request)
public class CreateOrderRequest extends ApiRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "标题", demo = "特色牛肉干")
    private String title;

    @MoneyConstraint(min = 100, max = 1000000)
    @OpenApiField(desc = "金额", constraint = "单笔金额最小1元，最大10000元", demo = "120.00")
    private Money amount;

    @Size(min = 20, max = 20)
    @OpenApiField(desc = "付款用户ID", demo = "201603080912340001")
    private String payerUserId;

    @OpenApiFieldCondition("单付款而用户ID(payerUserId)为空时,该字段必选")
    @Size(min = 20, max = 20)
    @OpenApiField(desc = "买家用户ID", demo = "201603080912340001")
    private String buyerUserId;

    @MobileNo
    @OpenApiField(desc = "买家手机号码", demo = "13676455680")
    private String buyeryMobileNo;

    @Email
    @OpenApiField(desc = "买家电子邮件", demo = "wangsa@gooogle.com")
    private String buyeryEmail;

    @CertNo
    @OpenApiField(desc = "买家身份证号码", demo = "510232987409587463")
    private String buyerCertNo;

    @NotEmpty
    @Size(min = 20, max = 20)
    @OpenApiField(desc = "卖家用户ID", demo = "201603080912340002")
    private String payeeUserId;

    @NotEmpty
    @Length(min = 6)
    @OpenApiField(desc = "交易密码", demo = "!QAZ@WSX", security = true)
    private String password;

    @Size(min = 1)
    @OpenApiField(desc = "商品信息")
    private List<GoodInfo> goodsInfos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(String payerUserId) {
        this.payerUserId = payerUserId;
    }

    public String getPayeeUserId() {
        return payeeUserId;
    }

    public void setPayeeUserId(String payeeUserId) {
        this.payeeUserId = payeeUserId;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getBuyeryMobileNo() {
        return buyeryMobileNo;
    }

    public void setBuyeryMobileNo(String buyeryMobileNo) {
        this.buyeryMobileNo = buyeryMobileNo;
    }

    public String getBuyeryEmail() {
        return buyeryEmail;
    }

    public void setBuyeryEmail(String buyeryEmail) {
        this.buyeryEmail = buyeryEmail;
    }

    public String getBuyerCertNo() {
        return buyerCertNo;
    }

    public void setBuyerCertNo(String buyerCertNo) {
        this.buyerCertNo = buyerCertNo;
    }

    public List<GoodInfo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
