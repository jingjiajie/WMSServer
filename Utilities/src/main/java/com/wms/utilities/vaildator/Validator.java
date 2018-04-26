package com.wms.utilities.vaildator;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
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