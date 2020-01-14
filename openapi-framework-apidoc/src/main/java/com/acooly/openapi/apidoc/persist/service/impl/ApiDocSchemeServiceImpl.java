/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Exceptions;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.arithmetic.tree.QuickTree;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.ApiDocErrorCodeEnum;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeDao;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeServiceDao;
import com.acooly.openapi.apidoc.persist.dao.ApiDocServiceDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务方案 Service实现
 * <p>
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 */
@Slf4j
@Service("apiDocSchemeService")
public class ApiDocSchemeServiceImpl extends EntityServiceImpl<ApiDocScheme, ApiDocSchemeDao> implements ApiDocSchemeService {

    @Resource
    private ApiDocServiceDao apiDocServiceDao;

    @Autowired
    private ApiDocProperties apiDocProperties;

    @Resource
    private ApiDocSchemeServiceDao apiDocSchemeServiceDao;

    @Override
    public ApiDocScheme createDefault() {
        List<ApiDocScheme> apiDocSchemes = getEntityDao().findBySchemeType(SchemeTypeEnum.auto);
        ApiDocScheme def = Collections3.getFirst(apiDocSchemes);
        if (def == null) {
            def = new ApiDocScheme();
            def.setAuthor("system");
            def.setSchemeNo("system" + ApiDocs.API_DOC_COMMON_SPLIT_CHAR + SchemeTypeEnum.auto.code());
            def.setSchemeType(SchemeTypeEnum.auto);
            def.setTitle("系统");
            save(def);
        }
        return def;
    }

    @Override
    public List<ApiDocScheme> findBySchemeType(SchemeTypeEnum schemeType) {
        if (schemeType == null) {
            return getEntityDao().findAll();
        } else {
            return getEntityDao().findBySchemeType(schemeType);
        }
    }

    @Override
    public List<ApiDocScheme> findBySchemeTypeAndCategory(String schemeType, String category) {
        Map<String, Object> map = Maps.newHashMap();
        if (Strings.isNotBlank(schemeType)) {
            map.put("EQ_schemeType", schemeType);
        }
        if (Strings.isNotBlank(category)) {
            map.put("EQ_category", category);
        }
        Map<String, Boolean> sortMap = Maps.newHashMap();
        sortMap.put("sortTime", false);
        return this.getEntityDao().list(map, sortMap);
    }

