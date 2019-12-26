package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author liangsong
 * @date 2019-12-12 16:52
 */
@Data
@ApiModel(value = "ApiDocMessageDto", description = "api服务报文消息详情")
public class ApiDocMessageDto implements Serializable {
    /**
     * 消息编号
     * serviceNo+messageType
     */
    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "消息编号（serviceNo+messageType）")
    private String messageNo;

    /**
     * 服务ID
     */
    @NotNull
    @ApiModelProperty(value = "api服务编号")
    private String serviceNo;

    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @ApiModelProperty(value = "消息类型")
    private MessageTypeEnum messageType = MessageTypeEnum.Request;

    /**
     * 自动说明
     */
    @Size(max = 255)
    @ApiModelProperty(value = "说明")
    private String note;

    /**
     * 人工说明
     */
    @Size(max = 255)
    @ApiModelProperty(value = "手工说明")
    private String manualNote;

//    /**
//     * 排序值
//     */
//    @ApiModelProperty(value = "排序值")
//    private Long sortTime = System.currentTimeMillis();

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String comments;

    /**
     * 签名
     */
    @Size(max = 128)
    @ApiModelProperty(value = "签名")
    private String signature;
}
