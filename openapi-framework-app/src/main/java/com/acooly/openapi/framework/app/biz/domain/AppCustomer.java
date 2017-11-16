package com.acooly.openapi.framework.app.biz.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.app.biz.enums.DeviceType;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * app_customer Entity
 *
 * <p>Date: 2015-05-12 13:39:30
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "app_customer")
@Getter
@Setter
public class AppCustomer extends AbstractEntity {

  /** 用户名 */
  @Column(
    name = "user_name",
    nullable = false,
    length = 16,
    columnDefinition = "varchar(16) not null COMMENT '用户名'"
  )
  private String userName;

  /** 访问码 */
  @Column(
    name = "access_key",
    nullable = false,
    length = 64,
    columnDefinition = "varchar(64) not null COMMENT '访问码'"
  )
  private String accessKey;

  /** 安全码 */
  @Column(
    name = "secret_key",
    nullable = false,
    length = 64,
    columnDefinition = "varchar(64) not null COMMENT '安全码'"
  )
  private String secretKey;

  /** 设备类型 */
  @Enumerated(EnumType.STRING)
  @Column(
    name = "device_type",
    nullable = true,
    length = 16,
    columnDefinition = "varchar(16) COMMENT '设备类型'"
  )
  private DeviceType deviceType;

  /** 设备型号 */
  @Column(
    name = "device_model",
    nullable = true,
    length = 64,
    columnDefinition = "varchar(64) COMMENT '设备型号'"
  )
  private String deviceModel;

  /** 设备标识 */
  @Column(
    name = "device_id",
    nullable = false,
    length = 64,
    columnDefinition = "varchar(64) not null COMMENT '设备标识'"
  )
  private String deviceId;

  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(
    name = "status",
    nullable = true,
    length = 16,
    columnDefinition = "varchar(16) COMMENT '状态'"
  )
  private EntityStatus status;
}
