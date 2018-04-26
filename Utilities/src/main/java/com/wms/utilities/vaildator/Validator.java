package com.wms.utilities.vaildator;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.thoughtworks.xstream.mapper.Mapper;
import com.wms.utilities.exceptions.service.WMSServiceException;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class Validator {
private List<ValidatorCondition> conditions=new ArrayList<ValidatorCondition>();
String key;
    public  Validator(String key1)
    {
        key=key1;
    }
    public void validate(Object object){
        for(ValidatorCondition condition:conditions){
            condition.validate(object);
        }
    }
    public void min(Object object){
        MinValidatorCondition minValidatorCondition=new MinValidatorCondition();
        minValidatorCondition.setKey(key);
        minValidatorCondition.dateDeliver(object);
        conditions.add(1,minValidatorCondition);
    }
    public void max(Object object){
        MaxValidatorCondition maxValidatorCondition=new MaxValidatorCondition();
        maxValidatorCondition.setKey(key);
        maxValidatorCondition.dateDeliver(object);
        conditions.add(1,maxValidatorCondition);
    }
    public void in(int[] object){
        InValidatorCondition inValidatorCondition=new InValidatorCondition();
        inValidatorCondition.setKey(key);
        inValidatorCondition.dateDeliver(object);
        conditions.add(1,inValidatorCondition);
    }
    public void length(Object object){
        LengthValidatorCondition lengthValidatorCondition=new LengthValidatorCondition();
        lengthValidatorCondition.setKey(key);
        lengthValidatorCondition.dateDeliver(object);
        conditions.add(1,lengthValidatorCondition);
    }
    public void notnull(){
        NotnullValidatorCondition notnullValidatorCondition=new NotnullValidatorCondition();
        notnullValidatorCondition.setKey(key);
        conditions.add(1,notnullValidatorCondition);
    }
    public void notEmpty(){
        NotEmptyValidatorCondition notEmptyValidatorCondition=new NotEmptyValidatorCondition();
        notEmptyValidatorCondition.setKey(key);
        conditions.add(1,notEmptyValidatorCondition);
    }
}
class ValidatorCondition {
    public void validate(Object values) { }
}
class MinValidatorCondition extends ValidatorCondition {
    private int min;
    private int actualValue;
    private String key;

    public void dateDeliver(Object object) {
        try {
            min = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
    }}

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
        }
        if (actualValue < min) {
            throw new WMSServiceException(key+ "的值小于最低值");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class MaxValidatorCondition extends ValidatorCondition {
    private int max;
    private int actualValue;
    private String key;

    public void dateDeliver(Object object) {
        try {
            max = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
        }
        if (actualValue > max) {
            throw new WMSServiceException(key+ "的值大于最大值");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}class InValidatorCondition extends ValidatorCondition {
    private int min;
    private int max;
    private int actualValue;
    private String key;

    public void dateDeliver(int[] object) {
        try {
            min = Integer.parseInt(String.valueOf(object[0]));
            max = Integer.parseInt(String.valueOf(object[1]));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
        }
        if (actualValue < min||actualValue>max) {
            throw new WMSServiceException(key+ "的值不在指定的区间范围内");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}class LengthValidatorCondition extends ValidatorCondition {
    private int length;
    private int actualValue;
    private String key;

    public void dateDeliver(Object object) {
        try {
            length = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value).length();

        } catch (NumberFormatException e) {
        }
        if (actualValue > length) {
            throw new WMSServiceException(key+ "字符串长度超过上限！");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class NotnullValidatorCondition extends ValidatorCondition {
    private int actualValue;
    private String key;

    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value).length();

        } catch (NumberFormatException e) {
        }
        if (actualValue == 0) {
            throw new WMSServiceException(key+ "的值不能为空");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class NotEmptyValidatorCondition extends ValidatorCondition {
    private String actualValue;
    private String key;


    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value);

        } catch (NumberFormatException e) {
        }
        if (actualValue == "") {
            throw new WMSServiceException(key+ "的值不能为空");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}