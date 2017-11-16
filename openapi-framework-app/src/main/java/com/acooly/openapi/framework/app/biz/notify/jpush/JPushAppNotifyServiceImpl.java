/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;
import com.acooly.openapi.framework.app.biz.enums.AppMessageStatus;
import com.acooly.openapi.framework.app.biz.enums.AppMessageType;
import com.acooly.openapi.framework.app.biz.notify.AppNotifyService;
import com.acooly.openapi.framework.app.biz.notify.NotifyMessage;
import com.acooly.openapi.framework.app.biz.notify.NotifyProperty;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushMessage;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushNotification;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushOptions;
import com.acooly.openapi.framework.app.biz.service.AppMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统通知 JPush实现
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
@Service
public class JPushAppNotifyServiceImpl implements AppNotifyService {

  private static final Logger logger = LoggerFactory.getLogger(JPushAppNotifyServiceImpl.class);

  @Resource protected JPushSendService jPushSendService;
  @Resource protected AppMessageService appMessageService;
  JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
  @Autowired private AppProperties appProperties;

  @Override
  public void group(NotifyMessage notifyMessage, List<String> targets, boolean save) {
    group(notifyMessage, targets, null, save);
  }

  @Override
  public void group(
      NotifyMessage notifyMessage, List<String> targets, String sender, boolean save) {
    try {
      JPushOrder order = new JPushOrder();
      // audience
      if (Collections3.isNotEmpty(targets)) {
        order.getAudience().setAlias(targets);
      }

      String title = notifyMessage.getTitle();
      String content = notifyMessage.getContent();
      Map<String, Object> extras = notifyMessage.getContext();
      // message
      JPushMessage jpushMessage = new JPushMessage();
      jpushMessage.setTitle(notifyMessage.getTitle());
      jpushMessage.setMsgContent(notifyMessage.getContent());
      Map<String, Object> context = notifyMessage.getContext();
      if (context != null && context.size() > 0) {
        jpushMessage.setExtras(context);
      }
      order.setMessage(jpushMessage);

      // notification && options
      JPushNotification jPushNotification = new JPushNotification();
      JPushNotification.Android android = new JPushNotification.Android();
      android.setAlert(content);
      android.setTitle(title);
      android.setExtras(extras);
      jPushNotification.setAndroid(android);
      JPushNotification.IOS ios = new JPushNotification.IOS();
      ios.setAlert(content);
      ios.setExtras(extras);
      jPushNotification.setIos(ios);
      order.setNotification(jPushNotification);

      boolean online = appProperties.getJpush().isApns() || notifyMessage.isOnline();
      JPushOptions jPushOptions =
          new JPushOptions(appProperties.getJpush().getTimeToLive(), online);
      order.setOptions(jPushOptions);

      Map<NotifyProperty, Object> properties = notifyMessage.getProperties();
      if (properties != null && properties.size() > 0) {
        JPushNotification jnotification =
            (JPushNotification) properties.get(NotifyProperty.JPush_Notification);
        if (jnotification != null) {
          order.setNotification(jnotification);
        }
        JPushOptions joptions = (JPushOptions) properties.get(NotifyProperty.JPush_Options);
        if (joptions != null) {
          order.setOptions(joptions);
        }
      }

      JPushResult result = jPushSendService.request(order);
      if (result.getHttpStatus() != 200) {
        throw new RuntimeException(result.getCode() + ":" + result.getMessage());
      }
      logger.info("JPush推送成功: request:{}, result:{}", order, result);
      if (save) {
        // 保存到数据库
        AppMessage appMessage = new AppMessage();
        appMessage.setTitle(notifyMessage.getTitle());
        appMessage.setContent(notifyMessage.getContent());
        if (context != null && context.size() > 0) {
          appMessage.setContext(
              jsonMapper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(context));
        }
        if (Collections3.isNotEmpty(targets)) {
          order.getAudience().setAlias(targets);
          appMessage.setReceivers(Strings.join(targets, ",") + ",");
        }
        if (Strings.isBlank(sender)) {
          sender = "system";
        }
        appMessage.setSender(sender);
        appMessage.setSendTime(new Date());
        appMessage.setType(
            Collections3.isNotEmpty(targets) ? AppMessageType.group : AppMessageType.broadcast);
        appMessage.setContentType(notifyMessage.getContentType());
        appMessage.setStatus(AppMessageStatus.success);
        if (properties != null && properties.size() > 0) {
          appMessage.setComments(jsonMapper.toJson(properties));
        }
        appMessageService.save(appMessage);
      }
    } catch (Exception e) {
      logger.error("JPush通知发送失败", e);
      throw new RuntimeException("JPush通知发送失败:" + e.getMessage());
    }
  }

  @Override
  public void broadcast(NotifyMessage notifyMessage, boolean save) {
    group(notifyMessage, null, save);
  }
}
