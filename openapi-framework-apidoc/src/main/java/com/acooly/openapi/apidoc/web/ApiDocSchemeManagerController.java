/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-19
 */
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceBusiTypeEnum;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceServiceTypeEnum;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 服务方案 管理控制器
 *
 * @author zhike
 * Date: 2019-02-19 10:24:17
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/apidoc/apiDocScheme")
public class ApiDocSchemeManagerController extends AbstractJQueryEntityController<ApiDocScheme, ApiDocSchemeService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        super.referenceData(request, model);
        String category = Servlets.getParameter(request, "category");
        request.setAttribute("category", category);
        model.put("allStatuss", DocStatusEnum.mapping());
        model.put("allServiceTypes", ApiDocServiceServiceTypeEnum.mapping());
        model.put("allBusiTypes", ApiDocServiceBusiTypeEnum.mapping());
    }

    @RequestMapping("/settingService")
    public String settingService(long id, Model model) {
        ApiDocScheme apiScheme = apiDocSchemeService.get(id);
        model.addAttribute("id", id);
        model.addAttribute("schemeNo", apiScheme.getSchemeNo());
        return "/manage/apidoc/schemeServiceEdit";
    }

    @Override
    public JsonListResult<ApiDocScheme> listJson(HttpServletRequest request, HttpServletResponse response) {
        return super.listJson(request, response);
    }

    /**
     * 获取单个解决方案详细信息
     *
     * @param id 解决方案主键
     * @return
     */
    @RequestMapping("getSignSchemeInfo")
    @ResponseBody
    public JSONObject getSignSchemeInfo(long id) {
        JSONObject reJsonObject = new JSONObject();
        boolean success = true;
        String message = "加载解决方案成功";
        try {
            ApiDocScheme apiScheme = apiDocSchemeService.get(id);
            JSONObject data = new JSONObject();
            data.put("schemeNo", apiScheme.getSchemeNo());
            data.put("name", apiScheme.getTitle());
            data.put("note", apiScheme.getNote());
            data.put("schemeType", apiScheme.getSchemeType());
            apiScheme.setServices(apiDocServiceService.findServicesBySchemeNo(apiScheme.getSchemeNo()));
            JSONArray serviceArray = new JSONArray();
            for (ApiDocService apiServiceDoc : apiScheme.getServices()) {
                JSONObject serviceObj = new JSONObject();
                serviceObj.put("serviceTitle", apiServiceDoc.getTitle());
                serviceObj.put("serviceNo", apiServiceDoc.getServiceNo());
                serviceArray.add(serviceObj);
            }
            data.put("services", serviceArray);
            reJsonObject.put("data", data);

        } catch (Exception e) {
            success = false;
            message = "加载解决方案失败";
            log.error("加载解决方案,异常信息:", e);
        }

        reJsonObject.put("success", success);
        reJsonObject.put("message", message);
        return reJsonObject;
    }


    /**
     * 获取当前解决方案服务列表以及以外的服务列表
     *
     * @param id
     * @return
     */
    @RequestMapping("getSelectSchemeList")
    @ResponseBody
    public JSONObject getSelectSchemeList(long id) {
        boolean success = true;
        String message = "获取解决方案选择列表成功";
        JSONObject reJsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            ApiDocScheme apiScheme = apiDocSchemeService.get(id);
            data = apiDocSchemeService.getSelectSchemeList(apiScheme.getSchemeNo());
            reJsonObject.put("data", data);
            reJsonObject.put("schemeNo", apiScheme.getSchemeNo());
        } catch (Exception e) {
            success = false;
            message = "获取解决方案选择列表出错";
            log.error("获取解决方案选择列表出错,异常信息:", e);
        }
        reJsonObject.put("success", success);
        reJsonObject.put("message", message);
        return reJsonObject;
    }

    /**
     * 为解决方案添加服务
     *
     * @param serviceNos
     * @param schemeNo
     * @return
     */
    @RequestMapping("addServicesToScheme")
    @ResponseBody
    public JSONObject addServicesToScheme(String[] serviceNos, String schemeNo) {
        boolean success = true;
        String message = "添加服务成功";
        JSONObject reJsonObject = new JSONObject();
        try {
            if (serviceNos != null && serviceNos.length > 0 && Strings.isNotBlank(schemeNo)) {
                for (String serviceNo : serviceNos) {
                    com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService apiSchemeServiceDoc = new com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService();
                    apiSchemeServiceDoc.setServiceNo(serviceNo);
                    apiSchemeServiceDoc.setSchemeNo(schemeNo);
                    apiSchemeServiceDoc.setSortTime(System.currentTimeMillis());
                    apiDocSchemeServiceService.save(apiSchemeServiceDoc);
                }
            } else {
                success = false;
                message = "服务码和解决方案ID不能为空";
                log.info("服务码和解决方案ID不能为空");
            }
        } catch (Exception e) {
            success = false;
            message = "添加服务出错";
            log.error("添加服务出错,异常信息:", e);
        }
        reJsonObject.put("success", success);
        reJsonObject.put("message", message);
        return reJsonObject;
    }

    /**
     * 移除当前解决方案的服务列表
     *
     * @param serviceNos
     * @param schemeNo
     * @return
     */
    @RequestMapping("deleteServicesToScheme")
    @ResponseBody
    public JSONObject deleteServicesToScheme(String[] serviceNos, String schemeNo) {
        boolean success = true;
        String message = "删除服务成功";
        JSONObject reJsonObject = new JSONObject();
        try {
            if (serviceNos != null && serviceNos.length > 0 && Strings.isNotBlank(schemeNo)) {
                for (String serviceNo : serviceNos) {
                    apiDocSchemeServiceService.deleteSchemeService(schemeNo, serviceNo);
                }
            } else {
                success = false;
                message = "服务码和解决方案ID不能为空";
                log.info("服务码和解决方案ID不能为空");
            }
        } catch (Exception e) {
            success = false;
            message = "删除服务出错";
            log.error("删除服务出错,异常信息:", e);
        }
        reJsonObject.put("success", success);
        reJsonObject.put("message", message);
        return reJsonObject;
    }

