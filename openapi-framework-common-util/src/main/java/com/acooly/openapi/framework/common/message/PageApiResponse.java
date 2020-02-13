package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.PageResult;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 分页响应报文
 *
 * @author zhangpu
 */
@Getter
@Setter
public abstract class PageApiResponse<T> extends ApiResponse {

    @NotNull
    @OpenApiField(desc = "总行数", demo = "200", ordinal = 1)
    private long totalRows = 0;

    @NotNull
    @OpenApiField(desc = "总页数", constraint = "总行数/页大小", demo = "10", ordinal = 2)
    private long totalPages = 0;

    @OpenApiField(desc = "页数据", constraint = "当totalRows大于0时，rows不为空", ordinal = 3)
    private List<T> rows = Lists.newArrayList();

    public void append(T t) {
        rows.add(t);
    }

    public <U> void setPageResult(PageResult<U> pageResult) {
        this.setPageResult(pageResult, null);
    }

    public <U> void setPageResult(PageResult<U> pageResult, BiConsumer<U, T> consumer) {
        Class<?> responseClass = Reflections.getSuperClassGenricType(getClass());
        PageInfo<U> pageInfo = pageResult.getDto();
        List<T> rows = Lists.newArrayList();
        for (U dto : pageInfo.getPageResults()) {
            T o = BeanCopier.copy(dto, (Class<T>) responseClass);
            if (consumer != null) {
                consumer.accept(dto, o);
            }
            rows.add(o);
        }
        this.setTotalPages(pageInfo.getTotalPage());
        this.setTotalRows(pageInfo.getTotalCount());
        this.setRows(rows);
    }
}