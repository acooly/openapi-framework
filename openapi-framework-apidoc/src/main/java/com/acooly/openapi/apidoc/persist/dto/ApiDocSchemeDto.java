package com.acooly.openapi.apidoc.persist.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liangsong
 * @date 2019-12-10 17:04
 */
@Data
public class ApiDocSchemeDto implements Serializable {

    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 主题
     */
    private String category;

    /**
     * 唯一编码
     */
    private String schemeNo;

    /**
     * 名称
     */
    private String title;

    /**
     * 层级路径
     */
    private String path;

    /**
     * 子节点数量
     */
    private Integer subCount;

    /**
     * 链接地址
     */
    private String link;

    /**
     * 备注
     */
    private String comments;

    /**
     * 所有的子
     */
    private List<ApiDocSchemeDto> children = new LinkedList<ApiDocSchemeDto>();

    /**
     * 详细内容
     */
    private String content;
}
