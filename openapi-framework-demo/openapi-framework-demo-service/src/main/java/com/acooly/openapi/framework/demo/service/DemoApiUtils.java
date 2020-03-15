/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 14:31
 */
package com.acooly.openapi.framework.demo.service;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.acooly.openapi.framework.demo.message.enums.GoodType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author zhangpu
 * @date 2020-03-05 14:31
 */
@Slf4j
public class DemoApiUtils {

    public static final String API_DEMO_DOC_TYPE_CODE = "demo";
    public static final String API_DEMO_DOC_TYPE_NAME = "演示";

    static String[] GOODS_NAMES = {
            "腾讯视频VIP卡", "华为MateBook13", "MAC-PRO14", "保暖杯", "咖啡杯",
            "咖啡", "马克杯", "充电器", "龙凤呈祥", "天子",
            "大彩黄鹤楼", "瑞士军刀", "DoStyle27寸", "牙签", "心相印抽纸",
            "小音箱", "鼠标", "橙子4个", "剃须刀", "威化饼干"
    };

    public static List<GoodsInfo> buildGoodsList(@Max(20) @Min(1) int size, String searchName) {
        List<GoodsInfo> goodsInfos = Lists.newArrayList();
        GoodsInfo goodsInfo = null;
        for (int i = 0; i < size; i++) {
            goodsInfo = new GoodsInfo(GOODS_NAMES[i % 20], i % 2 == 0 ? GoodType.virtual : GoodType.actual, (i + 1),
                    Money.cent(RandomUtils.nextLong(10000 * i, 10000 * (i + 1))));
            if (Strings.isNotBlank(searchName) && !Strings.contains(goodsInfo.getName(), searchName)) {
                continue;
            }
            goodsInfos.add(goodsInfo);
        }
        return goodsInfos;
    }

}
