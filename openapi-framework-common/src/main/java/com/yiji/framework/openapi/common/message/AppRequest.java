/**
 * create by zhangpu
 * date:2015年5月12日
 */
package com.yiji.framework.openapi.common.message;

import com.yiji.framework.openapi.common.annotation.OpenApiField;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author zhangpu
 *
 */
public class AppRequest extends ApiRequest {

	/** 设备唯一标识 */
	@NotEmpty
	@Size(min = 8, max = 128)
	@OpenApiField(desc = "设备标识", constraint = "APP客户端设备唯一标识")
	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
