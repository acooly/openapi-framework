package com.acooly.openapi.framework.core.log;

import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.Types;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangpu
 * @date 2019-05-23 17:23
 */
@Slf4j
public class SafetyLog {

    private static final Map<Class<? extends ApiMessage>, Map<String, Annotation>> holder = Maps.newConcurrentMap();


    public static Map<String, Annotation> getSafetyProperties(Class<? extends ApiMessage> clazz) {
        if (clazz == null) {
            return null;
        }
        if (holder.get(clazz) == null) {
            holder.put(clazz, getSafetyProperties(clazz, Maps.newHashMap()));
        }
        return holder.get(clazz);
    }

    private static Map<String, Annotation> getSafetyProperties(Class<?> clazz, Map<String, Annotation> safetyProperties) {
        Set<Field> fields = Reflections.getFields(clazz);
        for (Field field : fields) {
            if (Types.isJavaBean(field.getType())) {
                // JavaBean
                safetyProperties.putAll(getSafetyProperties(field.getType(), safetyProperties));
            } else if (Types.isCollection(field.getType())) {
                // 获取泛型，如果有泛型且是JavaBean，继续处理
                Class<?> genericType = Reflections.getParameterGenericType(field);
                if (genericType != null) {
                    safetyProperties.putAll(getSafetyProperties(genericType, safetyProperties));
                }
            } else if (Types.isMap(field.getType())) {
                // map暂时不支持（不建议使用Map结构）
            } else {
                ToString.Invisible invisible = AnnotationUtils.findAnnotation(field, ToString.Invisible.class);
                if (invisible != null) {
                    safetyProperties.put(field.getName(), invisible);
                    continue;
                }
                ToString.Maskable maskable = AnnotationUtils.findAnnotation(field, ToString.Maskable.class);
                if (maskable != null) {
                    safetyProperties.put(field.getName(), maskable);
                    continue;
                }
            }
        }
        return safetyProperties;
    }


}
