package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 错误码定义
 * 
 * @author zhangpu
 * 
 */
public enum FinancePayApiErrorCode implements Messageable {

	PARAMETER_ERROR("PARAMETER_ERROR", "参数校验失败"),

	BANKCARD_BINDING_ERROR("BANKCARD_BINDING_ERROR", "银行卡绑定失败"),
	
	WHITELIST_BIND_ERROR("WHITELIST_BIND_ERROR", "白名单绑定失败"),

	BANKCARD_UNBINDING_ERROR("BANKCARD_UNBINDING_ERROR", "银行卡解绑失败"),

	CERTIFY_APPLY_ERROR("CERTIFY_APPLY_ERROR", "实名认证申请失败"),

	CERTIFY_HAD_BE_PASSED("CERTIFY_HAD_BE_PASSED", "实名认证已通过"),

	QUERY_ERROR("QUERY_ERROR", "查询失败"),

	SEND_CAPTCHA_ERROR("SEND_CAPTCHA_ERROR", "发送验证码失败"),

	CHANGE_PASSWORD_ERROR("CHANGE_PASSWORD_ERROR", "修改密码失败"),

	LOGIN_FAIL("LOGIN_FAIL", "登录失败"),

	PERSONAL_REGISTER_ERROR("REGISTER_ERROR", "个人yonghu 注册失败"),
	
	REGISTER_BINDCARD_ERROR("REGISTER_BINDCARD_ERROR", "个人用户注册绑卡失败"),
	
	CORPORATE_REGISTER_ERROR("CORPORATE_REGISTER_ERROR", "企业用户注册失败"),

	REGISTER_CHECK("REGISTER_CHECK", "注册检查"),

	SMS_SEND_ERROR("SMS_SEND_ERROR", "短信发送失败"),
	
	TEMPLATE_SMS_SEND_ERROR("TEMPLATE_SMS_SEND_ERROR", "模板短信发送失败"),

	SMS_VALIDATE_ERROR("SMS_VALIDATE_ERROR", "短信验证失败"),

	SMS_CAPTCHA_INVAILD("SMS_CAPTCHA_INVAILD", "验证码错误或过期"),

	CONTENT_GET_ERROR("CONTENT_GET_ERROR", "内容获取失败"),

	VERSION_NOFOUND("VERSION_NOFOUND", "APP最新版本未找到"),
	
	CREATE_DEPOSIT_ERROR("CREATE_DEPOSIT_ERROR", "创建充值订单失败"),

	POS_DISTRIBUTION_ERROR("POS_DISTRIBUTION_ERROR", "Pos清分对账失败"),
	
	TRADE_ORDER_CREATE_ERROR("TRADE_ORDER_CREATE_ERROR", "pos清分对账失败"),
	
	POSPAY_ERROR("POSPAY_ERROR", "Pos支付失败"),
	
	NOTORDERPAY_ERROR("NOTORDERPAY_ERROR", "无订单支付"),
	
	TRADE_DISTRIBUTION_ERROR("TRADE_DISTRIBUTION_ERROR", "交易订单清分失败"),

	PROFIT_UNFREEZE_PROFIT_ERROR("PROFIT_UNFREEZE_PROFIT_ERROR", "交易分润解冻失败"),
	
	TRADE_PROFIT_REFOUND_ERROR("TRADE_PROFIT_REFOUND_ERROR", "交易分润退款失败"),
	
	TRADE_BALANCE_PAY_ERROR("TRADE_BALANCE_PAY_ERROR", "余额支付失败"),

	TRADE_ADVANCE_PAY_ERROR("TRADE_ADVANCE_PAY_ERROR", "垫付支付失败"),

	TRADE_DEDUCT_PAY_ERROR("TRADE_DEDUCT_PAY_ERROR", "代扣支付失败"),
	
	TRADE_TRANSFER_CARD_ERROR("TRADE_TRANSFER_CARD_ERROR", "转账到卡失败"),

	TRADE_WITHDRAW_CASH_ERROR("TRADE_WITHDRAW_CASH_ERROR", "提现失败"),
	
