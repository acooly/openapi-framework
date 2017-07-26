package com.acooly.openapi.framework.core.marshall.crypt;

public interface ApiMarshallCryptService {

	String encrypt(String property, String text, String key);

	String decrypt(String property, String text, String key);

}
