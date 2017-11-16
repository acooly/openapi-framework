package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.domain.AppBanner;
import com.acooly.openapi.framework.app.biz.service.AppBannerService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/app/appBanner")
public class AppBannerManagerController
        extends AppAbstractManageController<AppBanner, AppBannerService> {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private AppBannerService appBannerService;

    @Override
    protected AppBanner onSave(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            AppBanner entity,
            boolean isCreate)
            throws Exception {
        Map<String, UploadResult> uploadResults = doUpload(request);
        if (uploadResults != null && !uploadResults.isEmpty()) {
            UploadResult uploadResult = uploadResults.get("bannerFile");
            if (uploadResult != null) {
                entity.setMediaUrl(getDatabasePath(uploadResult));
            }
        }
        entity.setUpdateTime(new Date());
        return entity;
    }

    @Override
    protected void doRemove(
            HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
            throws Exception {
        if (ids == null || ids.length == 0) {
            return;
        }
        AppBanner appBanner = loadEntity(request);
        removeFile(appBanner.getMediaUrl());
        super.doRemove(request, response, model, ids);
    }

    private void removeFile(String path) {
        File file = new File(getStorageRoot() + path);
        try {
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    @RequestMapping(value = "moveTop")
    @ResponseBody
    public JsonResult moveTop(HttpServletRequest request, HttpServletResponse response) {

        JsonResult result = new JsonResult();
        try {
            String id = request.getParameter(getEntityIdName());
            getEntityService().moveTop(Long.valueOf(id));
            result.setMessage("置顶成功");
        } catch (Exception e) {
            handleException(result, "置顶", e);
        }
        return result;
    }

    @RequestMapping(value = "moveUp")
    @ResponseBody
    public JsonResult moveUp(HttpServletRequest request, HttpServletResponse response) {

        JsonResult result = new JsonResult();
        try {
            String id = request.getParameter(getEntityIdName());
            getEntityService().moveUp(Long.valueOf(id));
            result.setMessage("上移成功");
        } catch (Exception e) {
            handleException(result, "上移", e);
        }
        return result;
    }

    @Override
    protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
        Map<String, Boolean> sortMap = Maps.newHashMap();
        sortMap.put("sortTime",false);
        return sortMap;
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("serverRoot", oFileProperties.getServerRoot());
    }
}
