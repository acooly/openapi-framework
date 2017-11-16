/** create by zhangpu date:2015年11月22日 */
package com.acooly.openapi.framework.app.biz.notify.jpush.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2015年11月22日
 */
public class JPushNotification {

  private Android android;

  private IOS ios;

  public Android getAndroid() {
    return android;
  }

  public void setAndroid(Android android) {
    this.android = android;
  }

  public IOS getIos() {
    return ios;
  }

  public void setIos(IOS ios) {
    this.ios = ios;
  }

  public static class Android {
    /**
     * 通知内容
     *
     * <p>这里指定了，则会覆盖上级统一指定的 alert 信息；内容可以为空字符串，则表示不展示到通知栏。
     */
    @NotBlank private String alert;

    /**
     * 通知标题
     *
     * <p>如果指定了，则通知里原来展示 App名称的地方，将展示成这个字段。
     */
    private String title;

    /**
     * 通知栏样式ID
     *
     * <p>Android SDK 可设置通知栏样式，这里根据样式 ID 来指定该使用哪套样式。
     */
    @JsonProperty("builder_id")
    private Integer builderId;

    /**
     * 扩展字段
     *
     * <p>这里自定义 JSON 格式的 Key/Value 信息，以供业务使用。
     */
    private Map<String, Object> extras;

    public String getAlert() {
      return alert;
    }

    public void setAlert(String alert) {
      this.alert = alert;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public Integer getBuilderId() {
      return builderId;
    }

    public void setBuilderId(Integer builderId) {
      this.builderId = builderId;
    }

    public Map<String, Object> getExtras() {
      return extras;
    }

    public void setExtras(Map<String, Object> extras) {
      this.extras = extras;
    }
  }

  public static class IOS {
    /**
     * 通知内容
     *
     * <p>这里指定了，则会覆盖上级统一指定的 alert 信息；内容可以为空字符串，则表示不展示到通知栏。
     */
    @NotBlank private String alert;

    /**
     * 通知提示声音
     *
     * <p>如果无此字段，则此消息无声音提示；有此字段，如果找到了指定的声音就播放该声音，否则播放默认声音,如果此字段为空字符串，iOS 7 为默认声音，iOS 8 为无声音。(消息)
     * 说明：JPush 官方 API Library (SDK) 会默认填充声音字段。提供另外的方法关闭声音。
     */
    private String sound;

    /**
     * 应用角标
     *
     * <p>如果不填，表示不改变角标数字；否则把角标数字改为指定的数字；为 0 表示清除。JPush 官方 API Library(SDK)
     * 会默认填充badge值为"+1",详情参考：badge +1。
     */
    private Integer badge;

    /**
     * 推送唤醒
     *
     * <p>推送的时候携带"content-available":true 说明是 Background Remote Notification，如果不携带此字段则是普通的Remote
     * Notification。
     */
    @JsonProperty("content-available")
    private String contentAvailable;

    /** IOS8才支持。设置APNs payload中的"category"字段值 */
    private String category;

    /**
     * 扩展字段
     *
     * <p>这里自定义 JSON 格式的 Key/Value 信息，以供业务使用。
     */
    private Map<String, Object> extras;

    public String getAlert() {
      return alert;
    }

    public void setAlert(String alert) {
      this.alert = alert;
    }

    public String getSound() {
      return sound;
    }

    public void setSound(String sound) {
      this.sound = sound;
    }

    public Integer getBadge() {
      return badge;
    }

    public void setBadge(Integer badge) {
      this.badge = badge;
    }

    public String getContentAvailable() {
      return contentAvailable;
    }

    public void setContentAvailable(String contentAvailable) {
      this.contentAvailable = contentAvailable;
    }

    public String getCategory() {
      return category;
    }

    public void setCategory(String category) {
      this.category = category;
    }

    public Map<String, Object> getExtras() {
      return extras;
    }

    public void setExtras(Map<String, Object> extras) {
      this.extras = extras;
    }
  }
}
