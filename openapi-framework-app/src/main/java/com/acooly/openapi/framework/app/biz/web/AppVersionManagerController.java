package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;
import com.acooly.openapi.framework.app.biz.service.AppVersionService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

import static com.acooly.core.utils.Servlets.getHost;

@Controller
@RequestMapping(value = "/manage/module/app/appVersion")
public class AppVersionManagerController
        extends AppAbstractManageController<AppVersion, AppVersionService> {

    private static Map<String, String> allForceUpdates = Maps.newLinkedHashMap();
    private static Map<String, String> allDeviceTypes = Maps.newLinkedHashMap();

    static {
        allForceUpdates.put("0", "否");
        allForceUpdates.put("1", "是");
    }

    static {
        allDeviceTypes.put(AppVersion.DEVICE_TYPE_ANDROID, "android");
        allDeviceTypes.put(AppVersion.DEVICE_TYPE_IPHONE, "iphone");
    }

    @Autowired
    private AppVersionService appVersionService;

    @Override
    protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
        Map<String, Boolean> sortMap = Maps.newHashMap();
        sortMap.put("appCode", true);
        sortMap.put("deviceType", true);
        sortMap.put("versionCode", false);
        return sortMap;
    }

    @Override
    protected AppVersion onSave(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            AppVersion entity,
            boolean isCreate)
            throws Exception {

        String storageRoot = getStorageRoot();
        getUploadConfig().setStorageRoot(storageRoot);
        getUploadConfig().setUseMemery(false);
        getUploadConfig().setNeedRemaneToTimestamp(false);
        getUploadConfig().setNeedTimePartPath(false);
        getUploadConfig().setAllowExtentions("apk,APK,ipa,IPA");
        try {
            Map<String, UploadResult> uploadResults = doUpload(request);
            String fileName = null;
            String url = null;
            if (uploadResults != null && !uploadResults.isEmpty()) {
                Map.Entry<String, UploadResult> entry = uploadResults.entrySet().iterator().next();
                entity.setPath(entry.getValue().getFile().getPath());
                fileName = entry.getValue().getFile().getName();
                url = getUrl(request, fileName);
                entity.setUrl(url);
            }
        } catch (Exception e) {
            throw new RuntimeException("上传App程序发布失败:", e);
        }
        return super.onSave(request, response, model, entity, isCreate);
    }

    @Override
    protected UploadConfig getUploadConfig() {
        return super.uploadConfig;
    }


    protected String getUrl(HttpServletRequest request, String fileName) {
        String hostAndPath = null;
        if (Strings.containsIgnoreCase(oFileProperties.getServerRoot(), "http")) {
            // 媒体服务器配置了host
            hostAndPath = oFileProperties.getServerRoot();
        } else {
            hostAndPath = getHost(request) + oFileProperties.getServerRoot();
        }
        return hostAndPath + "/" + getAppStorageRoot() + "/" + fileName;
    }

    @Override
    protected void doRemove(
            HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
            throws Exception {
        if (ids == null || ids.length == 0) {
            return;
        }
        AppVersion version = loadEntity(request);
        if (StringUtils.isNotBlank(version.getPath())) {
            File file = new File(version.getPath());
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
            }
        }
        super.doRemove(request, response, model, ids);
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allForceUpdates", allForceUpdates);
        model.put("allDeviceTypes", allDeviceTypes);
        model.put("platformHost", getHost(request));
    }
}
