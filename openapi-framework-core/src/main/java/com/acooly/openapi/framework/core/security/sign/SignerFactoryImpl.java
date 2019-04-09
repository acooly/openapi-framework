/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * 签名工厂实现
 *
 * @author zhangpu
 * @date 2014年6月3日
 * @param <T>
 */
@Component
public class SignerFactoryImpl<T>
    implements SignerFactory<T>, ApplicationContextAware, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(SignerFactoryImpl.class);

  private ApplicationContext applicationContext;

  private EnumMap<SignTypeEnum, Signer<T>> signerMap;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void afterPropertiesSet() throws Exception {
    signerMap = Maps.newEnumMap(SignTypeEnum.class);
    Map<String, Signer> signers = applicationContext.getBeansOfType(Signer.class);
    for (Map.Entry<String, Signer> entry : signers.entrySet()) {
      Signer signer = entry.getValue();
      signerMap.put(signer.getSinType(), signer);
      logger.debug("加载{}签名处理器:{}", signer.getSinType(), signer.getClass().getName());
    }
  }

  @Override
  public Signer<T> getSigner(SignTypeEnum signType) {
    Signer<T> signer = signerMap.get(signType);
    if (signer == null) {
      throw new ApiServiceAuthenticationException("不支持的signType[" + signType + "]");
    }
    return signer;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