//    @Override
//    protected ApiDocScheme doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate) throws Exception {
//        ApiDocScheme entity = loadEntity(request);
//        if (entity == null) {
//            // create
//            allow(request, response, MappingMethod.create);
//            entity = getEntityClass().newInstance();
//            entity.setSchemeNo("build-" + System.currentTimeMillis());
//            entity.setSortTime(System.currentTimeMillis());
//        } else {
//            // update
//            allow(request, response, MappingMethod.update);
//        }
//        doDataBinding(request, entity);
//        onSave(request, response, model, entity, isCreate);
//        // 这里服务层默认是根据entity的Id是否为空自动判断是SAVE还是UPDATE.
//        if (isCreate) {
//            getEntityService().save(entity);
//        } else {
//            getEntityService().update(entity);
//        }
//        //保存或更新方案描述
//        createOrUpdateDesc(request, entity.getSchemeNo());
//        return entity;
//    }
//
//    /**
//     * 更新或保存方案描述
//     *
//     * @param request
//     */
//    private void createOrUpdateDesc(HttpServletRequest request, String schemeNo) {
//        String schemeDesc = Servlets.getParameter(request, "schemeDesc");
//        ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.findBySchemeNo(schemeNo);
//        boolean isCreate = false;
//        if (apiDocSchemeDesc == null) {
//            apiDocSchemeDesc = new ApiDocSchemeDesc();
//            isCreate = true;
//        }
//        apiDocSchemeDesc.setSchemeDesc(schemeDesc);
//        apiDocSchemeDesc.setSchemeNo(schemeNo);
//        if (isCreate) {
//            apiDocSchemeDescService.save(apiDocSchemeDesc);
//        } else {
//            apiDocSchemeDescService.update(apiDocSchemeDesc);
//        }
//    }

