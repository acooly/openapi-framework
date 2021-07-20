/**
 * acooly-coder-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-06-08 11:00
 */
package com.acooly.openapi.test.jspftl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * @author zhangpu
 * @date 2021-06-08 11:00
 */
@Slf4j
public class JspToFtlTest {

    @Test
    public void test() throws Exception{

        String jspPath = "/Users/zhangpu/workspace/acooly/v5/openapi-framework-5/openapi-framework-apidoc/src/main/resources/META-INF/resources/WEB-INF/jsp/manage/apidoc/apiDocScheme.jsp";
        String jspContent = FileUtils.readFileToString(new File(jspPath),"UTF-8");




    }

}
