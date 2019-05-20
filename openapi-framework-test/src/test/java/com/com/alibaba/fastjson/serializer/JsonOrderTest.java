/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-05-18 19:29
 */
package com.com.alibaba.fastjson.serializer;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.utils.DataTypeUtils;
import com.acooly.openapi.framework.common.utils.Ids;
import com.acooly.openapi.framework.common.utils.Reflections;
import com.acooly.openapi.framework.common.utils.json.MoneySerializer;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.OrderCreateRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhangpu
 * @date 2019-05-18 19:29
 */
@Slf4j
public class JsonOrderTest {


    @Test
    public void test2() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(Ids.getDid());
        request.setMerchOrderNo(Ids.getDid());
        request.setService("orderCreate");
        request.setTitle("同步请求创建订单\uD83D\uDC3E一休哥\uD83D\uDC3E ");
        request.setAmount(Money.cent(10021));
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("12345678900987654321");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext("asdfasdfasdfasdf");
        System.out.println(JSON.toJSONString(orderCreate()));
    }

    @Test
    public void testJsonOrder() {

//        ApiMessage apiMessage = new ApiMessage() {
//            @Override
//            public String getRequestNo() {
//                return Ids.getDid();
//            }
//        };
//        apiMessage.setPartnerId(Ids.getDid());
//        apiMessage.setService("testService");
//        apiMessage.setContext("zsdfadfasdfasdf");
//        Map ext = Maps.newHashMap();
//        ext.put("a", "b");
//        apiMessage.setExt(ext);

        OrderCreateRequest request = orderCreate();
        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
        serializeConfig.put(Money.class, MoneySerializer.INSTANCE);

        Set<Field> fields = Reflections.getFields(request.getClass());
        for (Field field : fields) {
            if (DataTypeUtils.isCollection(field)) {
                Class clazz = DataTypeUtils.getCollectionGenericType(field);
                log.info("clazz:{}", clazz.getName());
                if (DataTypeUtils.isObject(clazz)) {
                    serializeConfig.put(clazz, createJavaBeanSerializer(clazz));
                }
            }
        }


        serializeConfig.put(request.getClass(), createJavaBeanSerializer(request.getClass()));
        System.out.println(JSON.toJSONString(request));


    }

    private JavaBeanSerializer createJavaBeanSerializer(Class<?> clazz) {
        SerializeBeanInfo beanInfo = buildBeanInfo(clazz, (Map) null, null, false);
        return new JavaBeanSerializer(beanInfo);
    }

    private OrderCreateRequest orderCreate() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(Ids.getDid());
        request.setMerchOrderNo(Ids.getDid());
        request.setService("orderCreate");
        request.setTitle("同步请求创建订单\uD83D\uDC3E一休哥\uD83D\uDC3E ");
//        request.setAmount(Money.cent(10021));
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("12345678900987654321");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext("asdfasdfasdfasdf");
        List<GoodInfo> goodInfos = Lists.newArrayList();
        GoodInfo goodInfo = new GoodInfo();
        goodInfo.setGoodType(GoodType.actual);
        goodInfo.setName("天子精品");
        goodInfo.setPrice(Money.amout("400.00"));
        goodInfo.setReferUrl("http://acooly.cn/tianzi");
        goodInfos.add(goodInfo);

        request.setGoodsInfos(goodInfos);
        request.ext("xx", "oo");
        return request;
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> beanType, Map<String, String> aliasMap, PropertyNamingStrategy propertyNamingStrategy, boolean fieldBased) {
        JSONType jsonType = (JSONType) TypeUtils.getAnnotation(beanType, JSONType.class);
        String[] orders = null;
        String typeName = null;
        String typeKey = null;
        int features;
        if (jsonType != null) {
            orders = jsonType.orders();
            typeName = jsonType.typeName();
            if (typeName.length() == 0) {
                typeName = null;
            }

            PropertyNamingStrategy jsonTypeNaming = jsonType.naming();
            if (jsonTypeNaming != PropertyNamingStrategy.CamelCase) {
                propertyNamingStrategy = jsonTypeNaming;
            }

            features = SerializerFeature.of(jsonType.serialzeFeatures());

            for (Class supperClass = beanType.getSuperclass(); supperClass != null && supperClass != Object.class; supperClass = supperClass.getSuperclass()) {
                JSONType superJsonType = (JSONType) TypeUtils.getAnnotation(supperClass, JSONType.class);
                if (superJsonType == null) {
                    break;
                }

                typeKey = superJsonType.typeKey();
                if (typeKey.length() != 0) {
                    break;
                }
            }

            Class[] var16 = beanType.getInterfaces();
            int var18 = var16.length;

            for (int var12 = 0; var12 < var18; ++var12) {
                Class<?> interfaceClass = var16[var12];
                JSONType superJsonType = (JSONType) TypeUtils.getAnnotation(interfaceClass, JSONType.class);
                if (superJsonType != null) {
                    typeKey = superJsonType.typeKey();
                    if (typeKey.length() != 0) {
                        break;
                    }
                }
            }

            if (typeKey != null && typeKey.length() == 0) {
                typeKey = null;
            }
        } else {
            features = 0;
        }

        Map<String, Field> fieldCacheMap = new HashMap();
        ParserConfig.parserAllFieldToCache(beanType, fieldCacheMap);
        List<FieldInfo> fieldInfoList = fieldBased ? TypeUtils.computeGettersWithFieldBase(beanType, aliasMap, false, propertyNamingStrategy) : TypeUtils.computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, false, propertyNamingStrategy);
        FieldInfo[] fields = new FieldInfo[fieldInfoList.size()];
        fieldInfoList.toArray(fields);
        Object sortedFieldList;
        if (orders != null && orders.length != 0) {
            sortedFieldList = fieldBased ? TypeUtils.computeGettersWithFieldBase(beanType, aliasMap, true, propertyNamingStrategy) : TypeUtils.computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, true, propertyNamingStrategy);
        } else {
            sortedFieldList = new ArrayList(fieldInfoList);
            reSortByApiFiled((List) sortedFieldList);
//            Collections.sort((List) sortedFieldList);
        }

        FieldInfo[] sortedFields = new FieldInfo[((List) sortedFieldList).size()];
        ((List) sortedFieldList).toArray(sortedFields);
        if (Arrays.equals(sortedFields, fields)) {
            sortedFields = fields;
        }

        return new SerializeBeanInfo(beanType, jsonType, typeName, typeKey, features, fields, sortedFields);
    }

    public static void reSortByApiFiled(List<FieldInfo> fieldInfos) {


        Collections.sort(fieldInfos, new Comparator<FieldInfo>() {
            @Override
            public int compare(FieldInfo o1, FieldInfo o2) {
                int ordinal1 = getOrdinal(o1);
                int ordinal2 = getOrdinal(o2);
                if (ordinal1 < ordinal2) {
                    return -1;
                } else if (ordinal1 > ordinal2) {
                    return 1;
                }
                return 0;
            }

            private int getOrdinal(FieldInfo fieldInfo) {
                int ordinal = 0;
                OpenApiField openApiField = fieldInfo.getAnnation(OpenApiField.class);
                if (openApiField != null) {
                    ordinal = openApiField.ordinal();
                } else if (fieldInfo.getAnnotation() != null) {
                    ordinal = fieldInfo.getAnnotation().ordinal();
                }
                return ordinal;
            }
        });

    }


}
