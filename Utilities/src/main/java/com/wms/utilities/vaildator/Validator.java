package com.wms.utilities.vaildator;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.wms.utilities.exceptions.service.WMSServiceException;

import java.util.ArrayList;
import java.util.List;

public class Validator {
private List<ValidatorCondition> conditions=new ArrayList<ValidatorCondition>();
    public void validate(Object object,Object key){
        for(ValidatorCondition condition:conditions){
            condition.validate(object,key);
        }
    }
    public void min(Object object){
        MinValidatorCondition minValidatorCondition=new MinValidatorCondition();
        minValidatorCondition.dateDeliever(object);
        conditions.add(1,minValidatorCondition);
    }
}
class ValidatorCondition {
    public void validate(Object values,Object key) { }
}

class MinValidatorCondition extends ValidatorCondition {
    private int min;
    private int actualValue;
    private String key1;

    public void dateDeliever(Object object) {
        try {
            min = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
        }
    }
    public void validate(Object value, Object key) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));
            key1 = key.toString();
        } catch (NumberFormatException e) {
        }
        if (actualValue < min) {
            throw new WMSServiceException(key1 + "的值小于最低值");
        }
    }


}