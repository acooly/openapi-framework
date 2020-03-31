package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "ApiDocServiceDto", description = "api服务详情")
public class ApiDocServiceDto implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "所属方案编码")
    private String schemeNo;

    /**
     * 服务编号
     */
    @ApiModelProperty(value = "服务编号")
    private String serviceNo;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String name;

    /**
     * 服务版本
     */
    @ApiModelProperty(value = "服务版本")
    private String version = ApiConstants.VERSION_DEFAULT;

    /**
     * 服务标题
     */
    @ApiModelProperty(value = "服务标题")
    private String title;

    /**
     * 所属系统
     */
    @ApiModelProperty(value = "所属系统")
    private String owner;

    /**
     * 服务说明
     */
    @ApiModelProperty(value = "服务说明")
    private String note;

    /**
     * 手工说明
     */
    @ApiModelProperty(value = "手工说明")
    private String manualNote;

    /**
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型")
    private String serviceType = ResponseType.SYN.getMsg();

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private ApiBusiType busiType;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 消息体列表
     */
    @ApiModelProperty(value = "api消息体列表")
    private List<ApiDocMessageDto> apiDocMessages;
}
