/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-28
 *
 */
package com.yiji.framework.openapi.service.persistent;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.framework.openapi.common.dto.ApiServiceDto;
import com.yiji.framework.openapi.domain.ApiPartner;
import com.yiji.framework.openapi.domain.ApiPartnerService;
import com.yiji.framework.openapi.domain.ApiService;
import com.yiji.framework.openapi.domain.ApiServiceType;
import com.yiji.framework.openapi.service.ApiPartnerServiceService;
import com.yiji.framework.openapi.service.ApiServiceService;
import com.yiji.framework.openapi.service.ApiServiceTypeService;
import com.yiji.framework.openapi.service.persistent.dao.ApiPartnerServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * api_partner_service Service实现
 * <p>
 * Date: 2016-07-28 15:33:41
 *
 * @author acooly
 */
@Service("apiPartnerServiceService")
public class ApiPartnerServiceServiceImpl extends EntityServiceImpl<ApiPartnerService, ApiPartnerServiceDao> implements ApiPartnerServiceService {

    private static final String DEFALUT_TYPE_NAME = "其他";

    @Resource
    private ApiServiceTypeService apiServiceTypeService;
    @Resource
    private ApiServiceService apiServiceService;
    @Resource
    private com.yiji.framework.openapi.service.ApiPartnerService apiPartnerService;

    @Override
    public List<String> getAuthorizedServices(String partnerId) {
        return getEntityDao().getAuthorizedServices(partnerId);
    }

    @Override
    public boolean exsitService(List<Long> serviceIds) {
        return getEntityDao().countByApiserviceidIn(serviceIds) > 0;
    }

    @Transactional
    @Override
    public void batchSave(Long apiPartnerId, List<Long> serviceIds) {
        ApiPartner apiPartner = apiPartnerService.get(apiPartnerId);
        List<ApiPartnerService> allApis = getEntityDao().findByApipartnerid(apiPartnerId);
        List<Long> removeIds = Lists.newArrayList();
        for (ApiPartnerService apiPartnerService : allApis) {
            if (!serviceIds.contains(apiPartnerService.getApiserviceid())) {
                removeIds.add(apiPartnerService.getId());
            }
        }
        if (removeIds.size() > 0) {
            getEntityDao().removes(removeIds.toArray(new Long[]{}));
        }

        ApiPartnerService aps = null;
        ApiService apiService = null;
        List<ApiPartnerService> apss = Lists.newArrayList();
        for (Long apiserviceid : serviceIds) {
            aps = getEntityDao().findByApipartneridAndApiserviceid(apiPartnerId, apiserviceid);
            if (aps == null) {
                apiService = apiServiceService.get(apiserviceid);
                aps = new ApiPartnerService();
                aps.setApipartnerid(apiPartnerId);
                aps.setApiserviceid(apiserviceid);
                aps.setParnerName(apiPartner.getPartnerName());
                aps.setPartnerId(apiPartner.getPartnerId());
                aps.setServiceName(apiService.getName());
                aps.setServiceTitle(apiService.getTitle());
                aps.setServiceVersion(apiService.getVersion());
                apss.add(aps);
            }
        }
        saves(apss);
    }


    @Override
    public Map<String, List<ApiServiceDto>> searchServiceByPartner(Long id) {

        try {
            List<ApiServiceType> topTypes = apiServiceTypeService.loadTopTypes();
            List<ApiServiceDto> services = getEntityDao().queryPartnerService(id);
            Map<String, List<ApiServiceDto>> map = Maps.newLinkedHashMap();
            map.put(DEFALUT_TYPE_NAME, new ArrayList<ApiServiceDto>());
            String topPath = null;
            ApiServiceType topType = null;
            for (ApiServiceDto apiService : services) {
                topPath = Strings.substring(apiService.getPath(), 0, 3);
                topType = getTypeByPath(topTypes, topPath);
                if (topType == null) {
                    map.get(DEFALUT_TYPE_NAME).add(apiService);
                    continue;
                }

                if (map.get(topType.getName()) == null) {
                    map.put(topType.getName(), new ArrayList<ApiServiceDto>());
                }
                map.get(topType.getName()).add(apiService);
            }
            return map;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ApiServiceType getTypeByPath(List<ApiServiceType> topTypes, String path) {
        for (ApiServiceType type : topTypes) {
            if (Strings.equals(type.getPath(), path)) {
                return type;
            }
        }
        return null;
    }
}
