/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-29 14:29 创建
 */
package com.acooly.openapi.framework.common.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

/** @author acooly */
@Getter
@Setter
public class ApiServiceDto extends InfoBase {
  private Long id;
  private String title;
  private String name;
  private String version;
  private String path;
}
