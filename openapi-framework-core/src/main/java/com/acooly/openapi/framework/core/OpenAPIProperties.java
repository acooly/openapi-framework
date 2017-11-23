/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.openapi.framework.core;

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

import static com.acooly.openapi.framework.core.OpenAPIProperties.PREFIX;

/** @author qiubo@yiji.com */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class OpenAPIProperties {
  public static final String PREFIX = "acooly.openapi";

  /** 查询日志分离到不同的日志文件 */
  private Boolean queryLogSeparationEnable = false;

  /** 是否启用openapi性能日志 */
  private Boolean enablePerfLog = true;

  /** 是否在api中存储非查询类请求 */
  private Boolean saveOrder = true;
  /** 匿名访问 */
  private Anonymous anonymous = new Anonymous();
  /** 登录 */
  private Login login = new Login();

  @PostConstruct
  public void init() {
    this.getAnonymous().getServices().add("login");
    this.getAnonymous().getServices().add("bannerList");
    this.getAnonymous().getServices().add("appLatestVersion");
    this.getAnonymous().getServices().add("appCrashReport");
    this.getAnonymous().getServices().add("welcomeInfo");
    log.info("匿名访问服务配置:{}", this.getAnonymous());
    Validators.assertJSR303(anonymous);
  }

  @Data
  public static class Anonymous {

    private boolean enable = true;
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
  public static class Login {
    boolean enable = false;
    /** 设备绑定验证 */
    private boolean deviceIdCheck = false;
    /** 每次登陆动态生成秘钥，false表示登录后生成用户秘钥后不再改变 */
    private boolean secretKeyDynamic = false;
  }
}
