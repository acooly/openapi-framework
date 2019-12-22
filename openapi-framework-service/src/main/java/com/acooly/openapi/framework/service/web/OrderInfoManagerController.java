package com.acooly.openapi.framework.service.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangpu
 */
@Controller
@RequestMapping(value = "/manage/openapi/orderInfo")
public class OrderInfoManagerController extends AbstractJsonEntityController {

    @Resource
    private OrderInfoService orderInfoService;

    {
        allowMapping = "list,query";
    }

    @Override
    protected PageInfo<OrderDto> doList(
            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        return orderInfoService.query(
                getPageInfo(request), getSearchParams(request), getSortMap(request));
    }

    @Override
    public int getDefaultPageSize() {
        return 20;
    }

    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.index(request, response, model);
    }
}
