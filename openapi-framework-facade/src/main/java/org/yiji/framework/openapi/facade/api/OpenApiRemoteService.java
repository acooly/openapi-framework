/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package org.yiji.framework.openapi.facade.api;

import com.acooly.core.common.facade.ResultBase;
import org.yiji.framework.openapi.facade.order.ApiNotifyOrder;
import org.yiji.framework.openapi.facade.result.ApiNotifyResult;


/**
 * OpenApi 远程服务接口
 * 
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
	 * 
	 * 同步通知一般采用http redirect,由下层系统负责通知，这里统一提供对通知内容的签名和组装。
	 * 
	 * @param apiNotifyOrder
	 * @return ApiNotifyResult 包括处理状态，数据和签名。
	 */
	ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder);

}
