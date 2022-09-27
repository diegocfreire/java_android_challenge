package com.example.list_app.utils;

public class ObjectUtils {

    public static <T> boolean nonNull(T obj){
        return obj != null;
    }

    public static <T> boolean isNull(T obj){
        return obj == null;
    }

}
