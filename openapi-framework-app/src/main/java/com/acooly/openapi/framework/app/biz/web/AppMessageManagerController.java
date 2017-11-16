package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;
import com.acooly.openapi.framework.app.biz.enums.AppMessageContentType;
import com.acooly.openapi.framework.app.biz.enums.AppMessageStatus;
import com.acooly.openapi.framework.app.biz.enums.AppMessageType;
import com.acooly.openapi.framework.app.biz.service.AppMessageService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/app/appMessage")
public class AppMessageManagerController
    extends AbstractJQueryEntityController<AppMessage, AppMessageService> {

  private static Map<String, String> allTypes = AppMessageType.mapping();
  private static Map<String, String> allStatuss = AppMessageStatus.mapping();

  @Autowired private AppMessageService appMessageService;
  //@Autowired
  //private AppNotifyService appNotifyService;

  /**
   * 群发或广播
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "push")
  @ResponseBody
  public JsonEntityResult<AppMessage> push(
      HttpServletRequest request, HttpServletResponse response) {
    JsonEntityResult<AppMessage> result = new JsonEntityResult<AppMessage>();
    try {
      //String sessionUser = getSessionUser(request);
      //appNotifyService.group(getNotifyMessage(request), getTargets(request), sessionUser, true);
      result.setMessage("推送成功");
    } catch (Exception e) {
      handleException(result, "推送", e);
    }
    return result;
  }

  protected List<String> getTargets(HttpServletRequest request) {
    String target = request.getParameter("receivers");
    if (Strings.isNotBlank(target)) {
      return Lists.newArrayList(Strings.split(target, ","));
    } else {
      return null;
    }
  }

  //	protected NotifyMessage getNotifyMessage(HttpServletRequest request) {
  //		NotifyMessage nm = new NotifyMessage();
  //		nm.setTitle(request.getParameter("title"));
  //		nm.setContent(request.getParameter("content"));
  //		String online = request.getParameter("online");
  //		nm.setOnline(Strings.equalsIgnoreCase("true", online));
  //		String context = request.getParameter("context");
  //		String contentType = request.getParameter("contentType");
  //		if (Strings.isBlank(contentType)) {
  //			nm.setContentType(AppMessageContentType.normal);
  //		} else {
  //			nm.setContentType(AppMessageContentType.findStatus(contentType));
  //		}
  //		if (Strings.isNotBlank(context)) {
  //			@SuppressWarnings("unchecked")
  //			Map<String, Object> contextMap = JsonMapper.nonDefaultMapper().fromJson(context, Map.class);
  //			nm.setContext(contextMap);
  //		}
  //		Validators.assertJSR303(nm);
  //		return nm;
  //	}
  //
  //	protected String getSessionUser(HttpServletRequest request) {
  //		String[] keys = Strings.split(OFileConfig.OFILE_CHECK_SESSION_KEYS, ",");
  //		Object user = null;
  //		for (String key : keys) {
  //			user = request.getSession().getAttribute(key);
  //			if (user != null) {
  //				break;
  //			}
  //		}
  //		if (user != null) {
  //			return user.toString();
  //		}
  //		return null;
  //	}

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allTypes", allTypes);
    model.put("allStatuss", allStatuss);
    model.put("allContentTypes", AppMessageContentType.mapping());
  }
}
