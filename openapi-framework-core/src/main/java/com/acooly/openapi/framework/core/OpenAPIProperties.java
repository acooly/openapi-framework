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
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.acooly.openapi.framework.common.ApiConstants.ANONYMOUS_ACCESS_KEY;
import static com.acooly.openapi.framework.common.ApiConstants.ANONYMOUS_SECRET_KEY;
import static com.acooly.openapi.framework.core.OpenAPIProperties.PREFIX;

/**
 * OpenApi 全局配置文件
 *
 * @author qiubo@yiji.com
 * @author zhangpu
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class OpenAPIProperties {
    public static final String PREFIX = "acooly.openapi";

    /**
     * 是否在api中存储非查询类请求
     * 注意：如果开启，则默认查询类请求无法检测requestNo唯一性
     */
    private Boolean saveOrder = true;

    /**
     * 日志配置
     */
    private Log log = new Log();

    /**
     * 匿名访问
     */
    private Anonymous anonymous = new Anonymous();

    /**
     * 登录认证配置
     */
    private Login login = new Login();

    /**
     * 认证授权信息缓存配置
     */
    private AuthInfoCache authInfoCache = new AuthInfoCache();

    /**
     * 流控配置
     */
    private List<Rate> rates = Lists.newArrayList();

    /**
     * 权限配置
     */
    private Permi permi = new Permi();

    @PostConstruct
    public void init() {
        this.getAnonymous().getPermissions().put("openapicore", "*:login");
        this.getAnonymous().getPermissionSet().forEach(Permission::permMatch);
        Validators.assertJSR303(anonymous);
    }

    @Data
    public static class Anonymous {
        private boolean enable = true;
        /**
         * 匿名accessKey
         */
        @NotBlank
        private String accessKey = ANONYMOUS_ACCESS_KEY;
        /**
         * 匿名secretKey
         */
        @NotBlank
        @Length(min = 16)
        private String secretKey = ANONYMOUS_SECRET_KEY;
        /**
         * 匿名权限信息
         */
        private Map<String, String> permissions = Maps.newHashMap();

        public Set<String> getPermissionSet() {
            Set<String> set = Sets.newHashSet();
            permissions.values().forEach(s -> set.addAll(Splitter.on(",").trimResults().omitEmptyStrings().splitToList(s)));
            return set;
        }
    }

    @Data
    public static class Login {
        boolean enable = true;
        /**
         * 每次登陆动态生成秘钥，false表示登录后生成用户秘钥后不再改变
         */
        private boolean secretKeyDynamic = false;
    }


    @Data
    public static class AuthInfoCache {
        /**
         * 认证授权信息缓存是否开启
         */
        private boolean enable = true;

        /**
         * 一级本地缓存是否开始（注意：一级缓存为本地缓存，多节点时无法通过管理界面变更设置其他节点自动失效，只能通过到期时间）
         */
        private boolean levelOneEnable = true;

        /**
         * 一级缓存过期时间,单位：ms,默认10分钟
         */
        private int levelOneTimeout = 10 * 60 * 1000;

        /**
         * 二级缓存信息过期时间,单位：ms,默认10分钟（二级缓存开启收enable控制）
         */
        private int defaultTimeout = 10 * 60 * 1000;
    }

    @Data
    public static class Rate {
        /**
         * 支持*
         */
        private String partnerId;
        /**
         * 支持*
         */
        private String method;
        /**
         * 时间间隔，单位毫秒
         */
        private Integer interval = 1000;
        /**
         * 最大请求数
         */
        private Long maxRequests;

        public boolean acceptPartnerId(String partnerId) {
            if (this.partnerId.equals(ApiConstants.WILDCARD_TOKEN)) {
                return true;
            } else {
                return this.partnerId.equals(partnerId);
            }
        }

        public boolean acceptMethod(String method) {
            if (this.method.equals(ApiConstants.WILDCARD_TOKEN)) {
                return true;
            } else {
                return this.method.equals(method);
            }
        }

        public boolean allPartnerId() {
            return partnerId.equals(ApiConstants.WILDCARD_TOKEN);
        }

        public boolean allMethod() {
            return method.equals(ApiConstants.WILDCARD_TOKEN);
        }
    }

    @Data
    public static class Log {

        /**
         * 开始查询日志分离多个日志文件
         * 原: acooly.openapi.queryLogSeparationEnable
         */
        private Boolean multFileEnable = false;

        /**
         * 是否启用openapi性能日志
         * 原：acooly.openapi.enablePerfLog
         */
        private Boolean perfLogEnable = true;

        /**
         * 日志脱敏(默认关闭)
         */
        private Boolean safetyEnable = false;

        private String safetyIgnores;

        private String safetyMasks;
    }


    /**
     * 权限参数配置
     */
    @Data
    public static class Permi {

        /**
         * 是否开启权限脚本配置
         * 形如: *:*, accesKey:query*
         */
        Boolean scriptEnable = false;

    }


    /**
     * 查询日志分离到不同的日志文件
     */
    @Deprecated
    private Boolean queryLogSeparationEnable = false;

    /**
     * 是否启用openapi性能日志
     */
    @Deprecated
    private Boolean enablePerfLog = true;

    /**
     * 日志脱敏(默认关闭)
     */
    @Deprecated
    private Boolean logSafety = false;
    @Deprecated
    private String logSafetyIgnores;
    @Deprecated
    private String logSafetyMasks;

}