	TRADE_FREEZE_WITHDRAW_CASH_ERROR("TRADE_FREEZE_WITHDRAW_CASH_ERROR", "解冻提现失败"),
	
	QUERY_DEPOSIT_ERROR("QUERY_DEPOSIT_ERROR", "查询充值失败"),

	QUERY_TRADE_ORDER_ERROR("QUERY_TRADE_ORDER_ERROR", "查询交易订单失败"),
	
	QUERY_ACCOUNT_CHANGE_ERROR("QUERY_ACCOUNT_CHANGE_ERROR","进出账查询失败"),
	
	QUERY_WITHDRAW_TRADE_ERROR("QUERY_WITHDRAW_TRADE_ERROR","查询提现交易失败"),
	
	QUERY_DEDUCT_TRADE_ERROR("QUERY_DEDUCT_TRADE_ERROR","查询充值代扣交易失败"),

	TRADE_DEDUCT_DEPOSIT_ERROR("TRADE_DEDUCT_DEPOSIT_ERROR", "代扣充值失败"),
	
	TRADE_YJF_BANK_DEPOSIT_ERROR("TRADE_YJF_BANK_DEPOSIT_ERROR", "易极付网银充值失败"),

	QUERY_USER_BALANCE_ERROR("QUERY_USER_BALANCE_ERROR", "查询用户余额失败"),
	
	QUERY_OPERATOR_BALANCE_ERROR("QUERY_OPERATOR_BALANCE_ERROR", "查询运营商余额失败"),

	BALANCE_NOT_ENOUGH("BALANCE_NOT_ENOUGH", "账户余额不足"),
	
	OFFLINE_CHARGE_ERROR("OFFLINE_CHARGE_ERROR", "运营商账户线下操作失败"),
	
	REALNAME_INFO_MODIFY_ERROR("REALNAME_INFO_MODIFY_ERROR", "修改用户实名信息失败"),
	
	FREEZE_ERROR("FREEZE_ERROR", "冻结失败"),
	
	UNFREEZE_ERROR("UNFREEZE_ERROR", "解冻失败"),
	
	USER_NOT_FOUND_ERROR("USER_NOT_FOUND_ERROR", "用户不存在"),
	
	TRANSACTOR_CREATE_ERROR("TRANSACTOR_CREATE", "经营人新增失败"),
	
	TRANSACTOR_DELETE_ERROR("TRANSACTOR_DELETE", "经营人删除失败"),
	
	TRANSACTOR_QUERY_ERROR("TRANSACTOR_QUERY", "经营人信息查询失败"),
	
	ACCOUNT_CREATE_ERROR("ACCOUNT_CREATE_ERROR", "资金账户创建失败"),
	
	TRADE_PAY_REDIRECT_ERROR("TRADE_PAY_REDIRECT_ERROR", "跳转支付页面失败"),
	;

	private final String code;
	private final String message;

	private FinancePayApiErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (FinancePayApiErrorCode type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	/**
	 * 通过枚举值码查找枚举值。
	 * 
	 * @param code
	 *            查找枚举值的枚举值码。
	 * @return 枚举值码对应的枚举值。
	 * @throws IllegalArgumentException
	 *             如果 code 没有对应的 Status 。
	 */
	public static FinancePayApiErrorCode findStatus(String code) {
		for (FinancePayApiErrorCode status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("FinancePayApiErrorCode not legal:" + code);
	}

	/**
	 * 获取全部枚举值。
	 * 
	 * @return 全部枚举值。
	 */
	public static List<FinancePayApiErrorCode> getAllEnum() {
		List<FinancePayApiErrorCode> list = new ArrayList<FinancePayApiErrorCode>();
		for (FinancePayApiErrorCode status : values()) {
			list.add(status);
		}
		return list;
	}

	/**
	 * 获取全部枚举值码。
	 * 
	 * @return 全部枚举值码。
	 */
	public static List<String> getAllEnumCode() {
		List<String> list = new ArrayList<String>();
		for (FinancePayApiErrorCode status : values()) {
			list.add(status.code());
		}
		return list;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", this.code, this.message);
	}

}
