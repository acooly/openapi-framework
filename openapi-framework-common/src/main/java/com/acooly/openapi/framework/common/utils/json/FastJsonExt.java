/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-05-19 00:40
 */
package com.acooly.openapi.framework.common.utils.json;

import com.acooly.core.utils.Reflections;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.utils.DataTypeUtils;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhangpu
 * @date 2019-05-19 00:40
 */
@Slf4j
public class FastJsonExt {


    public static void registerSerializer(SerializeConfig serializeConfig, Class<?> apiMessageClass) {
        // 只注册一层
        Set<Field> fields = Reflections.getFields(apiMessageClass.getClass());
        for (Field field : fields) {
            if (DataTypeUtils.isCollection(field) && !field.getType().isArray()) {
                Class clazz = DataTypeUtils.getCollectionGenericType(field);
                if (DataTypeUtils.isObject(clazz)) {
                    serializeConfig.put(clazz, createJavaBeanSerializer(clazz));
                }
            }
        }
        serializeConfig.put(apiMessageClass, createJavaBeanSerializer(apiMessageClass));
    }

    public static JavaBeanSerializer createJavaBeanSerializer(Class<?> clazz) {
        SerializeBeanInfo beanInfo = buildBeanInfo(clazz, (Map) null, null, false);
        return new JavaBeanSerializer(beanInfo);
    }

    /**
     * 扣取代码，重构
     *
     * @param beanType
     * @param aliasMap
     * @param propertyNamingStrategy
     * @param fieldBased
     * @return
     */
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
            // 替换默认JSONField.ordinal排序为OpenApiField.ordinal排序
            openApiFieldOrdinal((List) sortedFieldList);
//            Collections.sort((List) sortedFieldList);
        }

        FieldInfo[] sortedFields = new FieldInfo[((List) sortedFieldList).size()];
        ((List) sortedFieldList).toArray(sortedFields);
        if (Arrays.equals(sortedFields, fields)) {
            sortedFields = fields;
        }

        return new SerializeBeanInfo(beanType, jsonType, typeName, typeKey, features, fields, sortedFields);
    }

    public static void openApiFieldOrdinal(List<FieldInfo> fieldInfos) {

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
