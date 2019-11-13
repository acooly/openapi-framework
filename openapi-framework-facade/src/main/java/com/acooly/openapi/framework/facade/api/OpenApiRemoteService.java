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
import com.acooly.openapi.framework.facade.order.ApiSignOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.facade.result.ApiSignResult;

/**
 * OpenApi 远程服务接口
 *
 * <p>可用于RPC，SOAP等内部通讯，由下层系统根据该接口调用OpenApi提供的服务统一对外通知。
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
     * 异步发送
     * 不根据OpenApi的请求订单发送（GID），
     * 注意：无订单通知的ApiNotifyOrder中parameters参数无效，必须采用ApiNotify对象报文方式组装通知报文数据，
     * 也就是说必须由OpenApi层先定义通知报文对象，保障OpenApi层对通知可识别，可文档化
     *
     * @param apiNotifyOrder
     * @return
     */
    ResultBase sendNotify(ApiNotifyOrder apiNotifyOrder);

    /**
     * 同步通知
     *
     * <p>同步通知一般采用http redirect,由下层系统负责通知，这里统一提供对通知内容的签名和组装。
     *
     * @param apiNotifyOrder
     * @return ApiNotifyResult 包括处理状态，数据和签名。
     */
    ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder);


    /**
     * 验证报文签名
     * <p>
     * 用于：下层服务收到跳转的请求时，验证签名保障安全合法。
     *
     * @param apiVerifyOrder
     * @return
     */
    ResultBase verify(ApiVerifyOrder apiVerifyOrder);

    /**
     * 签名报文
     * 用于：下层服务根据需要对直接出口报文签名
     *
     * @param apiSignOrder
     * @return
     */
    ApiSignResult sign(ApiSignOrder apiSignOrder);
}
