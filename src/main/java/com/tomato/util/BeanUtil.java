package com.tomato.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class BeanUtil {
    private static final Map<Class<?>, Map<String, WritableProperty>> cacheWritableProperties = new ConcurrentHashMap<>(); // Synchronized

    // Prevent instantiation
    private BeanUtil() {
        super();
    }

    /**
     * @param <T>
     * @param value
     * @param requiredType
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertBigDecimal(BigDecimal value, Class<T> requiredType) {
        if (null == value || null == requiredType) {
            return (T) value;
        } else if (requiredType.equals(double.class) || requiredType.equals(Double.class)) {
            return (T) new Double(value.doubleValue());
        } else if (requiredType.equals(int.class) || requiredType.equals(Integer.class)) {
            return (T) Integer.valueOf(value.intValue());
        } else if (requiredType.equals(short.class) || requiredType.equals(Short.class)) {
            return (T) new Short(value.shortValue());
        } else if (requiredType.equals(byte.class) || requiredType.equals(Byte.class)) {
            return (T) new Byte(value.byteValue());
        } else if (requiredType.equals(long.class) || requiredType.equals(Long.class)) {
            return (T) Long.valueOf(value.longValue());
        } else if (requiredType.equals(float.class) || requiredType.equals(Float.class)) {
            return (T) new Float(value.floatValue());
        } else {
            return (T) value;
        }
    }

    /**
     * @param value
     * @param type
     *
     * @return
     */
    public static boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (null == value || type.isInstance(value)) {
            return true;
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.class.isInstance(value);
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return Short.class.isInstance(value);
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return Byte.class.isInstance(value);
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return Long.class.isInstance(value);
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.class.isInstance(value);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return Float.class.isInstance(value);
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            return Character.class.isInstance(value);
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.class.isInstance(value);
        } else {
            return false;
        }
    }

    /**
     * @param <T>
     * @param type
     *
     * @return 永远不会返回null
     */
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("试图实例化不能实例化类: " + type.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("试图实例化类权限不足: " + type.getName(), e);
        }
    }

    /**
     * @param className
     *
     * @return 永远不会返回null
     *
     * @throws ClassNotFoundException
     */
    public static Object newInstance(String className) throws ClassNotFoundException {
        return newInstance(ClassUtil.loadClass(className));
    }

    /**
     * @param <T>
     * @param type
     * @param parameterTypes
     * @param parameters
     *
     * @return
     */
    public static <T> T newInstance(Class<T> type, Class<?>[] parameterTypes, Object[] parameters) {
        T result = null;
        if (parameters == null) {
            parameters = new Object[0];
        }
        if (parameterTypes == null) {
            parameterTypes = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                // TODO null?
                parameterTypes[i] = parameters[i].getClass();
            }
        }
        if (parameters.length != parameterTypes.length) {
            throw new RuntimeException("声明的构造器参数个数与传入的参数个数必须相同");
        }
        Constructor<T> constructor = null;
        try {
            constructor = type.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("在[%s]中没有找到参数类型的构造器", type.getName()), e);
        } catch (SecurityException e) {
            throw new RuntimeException(String.format("不允许调用[{}]中的该构造函数", type.getName()), e);
        }
        try {
            result = constructor.newInstance(parameters);
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("[{}]不能被初始化", type.getName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("权限不足, 无法调用[{}]的构造函数", type.getName()), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("调用[{}]构造函数时传递的参数不正确", type.getName()), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(String.format("调用[{}]的构造函数执行时抛出异常", type.getName()), e);
        }
        return result;
    }

    /**
     * @param <T>
     * @param type
     * @param parameters
     *
     * @return
     */
    public static <T> T newInstance(Class<T> type, Object[] parameters) {
        return newInstance(type, null, parameters);
    }

    /**
     * @author WuJianqiang
     * @since Jul 21, 2016 9:47:42 PM
     */
    public static class WritableProperty {
        private String name;
        private Class<?> type;
        private Method getter;
        private Method setter;
        private boolean primitive;

        /**
         * @param name
         * @param type
         * @param getter
         * @param setter
         */
        public WritableProperty(String name, Class<?> type, Method getter, Method setter) {
            this.name = name;
            this.type = type;
            this.getter = getter;
            this.setter = setter;
            this.primitive = (null != type && type.isPrimitive());
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the type
         */
        public Class<?> getType() {
            return type;
        }

        /**
         * @return the getter
         */
        public Method getGetter() {
            return getter;
        }

        /**
         * @return the setter
         */
        public Method getSetter() {
            return setter;
        }

        /**
         * @return the primitive
         */
        public boolean isPrimitive() {
            return primitive;
        }

    }

    /**
     * @param type
     *
     * @return
     */
    public static Map<String, WritableProperty> getWritableProperties(Class<?> type) {
        Map<String, WritableProperty> result = cacheWritableProperties.get(type);
        if (null == result) {
            synchronized (type) {
                result = cacheWritableProperties.get(type);
                if (null == result) {
                    try {
                        String name;
                        Method setter, getter;
                        Class<?> propType;
                        WritableProperty wp;

                        result = new HashMap<>();
                        BeanInfo beanInfo = Introspector.getBeanInfo(type);
                        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                        for (PropertyDescriptor pd : pds) {
                            setter = pd.getWriteMethod();
                            if (null != setter) {
                                name = pd.getName();
                                propType = pd.getPropertyType();
                                getter = pd.getReadMethod();
                                wp = new WritableProperty(name, propType, getter, setter);
                                result.put(name.toLowerCase(), wp);
                            }
                        }
                        result = Collections.unmodifiableMap(result);
                        cacheWritableProperties.put(type, result);
                    } catch (IntrospectionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param wps
     * @param rsmd
     *
     * @return 永远不会返回null
     *
     * @throws SQLException
     */
    public static Map<Integer, WritableProperty> mapPropertyToColumn(Map<String, WritableProperty> wps, ResultSetMetaData rsmd) throws SQLException {
        String column, key, keyAlt;
        WritableProperty wp;

        Map<Integer, WritableProperty> iwps = new HashMap<>();
        int colCount = rsmd.getColumnCount();
        for (int j = 1; j <= colCount; ++j) {
            // DO NOT USE getColumnName()
            column = rsmd.getColumnLabel(j);
            if (null != column) {
                key = column.toLowerCase();
                wp = wps.get(key);
                if (null != wp) {
                    iwps.put(j, wp);
                } else {
                    keyAlt = StringUtil.deleteAll(key, '_');
                    if (!key.equals(keyAlt)) {
                        wp = wps.get(keyAlt);
                        if (null != wp) {
                            iwps.put(j, wp);
                        }
                    }
                }
            }
        }

        if (iwps.size() > 0) {
            return iwps;
        } else {
            throw new RuntimeException(String.format("在%d个属性和%d个列之间没有一个合适的匹配！", wps.size(), colCount));
        }
    }

    /**
     * @param wps
     * @param columns
     *
     * @return 永远不会返回null
     */
    public static Map<String, WritableProperty> mapPropertyToColumn(Map<String, WritableProperty> wps, Set<String> columns) {
        String key, keyAlt;
        WritableProperty wp;

        Map<String, WritableProperty> swps = new HashMap<>();
        for (String column : columns) {
            if (null != column) {
                key = column.toLowerCase();
                wp = wps.get(key);
                if (null != wp) {
                    swps.put(column, wp);
                } else {
                    keyAlt = StringUtil.deleteAll(key, '_');
                    if (!key.equals(keyAlt)) {
                        wp = wps.get(keyAlt);
                        if (null != wp) {
                            swps.put(column, wp);
                        }
                    }
                }
            }
        }

        if (swps.size() > 0) {
            return swps;
        } else {
            throw new RuntimeException(String.format("在%d个属性和%d个列之间没有一个合适的匹配！", wps.size(), columns.size()));
        }
    }

    /**
     * @param bean
     * @param pd
     *
     * @return
     */
    public static Object callGetter(Object bean, PropertyDescriptor pd) {
        Method getter = pd.getReadMethod();
        if (null != getter) {
            return callGetter(bean, getter);
        } else {
            throw new RuntimeException(String.format("%s.%s属性没有读方法！", ClassUtil.getSimpleName(bean), pd.getName()));
        }
    }

    /**
     * @param bean
     * @param getter
     *
     * @return
     */
    public static Object callGetter(Object bean, Method getter) {
        try {
            return getter.invoke(bean);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bean
     * @param pd
     * @param value
     */
    public static void callSetter(Object bean, PropertyDescriptor pd, Object value) {
        Method setter = pd.getWriteMethod();
        if (null != setter) {
            callSetter(bean, setter, value);
        } else {
            throw new RuntimeException(String.format("%s.%s属性没有写方法！", ClassUtil.getSimpleName(bean), pd.getName()));
        }
    }

    /**
     * @param bean
     * @param setter
     * @param value
     */
    public static void callSetter(Object bean, Method setter, Object value) {
        Class<?> param = setter.getParameterTypes()[0];
        // TODO 整一个完整类型转换工具，可直接使用或参考 Spring framework
        if (value instanceof String) {
            // 必须 Number 范围宽的在前，param 范围窄的在后
            if (Number.class.isAssignableFrom(param)) {
                BigDecimal num = NumberUtil.bigDecimalOf((String) value, NumberUtil.INVALID_NUMBER);
                if (param.isAssignableFrom(BigDecimal.class)) {
                    value = num;
                } else {
                    value = convertBigDecimal(num, param);
                }
            } else if (param.isPrimitive() && (param.isAssignableFrom(int.class) || param.isAssignableFrom(long.class) //
                    || param.isAssignableFrom(byte.class) || param.isAssignableFrom(short.class) //
                    || param.isAssignableFrom(float.class) || param.isAssignableFrom(double.class))) {
                BigDecimal num = NumberUtil.bigDecimalOf((String) value, NumberUtil.INVALID_NUMBER);
                if (null == num) {
                    String format = "%s.%s(%s)方法参数为基本类型，不允许为空值(null)！";
                    throw new RuntimeException(String.format(format, ClassUtil.getSimpleName(bean), setter.getName(), ClassUtil.getSimpleName(param)));
                }
            } else if (param.isAssignableFrom(Date.class)) {
                Date date = DateUtil.dateOf((String) value, DateUtil.INVALID_DATE);
            } else if (param.isAssignableFrom(Calendar.class)) {
                Date date = DateUtil.dateOf((String) value, DateUtil.INVALID_DATE);
            }
        } else if (value instanceof BigDecimal && !param.equals(BigDecimal.class)) {
            value = convertBigDecimal((BigDecimal) value, param);
        }
        // Don't call setter if the value object isn't the right type
        if (isCompatibleType(value, param)) {
            try {
                setter.invoke(bean, value);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            String format = "数据类型(%s)与%s.%s(%s)方法参数不一致！";
            throw new RuntimeException(
                    String.format(format, ClassUtil.getSimpleName(value), ClassUtil.getSimpleName(bean), setter.getName(), ClassUtil.getSimpleName(param)));
        }
    }

    /**
     * @param names
     *
     * @return
     */
    private static String getNestedName(StringBuilder names) {
        if (names.length() > 0) {
            String nestedName;
            int index = names.indexOf(".");
            if (index < 0) {
                nestedName = names.toString();
                names.setLength(0);
            } else if (index > 0) {
                nestedName = names.substring(0, index);
                names.delete(0, index + 1);
            } else {
                throw new IllegalArgumentException(names.toString());
            }
            return nestedName;
        }
        return null;
    }

    /**
     * @param e
     * @param type
     * @param method
     * @param args
     *
     * @return
     */
    private static RuntimeException handleInvokeException(Exception e, Class<?> type, Method method, Object... args) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("Invocation of init method failed: ");
        if (null == type) {
            type = method.getDeclaringClass();
        }
        ClassUtil.buildMethodMessage(sb, ClassUtil.getSimpleName(type), method.getName(), ClassUtil.getClasses(args));
        return new RuntimeException(sb.toString(), e);
    }

    /**
     * @param method
     * @param args
     *
     * @return
     */
    public static Object callMethod(Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalArgumentException e) {
            throw handleInvokeException(e, null, method, args);
        } catch (IllegalAccessException e) {
            throw handleInvokeException(e, null, method, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param type
     * @param method
     * @param args
     *
     * @return
     */
    public static Object callMethod(Class<?> type, Method method, Object... args) {
        try {
            if (Modifier.isStatic(method.getModifiers())) {
                return method.invoke(null, args);
            } else {
                return method.invoke(newInstance(type), args);
            }
        } catch (IllegalArgumentException e) {
            throw handleInvokeException(e, type, method, args);
        } catch (IllegalAccessException e) {
            throw handleInvokeException(e, type, method, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bean
     * @param method
     * @param args
     *
     * @return
     */
    public static Object callMethod(Object bean, Method method, Object... args) {
        try {
            return method.invoke(bean, args);
        } catch (IllegalArgumentException e) {
            throw handleInvokeException(e, bean.getClass(), method, args);
        } catch (IllegalAccessException e) {
            throw handleInvokeException(e, bean.getClass(), method, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param type
     * @param methodName
     * @param args
     *
     * @return
     */
    public static Object callMethod(Class<?> type, String methodName, Object... args) {
        return callMethod(type, ClassUtil.getMethod(type, methodName, ClassUtil.getClasses(args)), args);
    }

    /**
     * @param bean
     * @param methodName
     * @param args
     *
     * @return
     */
    public static Object callMethod(Object bean, String methodName, Object... args) {
        return callMethod(bean, ClassUtil.getMethod(bean.getClass(), methodName, ClassUtil.getClasses(args)), args);
    }

    /**
     * @param className
     * @param methodName
     * @param args
     *
     * @return
     *
     * @throws ClassNotFoundException
     */
    public static Object callMethod(String className, String methodName, Object... args) throws ClassNotFoundException {
        return callMethod(ClassUtil.loadClass(className), methodName, args);
    }

    /**
     * @param bean
     * @param field
     *
     * @return
     *
     * @see Field#get
     */
    public static Object getFieldValue(Object bean, Field field) {
        try {
            return field.get(bean);
        } catch (Exception e) {
            String format = "getFieldValue({}, {})";
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bean
     * @param field
     * @param value
     *
     * @return
     *
     * @see Field#set
     */
    public static void setFieldValue(Object bean, Field field, Object value) {
        boolean c = false;
        if (!field.isAccessible()) {
            field.setAccessible(c = true);
        }
        try {
            field.set(bean, value);
        } catch (Exception e) {
            String format = "setFieldValue(%s, %s, %s)";
            throw new RuntimeException(String.format(format, ClassUtil.getSimpleName(bean), field.getName(), ClassUtil.getSimpleName(value)), e);
        }
        if (c) {
            field.setAccessible(false);
        }
    }

    /**
     * @param bean
     * @param names
     *         支持级联 name1.name2.name3...nameN
     *
     * @return 最后返回 nameN 的字段值
     *
     * @see Field#get
     */
    public static Object getFieldValue(Object bean, String names) {
        if (null == bean) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (null == names || names.isEmpty()) {
            throw new IllegalArgumentException("names");
        }
        if (names.indexOf('.') < 0) {
            return getFieldValue(bean, ClassUtil.getField(bean.getClass(), names));
        } else {
            StringBuilder sb = new StringBuilder(names);
            String nestedName = getNestedName(sb);
            Object nestedBean = null;
            Field field;

            for (; null != nestedName; nestedName = getNestedName(sb)) {
                field = ClassUtil.getField(bean.getClass(), nestedName);
                nestedBean = getFieldValue(bean, field);
                if (sb.length() > 0) {
                    if (null != nestedBean) {
                        bean = nestedBean;
                    } else {
                        String format = "试图获取%s的声明字段%s的值是 null，无法继续获取字段%s";
                        throw new RuntimeException(String.format(format, ClassUtil.getSimpleName(bean), nestedName, sb.toString()));
                    }
                }
            }
            return nestedBean;
        }
    }

    /**
     * @param bean
     * @param names
     *         支持级联 name1.name2.name3...nameN
     * @param value
     *         最后设置 nameN 的字段值
     *
     * @see Field#set
     */
    public static void setFieldValue(Object bean, String names, Object value) {
        if (null == bean) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (null == names || names.isEmpty()) {
            throw new IllegalArgumentException("names");
        }
        if (names.indexOf('.') < 0) {
            setFieldValue(bean, ClassUtil.getField(bean.getClass(), names), value);
        } else {
            StringBuilder sb = new StringBuilder(names);
            String nestedName = getNestedName(sb);
            Object nestedBean = null;
            Field field;

            for (; null != nestedName; nestedName = getNestedName(sb)) {
                field = ClassUtil.getField(bean.getClass(), nestedName);
                if (sb.length() > 0) {
                    nestedBean = getFieldValue(bean, field);
                    if (null != nestedBean) {
                        bean = nestedBean;
                    } else {
                        String format = "试图获取 {} 的声明字段 {} 的值是 null，无法继续获取字段 {}";
                        throw new RuntimeException(String.format(format, ClassUtil.getSimpleName(bean), nestedName, sb.toString()));
                    }
                } else {
                    setFieldValue(bean, field, value);
                }
            }
        }
    }

    /**
     * @param <V>
     * @param <T>
     * @param map
     * @param bean
     *         不允许为数组、基本类型、抽象类和接口等
     * @param swps
     *
     * @return
     */
    public static <V, T> void toBean(Map<String, V> map, T bean, Map<String, WritableProperty> swps) {
        WritableProperty wp;
        for (Entry<String, WritableProperty> entry : swps.entrySet()) {
            wp = entry.getValue();
            V value = map.get(entry.getKey());
            if (null == value && wp.isPrimitive()) {
                // 若属性为基本类型，则遇到列值为空值(null)时临时跳过，不覆盖属性缺省值
                continue;
            }
            callSetter(bean, wp.getSetter(), value);
        }
    }

    /**
     * @param <V>
     * @param <T>
     * @param map
     * @param type
     *         不允许为数组、基本类型、抽象类和接口等
     * @param swps
     *
     * @return
     */
    public static <V, T> T toBean(Map<String, V> map, Class<T> type, Map<String, WritableProperty> swps) {
        T bean = newInstance(type);
        toBean(map, bean, swps);
        return bean;
    }

    /**
     * @param <V>
     * @param <T>
     * @param map
     * @param type
     *         不允许为基本类型、抽象类和接口等
     *         <ul>
     *         T 类型支持继承自 Map、List 接口的实现类和 Object[] 数组，常用的有如：
     *         <li>{@code HashMap.class} // （列名，无序）,
     *         <li>{@code LinkedHashMap.class} // （列名，顺序）,
     *         <li>{@code ArrayList.class} // （列号，顺序）,
     *         <li>{@code Object[].class} // （列号，顺序）
     *         </ul>
     *
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <V, T> T toBean(Map<String, V> map, Class<T> type) {
        T result;
        if (null != map && map.size() > 0) {
            if (Map.class.isAssignableFrom(type)) {
                Map beanMap = (Map) newInstance(type);
                beanMap.putAll(map);
                result = (T) beanMap;
            } else if (List.class.isAssignableFrom(type)) {
                List beanList = (List) newInstance(type);
                for (V value : map.values()) {
                    beanList.add(value);
                }
                result = (T) beanList;
            } else if (type.isArray()) {
                Object[] beanArray = map.values().toArray();
                result = (T) beanArray;
            } else {
                Map<String, WritableProperty> wps = getWritableProperties(type);
                wps = mapPropertyToColumn(wps, map.keySet());
                result = toBean(map, type, wps);
                wps.clear();
            }
        } else {
            result = null;
        }
        return result;
    }

    /**
     * @param <V>
     * @param <T>
     * @param map
     * @param bean
     *         不允许为基本类型、抽象类和接口等
     *         <ul>
     *         T 类型支持继承自 Map、List 接口的实现类和 Object[] 数组，常用的有如：
     *         <li>{@code HashMap} // （列名，无序）,
     *         <li>{@code LinkedHashMap} // （列名，顺序）,
     *         <li>{@code ArrayList} // （列号，顺序）,
     *         <li>{@code Object[]} // （列号，顺序）
     *         </ul>
     *
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <V, T> void toBean(Map<String, V> map, T bean) {
        if (null != bean && null != map && map.size() > 0) {
            if (Map.class.isInstance(bean)) {
                Map beanMap = (Map) bean;
                beanMap.putAll(map);
            } else if (List.class.isInstance(bean)) {
                List beanList = (List) bean;
                for (V value : map.values()) {
                    beanList.add(value);
                }
            } else if (bean.getClass().isArray()) {
                Object[] beanArray = map.values().toArray();
                Object[] destArray = (Object[]) bean;
                int beanLen = beanArray.length;
                int destLen = destArray.length;
                if (beanLen > destLen) {
                    throw new RuntimeException("BeanUtil.toBean(map, bean) 方法的 bean 参数存储空间不足！");
                } else if (beanLen > 0) {
                    System.arraycopy(beanArray, 0, destArray, 0, beanLen);
                }
            } else {
                Map<String, WritableProperty> wps = getWritableProperties(bean.getClass());
                wps = mapPropertyToColumn(wps, map.keySet());
                toBean(map, bean, wps);
                wps.clear();
            }
        }
    }

    /**
     * @param <V>
     * @param <T>
     * @param mapList
     * @param beans
     *         T 类型不允许为基本类型、抽象类和接口等
     *         <ul>
     *         T 类型支持继承自 Map、List 接口的实现类和 Object[] 数组，常用的有如：
     *         <li>{@code List<HashMap>} // （列名，无序）,
     *         <li>{@code List<LinkedHashMap>} // （列名，顺序）,
     *         <li>{@code List<ArrayList>} // （列号，顺序）,
     *         <li>{@code List<Object[]>} // （列号，顺序）
     *         </ul>
     * @param beanType
     *         T 类型不允许为基本类型、抽象类和接口等
     *         <ul>
     *         T 类型支持继承自 Map、List 接口的实现类和 Object[] 数组，常用的有如：
     *         <li>{@code HashMap.class} // （列名，无序）,
     *         <li>{@code LinkedHashMap.class} // （列名，顺序）,
     *         <li>{@code ArrayList.class} // （列号，顺序）,
     *         <li>{@code Object[].class} // （列号，顺序）
     *         </ul>
     *
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <V, T> int toBean(List<Map<String, V>> mapList, List<T> beans, Class<T> beanType) {
        int count = 0;
        if (null != mapList && mapList.size() > 0) {
            if (Map.class.isAssignableFrom(beanType)) {
                Map beanMap;
                for (Map<String, V> map : mapList) {
                    if (null != map && map.size() > 0) {
                        beanMap = (Map) newInstance(beanType);
                        beanMap.putAll(map);
                    } else {
                        beanMap = Collections.emptyMap();
                    }
                    beans.add((T) beanMap);
                    ++count;
                }
            } else if (List.class.isAssignableFrom(beanType)) {
                List beanList;
                for (Map<String, V> map : mapList) {
                    if (null != map && map.size() > 0) {
                        beanList = (List) newInstance(beanType);
                        for (V value : map.values()) {
                            beanList.add(value);
                        }
                    } else {
                        beanList = Collections.emptyList();
                    }
                    beans.add((T) beanList);
                    ++count;
                }
            } else if (beanType.isArray()) {
                Object[] beanArray;
                for (Map<String, V> map : mapList) {
                    if (null != map && map.size() > 0) {
                        beanArray = map.values().toArray();
                    } else {
                        beanArray = (Object[]) newInstance(beanType);
                    }
                    beans.add((T) beanArray);
                    ++count;
                }
            } else {
                Map<String, WritableProperty> wps = null;
                for (Map<String, V> map : mapList) {
                    if (null != map && map.size() > 0) {
                        if (null == wps) {
                            wps = getWritableProperties(beanType);
                            wps = mapPropertyToColumn(wps, map.keySet());
                        }
                        beans.add(toBean(map, beanType, wps));
                        ++count;
                    }
                }
                if (null != wps) {
                    wps.clear();
                }
            }
        }
        return count;
    }

    /**
     * @param bean
     * @param propertyMap
     * @param addOnly
     *         仅增加而已，不替换现有的。默认为 {@code false}
     * @param includeNull
     *         包括空值(null)，如果是 {@code propertyMap} 是 {@code ConcurrentMap} 接口类型则强制不包括。默认为
     *         {@code false}
     */
    public static void toMapObject(Object bean, Map<String, Object> propertyMap, boolean addOnly, boolean includeNull) {
        if (null != bean) {
            Object value;
            if (includeNull) {
                includeNull = !(propertyMap instanceof ConcurrentMap);
            }

            String name;
            Method getter;
            WritableProperty wp;
            Map<String, WritableProperty> wps = getWritableProperties(bean.getClass());
            for (Entry<String, WritableProperty> entry : wps.entrySet()) {
                wp = entry.getValue();
                getter = wp.getGetter();
                if (null != getter) {
                    name = wp.getName();
                    if (!addOnly || !propertyMap.containsKey(name)) {
                        value = callGetter(bean, getter);
                        if (includeNull || null != value) {
                            propertyMap.put(name, value);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param bean
     * @param propertyMap
     * @param addOnly
     *         仅增加而已，不替换现有的
     */
    public static void toMapObject(Object bean, Map<String, Object> propertyMap, boolean addOnly) {
        toMapObject(bean, propertyMap, addOnly, false);
    }

    /**
     * @param bean
     * @param propertyMap
     */
    public static void toMapObject(Object bean, Map<String, Object> propertyMap) {
        toMapObject(bean, propertyMap, false, false);
    }

    /**
     * @param bean
     * @param propertyMap
     * @param addOnly
     *         仅增加而已，不替换现有的。默认为 {@code false}
     * @param includeNull
     *         包括空值(null)，如果是 {@code propertyMap} 是 {@code ConcurrentMap} 接口类型则强制不包括。默认为
     *         {@code false}
     */
    public static void toMapString(Object bean, Map<String, String> propertyMap, boolean addOnly, boolean includeNull) {
        if (null != bean) {
            Object value;
            if (includeNull) {
                includeNull = !(propertyMap instanceof ConcurrentMap);
            }

            String name;
            Method getter;
            WritableProperty wp;
            Map<String, WritableProperty> wps = getWritableProperties(bean.getClass());
            for (Entry<String, WritableProperty> entry : wps.entrySet()) {
                wp = entry.getValue();
                getter = wp.getGetter();
                if (null != getter) {
                    name = wp.getName();
                    if (!addOnly || !propertyMap.containsKey(name)) {
                        value = callGetter(bean, getter);
                        if (null != value) {
                            propertyMap.put(name, StringUtil.valueOf(value));
                        } else if (includeNull) {
                            propertyMap.put(name, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param bean
     * @param propertyMap
     * @param addOnly
     *         仅增加而已，不替换现有的
     */
    public static void toMapString(Object bean, Map<String, String> propertyMap, boolean addOnly) {
        toMapString(bean, propertyMap, addOnly, false);
    }

    /**
     * @param bean
     * @param propertyMap
     */
    public static void toMapString(Object bean, Map<String, String> propertyMap) {
        toMapString(bean, propertyMap, false, false);
    }

    /**
     * @param <K>
     * @param <V>
     * @param list
     * @param keyProperty
     * @param map
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(List<V> list, String keyProperty, Map<K, V> map) {
        if (null != list && list.size() > 0) {
            K key;
            Field keyField = null;
            for (V obj : list) {
                if (null != obj) {
                    if (null == keyField) {
                        keyField = ClassUtil.getField(obj.getClass(), keyProperty);
                    }
                    key = (K) BeanUtil.getFieldValue(obj, keyField);
                    map.put(key, obj);
                }
            }
        }
        return map;
    }

    /**
     * @param <K>
     * @param <V>
     * @param list
     * @param keyProperty
     *
     * @return
     */
    public static <K, V> Map<K, V> toHashMap(List<V> list, String keyProperty) {
        Map<K, V> map = new HashMap<>();
        return toMap(list, keyProperty, map);
    }

    /**
     * @param <K>
     * @param <V>
     * @param list
     * @param keyProperty
     *
     * @return
     */
    public static <K, V> Map<K, V> toLinkedHashMap(List<V> list, String keyProperty) {
        Map<K, V> map = new LinkedHashMap<>();
        return toMap(list, keyProperty, map);
    }

    /**
     * @param list
     * @param keyProperty
     * @param findKey
     * @param <K>
     * @param <V>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> V find(List<V> list, String keyProperty, K findKey) {
        if (null != list && list.size() > 0) {
            K key;
            Field keyField = null;
            for (V obj : list) {
                if (null != obj) {
                    if (null == keyField) {
                        keyField = ClassUtil.getField(obj.getClass(), keyProperty);
                    }
                    key = (K) BeanUtil.getFieldValue(obj, keyField);
                    if (null != findKey) {
                        if (findKey.equals(key)) {
                            return obj;
                        }
                    } else if (null == key) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

}
