package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 抽象签名实现
 *
 * @author zhangpu
 * @date 2015/1/23.
 */
@Slf4j
public abstract class AbstractSigner implements Signer<ApiContext> {


    @Override
    public String sign(String body, String key) {
        return doSign0(body, key).getLeft();
    }

    @Override
    public void verify(String sign, String key, String body)
            throws ApiServiceAuthenticationException {


        Pair<String, String> signPair = doSign0(body, key);
        if (!sign.equals(signPair.getLeft())) {
            throw new ApiServiceAuthenticationException("待签名字符串[" + signPair.getRight() + "]");
        }
    }


    protected Pair<String, String> doSign0(String body, Object key) {
        String waitToSignStr = body;
        String signature = doSign(waitToSignStr, (String) key);
        log.debug("请求报文待签字符串:{},key:{},signature:{}", waitToSignStr, key, signature);
        Pair<String, String> pair = Pair.of(signature, waitToSignStr);
        return pair;
    }


    protected abstract String doSign(String waitToSignStr, String key);

}
