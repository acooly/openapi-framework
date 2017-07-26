/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service.persistent;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.domain.ApiPartner;
import com.acooly.openapi.framework.service.ApiPartnerService;
import com.acooly.openapi.framework.service.persistent.dao.ApiPartnerDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 合作方管理 Service实现
 * <p>
 * Date: 2016-07-16 02:05:01
 *
 * @author acooly
 */
@Service("apiPartnerService")
public class ApiPartnerServiceImpl extends EntityServiceImpl<ApiPartner, ApiPartnerDao> implements ApiPartnerService {

    @Override
    public void save(ApiPartner o) throws BusinessException {
        //校验商户ID是否重复
        ApiPartner apiPartner;
        if (o.getId() == null) {
             apiPartner = this.getEntityDao().queryByPartnerId(o.getPartnerId());
        } else {
             apiPartner = this.getEntityDao().queryExceptIdByPartnerId(o.getId(), o.getPartnerId());
        }
        if (apiPartner != null) {
            throw new RuntimeException("合作方编码已经存在...");
        }
        super.save(o);
    }

    @Override
    public String generatePartnerid() {
        return Ids.getDid();
    }

    @Override
    public String getPartnerSercretKey(String partnerId) {
        return getEntityDao().getPartnerSercretKey(partnerId);
    }

    @Override
    public String generateDigestSecurityKey(SignType signType) {
        if (signType == null) {
            signType = SignType.MD5;
        }
        if (signType == SignType.MD5) {
            // 32字节长度
            return DigestUtils.md5Hex(Dates.format(new Date()) + RandomStringUtils.randomAscii(5));
        } else {
            throw new UnsupportedOperationException("不支持的signType:" + signType);
        }
    }
}
