/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.facade.api;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.facade.order.ApiQueryOrder;


/**
 * OpenApi 远程服务接口
 * <p>
 * 可用于RPC，SOAP等内部通讯，由下层系统根据该接口调用OpenApi提供的服务统一对外通知。
 *
 * @author zhangpu
 */
public interface OpenApiRemoteService {

    /**
     * 异步通知
     *
     * @param apiNotifyOrder
     * @return ResultBase 中的status为sucess表示通知已投递
     */
    ResultBase asyncNotify(ApiNotifyOrder apiNotifyOrder);

    /**
     * 同步通知
     * <p>
     * 同步通知一般采用http redirect,由下层系统负责通知，这里统一提供对通知内容的签名和组装。
     *
     * @param apiNotifyOrder
     * @return ApiNotifyResult 包括处理状态，数据和签名。
     */
    ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder);


    /**
     * 异步通知
     * 此异步通知接口不经过过openapi数据库，直接将下层系统传递的参数通过notifyUrl发送出去
     * 参数parameters中notifyUrl必传，partnerId和git必传，如果没有可以自行new一个
     *
     * @param apiNotifyOrder
     * @return ResultBase 中的status为sucess表示通知已投递
     */
    ResultBase sendMessage(ApiNotifyOrder apiNotifyOrder);

    /**
     * 根据partnerId获取商户密钥
     * 返回的商户密钥从map里面取，key=sercretKey
     * 参数partnerId和git必传
     *
     * @param apiQueryOrder
     * @return
     */
    ResultBase getPartnerSercretKey(ApiQueryOrder apiQueryOrder);


}
