package com.acooly.openapi.framework.app.biz.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * app_start_guide Entity
 *
 * <p>Date: 2015-05-22 14:44:16
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "app_start_guide")
@Getter
@Setter
public class AppStartGuide extends AbstractEntity {
  /** 排序（大小顺序） */
  @Column(
    name = "sort_order",
    nullable = false,
    columnDefinition = "bigint not null COMMENT '排序（大小顺序）'"
  )
  private long sortOrder = 1l;
  /** 默认图片 */
  @Column(
    name = "image_default",
    nullable = false,
    length = 255,
    columnDefinition = "varchar(255) not null COMMENT '默认图片'"
  )
  private String imageDefault;
  /** IPHONE4 */
  @Column(
    name = "image_iphone4",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT 'IPHONE4图片'"
  )
  private String imageIphone4;
  /** iphone5/6: 1242*2208 */
  @Column(
    name = "image_iphone6",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT 'iphone5/6图片'"
  )
  private String imageIphone6;
  /** android: 1080 * 1920 */
  @Column(
    name = "image_android",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT 'android图片'"
  )
  private String imageAndroid;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(
    name = "status",
    nullable = false,
    length = 16,
    columnDefinition = "varchar(16) not null COMMENT '状态'"
  )
  private EntityStatus status = EntityStatus.Enable;

  /** 备注 */
  @Column(
    name = "comments",
    nullable = true,
    length = 255,
    columnDefinition = "varchar(255) COMMENT '备注'"
  )
  private String comments;
}
