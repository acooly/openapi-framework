package com.yiji.framework.openapi.core.marshall;

import com.alibaba.fastjson.JSONException;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.yiji.framework.openapi.common.annotation.OpenApiAlias;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.convert.ApiServiceConversionService;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.core.exception.impl.ApiServiceParamFormatException;
import com.yiji.framework.openapi.common.utils.json.JsonMarshallor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现对对象属性的访问 如果是复杂对象,用json字符串存取.
 *
 * @author qzhanbo@yiji.com
 */
public class ObjectAccessor<T> {
    private static final Logger logger = LoggerFactory.getLogger(ObjectAccessor.class);

    private final T target;
    private static ConversionService conversionService = ApiServiceConversionService.INSTANCE;

    private static JsonMarshallor jsonMarshallor = JsonMarshallor.INSTANCE;

    private Map<String, Field> fieldMap;
    private final static ConcurrentMap<Class<?>, Map<String, Field>> classMap = Maps.newConcurrentMap();

    /**
     * 忽略transient字段
     */
    private final static Predicate<Field> transientDenyPredicate = new Predicate<Field>() {
        @Override
        public boolean apply(Field input) {
            if (input == null) {
                return false;
            }

            return !Modifier.isTransient(input.getModifiers());
        }
    };
    /**
     * 忽略transient字段
     */
    private final static Predicate<Field> allAcceptPredicate = new Predicate<Field>() {
        @Override
        public boolean apply(Field input) {
            return true;
        }
    };

    public static <T> ObjectAccessor<T> of(T target) {
        return new ObjectAccessor<T>(target);
    }

