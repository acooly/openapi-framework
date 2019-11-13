package com.acooly.openapi;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;

/** @author qiubo */
@BootApp(sysName = "openapi", httpPort = 8089)
public class Main {
  public static void main(String[] args) {
    Apps.setProfileIfNotExists("sdev");
    new SpringApplication(TestMain.class).run(args);
  }
}
