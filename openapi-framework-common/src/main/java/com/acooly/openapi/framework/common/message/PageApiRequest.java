package com.acooly.openapi.framework.common.message;


import com.acooly.openapi.framework.common.annotation.OpenApiField;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页请求报文
 * 
 * @author zhangpu
 *
 */
public abstract class PageApiRequest extends ApiRequest {

	@Min(1)
	@OpenApiField(desc = "分页页号", constraint = "默认为：1，表示获取那页数据，页号默认为1，调用端可以根据第一次返回信息中的pageSize字段计算下次访问的页号。")
	private int start = 1;

	@Min(1)
	@Max(100)
	@OpenApiField(desc = "分页大小", constraint = "默认为：20，表示返回的一页数据条数")
	private int limit = 20;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
