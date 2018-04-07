package com.wms.utilities.datastructures;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.exceptions.ConditionException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Condition {
    private ConditionItem[] conditions = null;
    private OrderItem[] orders = null;

    public static Condition fromJson(String jsonStr) throws ConditionException {
        Gson gson = new Gson();
        Condition result = gson.fromJson(jsonStr, new TypeToken<Condition>() {
        }.getType());
        return result;
    }

    public Query createQuery(String entityName, Session session) {
        ConditionItem[] conditionItems = this.conditions;
        OrderItem[] orderItems = this.orders;
        StringBuffer hqlString = new StringBuffer("from " + entityName + " ");
        Map<String, Object> queryParams = new HashMap<String, Object>();
        int valueNum = 0;
        if (conditionItems != null) {
            if (conditionItems.length != 0) {
                hqlString.append("where 1=1 ");
            }

            for (int i = 0; i < conditionItems.length; i++, valueNum++) {
                ConditionItem cond = conditionItems[i];
                //将double的value，如果是整数的，转换成整数。避免id这种INT字段被赋double值造成错误
                Object[] condValues = cond.getValues();
                for(int j=0;j<condValues.length;j++){
                    if(condValues[j] instanceof Double){
                        Double doubleValue = (Double)condValues[j];
                        if(doubleValue.intValue() == doubleValue){
                            condValues[j] = doubleValue.intValue();
                        }else if(doubleValue.longValue() == doubleValue){
                            condValues[j] = doubleValue.longValue();
                        }
                    }
                }
                switch (cond.getRelation()) {
                    case EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("EQUAL relation needs one value");
                        }
                        hqlString.append(String.format("AND %s = :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case GREATER_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN relation needs one value");
                        }
                        hqlString.append(String.format("AND %s > :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case LESS_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN relation needs one value");
                        }
                        hqlString.append(String.format("AND %s < :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case BETWEEN:
                        if (condValues.length != 2) {
                            throw new ConditionException("BETWEEN relation needs two values");
                        }
                        hqlString.append(String.format("AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), valueNum, valueNum));
                        queryParams.put("value" + i + "_1", condValues[0]);
                        queryParams.put("value" + i + "_2", condValues[1]);
                        break;
                    default:
                        throw new ConditionException("Unsupported relation " + cond.getRelation().toString());
                }
            }
        }
        if (orderItems != null) {
            if (orderItems.length > 0) {
                hqlString.append(" order by 1");
            }
            for (int i = 0; i < orderItems.length; i++, valueNum++) {
                hqlString.append(String.format(",%s %s", orderItems[i].getKey(), orderItems[i].getOrder().toString()));
            }
        }
        Query query = session.createQuery(hqlString.toString());
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    public ConditionItem[] getConditions() {
        return conditions;
    }

    public void setConditions(ConditionItem[] conditions) {
        this.conditions = conditions;
    }

    public OrderItem[] getOrders() {
        return orders;
    }

    public void setOrders(OrderItem[] orders) {
        this.orders = orders;
    }
}
