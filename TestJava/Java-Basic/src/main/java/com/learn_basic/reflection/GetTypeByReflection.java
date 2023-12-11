package com.learn_basic.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

// 通过反射 获取泛型
public class GetTypeByReflection {
    public void test01(Map<String, User> map, List<User> list){
        System.out.println("test01");
    }

    public Map<String, User> test02(){
        System.out.println("test02");
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = GetTypeByReflection.class.getMethod("test01", Map.class, List.class);

        // 获取泛型参数化类型
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println("# genericParameterType: " + genericParameterType);

            // 对每一个参数，如果是参数化类型，就获取真实的类型
            if(genericParameterType instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType) genericParameterType).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    System.out.println("# actualTypeArgument: " +actualTypeArgument);
                }
            }
        }

        System.out.println("  ===============================  ");

        method = GetTypeByReflection.class.getMethod("test02", null);
        Type genericReturnType = method.getGenericReturnType();
        if(genericReturnType instanceof ParameterizedType){
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println("# actualTypeArgument: " +actualTypeArgument);
            }
        }
    }

}

// # genericParameterType: java.util.Map<java.lang.String, com.reflection.User>
// # actualTypeArgument: class java.lang.String
// # actualTypeArgument: class com.reflection.User
// # genericParameterType: java.util.List<com.reflection.User>
// # actualTypeArgument: class com.reflection.User
// ===============================
// # actualTypeArgument: class java.lang.String
// # actualTypeArgument: class com.reflection.User
