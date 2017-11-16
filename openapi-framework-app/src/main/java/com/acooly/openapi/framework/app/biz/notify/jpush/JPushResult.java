/** create by zhangpu date:2015年11月5日 */
package com.acooly.openapi.framework.app.biz.notify.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JPush 响应对象
 *
 * @author zhangpu
 * @date 2015年11月5日
 */
public class JPushResult {

  /** http 状态码 */
  private int httpStatus;

  private String code;

  private String message;

  @JsonProperty("sendno")
  private String sendNo;

  @JsonProperty("msg_id")
  private String msgId;

  public int getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }

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

  public String getSendNo() {
    return sendNo;
  }

  public void setSendNo(String sendNo) {
    this.sendNo = sendNo;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  @Override
  public String toString() {
    return String.format(
        "{httpStatus:%s, code:%s, message:%s, sendNo:%s, msgId:%s}",
        httpStatus, code, message, sendNo, msgId);
  }
}
