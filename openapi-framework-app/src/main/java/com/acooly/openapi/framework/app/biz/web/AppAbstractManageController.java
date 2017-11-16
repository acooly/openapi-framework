/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-24 04:42 创建
 */
package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.utils.Strings;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.openapi.framework.app.AppProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/** @author acooly */
public abstract class AppAbstractManageController<T extends Entityable, M extends EntityService<T>>
    extends AbstractJQueryEntityController<T, M> {

  @Autowired protected OFileProperties oFileProperties;

  @Autowired protected AppProperties appProperties;

  /**
   * 获取APP模块存储路径
   *
   * @return
   */
  protected String getStorageRoot() {
    String home = oFileProperties.getStorageRoot() + File.separator + getAppStorageRoot();
    File file = new File(home);
    if (!file.exists()) {
      file.mkdirs();
    }
    return home;
  }

  @Override
  protected UploadConfig getUploadConfig() {
    UploadConfig config = super.getUploadConfig();
    String storageRoot = getStorageRoot();
    config.setStorageRoot(storageRoot);
    config.setUseMemery(false);
    config.setAllowExtentions("jpg,gif,png");
    return config;
  }

  /**
   * APP模块的存储相对路径
   *
   * @return
   */
  protected String getAppStorageRoot() {
    return appProperties.getStoragePath();
  }

  protected String getDatabasePath(UploadResult uploadResult) {
    String filePath = uploadResult.getFile().getPath();
    filePath = Strings.replace(filePath, "\\", "/");
    String rootPath = new File(oFileProperties.getStorageRoot()).getPath();
    String relativePath = StringUtils.substringAfter(filePath, rootPath);
    if (!Strings.startsWith(relativePath, "/")) {
      relativePath = "/" + relativePath;
    }
    return relativePath;
  }
}
