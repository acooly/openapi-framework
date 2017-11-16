/** create by zhangpu date:2015年5月30日 */
package com.acooly.openapi.framework.app.biz.dao.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.jpa.JapDynamicQueryDao;
import com.acooly.openapi.framework.app.biz.dao.AppMessageDaoCustom;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;

/** @author zhangpu */
public class AppMessageDaoImpl extends JapDynamicQueryDao<AppMessage>
    implements AppMessageDaoCustom {

  @Override
  public PageInfo<AppMessage> pageQueryUserMessages(
      PageInfo<AppMessage> pageInfo, String userName) {
    String ql =
        "from AppMessage where type='broadcast' or receivers like '%"
            + userName
            + "%' order by id desc";
    return pagedQueryByJpql(pageInfo, ql);
  }
}
