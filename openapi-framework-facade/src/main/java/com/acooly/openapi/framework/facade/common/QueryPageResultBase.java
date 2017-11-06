/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-11-10 01:33 创建
 */
package com.acooly.openapi.framework.facade.common;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.ResultBase;

/** @author acooly */
public class QueryPageResultBase<T> extends ResultBase {

  /** 分页对象 */
  private PageInfo<T> pageInfo = new PageInfo<>(1, 10);

  public PageInfo<T> getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo<T> pageInfo) {
    this.pageInfo = pageInfo;
  }
}
