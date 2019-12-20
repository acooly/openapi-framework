package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.apidoc.portal.dto.ApiDocServiceDto;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liangsong
 * @date 2019-12-13 17:30
 */
@Slf4j
@RestController
@RequestMapping("/docs/api")
public class ApiDocApiRestPortalController {

    @Autowired
    private ApiDocServiceService apiDocServiceService;

    /**
     * 查询方案下的指定Api接口内容
     */
    @ResponseBody
    @GetMapping(value = {"/serviceDetail"})
    public JsonEntityResult serviceDetail(String schemeNo, String serviceNo,
                                          HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonEntityResult<ApiDocServiceDto> result = new JsonEntityResult<>();
        ApiDocService apiDocService = apiDocServiceService.loadApiDocServiceByNo(serviceNo);
        if (apiDocService == null) {
            result.setSuccess(false);
            result.setCode(ResultStatus.failure.getCode());
            result.setMessage("api信息不存在");
            return result;
        }
        ApiDocServiceDto apiDocServiceDto = JSON.parseObject(JSON.toJSONString(apiDocService), ApiDocServiceDto.class);
        result.setEntity(apiDocServiceDto);
        return result;
    }
}
