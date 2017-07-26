package com.acooly.openapi.framework.manage.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.framework.domain.OrderInfo;
import com.acooly.openapi.framework.service.OrderInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/manage/openapi/orderInfo")
public class OrderInfoManagerController extends AbstractJQueryEntityController {

    {
        allowMapping = "list,query";
    }

    @Resource
    private OrderInfoService orderInfoService;

    @Override
    protected PageInfo<OrderInfo> doList(HttpServletRequest request, HttpServletResponse response, Model model)
            throws Exception {
        return orderInfoService.query(getPageInfo(request), getSearchParams(request), getSortMap(request));
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