    /**
     * 注意：合并操作不加全局事务，降低事务粒度。
     * 如果失败不一致，重新生成一次即可
     *
     * @param apiDocSchemes
     */
    @Override
    public void merge(List<ApiDocScheme> apiDocSchemes) {
        try {
            if (Collections3.isEmpty(apiDocSchemes)) {
                return;
            }
            Map<String, ApiDocScheme> atMap = apiDocSchemes.stream().collect(Collectors.toMap(ApiDocScheme::getSchemeNo, apiDocScheme -> apiDocScheme));
            List<ApiDocScheme> persists = findBySchemeTypeAndCategory(null, ApiDocProperties.DEFAULT_CATEGORY);
            if (persists == null) {
                persists = Lists.newArrayList();
            }
            List<Serializable> needRemoves = Lists.newArrayList();
            // 删除注解中无，而数据库中存在的方案
            for (ApiDocScheme scheme : persists) {
                // 删除自动生成已持久化的未匹配的scheme
                if (atMap.get(scheme.getSchemeNo()) == null) {
                    needRemoves.add(scheme.getId());
                }
            }
            //删除系统默认生成的所有服务解决方案
            List<ApiDocScheme> commonSchemes = findBySchemeType(SchemeTypeEnum.common);
            if (apiDocProperties.isDefaultSchemeEnable()) {
                for (ApiDocScheme scheme : commonSchemes) {
                    // 删除自动生成已持久化的未匹配的scheme
                    if (atMap.get(scheme.getSchemeNo()) == null) {
                        needRemoves.add(scheme.getId());
                    }
                }
            }
            if (Collections3.isNotEmpty(needRemoves)) {
                removes(needRemoves.toArray(new Serializable[]{}));
                persists.removeIf(persist -> needRemoves.contains(persist.getId()));
            }

            // 将剩余存在的方案转换为schemeNo为key的map
            Map<String, ApiDocScheme> dbMap = persists.stream().collect(Collectors.toMap(ApiDocScheme::getSchemeNo, apiDocScheme -> apiDocScheme));

            List<ApiDocScheme> needSaves = Lists.newArrayList();
            for (ApiDocScheme apiDocScheme : apiDocSchemes) {
                // 注解标注的scheme在数据库中不存在，则需要插入数据库
                if (dbMap.get(apiDocScheme.getSchemeNo()) == null) {
                    needSaves.add(apiDocScheme);
                }
            }

            if (Collections3.isNotEmpty(needSaves)) {
                saves(needSaves);
                // 将保存之后的列表全部加入到list中，此list现包含所有数据
            }

            persists = findBySchemeTypeAndCategory(null, ApiDocProperties.DEFAULT_CATEGORY);
            dbMap = persists.stream().collect(Collectors.toMap(ApiDocScheme::getSchemeNo, apiDocScheme -> apiDocScheme));

            List<ApiDocScheme> needUpdates = Lists.newArrayList();
            for (ApiDocScheme persist : persists) {
                boolean needUpdate = false;
                // 基于数据库存储的schemeNo获取注解中定义的scheme
                ApiDocScheme atScheme = atMap.get(persist.getSchemeNo());
                // 获取数据库中父级scheme
                ApiDocScheme parentDbScheme = dbMap.get(atScheme.getParentSchemeNo());

                // 判断父节点是否发生变化 (数据库存储的父节点与扫描的父节点不一致或parentId与parentSchemeNo不匹配)
                if (!Strings.equals(persist.getParentSchemeNo(), atScheme.getParentSchemeNo())) {
                    needUpdate = true;
                }
                if (parentDbScheme != null && !parentDbScheme.getId().equals(persist.getParentId())) {
                    needUpdate = true;
                }
                // 判断子节点数量是否发生变化
                if (!persist.getSubCount().equals(atScheme.getSubCount())) {
                    needUpdate = true;
                    persist.setSubCount(atScheme.getSubCount());
                }
                if (needUpdate) {
                    if (parentDbScheme != null) {
                        persist.setParentSchemeNo(atScheme.getParentSchemeNo());
                        persist.setParentId(parentDbScheme.getId());
                        persist.setPath(ApiDocScheme.TOP_PARENT_PATH + persist.getParentId() + ApiDocScheme.TOP_PARENT_PATH);
                    } else {
                        persist.setParentSchemeNo(null);
                        persist.setParentId(ApiDocScheme.TOP_PARENT_ID);
                        persist.setPath(ApiDocScheme.TOP_PARENT_PATH);
                    }
                    needUpdates.add(persist);
                }
            }
            if (Collections3.isNotEmpty(needUpdates)) {
                saves(needUpdates);
            }
            log.info("合并服务方案 成功。 新增: {}， 删除冗余:{}，更新: {} ", needSaves.size(), needRemoves.size(), needUpdates.size());
        } catch (Exception e) {
            e.printStackTrace();
            throw Exceptions.runtimeException("合并方案数据库失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject getSelectSchemeList(String schemeNo) {
        JSONObject data = new JSONObject();
        List<ApiDocService> getOtherSchemeServices = apiDocServiceDao.getOtherSchemeServices(schemeNo);
        JSONArray getOtherSchemeServicesArray = getServices(getOtherSchemeServices);
        List<ApiDocService> getSchemeServices = apiDocServiceDao.findServicesBySchemeNo(schemeNo);
        JSONArray getSchemeServicesArray = getServices(getSchemeServices);
        data.put("otherAllServices", getOtherSchemeServicesArray);
        data.put("schemeServices", getSchemeServicesArray);
        return data;
    }

    private JSONArray getServices(List<ApiDocService> serviceDocs) {
        JSONArray retrunArray = new JSONArray();
        for (ApiDocService apiServiceDoc : serviceDocs) {
            JSONObject apiServiceDocObj = new JSONObject();
            apiServiceDocObj.put("serviceTitle", apiServiceDoc.getTitle());
            apiServiceDocObj.put("serviceNo", apiServiceDoc.getServiceNo());
            retrunArray.add(apiServiceDocObj);
        }
        return retrunArray;
    }

    @Override
    public void moveUp(Long id) {
        try {
            ApiDocScheme apiDocScheme = get(id);
            long current = apiDocScheme.getSortTime();
            ApiDocScheme beforeScheme = getEntityDao().findBeforeOne(current, id);
            if (beforeScheme != null) {
                apiDocScheme.setSortTime(beforeScheme.getSortTime());
                beforeScheme.setSortTime(current);
                this.update(apiDocScheme);
                this.update(beforeScheme);
            }
        } catch (Exception e) {
            throw new BusinessException("上移失败", e);
        }
    }

    @Override
    public void moveTop(Long id) {
        try {
            ApiDocScheme apiDocScheme = get(id);
            apiDocScheme.setSortTime((new Date()).getTime());
            update(apiDocScheme);
        } catch (Exception e) {
            throw new BusinessException("置顶失败", e);
        }
    }

    /**
     * 新增节点
     *
     * @param o
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(ApiDocScheme o) throws BusinessException {
        // 设置parentId默认值
        if (o.getParentId() == null) {
            o.setParentId(ApiDocScheme.TOP_PARENT_ID);
        }

        // 设置默认theme
        if (Strings.isBlank(o.getCategory())) {
            o.setCategory(ApiDocProperties.DEFAULT_CATEGORY);
        }

        // 更新父节点计数
        ApiDocScheme parent = null;
        if (o.getParentId() > ApiDocScheme.TOP_PARENT_ID) {
            parent = get(o.getParentId());
            parent.setSubCount(parent.getSubCount() + 1);
            update(parent);
        }

        if (Strings.isBlank(o.getSchemeNo())) {
            o.setSchemeNo(o.getCategory() + "_" + System.currentTimeMillis());
        }

        // 设置当前节点的path(以目录服务)
        // 有parent
        if (parent != null) {
            String parentPath = parent.getPath();
            if (Strings.isBlank(parentPath)) {
                parentPath = ApiDocScheme.TOP_PARENT_PATH;
            }
            o.setParentSchemeNo(parent.getSchemeNo());
            o.setPath(parentPath + parent.getId() + ApiDocScheme.TOP_PARENT_PATH);
        } else {
            o.setPath(ApiDocScheme.TOP_PARENT_PATH);
        }
        // 设置当前时间为新节点的排序值
        if (o.getSortTime() == null) {
            o.setSortTime(System.currentTimeMillis());
        }
        super.save(o);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeById(Serializable id) throws BusinessException {
        ApiDocScheme treeType = get(id);
        // 检查是否有子节点
        if (treeType.getSubCount() > 0) {
            log.info("删除节点 [失败] 原因: {}, 节点: {}", ApiDocErrorCodeEnum.EXIST_SUB_NODE_CONFLICT
                    , treeType);
            Exceptions.rethrow(ApiDocErrorCodeEnum.EXIST_SUB_NODE_CONFLICT);
        }
        // 更新父节点计数
        if (!ApiDocScheme.TOP_PARENT_ID.equals(treeType.getParentId())) {
            ApiDocScheme parent = get(treeType.getParentId());
            parent.setSubCount(parent.getSubCount() - 1);
            update(parent);
        }
        // 删除本节点
        super.removeById(id);
    }

    @Override
    public List<ApiDocScheme> level(Long parentId, DocStatusEnum status) {
        return level(parentId, status);
    }

    @Override
    public List<ApiDocScheme> level(Long parentId, String category, DocStatusEnum status) {
        Long queryParentId = Strings.isBlankDefault(parentId, ApiDocScheme.TOP_PARENT_ID);
        String queryCategory = Strings.isBlankDefault(category, ApiDocProperties.DEFAULT_CATEGORY);
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_category", queryCategory);
        map.put("EQ_parentId", queryParentId);
        if (status != null) {
            map.put("EQ_status", status);
        }
        Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
        sortMap.put("sortTime", false);
        sortMap.put("id", false);
        return query(map, sortMap);
    }

    @Override
    public List<ApiDocScheme> tree(String category, Long rootId, DocStatusEnum status, boolean loadApis) {
        String rootPath = ApiDocScheme.TOP_PARENT_PATH;
        ApiDocScheme rootApiDocScheme = null;
        if (rootId != null && !rootId.equals(ApiDocScheme.TOP_PARENT_ID)) {
            rootApiDocScheme = get(rootId);
            if (rootApiDocScheme == null) {
                throw new BusinessException("参数错误，节点不存在");
            }
            rootPath = ApiDocScheme.TOP_PARENT_PATH + rootId + ApiDocScheme.TOP_PARENT_PATH;
        }
        Map<String, Object> params = Maps.newHashMap();
        category = Strings.isBlankDefault(category, ApiDocProperties.DEFAULT_CATEGORY);
        rootPath = Strings.isBlankDefault(rootPath, ApiDocScheme.TOP_PARENT_PATH);
        params.put("EQ_category", category);
        params.put("RLIKE_path", rootPath);
        if (status != null) {
            params.put("EQ_status", status);
        }
        List<ApiDocScheme> treeTypes = this.getEntityDao().treeQuery(category, rootId, rootPath, status);
        if (loadApis) {
            List<ApiDocService> docServices = apiDocSchemeServiceDao.findAllSchemeService();
            for (ApiDocScheme apiDocScheme : treeTypes) {
                for (ApiDocService docService : docServices) {
                    if (apiDocScheme.getSchemeNo().equals(docService.getSchemeNo())) {
                        apiDocScheme.append(docService);
                        break;
                    }
                }
            }
        }
        return doTree(treeTypes);
    }

    @Override
    public List<ApiDocScheme> tree(Long rootId, DocStatusEnum status) {
        return tree(ApiDocProperties.DEFAULT_CATEGORY, rootId, status, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(String sourceId, String targetId, String point) {
        Map<Long, ApiDocScheme> nodeMap = Maps.newHashMap();
        //源节点
        ApiDocScheme source = loadNodeById(Long.valueOf(sourceId), nodeMap);
        //目标节点
        ApiDocScheme target = loadNodeById(Long.valueOf(targetId), nodeMap);
        //目标节点父节点id
        Long targetParentId = target.getParentId();
        //源节点父节点id
        Long beforeMoveParentId = source.getParentId();
        Long afterMoveParentId = null;
        if ("append".equals(point)) {
            afterMoveParentId = target.getId();
            source.setParentId(target.getId());
            source.setParentSchemeNo(target.getSchemeNo());
            source.setPath(target.getPath() + targetId + ApiDocScheme.TOP_PARENT_PATH);
            source.setSortTime(System.currentTimeMillis());
        } else if ("top".equals(point)) {
            afterMoveParentId = target.getParentId();
            source.setSortTime(target.getSortTime() + 1);
            source.setParentId(targetParentId);
            source.setParentSchemeNo(target.getParentSchemeNo());
            source.setPath(target.getPath());
        } else if ("bottom".equals(point)) {
            afterMoveParentId = target.getParentId();
            source.setSortTime(target.getSortTime() - 1);
            source.setParentId(targetParentId);
            source.setParentSchemeNo(target.getParentSchemeNo());
            source.setPath(target.getPath());
        }
        //仅处理source节点更新
        this.update(source);

        //更新源父节点计数,当前移动的节点源父节点不为根目录并且不在同一个父级节点之间移动时，才更新
        if (!ApiDocScheme.TOP_PARENT_ID.equals(beforeMoveParentId) && !beforeMoveParentId.equals(targetParentId)) {
            ApiDocScheme sourceParent = loadNodeById(Long.valueOf(beforeMoveParentId), nodeMap);
            sourceParent.setSubCount(sourceParent.getSubCount() - 1);
            this.update(sourceParent);
        }
        //更新目标父节点计数，当前移动的移动至非根节点并且不在同一个父级节点之间移动时，才更新
        if (!ApiDocScheme.TOP_PARENT_ID.equals(afterMoveParentId) && !beforeMoveParentId.equals(targetParentId)) {
            ApiDocScheme targetParent = loadNodeById(Long.valueOf(afterMoveParentId), nodeMap);
            targetParent.setSubCount(targetParent.getSubCount() + 1);
            this.update(targetParent);
        }
    }

    @Override
    public ApiDocScheme findBySchemeNo(String schemeNo) {
        if (Strings.isBlank(schemeNo)) {
            return null;
        }
        return this.getEntityDao().findUniqu("EQ_schemeNo", schemeNo);
    }

    /**
     * 排序处理
     * 每层内排序规则：sortTime desc, id desc
     *
     * @return
     */
    protected List doTree(List<ApiDocScheme> catalogLists) {
        return QuickTree.quickTree(catalogLists, ApiDocScheme.TOP_PARENT_ID,
                Comparator.nullsLast(Comparator.comparing((ApiDocScheme t) -> -t.getSortTime())
                        .thenComparing(t -> -t.getId())));
    }


    protected ApiDocScheme loadNodeById(Long id, Map<Long, ApiDocScheme> nodeMap) {
        if (nodeMap.get(id) != null) {
            return nodeMap.get(id);
        }
        ApiDocScheme apiDocScheme = this.get(id);
        if (apiDocScheme == null) {
            throw new BusinessException("数据异常");
        }
        nodeMap.put(id, apiDocScheme);
        return apiDocScheme;
    }
}
