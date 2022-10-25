/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-30 19:31
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * AccessKey工具
 *
 * @author zhangpu
 * @date 2020-01-30 19:31
 */
@Slf4j
public class AccessKeys {

    public static final String ACCESSKEY_SUB_SPLIT_CHAR = "#";

    /**
     * 生成新AccessKey
     *
     * @return
     */
    public static String newAccessKey() {
        return Ids.getDid();
    }

    /**
     * 生成新SecretKey
     *
     * @return
     */
    public static String newSecretKey() {
        return DigestUtils.md5Hex(Dates.format(new Date()) + RandomStringUtils.randomAscii(5));
    }

    /**
     * 获取subAccessKey中的userKey
     *
     * @param accessKey
     * @return
     */
    public static String getCustomerId(String accessKey) {
        if (Strings.contains(accessKey, ACCESSKEY_SUB_SPLIT_CHAR)) {
            return Strings.substringAfter(accessKey, ACCESSKEY_SUB_SPLIT_CHAR);
        } else {
            return null;
        }
    }

    /**
     * 获取规范的AccessKey
     * 兼容动态AccessKey和subAccessKey模式，形如：accessKey#userkey模式
     *
     * @return
     */
    public static String getCanonicalAccessKey(String accessKey) {
        if (Strings.contains(accessKey, ACCESSKEY_SUB_SPLIT_CHAR)) {
            return Strings.substringBefore(accessKey, ACCESSKEY_SUB_SPLIT_CHAR);
        }
        return accessKey;
    }
}
