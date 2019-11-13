/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.facade.result;

import com.acooly.core.common.facade.ResultBase;
import lombok.Getter;
import lombok.Setter;

/**
 * 签名结果
 *
 * @author zhangpu
 * @date 2019-10-19
 */
@Getter
@Setter
public class ApiSignResult extends ResultBase {

    /**
     * 签名
     */
    private String sign;


}
