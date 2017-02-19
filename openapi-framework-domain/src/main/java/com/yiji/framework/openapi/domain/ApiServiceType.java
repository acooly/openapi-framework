/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.domain;


import com.acooly.core.common.domain.AbstractEntity;
import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务分类 Entity
 *
 * @author acooly
 *         Date: 2016-07-16 01:57:05
 */
@Entity
@Table(name = "api_service_type")
public class ApiServiceType extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 父类型
     */
    private Long parentId;

    /**
     * 搜索路径
     */
    private String path;

    /**
     * 排序
     */
    private long sortTime;

    /**
     * 类型名称
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * update_time
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String comments;

    /**
     * 所有的子
     */
    @Transient
    private List<ApiServiceType> children = new LinkedList<ApiServiceType>();

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Transient
    public List<ApiServiceType> getChildren() {
        return children;
    }

    public void setChildren(List<ApiServiceType> children) {
        this.children = children;
    }

    public void addChild(ApiServiceType node) {
        this.children.add(node);
    }

    public long getSortTime() {
        return sortTime;
    }

    public void setSortTime(long sortTime) {
        this.sortTime = sortTime;
    }

    public static class NodeComparator implements Comparator<ApiServiceType> {
        @Override
        public int compare(ApiServiceType node1, ApiServiceType node2) {
            long orderTime1 = node1.getSortTime();
            long orderTime2 = node2.getSortTime();
            return orderTime1 > orderTime2 ? -1 : (orderTime1 == orderTime2 ? 0 : 1);
        }
    }

}
