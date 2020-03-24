package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeDescService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.portal.dto.ApiDocSchemeDto;
import com.acooly.openapi.apidoc.portal.dto.ApiDocServiceDto;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liangsong
 * @date 2019-12-12 08:55
 */
@Slf4j
@RestController
@RequestMapping("/docs/scheme")
public class ApiDocSchemeRestPortalController {

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocSchemeDescService apiDocSchemeDescService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;

    /**
     * 目录列表，支持传入根目录id
     */
    @ResponseBody
    @GetMapping(value = {"/catalogList"})
    @ApiOperation("文档-查询分类目录列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "category", value = "文档分类{api:api文档,product:产品文档}", required = true, paramType = "query"),
            @ApiImplicitParam(name = "schemeNo", value = "当前文档方案编码", required = false, paramType = "query"),
            @ApiImplicitParam(name = "loadApis", value = "加载api列表", required = false, paramType = "query")})
    public JsonListResult<ApiDocSchemeDto> catalogList(String category, String schemeNo, boolean loadApis, HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonListResult<ApiDocSchemeDto> result = new JsonListResult<ApiDocSchemeDto>();
        Long treeId = null;
        if (Strings.isNotBlank(schemeNo)) {
            ApiDocScheme scheme = apiDocSchemeService.findBySchemeNo(schemeNo);
            // 如果schemeNo不为空但未查询到值，直接返回
            if (scheme == null || Strings.isBlank(scheme.getPath())) {
                return result;
            }
            // treeId首先赋值为当前schemeNo对应节点id
            treeId = scheme.getId();
            // 如果当前节点为子节点，则找到最顶级节点id
            if (!ApiDocScheme.TOP_PARENT_ID.equals(scheme.getParentId())) {
                String[] pathIds = scheme.getPath().split("/");
                treeId = Long.parseLong(pathIds[1]);
            }
        }
        List<ApiDocScheme> list = apiDocSchemeService.tree(category, treeId, DocStatusEnum.onShelf, schemeNo);
        if (Collections3.isEmpty(list)) {
            return result;
        }
        List<ApiDocSchemeDto> resultList = JSON.parseArray(JSON.toJSONString(list), ApiDocSchemeDto.class);
        result.setRows(resultList);
        result.setTotal(Long.valueOf(resultList.size()));
        return result;
    }

    /**
     * 内容详情
     */
    @ResponseBody
    @GetMapping(value = {"/contentDetail"})
    @ApiOperation("文档-文档内容详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "schemeNo", value = "当前文档方案编码", required = true, paramType = "query")})
    public JsonEntityResult contentDetail(String schemeNo, HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonEntityResult<ApiDocSchemeDto> result = new JsonEntityResult<>();
        ApiDocScheme apiDocScheme = apiDocSchemeService.findBySchemeNo(schemeNo);
        ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.get(apiDocScheme.getId());
        if (apiDocScheme == null) {
            result.setSuccess(false);
            result.setMessage("方案不存在");
            return result;
        }
        ApiDocSchemeDto dto = new ApiDocSchemeDto();
        BeanCopier.copy(apiDocScheme, dto);
        if (apiDocSchemeDesc != null) {
            dto.setContent(HtmlUtils.htmlUnescape(apiDocSchemeDesc.getSchemeDesc()));
        }
        List<ApiDocService> schemeServices = apiDocSchemeServiceService.findContentServices(apiDocScheme.getSchemeNo());
        if (Collections3.isNotEmpty(schemeServices)) {
            List<ApiDocServiceDto> serviceList = JSON.parseArray(JSON.toJSONString(schemeServices), ApiDocServiceDto.class);
            dto.setServices(serviceList);
        }
        result.setEntity(dto);
        return result;
    }


    /**
     * 查询方案下的服务列表
     * 支持通过关键词查询服务名、服务标题来进行搜索
     */
    @ResponseBody
    @GetMapping(value = {"/serviceList"})
    @ApiOperation("文档-api列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "schemeNo", value = "文档编码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "keywords", value = "搜索关键字", required = false, paramType = "query")})
    public JsonListResult<ApiDocServiceDto> serviceList(String schemeNo, String keywords,
                                                        HttpServletRequest request, HttpServletResponse response, Model model) {
        String tmp = null;
        if (Strings.isNotBlank(keywords)) {
            tmp = Strings.trim(keywords).toUpperCase();
        }
        List<ApiDocService> list = apiDocSchemeServiceService.findContentServices(schemeNo,keywords);
        JsonListResult<ApiDocServiceDto> result = new JsonListResult<>();
        if (Collections3.isNotEmpty(list)) {
            List<ApiDocServiceDto> resultList = JSON.parseArray(JSON.toJSONString(list), ApiDocServiceDto.class);
            result.setRows(resultList);
            result.setTotal(Long.valueOf(resultList.size()));
        }
        return result;
    }
}
