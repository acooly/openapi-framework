package com.acooly.openapi.framework.service.test;

import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.UUID;

/** @author qiubo@yiji.com */
@Slf4j
public class CreateOrderApiServiceTest extends AbstractApiServieTests {
  @Test
  public void name() throws Exception {
    String url = "http://127.0.0.1:8089/gateway.do";
    CreateOrderRequest request = new CreateOrderRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setMerchOrderNo("1234567890=-09876543");
    request.setTitle("同步请求创建订单");
    request.setPayeeUserId("12345678900987654321");
    request.setPayerUserId("09876543211234567890");
    request.setBuyerUserId("09876543211234567890");
    request.setBuyeryEmail("qiuboboy@qq.com");
    request.setBuyeryMobileNo("13898765453");
    request.setBuyerCertNo("330702194706165014");
    request.setPassword(encrypt("12312312"));
    request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
    request.setService("createOrder");
    request.setPartnerId("test");
    String body = JSON.toJSONString(request);
    String responseBody =
        HttpRequest.post(url)
            .header("signType", "MD5")
            .header("sign", sign(body))
            .send(body)
            .body();
    log.info("{}", responseBody);
  }

  public String sign(String body) {
    return DigestUtils.md5Hex(body + key);
  }
}
