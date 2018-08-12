/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-01-02 15:23 创建
 */
package com.acooly.openapi.apidoc;

import com.acooly.core.common.boot.Apps;
import com.acooly.module.test.AppTestBase;
import com.acooly.openapi.apidoc.generator.ApiDocGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhangpu 2018-01-02 15:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Main.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ApiDocBuilderTest extends AppTestBase {

    public static final String PROFILE = "sdev";

    //设置环境
    static {
        Apps.setProfileIfNotExists(PROFILE);
    }

    @Autowired
    ApiDocGenerator apiDocGenerator;

    @Test
    public void testBuilder() {
        apiDocGenerator.build();
    }


}
