package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liangsong
 * @date 2019-12-12 11:25
 */
@Data
public class ApiDocServiceDto implements Serializable {

    private String id;

    /**
     * 服务编号
     */
    private String serviceNo;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务版本
     */
    private String version = ApiConstants.VERSION_DEFAULT;

    /**
     * 服务标题
     */
    private String title;

    /**
     * 所属系统
     */
    private String owner;

    /**
     * 服务说明
     */
    private String note;

    /**
     * 手工说明
     */
    private String manualNote;

    /**
     * 服务类型
     */
    private ResponseType serviceType = ResponseType.SYN;

    /**
     * 业务类型
     */
    private ApiBusiType busiType;

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /**
     * 消息体列表
     */
    private List<ApiDocMessageDto> apiDocMessages;
}
