package com.acooly.openapi.framework.common.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Strings extends StringUtils {

	public static <T> T isBlankDefault(T text, T defaultValue) {
		if (text == null) {
			return defaultValue;
		}
		if (text.getClass().isAssignableFrom(String.class)) {
			return Strings.isBlank((String) text) ? defaultValue : text;
		} else {
			return text;
		}

	}

	public static boolean matcher(String regex, String value) {
		Pattern p = Pattern.compile(regex);
		return p.matcher(value).matches();
	}

	/**
	 * 前置和后置mask
	 * 
	 * @param text
	 * @param preSize
	 * @param postSize
	 * @return
	 */
	public static String mask(String text, int preSize, int postSize) {
		return mask(text, preSize, postSize, '*');
	}

	/**
	 * 前置和后置mask
	 * 
	 * @param text
	 * @param preSize
	 * @param postSize
	 * @param replaceChar
	 * @return
	 */
	public static String mask(String text, int preSize, int postSize, Character replaceChar) {
		String source = trimToEmpty(text);
		if (isBlank(source)) {
			return text;
		}
		if (replaceChar == null) {
			replaceChar = '*';
		}
		if (preSize + postSize >= text.length()) {
			return leftPad("", text.length(), replaceChar);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(leftPad("", preSize, replaceChar));
		sb.append(substring(text, preSize, text.length() - postSize));
		sb.append(leftPad("", postSize, replaceChar));
		return sb.toString();
	}

	public static String maskUserName(String text) {
		return maskReverse(text, 2, 1);
	}

	public static String maskBankCardNo(String text) {
		return maskReverse(text, 4, 3);
	}

	public static String maskIdCardNo(String text) {
		return maskReverse(text, 3, 4);
	}

	public static String maskMobileNo(String text) {
		return maskReverse(text, 3, 3);
	}

	public static String maskReverse(String text, int start, int end) {
		return maskReverse(text, start, end, '*');
	}

	public static String maskReverse(String text, int start, int end, Character replaceChar) {
		String source = trimToEmpty(text);
		if (isBlank(source)) {
			return text;
		}
		if (replaceChar == null) {
			replaceChar = '*';
		}
		if (start >= text.length() || end >= text.length() || text.length() - start - end < 0) {
			return leftPad("", text.length(), replaceChar);
		}

		return left(text, start) + leftPad("", text.length() - start - end, replaceChar) + right(text, end);
	}

	public static void main(String[] args) {
		String text = "zhangpu";
		System.out.println("用户名: " + maskReverse(text, 2, 1, '*'));
		text = "510221198209476371";
		System.out.println("身份证: " + maskReverse(text, 3, 4, '*'));
		text = "13896177630";
		System.out.println("手机号: " + maskReverse(text, 3, 3, '*'));
		text = "6221880231092323876";
		System.out.println("银行卡: " + maskReverse(text, 4, 3, '*'));
	}
}
