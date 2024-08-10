package kall.reflection;

import kall.entity.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GetTargetArray {
    private ArrayList<String> arr;
    private GetTargetArray() {
        System.out.println("__开始初始化GetTargetArray....");
        Class<Data> clazz = Data.class;
        Field[] fields = clazz.getDeclaredFields();
        arr = new ArrayList<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            arr.add(fields[i].getName());
        }
    }

    private static class Instance {
        private static GetTargetArray instance = new GetTargetArray();
    }
    public static GetTargetArray getInstance(){
        return Instance.instance;
    }

    public ArrayList<String> getArray(){
        return arr;
    }
}
