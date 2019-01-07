package com.acooly.openapi.apidoc;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ZHANGPU
 */
@BootApp(sysName = "apidoc", httpPort = 8089)
@ComponentScan({"com.acooly.module","com.acooly.openapi.apidoc"})
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("sdev");
        new SpringApplication(Main.class).run(args);
    }
}
