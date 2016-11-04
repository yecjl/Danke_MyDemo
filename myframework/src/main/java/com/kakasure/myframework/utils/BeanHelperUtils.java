package com.kakasure.myframework.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 把一个JavaBean 转换为Map
 * Created by danke on 2016/1/6.
 */
public class BeanHelperUtils {

    /**
     * 将JavaBean转换为map
     * 请求的参数都是String类型的
     *
     * @param bean
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> convertBeanToMap(Object bean) throws IllegalAccessException {
        HashMap<String, Object> data = new HashMap<>();
        if (bean != null) {
            Field[] fields = bean.getClass().getDeclaredFields();
            addFiledsToMap(bean, fields, data);
            Class<?> superclass = bean.getClass().getSuperclass();
            while (superclass != null && !(superclass.getName().equals(Object.class.getName()))) {
                Field[] superFields = superclass.getDeclaredFields();
                addFiledsToMap(bean, superFields, data);
                superclass = superclass.getSuperclass();
            }
        }
        return data;
    }

    /**
     * 添加指定字段到map中
     *
     * @param bean
     * @param fields
     * @param data
     * @throws IllegalAccessException
     */
    private static void addFiledsToMap(Object bean, Field[] fields, HashMap<String, Object> data) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(bean) != null) {
                data.put(field.getName(), field.get(bean));
            }
        }
    }

    /**
     * 将url的参数 转换为 Map集合
     *
     * @param str
     * @return
     */
    public static Map<String, String> getUrlParams(String str) {
        Map<String, String> map = new HashMap<>();
        if (StringUtil.isNull(str)) {
            return map;
        }

        int index = str.indexOf("?");
        if (index == -1) {
            return map;
        }

        String substring = str.substring(index + 1);
        String[] params = substring.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            map.put(param[0], param[1]);
        }

        return map;
    }
}