    private ObjectAccessor(T target) {
        Assert.notNull(target, "Target object must not be null");
        this.target = target;
        Map<String, Field> fieldMap = classMap.get(target.getClass());
        if (fieldMap == null) {
            final Map<String, Field> tmpMap = Maps.newHashMap();
            ReflectionUtils.doWithFields(this.target.getClass(), new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) {
                    OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
                    if (openApiField == null) {
                        logger.warn("发现没有标注OpenApiField的字段{}", field);
                        return;
                    }
                    if (tmpMap.containsKey(field.getName())) {
                        throw new ApiServiceException(ApiServiceResultCode.FIELD_NOT_UNIQUE,
                                field + "和" + tmpMap.get(field.getName()) + "重名");
                    } else {
                        tmpMap.put(field.getName(), field);
                    }
                }
            });
            classMap.put(target.getClass(), tmpMap);
            fieldMap = tmpMap;
        }
        this.fieldMap = fieldMap;
    }

    /**
     * 判断字段是否需要加密
     *
     * @param property
     * @return
     */
    public boolean isSecurityField(String property) {
        Field field = this.fieldMap.get(property);
        if (field == null) {
            return false;
        }
        OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
        return openApiField != null && openApiField.security();
    }

    public String getFieldAlias(String property) {
        Field field = this.fieldMap.get(property);
        if (field == null) {
            return property;
        }
        OpenApiAlias openApiAlias = field.getAnnotation(OpenApiAlias.class);
        return (openApiAlias != null && Strings.isNullOrEmpty(openApiAlias.value())) ? openApiAlias.value() : property;
    }

    /**
     * 获取对象属性名集合
     *
     * @return
     */
    public Set<String> propertySet() {
        return this.fieldMap.keySet();
    }

    /**
     * 获取非transient属性名集合
     *
     * @return
     */
    public Set<String> propertySetExcludeTransient() {
        Set<String> fieldSet = Sets.newHashSet();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            if (!Modifier.isTransient(entry.getValue().getModifiers())) {
                fieldSet.add(entry.getKey());
            }
        }
        return fieldSet;
    }

    /**
     * 获取属性字符串 如果属性标注OpenApiField.type=JSON,则设置json字符串
     *
     * @param propertyName
     * @return
     * @throws org.springframework.beans.BeansException
     */
    public String getPropertyValue(String propertyName) throws BeansException {
        Field field = this.fieldMap.get(propertyName);
        if (field == null) {
            return null;
        }
        Object obj = null;
        try {
            ReflectionUtils.makeAccessible(field);
            obj = field.get(this.target);
            if (obj == null) {
                return null;
            }
            if (isCollection(field)) {
                return jsonMarshallor.marshall(obj);
            }
            // 处理复杂类型会报错
            return conversionService.convert(obj, String.class);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            logger.error("属性{}读取失败", field.getType(), ex);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "property=" + propertyName + "读取失败");
        } catch (ConverterNotFoundException ex) {
            return jsonMarshallor.marshall(obj);
        } catch (Exception ex) {
            logger.error("读取属性失败", ex);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "property=" + propertyName + "读取失败");
        }
    }

    /**
     * 设置属性 如果属性标注OpenApiField.type=JSON,则设置json字符串
     *
     * @param propertyName
     * @param newValue
     */
    public void setPropertyValue(String propertyName, Object newValue) {
        Field field = this.fieldMap.get(propertyName);
        if (field == null || newValue == null) {
            return;
        }
        try {
            ReflectionUtils.makeAccessible(field);
            if (isCollection(field)) {
                parse((String) newValue, field);
                return;
            } else {
                Object convertedValue = conversionService.convert(newValue, field.getType());
                field.set(this.target, convertedValue);
            }

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            logger.error("属性{}设置失败", field.getType(), ex);
            throw new ApiServiceParamFormatException(field.getName(), newValue, field.getType().getSimpleName());
        } catch (ConverterNotFoundException ex) {
            parse((String) newValue, field);
        } catch (ConversionFailedException cfe) {
            String enumOptions = null;
            if (field.getType().isEnum()) {
                // 如果枚举值为空,忽略
                if (Strings.isNullOrEmpty(newValue.toString())) {
                    return;
                }

                Object[] objects = field.getType().getEnumConstants();
                enumOptions = Arrays.toString(objects);
            } else {
                logger.error("属性{}设置失败", field.getType(), cfe);
            }
            throw new ApiServiceParamFormatException(field.getName(), newValue,
                    field.getType().getSimpleName() + (enumOptions != null ? enumOptions : ""));
        } catch (ApiServiceParamFormatException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("属性{}设置失败,值={}", field.toString(), newValue);
            throw new ApiServiceParamFormatException(field.getName(), newValue, field.getType().getSimpleName());
        }
    }

    private boolean isCollection(Field field) {
        return List.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())
                || Set.class.isAssignableFrom(field.getType()) || field.getType().isArray();
    }

    private boolean isJSONObject(Field field) {
        Class type = field.getType();
        return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type) || type.isArray()
                || Number.class.isAssignableFrom(type) || type.isPrimitive() || Boolean.class.isAssignableFrom(type);
    }

    /**
     * 获取对象数据,忽略transient字段,复杂对象转换为json
     *
     * @return
     */
    public Map<String, String> getAllDataExcludeTransient() {
        return getAllData(transientDenyPredicate);
    }

    public Map<String, Object> getMarshallData() {
        Assert.notNull(target, "predicate must not be null");
        Map<String, Object> dataMap = Maps.newTreeMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            if (!transientDenyPredicate.apply(entry.getValue())) {
                continue;
            }
            Object value = this.getMarshallValue(entry.getKey());
            if (value != null) {
                dataMap.put(entry.getKey(), value);
            }
        }
        return dataMap;
    }

    public Object getMarshallValue(String propertyName) throws BeansException {
        Field field = this.fieldMap.get(propertyName);
        if (field == null) {
            return null;
        }
        Object obj = null;
        try {
            ReflectionUtils.makeAccessible(field);
            obj = field.get(this.target);
            if (obj == null) {
                return null;
            } else if (obj instanceof String || isJSONObject(field)) {
                return obj;
            } else if (isCollection(field)) {
                return jsonMarshallor.marshall(obj);
            }
            // 处理复杂类型会报错
            return conversionService.convert(obj, String.class);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            logger.error("属性{}读取失败", field.getType(), ex);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "property=" + propertyName + "读取失败");
        } catch (ConverterNotFoundException ex) {
            return jsonMarshallor.marshall(obj);
        } catch (Exception ex) {
            logger.error("读取属性失败", ex);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "property=" + propertyName + "读取失败");
        }
    }

    /**
     * 获取对象所有属性,复杂对象转换为json
     *
     * @param predicate
     * @return
     */
    public Map<String, String> getAllData(Predicate<Field> predicate) {
        Assert.notNull(target, "predicate must not be null");
        Map<String, String> dataMap = Maps.newTreeMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            if (!predicate.apply(entry.getValue())) {
                continue;
            }
            String value = this.getPropertyValue(entry.getKey());
            if (value != null) {
                dataMap.put(entry.getKey(), value);
            }
        }
        return dataMap;
    }

    /**
     * 为了json转换,需要把某些对象保留类型.
     *
     * @param
     * @return 获得此map后, 在通过json Marshall 输出.
     */
    public Map<String, Object> getAllDataExcludeTransientForJsonProcess() {
        return getAllDataForJsonProcess(transientDenyPredicate);
    }

    public Map<String, Object> getAllDataForJsonProcess(Predicate<Field> predicate) {
        Assert.notNull(target, "predicate must not be null");
        Map<String, Object> dataMap = Maps.newHashMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            if (!predicate.apply(entry.getValue())) {
                continue;
            }

            Field field = entry.getValue();
            Object obj = null;
            try {
                ReflectionUtils.makeAccessible(field);
                obj = field.get(this.target);
                if (obj == null) {
                    continue;
                }
                if (obj instanceof String) {
                    dataMap.put(entry.getKey(), obj);
                    continue;
                } else if (isJSONObject(field)) {
                    dataMap.put(entry.getKey(), obj);
                    continue;
                }
                String value = conversionService.convert(obj, String.class);
                dataMap.put(entry.getKey(), value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error("属性{}读取失败", field.getType(), ex);
            } catch (ConverterNotFoundException ex) {
                dataMap.put(entry.getKey(), obj);
            } catch (Exception ex) {
                logger.error("读取属性失败", ex);
            }
        }
        return dataMap;
    }

    /**
     * 获取对象所有属性,复杂对象转换为json
     *
     * @return
     */
    public Map<String, String> getAllData() {
        return getAllData(allAcceptPredicate);
    }

    private void parse(String newValue, Field field) {
        try {
            TypeToken typeToken = TypeToken.of(field.getGenericType());
            Object convertedValue = jsonMarshallor.parse(newValue, typeToken.getType());
            field.set(this.target, convertedValue);
        } catch (Exception e) {
            logger.error("属性{}设置失败,值={},原因={}", field.toString(), newValue, e.getMessage());
            if (e instanceof JSONException) {
                checkEnumConvertException(e, newValue, field);
                Throwable cause = e.getCause();
                if (cause instanceof ApiServiceParamFormatException) {
                    ApiServiceParamFormatException ex = (ApiServiceParamFormatException) cause;
                    String requiredTypeOrFormat = ex.getRequiredTypeOrFormat();
                    throw new ApiServiceParamFormatException(field.getName(), newValue,
                            field.getType().getSimpleName() + "." + requiredTypeOrFormat);
                }
            }
            throw new ApiServiceParamFormatException(field.getName(), newValue, field.getType().getSimpleName());
        }

    }

    private void checkEnumConvertException(Exception e, String newValue, Field field) {
        String message = e.getMessage();
        if (message != null && message.indexOf("enum") > -1) {
            String noExists = "No enum constant";
            // 解析 com.alibaba.fastjson.JSONException: No enum constant
            // com.yjf.common.payengine.enums.BankCardTypeEnum.DEBIT
            if (message.contains(noExists)) {
                String msg = "枚举类型:"
                        + message.substring(message.indexOf(noExists) + noExists.length(), message.length()) + "不存在";
                throw new ApiServiceParamFormatException(field.getName(), newValue, msg);
            }
            String parseError = "parse enum";
            String valueStr = ", value : ";
            // 解析 parse enum com.yjf.common.payengine.enums.BankCodeEnum error,
            // value : 123
            if (message.contains(parseError)) {
                String enumName = message.substring(message.indexOf(parseError) + 1 + parseError.length(),
                        message.indexOf("error") - 1);
                String value = message.substring(message.indexOf(valueStr) + valueStr.length());
                String msg = "枚举类型:" + enumName + " 值:" + value + "不存在";
                throw new ApiServiceParamFormatException(field.getName(), newValue, msg);
            }
        }

    }
}
