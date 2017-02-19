package com.yiji.framework.openapi.service.test.request;


import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.service.test.enums.TransactorType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 查询经营人   请求报文
 *
 * @author faZheng
 *
 */
@OpenApiMessage(service = "transactorQuery", type = ApiMessageType.Request)
public class TransactorQueryRequest extends ApiRequest {
	
	/** 所属企业用户ID */
	@NotEmpty
	@Length(max = 32)
	@OpenApiField(desc = "所属企业用户ID", constraint = "所属企业用户ID")
	private String userId;
	
	/** 经营人类型 **/
	@OpenApiField(desc = "经营人类型", constraint = "经营人类型")
	private TransactorType transactorType;
	
	/** 经营人ID **/
	@Length(max = 32)
	@OpenApiField(desc = "经营人ID", constraint = "经营人ID")
	private String transactorId;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public TransactorType getTransactorType() {
		return this.transactorType;
	}

	public void setTransactorType(TransactorType transactorType) {
		this.transactorType = transactorType;
	}

	public String getTransactorId() {
		return this.transactorId;
	}

	public void setTransactorId(String transactorId) {
		this.transactorId = transactorId;
	}
}

    