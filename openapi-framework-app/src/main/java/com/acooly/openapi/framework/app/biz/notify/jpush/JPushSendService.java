/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush;

import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushMessage;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushNotification;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * JPush 发送服务
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
@Service
public class JPushSendService {

  private static final Logger logger = LoggerFactory.getLogger(JPushSendService.class);
  JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
  @Autowired private AppProperties appProperties;

  public static void main(String[] args) {
    String title = "测试推送";
    String content = "您发布的债权交易(急用钱钱，转出部分)已有用户(buyer)发起交易申请,请及时处理.";
    Map<String, Object> extras = Maps.newHashMap();
    extras.put("type", "applyTrade");
    extras.put("tradeId", 1);

    JPushOrder order = new JPushOrder();
    // 推送用户
    order.getAudience().setRegistrationId(Lists.newArrayList(new String[] {"buyer"}));
    // 推送选项参数
    order.getOptions().setApnsProduction(false);
    JPushMessage jm = new JPushMessage();
    jm.setTitle(title);
    jm.setMsgContent(content);
    jm.setContentType("text/html");
    jm.setExtras(extras);
    order.setMessage(jm);

    JPushNotification jn = new JPushNotification();

    JPushNotification.Android android = new JPushNotification.Android();
    android.setTitle(title);
    android.setAlert(content);
    android.setExtras(extras);
    android.setBuilderId(100);
    JPushNotification.IOS ios = new JPushNotification.IOS();
    ios.setAlert(content);
    ios.setBadge(1);
    ios.setCategory("category");
    ios.setContentAvailable("true");
    ios.setSound("sound");
    ios.setExtras(extras);
    jn.setAndroid(android);
    jn.setIos(ios);
    order.setNotification(jn);
    System.out.println(order);
    // JPushSendService jss = new JPushSendService();
    // JPushResult result = jss.request(order);
    // System.out.println(result);

  }

  public JPushResult request(JPushOrder order) {
    try {
      if (!appProperties.getJpush().isEnable()) {
        throw new RuntimeException("jpush发送开关已关闭");
      }
      Https https = Https.getInstance();
      Map<String, String> headMap = Maps.newHashMap();
      headMap.put("Authorization", getAuthorization());
      logger.info("JPush request:{}", order);
      HttpResult result =
          https.post(
              appProperties.getJpush().getGateway(),
              order.toString(),
              headMap,
              true,
              ContentType.APPLICATION_JSON);
      logger.info("JPush response:{}", result);
      JPushResult jpushResult = new JPushResult();
      jpushResult.setCode(String.valueOf(result.getStatus()));
      jpushResult.setHttpStatus(result.getStatus());
      if (result.getStatus() == 200) {
        jsonMapper.update(result.getBody(), jpushResult);
      } else {
        JPushReturn jr = jsonMapper.fromJson(result.getBody(), JPushReturn.class);
        jpushResult.setCode(jr.getError().getCode());
        jpushResult.setMessage(jr.getError().getMessage());
        jpushResult.setMsgId(jr.getMsg_id());
        jpushResult.setSendNo(jr.getSendno());
      }
      return jpushResult;
    } catch (Exception e) {
      throw new RuntimeException("JPush 发送失败:" + e.getMessage());
    }
  }

  protected String getAuthorization() {
    String p =
        appProperties.getJpush().getAppKey() + ":" + appProperties.getJpush().getMasterSecret();
    return "Basic " + Base64.encodeBase64String(p.getBytes());
  }

  static class JPushReturn {
    private String msg_id;
    private String sendno;
    private JPushError error;

    public String getMsg_id() {
      return msg_id;
    }

    public void setMsg_id(String msg_id) {
      this.msg_id = msg_id;
    }

    public String getSendno() {
      return sendno;
    }

    public void setSendno(String sendno) {
      this.sendno = sendno;
    }

    public JPushError getError() {
      return error;
    }

    public void setError(JPushError error) {
      this.error = error;
    }
  }

  static class JPushError {
    private String code;
    private String message;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
