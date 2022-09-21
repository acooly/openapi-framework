/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-20 15:27
 */
package com.acooly.openapi.test.apidoc;

import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.utils.ApiAnnotations;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author zhangpu
 * @date 2022-09-20 15:27
 */
@Slf4j
public class ApiAnnotationsTest {

    @Test
    public void testDemoApiMessage() {
//        GoodsInfo goodsInfo = ApiAnnotations.demoApiMessage(GoodsInfo.class);
//        log.info("goodsInfo: {}", goodsInfo);
        ApiAnnotations.MAX_LIST_SIZE = 10;
        MessageNode messageNode = ApiAnnotations.demoApiMessage(MessageNode.class);
        log.info("MessageNode: {}", messageNode);
    }


    @Test
    public void testAAA() {
        GoodsInfo goodsInfo = new GoodsInfo();
        Field field = Reflections.getAccessibleField(goodsInfo, "goodType");
        System.out.println(field.getType().isEnum());
        Object[] objects = field.getType().getEnumConstants();
        System.out.println(field.getType().getEnumConstants()[0]);
        Messageable messageable = null;
        for (Object object : objects) {
            messageable = (Messageable) object;
            System.out.println(object);
        }
    }

}
