/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush.dto;

import com.acooly.core.utils.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zhangpu
 * @date 2015年11月4日
 */
public class JPushOptions {

  /** 推送序号 纯粹用来作为 API 调用标识，API 返回时被原样返回，以方便 API 调用方匹配请求与返回。 */
  @JsonProperty("sendno")
  private Integer sendNo;
  /**
   * 离线消息保留时长
   *
   * <p>推送当前用户不在线时，为该用户保留多长时间的离线消息，以便其上线时再次推送。默认 86400 （1 天），最长 10 天。设置为 0
   * 表示不保留离线消息，只有推送当前在线的用户可以收到。
   */
  @JsonProperty("time_to_live")
  private Integer timeToLive;
  /**
   * 要覆盖的消息ID
   *
   * <p>如果当前的推送要覆盖之前的一条推送，这里填写前一条推送的 msg_id 就会产生覆盖效果，即：1）该 msg_id 离线收到的消息是覆盖后的内容；2）即使该 msg_id
   * Android 端用户已经收到，如果通知栏还未清除，则新的消息内容会覆盖之前这条通知；覆盖功能起作用的时限是：1 天。 如果在覆盖指定时限内该 msg_id 不存在，则返回 1003
   * 错误，提示不是一次有效的消息覆盖操作，当前的消息不会被推送。
   */
  @JsonProperty("override_msg_id")
  private Long overrideMsgId;
  /**
   * APNs是否生产环境
   *
   * <p>True 表示推送生产环境，False 表示要推送开发环境； 如果不指定则为推送生产环境。
   */
  @JsonProperty("apns_production")
  private Boolean apnsProduction;
  /**
   * 定速推送时长（分钟）
   *
   * <p>又名缓慢推送，把原本尽可能快的推送速度，降低下来，在给定的 n 分钟内，均匀地向这次推送的目标用户推送。最大值为 1440。未设置则不是定速推送。
   */
  @JsonProperty("big_push_duration")
  private Integer bigPushDuration;

  public JPushOptions() {}

  public JPushOptions(Integer timeToLive, Boolean apnsProduction) {
    super();
    this.timeToLive = timeToLive;
    this.apnsProduction = apnsProduction;
  }

  public Integer getSendNo() {
    return sendNo;
  }

  public void setSendNo(Integer sendNo) {
    this.sendNo = sendNo;
  }

  public Integer getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(Integer timeToLive) {
    this.timeToLive = timeToLive;
  }

  public Long getOverrideMsgId() {
    return overrideMsgId;
  }

  public void setOverrideMsgId(Long overrideMsgId) {
    this.overrideMsgId = overrideMsgId;
  }

  public Boolean getApnsProduction() {
    return apnsProduction;
  }

  public void setApnsProduction(Boolean apnsProduction) {
    this.apnsProduction = apnsProduction;
  }

  public Integer getBigPushDuration() {
    return bigPushDuration;
  }

  public void setBigPushDuration(Integer bigPushDuration) {
    this.bigPushDuration = bigPushDuration;
  }

  @Override
  public String toString() {
    return JsonMapper.nonEmptyMapper().toJson(this);
  }
}
