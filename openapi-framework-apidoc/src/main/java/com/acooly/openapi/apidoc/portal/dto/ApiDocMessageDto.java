package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
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
public class ApiDocMessageDto implements Serializable {
    /**
     * 消息编号
     * serviceNo+messageType
     */
    @NotBlank
    @Size(max = 64)
    private String messageNo;

    /**
     * 服务ID
     */
    @NotNull
    private String serviceNo;

    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private MessageTypeEnum messageType = MessageTypeEnum.Request;

    /**
     * 自动说明
     */
    @Size(max = 255)
    private String note;

    /**
     * 人工说明
     */
    @Size(max = 255)
    private String manualNote;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    /**
     * 签名
     */
    @Size(max = 128)
    private String signature;
}
