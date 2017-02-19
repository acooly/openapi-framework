package com.yiji.framework.openapi.demo.server.message.response;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiResponse;
import com.yiji.framework.openapi.demo.server.message.dto.GoodInfo;

import java.util.List;

/**
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiMessage(service = "createOrder", type = ApiMessageType.Response)
public class CreateOrderResponse extends ApiResponse {

    @OpenApiField(desc = "测试金额", demo = "120.00")
    private Money testMoney;

    @OpenApiField(desc = "测试List返回")
    private List<GoodInfo> goodInfos;

    @OpenApiField(desc = "状态")
    private SimpleStatus status;

    public Money getTestMoney() {
        return testMoney;
    }

    public List<GoodInfo> getGoodInfos() {
        return goodInfos;
    }

    public void setGoodInfos(List<GoodInfo> goodInfos) {
        this.goodInfos = goodInfos;
    }

    public void setTestMoney(Money testMoney) {
        this.testMoney = testMoney;
    }

    public SimpleStatus getStatus() {
        return status;
    }

    public void setStatus(SimpleStatus status) {
        this.status = status;
    }
}
