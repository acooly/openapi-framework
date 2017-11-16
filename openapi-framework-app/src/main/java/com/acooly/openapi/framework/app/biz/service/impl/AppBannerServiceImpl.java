package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppBannerDao;
import com.acooly.openapi.framework.app.biz.domain.AppBanner;
import com.acooly.openapi.framework.app.biz.service.AppBannerService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Service("appBannerService")
public class AppBannerServiceImpl extends EntityServiceImpl<AppBanner, AppBannerDao>
        implements AppBannerService {

    @Override
    public void moveUp(Serializable id) {
        try {
            AppBanner appBanner = get(id);
            Date current = appBanner.getSortTime();

            AppBanner perv = null;

            PageInfo<AppBanner> pageInfo = new PageInfo<AppBanner>(1, (int) 1);
            Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
            sortMap.put("sortTime", true);
            Map<String, Object> searchMap = Maps.newHashMap();
            searchMap.put("GT_sortTime",current);
            pageInfo = getEntityDao().query(pageInfo, searchMap, sortMap);
            if (pageInfo.getPageResults() != null && pageInfo.getPageResults().size() > 0) {
                perv = pageInfo.getPageResults().get(0);
            }

            if (perv != null) {
                appBanner.setSortTime(perv.getSortTime());
                perv.setSortTime(current);
                save(perv);
            } else {
                appBanner.setSortTime(new Date());
            }
            save(appBanner);
        } catch (Exception e) {
            throw new BusinessException("上移失败", e);
        }


    }

    @Override
    public void moveTop(Serializable id) {
        AppBanner appBanner = get(id);
        try {
            appBanner.setSortTime(new Date());
            save(appBanner);
        } catch (Exception e) {
            throw new BusinessException("置顶失败", e);
        }
    }
}
