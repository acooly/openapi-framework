/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.openapi.framework.app;

import com.acooly.core.utils.validate.Validators;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.acooly.openapi.framework.app.AppProperties.PREFIX;

/** @author qiubo@yiji.com */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class AppProperties {
  public static final String PREFIX = "acooly.openapi.app";
  private Boolean enable = true;
  private String storagePath = "app";

  private JPush jpush = new JPush();
  /** 设备绑定验证 */
  private boolean deviceIdCheck = false;
  /** 每次登陆动态生成秘钥，false表示登录后生成用户秘钥后不再改变 */
  private boolean secretKeyDynamic = false;

  private Anonymous anonymous = new Anonymous();

  private NotifySender notifysender = new NotifySender();

  @PostConstruct
  public void init() {
    this.getAnonymous().getServices().add("login");
    this.getAnonymous().getServices().add("bannerList");
    this.getAnonymous().getServices().add("appLatestVersion");
    this.getAnonymous().getServices().add("appCrashReport");
    this.getAnonymous().getServices().add("welcomeInfo");
    log.info("app匿名配置:{}", this.getAnonymous());
    Validators.assertJSR303(anonymous);
  }

  @Data
  public static class JPush {
    private boolean enable = true;
    private String gateway = "https://api.jpush.cn/v3/push";
    private String appKey;
    private String masterSecret;
    /** 离线消息保留时间,单位秒，默认1天(86400)，0不保存，最大10天 */
    private int timeToLive = 86400;

    private boolean apns = false;
  }

  @Data
  public static class Anonymous {
    /** 匿名accessKey */
    @NotBlank private String accessKey = "anonymous";
    /** 匿名secretKey */
    @NotBlank
    @Length(min = 16)
    private String secretKey = "anonymouanonymou";
    /** 匿名services */
    @NotNull private List<String> services = Lists.newArrayList();
  }

  @Data
  public static class NotifySender {
    /** 通知链接超时时间，默认10S */
    private String connectionTimeout = "10000";
    /** 通知socket超时时间，默认10S */
    private String socketTimeout = "10000";
  }
}
