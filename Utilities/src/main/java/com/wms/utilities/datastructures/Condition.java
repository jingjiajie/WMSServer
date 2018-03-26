package com.wms.utilities.datastructures;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.exceptions.ConditionException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.Map;

public class Condition {
    private ConditionItem[] conditions = null;
    private OrderItem[] orders = null;

    public static Condition fromJson(String jsonStr) throws ConditionException{
        Gson gson = new Gson();
        Condition result = gson.fromJson(jsonStr,new TypeToken<Condition>(){}.getType());
        return result;
    }

    public Query createQuery(String entityName, Session session){
        ConditionItem[] conditionItems = this.conditions;
        OrderItem[] orderItems = this.orders;
        StringBuffer hqlString = new StringBuffer("from "+entityName+" ");
        Map<String,Object> queryParams = new HashMap<String, Object>();
        int valueNum = 0;
        if(conditionItems != null) {
            if (conditionItems.length != 0) {
                hqlString.append("where 1=1 ");
            }

            for (int i=0; i < conditionItems.length; i++,valueNum++) {
                ConditionItem cond = conditionItems[i];
                switch (cond.getRelation()) {
                    case EQUAL:
                        if (cond.getValues().length != 1) {
                            throw new ConditionException("EQUAL relation needs one value");
                        }
                        hqlString.append(String.format("AND %s = :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, cond.getValues()[0]);
                        break;
                    case GREATER_THAN:
                        if (cond.getValues().length != 1) {
                            throw new ConditionException("GREATER_THAN relation needs one value");
                        }
                        hqlString.append(String.format("AND %s > :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, cond.getValues()[0]);
                        break;
                    case LESS_THAN:
                        if (cond.getValues().length != 1) {
                            throw new ConditionException("LESS_THAN relation needs one value");
                        }
                        hqlString.append(String.format("AND %s < :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, cond.getValues()[0]);
                        break;
                    case BETWEEN:
                        if (cond.getValues().length != 2) {
                            throw new ConditionException("BETWEEN relation needs two values");
                        }
                        hqlString.append(String.format("AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), valueNum,valueNum));
                        queryParams.put("value" + i + "_1", cond.getValues()[0]);
                        queryParams.put("value" + i + "_2", cond.getValues()[1]);
                        break;
                    default:
                        throw new ConditionException("Unsupported relation " + cond.getRelation().toString());
                }
            }
        }
        if(orderItems != null){
            if(orderItems.length > 0){
                hqlString.append("order by 1");
            }
            for(int i=0;i<orderItems.length;i++,valueNum++){
                hqlString.append(String.format(",%s %s",orderItems[i].getKey(),orderItems[i].getOrder().toString()));
            }
        }
        Query query = session.createQuery(hqlString.toString());
        for(Map.Entry<String,Object> entry : queryParams.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
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
