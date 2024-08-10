package kall.reflection;

import kall.entity.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InjectData {
    private AtomicInteger count = new AtomicInteger(1);

    public List<Data> injectTest(List<String> onlyRead) {
        List<Data> dataS = new ArrayList<>(onlyRead.size());
        for (String s : onlyRead) {
//            System.out.println(count.getAndIncrement() + ":"+ s);
            String[] values = s.split("\t");
            Data data = new Data();
            Class<?> clazz = data.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    try {
                        fields[i].setAccessible(true);
                        fields[i].set(data, convertValue(fields[i].getType(), -1124));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

                try {
                    fields[i].setAccessible(true);
                    fields[i].set(data, convertValue(fields[i].getType(), values[i]));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            dataS.add(data);

        }

        return dataS;
    }

    private static Object convertValue(Class<?> fieldType, Object value) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (fieldType == String.class) {
            return value.toString();
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value.toString());
        }
        // 其他类型的转换
        return value;
    }
}
