package com.acooly.openapi.framework.service.test.response;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zhangpu on 2016/2/12.
 */
@Getter
@Setter
public class OrderCreateResponse extends ApiResponse {

    @OpenApiField(desc = "测试金额", demo = "120.00", ordinal = 1)
    private Money testMoney;

    @OpenApiField(desc = "测试List返回", ordinal = 2)
    private List<GoodInfo> goodInfos;

    @NotNull
    @OpenApiField(desc = "商品信息变更测试", ordinal = 3)
    private GoodInfo goodInfo;

    @OpenApiField(desc = "状态", ordinal = 4)
    private SimpleStatus status;

}
