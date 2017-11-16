/** create by zhangpu date:2015年11月3日 */
package com.acooly.openapi.framework.app.biz.notify;

import java.util.List;

/**
 * APP 通知服务
 *
 * @author zhangpu
 * @date 2015年11月3日
 */
public interface AppNotifyService {

  /**
   * 群发
   *
   * @param notifyMessage 消息
   * @param targets 发送目标，一般使用唯一用户标识
   * @param save 是否持久化 true:持久化(系统消息)
   */
  void group(NotifyMessage notifyMessage, List<String> targets, boolean save);

  void group(NotifyMessage notifyMessage, List<String> targets, String sender, boolean save);

  /**
   * 广播
   *
   * @param notifyMessage
   */
  void broadcast(NotifyMessage notifyMessage, boolean save);
}
