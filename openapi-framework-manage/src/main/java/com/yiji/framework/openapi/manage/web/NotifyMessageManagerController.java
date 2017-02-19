package com.yiji.framework.openapi.manage.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.yiji.framework.openapi.common.enums.TaskStatus;
import com.yiji.framework.openapi.domain.NotifyMessage;
import com.yiji.framework.openapi.service.NotifyMessageService;

@Controller
@RequestMapping(value = "/manage/openapi/notifyMessage")
public class NotifyMessageManagerController extends AbstractJQueryEntityController {

    {
        allowMapping = "query,list,update";
    }

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Override
    protected PageInfo<NotifyMessage> doList(HttpServletRequest request, HttpServletResponse response, Model model)
            throws Exception {
        return notifyMessageService.query(getPageInfo(request), getSearchParams(request), getSortMap(request));
    }


    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.index(request, response, model);
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
        allow(request, response, MappingMethod.update);
        try {
            model.addAllAttributes(referenceData(request));
            String id = request.getParameter("id");
            NotifyMessage notifyMessage = notifyMessageService.get(Long.valueOf(id));
            model.addAttribute("action", ACTION_EDIT);
            model.addAttribute("notifyMessage", notifyMessage);
        } catch (Exception e) {
            handleException("编辑", e, request);
        }
        return getEditView();
    }

    @Override
    public JsonEntityResult updateJson(HttpServletRequest request, HttpServletResponse response) {
        allow(request, response, MappingMethod.update);
        JsonEntityResult<NotifyMessage> result = new JsonEntityResult<NotifyMessage>();
        try {
            String id = request.getParameter("id");
            NotifyMessage notifyMessage = notifyMessageService.get(Long.valueOf(id));
            doDataBinding(request, notifyMessage);
            notifyMessageService.updateForManage(notifyMessage);
            result.setEntity(notifyMessage);
            result.setMessage("更新成功");
        } catch (Exception e) {
            handleException(result, "更新", e);
        }
        return result;
    }


    @Override
    protected void referenceData(HttpServletRequest request, Map model) {
        model.put("allStatus", TaskStatus.mapping());
    }
}
