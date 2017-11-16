package com.acooly.openapi.framework.app.biz.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 手机客户端版本 Entity
 *
 * <p>Date: 2014-10-25 23:16:14
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "app_version")
@Getter
@Setter
public class AppVersion extends AbstractEntity {
  public static final String DEVICE_TYPE_ANDROID = "android";
  public static final String DEVICE_TYPE_IPHONE = "iphone";

  @Column(
    name = "app_code",
    nullable = false,
    length = 32,
    columnDefinition = "varchar(32) not null COMMENT '应用编码'"
  )
  private String appCode;

  @Column(
    name = "app_name",
    nullable = false,
    length = 32,
    columnDefinition = "varchar(32) not null COMMENT '应用名称'"
  )
  private String appName;

  /** 设备类型 */
  @Column(
    name = "device_type",
    nullable = false,
    length = 32,
    columnDefinition = "varchar(32) not null COMMENT '设备类型{android:android,iphone:iphone}'"
  )
  private String deviceType;

  /** 版本号 如：1.0.0, 用于显示 */
  @Column(
    name = "version_name",
    nullable = false,
    length = 16,
    columnDefinition = "varchar(16) not null COMMENT '版本号(如：1.0.0, 用于显示)'"
  )
  private String versionName;

  /** 版本编码 通过这个的最大值判断最新版本 */
  @Column(
    name = "version_code",
    nullable = false,
    columnDefinition = "int not null COMMENT '版本编码(通过这个的最大值判断最新版本)'"
  )
  private int versionCode = 0;

  /** 版本说明 */
  @Column(
    name = "subject",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '版本说明'"
  )
  private String subject;

  /** 真实下载URL，全URL */
  @Column(
    name = "url",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '下载URL'"
  )
  private String url;

  /** 物理路径 */
  @Column(
    name = "path",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '物理路径'"
  )
  private String path;

  /** 苹果安装地址 */
  @Column(
    name = "apple_url",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '苹果安装地址'"
  )
  private String appleUrl;

  /** 发布时间 */
  @Column(
    name = "pub_time",
    nullable = false,
    columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '发布时间'"
  )
  private Date pubTime = new Date();

  /** 是否强制更新 0：否，1：是 */
  @Column(
    name = "force_update",
    nullable = false,
    columnDefinition = "int not null COMMENT '是否强制更新{0:否,1:是}'"
  )
  private int forceUpdate = 0;

  /** 备注 */
  @Column(
    name = "comments",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '备注'"
  )
  private String comments;
}
