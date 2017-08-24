package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.PageOrder;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页请求报文
 *
 * @author zhangpu
 */
@Getter
@Setter
public abstract class PageApiRequest extends ApiRequest {

  @Min(1)
  @OpenApiField(
    desc = "分页页号",
    constraint = "默认为：1，表示获取那页数据，页号默认为1，调用端可以根据第一次返回信息中的pageSize字段计算下次访问的页号。"
  )
  private int start = 1;

  @Min(1)
  @Max(100)
  @OpenApiField(desc = "分页大小", constraint = "默认为：20，表示返回的一页数据条数")
  private int limit = 20;

  public <T extends OrderBase> T toOrder(Class<T> clazz) {
    T order = super.toOrder(clazz);
    if (PageOrder.class.isAssignableFrom(clazz)) {
      ((PageOrder) order).setPageInfo(new PageInfo<T>(this.getLimit(), this.getStart()));
    }
    return order;
  }
}
