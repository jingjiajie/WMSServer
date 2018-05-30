package com.wms.utilities;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class IDChecker {
    @Autowired
    ApplicationContext applicationContext;

    public <TService> IDChecker check(
            Class<TService> serviceClass,
            String accountBook,
            Integer id,
            String hintName) throws WMSServiceException{

        if(id == null) {
            throw new WMSServiceException("请填写" + hintName + "！");
        }

        Method methodFind;
        try {
            methodFind = serviceClass.getMethod("find", String.class, Condition.class);
            methodFind.setAccessible(true);
        }catch (NoSuchMethodException e){
            throw new WMSServiceException(String.format("IDChecker接收的Service(%s)不包含find(String,Condition)，调用失败！",serviceClass.getName()));
        }
        TService service = applicationContext.getBean(serviceClass);
        Object[] invokeResult;
        try {
            invokeResult = (Object[]) methodFind.invoke(service, accountBook, new Condition().addCondition("id", id));
        }catch (InvocationTargetException e){
            throw new WMSServiceException(String.format("IDChecker调用find失败！(%s)：%s",serviceClass.getName(),e.getTargetException().getMessage()));
        }catch (IllegalAccessException e){
            throw new WMSServiceException(String.format("IDChecker调用find失败！(%s)",serviceClass.getName()));
        }

        if (invokeResult.length == 0) {
            throw new WMSServiceException(String.format("%s不存在，请重新提交！(%d)",hintName,id));
        }
        return this;
    }
}
