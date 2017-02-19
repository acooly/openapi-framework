/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.yiji.framework.openapi.common.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.acooly.core.utils.Dates;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * 
 * @author calvin
 * @author zhangpu
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Reflections {
	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(Reflections.class);

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	public static List<Object> invokeGetter(Object obj, String[] propertyNames) {
		List<Object> list = new ArrayList<Object>(propertyNames.length);
		for (String propertyName : propertyNames) {
			Object propertyValue = null;
			if (StringUtils.contains(propertyName, ".")) {
				String[] propertyNamePaths = StringUtils.split(propertyName, ".");
				Object temp = obj;
				for (String propertyNamePath : propertyNamePaths) {
					if (temp == null) {
						break;
					}
					temp = Reflections.invokeGetter(temp, propertyNamePath);
				}
				propertyValue = temp;
			} else {
				propertyValue = Reflections.invokeGetter(obj, propertyName);
			}
			list.add(propertyValue);
		}
		return list;
	}

	public static List<String> invokeGetterToString(Object obj, String[] propertyNames) {
		List<Object> list = invokeGetter(obj, propertyNames);
		List<String> result = new ArrayList<String>(list.size());
		for (Object object : list) {
			if (object == null) {
				result.add(null);
			} else if (object instanceof Date) {
				result.add(Dates.format((Date) object));
			} else if (object instanceof Calendar) {
				result.add(Dates.format(((Calendar) object).getTime()));
			} else {
				result.add(object.toString());
			}
		}
		return result;
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		invokeMethodByName(obj, setterMethodName, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	public static Object getFieldValue(final Object obj, final Field field) {
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	public static void setFieldValue(final Object obj, final Field field, final Object value) {
		makeAccessible(field);
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj,
	 * Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
				.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
				.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg.
	 * public UserDao extends HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 如public UserDao
	 * extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class<?> getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}

	public static Set<String> getFieldNames(Object instance) {
		Class<?> pojoClass = instance.getClass();
		return getFieldNames(pojoClass);
	}

	public static Set<String> getFieldNames(Class<?> pojoClass) {
		Set<String> propertyNames = new HashSet<String>();
		Class<?> clazz = pojoClass;
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					propertyNames.add(field.getName());

				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null && !clazz.getSimpleName().equalsIgnoreCase("Object"));
		return propertyNames;
	}

	/**
	 * 根據類型返回類型所有的屬性Field，支持繼承向上搜索
	 * 
	 * @param pojoClass
	 * @return
	 */
	public static Set<Field> getFields(Class<?> pojoClass) {
		Set<Field> allFields = new HashSet<Field>();
		Class<?> clazz = pojoClass;
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					allFields.add(field);

				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null && !clazz.getSimpleName().equalsIgnoreCase("Object"));
		return allFields;
	}

	public static Set<String> getSimpleFieldNames(Class<?> pojoClass) {
		Set<String> propertyNames = new HashSet<String>();
		Class<?> clazz = pojoClass;
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers()) && (field.getType().isPrimitive()
						|| isWrapClass(field.getType()) || field.getType().isAssignableFrom(Timestamp.class)
						|| field.getType().isAssignableFrom(Date.class)
						|| field.getType().isAssignableFrom(String.class)
						|| field.getType().isAssignableFrom(Calendar.class))) {
					propertyNames.add(field.getName());
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null && !clazz.getSimpleName().equalsIgnoreCase("Object"));
		return propertyNames;
	}

	public static boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	/**
	 * 将对象参数转换为{@link Class}参数形式。
	 * 
	 * @param parameters
	 *            对象参数。
	 * @return 对象参数对应的{@link Class}参数，如果 parameters 为 null 则返回 null 。
	 */
	public static Class<?>[] processParameterToParameterType(Object... parameters) {
		if (parameters == null) {
			return null;
		}
		Class<?>[] parameter = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameter[i] = parameters[i].getClass();
		}
		return parameter;
	}

	/**
	 * 创建一个对象实例。该方法会根据参数自动判断参数类型。
	 * 
	 * @param <T>
	 *            实例对象的类型。
	 * @param clazz
	 *            要创建对象的类的{@link Class}对象。
	 * @param parameters
	 *            要传递给构造方法的参数。
	 * @return 对应的实例。
	 * @throws NoSuchMethodException
	 *             如果没有指定的构造方法。
	 * @throws InstantiationRuntimeException
	 *             生成类的实例时发生错误。
	 * @throws InvocationTargetRunTimeException
	 *             如果构造方法抛出异常。
	 */
	public static <T> T createObject(Class<T> clazz, Object... parameters) throws NoSuchMethodException {
		if (clazz == null) {
			throw new RuntimeException("不能'null' 创建实例。");
		}
		Class<?>[] parameterTypes = processParameterToParameterType(parameters);
		return createObject(clazz, parameterTypes, parameters);
	}

	/**
	 * 创建一个对象实例。
	 * 
	 * @param <T>
	 *            实例对象的类型
	 * @param clazz
	 *            要创建对象的类的{@link Class}对象。
	 * @param parameterTypes
	 *            参数的类型。
	 * @param parameters
	 *            要传递给构造方法的参数。
	 * @return 对应的实例。
	 * @throws NoSuchMethodException
	 *             如果没有指定的构造方法。
	 * @throws InstantiationRuntimeException
	 *             生成类的实例时发生错误。
	 * @throws InvocationTargetRunTimeException
	 *             如果构造方法抛出异常。
	 */
	public static <T> T createObject(Class<T> clazz, Class<?>[] parameterTypes, Object[] parameters)
			throws NoSuchMethodException {
		if (clazz == null) {
			throw new RuntimeException("不能 'null' 创建实例。");
		}
		Constructor<T> constructor;
		if (ArrayUtils.isEmpty(parameterTypes)) {
			constructor = clazz.getDeclaredConstructor();
			parameters = null;
		} else {
			constructor = clazz.getDeclaredConstructor(parameterTypes);
		}
		return createObject(constructor, parameters);
	}

	/**
	 * 使用构造方法的 Constructor 对象创建一个对象实例。
	 * 
	 * @param constructor
	 *            构造方法的 Constructor 对象。
	 * @param parameters
	 *            要传递给构造方法的参数。
	 * @return 对应的实例。
	 * @throws NullPointerException
	 *             如果 constructor 为 null 。
	 * @throws InstantiationRuntimeException
	 *             生成类的实例时发生错误。
	 * @throws InvocationTargetRunTimeException
	 *             如果构造方法抛出异常。
	 */
	public static <T> T createObject(Constructor<T> constructor, Object... parameters) {
		if (!Modifier.isPublic(constructor.getModifiers())) {
			constructorSetAccessible(constructor);
		}
		try {
			return constructor.newInstance(parameters);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("向该构造方法传递了一个不正确的参数。");
		} catch (InstantiationException e) {
			throw new RuntimeException("无法为接口或者抽象类创建实例。");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("预创建的对象无法构造。");
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getTargetException());
		}
	}

	public static void constructorSetAccessible(Constructor<?> constructor) {
		setAccessible(constructor);
	}

	private static void setAccessible(final AccessibleObject accessibleObject) {
		if (!accessibleObject.isAccessible()) {
			if (System.getSecurityManager() != null) {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						accessibleObject.setAccessible(true);
						return null;
					}
				});
			} else {
				accessibleObject.setAccessible(true);
			}
		}
	}

}
