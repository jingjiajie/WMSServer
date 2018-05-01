package com.wms.utilities;

import com.wms.utilities.exceptions.service.WMSServiceException;

import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectHelper {
    public static <TSource, TTarget> void copyFields(TSource source, TTarget target) {
        Class sourceClass = source.getClass();
        Class targetClass = target.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field curSourceField : sourceFields) {
            curSourceField.setAccessible(true);
            Field curTargetField;
            try {
                curTargetField = targetClass.getDeclaredField(curSourceField.getName());
            } catch (NoSuchFieldException ex) {
                continue;
            }
            try {
                curTargetField.setAccessible(true);
                Object sourceValue = curSourceField.get(source);
                curTargetField.set(target, sourceValue);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }

    public static <TSource, TTarget> void copyFields(TSource[] sources, TTarget[] targets) {
        for (int i = 0; i < sources.length; i++) {
            copyFields(sources[i], targets[i]);
        }
    }

    @SuppressWarnings("unchecked")
    public static <TSource, TTarget> TTarget[] createAndCopyFields(TSource[] sources,Class<TTarget> targetType){
        TTarget[] targetArray = (TTarget[]) Array.newInstance(targetType,sources.length);
        try {
            for (int i = 0; i < targetArray.length; i++) {
                targetArray[i] = (TTarget) targetType.newInstance();
            }
        }catch (Exception ex){
            throw new WMSServiceException("无法创建 "+targetType.getName()+" 的实例，请检查是否存在无参构造函数");
        }
        copyFields(sources,targetArray);
        return targetArray;
    }

    @SuppressWarnings("unchecked")
    public static <TSource, TTarget> TTarget createAndCopyFields(TSource source,Class<TTarget> targetType){
        TSource[] sourceArray = (TSource[]) Array.newInstance(source.getClass(),1);
        sourceArray[0] = source;
        return createAndCopyFields(sourceArray,targetType)[0];
    }
}