//    @Override
//    public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
//        allow(request, response, MappingMethod.update);
//        try {
//            model.addAllAttributes(referenceData(request));
//            ApiDocScheme entity = loadEntity(request);
//            ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.findBySchemeNo(entity.getSchemeNo());
//            entity.setApiDocschemeDesc(apiDocSchemeDesc);
//            model.addAttribute("action", ACTION_EDIT);
//            model.addAttribute(getEntityName(), entity);
//            onEdit(request, response, model, entity);
//        } catch (Exception e) {
//            log.warn(getExceptionMessage("edit", e), e);
//            handleException("编辑", e, request);
//        }
//        return getEditView();
//    }

    /**
     * 解决方案置顶
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "moveTop")
    @ResponseBody
    public JsonResult moveTop(HttpServletRequest request, HttpServletResponse response) {

        JsonResult result = new JsonResult();
        try {
            String id = request.getParameter("id");
            this.getEntityService().moveTop(Long.valueOf(id));
            result.setMessage("置底成功");
        } catch (Exception e) {
            handleException(result, "置底", e);
        }
        return result;
    }

    /**
     * 上移解决方案
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "moveUp")
    @ResponseBody
    public JsonResult moveUp(HttpServletRequest request, HttpServletResponse response) {

        JsonResult result = new JsonResult();
        try {
            String id = request.getParameter("id");
            this.getEntityService().moveUp(Long.valueOf(id));
            result.setMessage("下移成功");
        } catch (Exception e) {
            handleException(result, "下移", e);
        }
        return result;
    }

    /**
     * 移动解决方案
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "changeSchemaServiceOrder")
    @ResponseBody
    public JsonResult moveDownService(HttpServletRequest request, HttpServletResponse response,Long id, String direction) {

        JsonResult result = new JsonResult();
        try {
            if(Strings.equals(direction,"down")) {
                apiDocSchemeServiceService.moveDown(id);
            }else if (Strings.equals(direction,"up")){
                apiDocSchemeServiceService.moveUp(id);
            }else{
                apiDocSchemeServiceService.moveTop(id);
            }
            result.setMessage("操作成功");
        } catch (Exception e) {
            handleException(result, "操作", e);
        }
        return result;
    }

    /**
     * 删除关联关系
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "removeSchemaService")
    @ResponseBody
    public JsonResult removeSchemaService(HttpServletRequest request, HttpServletResponse response,Long id) {

        JsonResult result = new JsonResult();
        try {
            apiDocSchemeServiceService.remove(id);
            result.setMessage("操作成功");
        } catch (Exception e) {
            handleException(result, "操作", e);
        }
        return result;
    }

    @Override
    protected PageInfo<ApiDocScheme> doList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Boolean> sortMap = Maps.newHashMap();
        sortMap.put("sortTime", false);
        return getEntityService()
                .query(getPageInfo(request), getSearchParams(request), sortMap);
    }


    @RequestMapping("/move")
    @ResponseBody
    public JsonResult move(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult();
        String point = request.getParameter("point");
        String sourceId = request.getParameter("sourceId");
        String targetId = request.getParameter("targetId");
        try {
            apiDocSchemeService.move(sourceId, targetId, point);
        } catch (Exception e) {
            handleException(result, "移动异常", e);
        }
        return result;
    }

    @RequestMapping("/changeStatus")
    @ResponseBody
    public JsonResult changeStatus(HttpServletRequest request, HttpServletResponse response, Model model, Long id, String status) {
        JsonResult result = new JsonResult();
        try {
            ApiDocScheme apiDocScheme = apiDocSchemeService.get(id);
            apiDocScheme.setStatus(DocStatusEnum.find(status));
            apiDocSchemeService.update(apiDocScheme);
        } catch (Exception e) {
            handleException(result, "移动异常", e);
        }
        return result;
    }


    @RequestMapping(value = "queryTree")
    @ResponseBody
    public JsonListResult<ApiDocScheme> queryTree(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocScheme> result = new JsonListResult<>();
        try {
            result.appendData(referenceData(request));
            Long parentId = Servlets.getLongParameter(request, "id");
            String category = Servlets.getParameter(request, "category");
            List<ApiDocScheme> entities = apiDocSchemeService.level(parentId, category, null);
            result.setTotal((long) entities.size());
            result.setRows(entities);
        } catch (Exception e) {
            handleException(result, "列表查询", e);
        }
        return result;
    }

    @RequestMapping(value = "loadTree")
    @ResponseBody
    public JsonListResult<ApiDocScheme> loadTree(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocScheme> result = new JsonListResult<>();
        try {
            String category = Servlets.getParameter(request, "category");
            Long parentId = Servlets.getLongParameter(request, "parentId");
            result.appendData(referenceData(request));
            List<ApiDocScheme> entities = Strings.isBlank(category) ? apiDocSchemeService.tree(parentId, null) :
                    apiDocSchemeService.tree(category, parentId, null, null);
            result.setTotal((long) entities.size());
            result.setRows(entities);
        } catch (Exception e) {
            handleException(result, "加载数据树", e);
        }
        return result;
    }

    @Override
    protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        Long parentId = Servlets.getLongParameter(request, "parentId");
        String category = Servlets.getParameter(request, "category");
        model.addAttribute("category", category);
        model.addAttribute("parent", getParent(parentId));
        super.onCreate(request, response, model);
    }

    @Override
    protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ApiDocScheme entity) {
        if (!ApiDocScheme.TOP_PARENT_ID.equals(entity.getParentId())) {
            model.addAttribute("parent", getParent(entity.getParentId()));
        }
        String category = Servlets.getParameter(request, "category");
        model.addAttribute("category", category);
        super.onEdit(request, response, model, entity);
    }

    private ApiDocScheme getParent(Long parentId) {
        if (parentId != null) {
            return apiDocSchemeService.get(parentId);
        } else {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping("/schemeServiceList")
    private JsonListResult<ApiDocService> schemeServiceList(String schemeNo, HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocService> result = new JsonListResult<>();
        try {
            result.appendData(referenceData(request));
            List<ApiDocService> apiDocServices = apiDocSchemeServiceService.findSchemeApiDocServices(schemeNo);
            result.setRows(apiDocServices);
            result.setMessage("查询服务信息成功");
        } catch (Exception var5) {
            this.handleException(result, "查询", var5);
        }
        return result;
    }
}
