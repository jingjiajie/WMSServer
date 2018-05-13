package com.wms.utilities.datastructures;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.exceptions.ConditionException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import javafx.application.ConditionalFeature;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Condition {
    private List<ConditionItem> conditions = new ArrayList<>();
    private List<OrderItem> orders = new ArrayList<>();
    private long page = -1;
    private long pageSize = 50;

    public static Condition fromJson(String jsonStr) throws ConditionException {
        Gson gson = new Gson();
        Condition result = gson.fromJson(jsonStr, new TypeToken<Condition>() {
        }.getType());
        return result;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public Query createQuery(String entityName, Session session) {
        return this.createQuery(entityName, session, false);
    }

    public Query createQuery(String entityName, Session session, boolean returnCount) {
        List<ConditionItem> conditionItems = this.conditions;
        List<OrderItem> orderItems = this.orders;
        StringBuffer hqlString = new StringBuffer();
        if (returnCount) {
            hqlString.append("select count(*) ");
        }
        hqlString.append("from " + entityName + " ");
        Map<String, Object> queryParams = new HashMap<String, Object>();
        int valueNum = 0;
        if (conditionItems != null) {
            if (conditionItems.size() != 0) {
                hqlString.append("where 1=1 ");
            }

            for (int i = 0; i < conditionItems.size(); i++, valueNum++) {
                ConditionItem cond = conditionItems.get(i);
                Object[] condValues = cond.getValues();
                for (int j = 0; j < condValues.length; j++) {
                    if (condValues[j] == null) continue;
                    //将double的value，如果是整数的，转换成整数。避免id这种INT字段被赋double值造成错误
                    if (condValues[j] instanceof Double) {
                        Double doubleValue = (Double) condValues[j];
                        if (doubleValue.intValue() == doubleValue) {
                            condValues[j] = doubleValue.intValue();
                        } else if (doubleValue.longValue() == doubleValue) {
                            condValues[j] = doubleValue.longValue();
                        }
                    }
                    //如果有日期字符串，转换为日期类型，避免SQL查询类型错误
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        condValues[j] = sdf1.parse(condValues[j].toString());
                    } catch (Exception e) {
                        //do nothing
                    }
                    try {
                        condValues[j] = sdf2.parse(condValues[j].toString());
                    } catch (ParseException e) {
                        //do nothing
                    }
                    try {
                        condValues[j] = sdf3.parse(condValues[j].toString());
                    } catch (ParseException e) {
                        //do nothing
                    }

                }
                switch (cond.getRelation()) {
                    case EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s = :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case NOT_EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("NOT_EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <> :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case CONTAINS:
                        if (condValues.length != 1) {
                            throw new ConditionException("CONTAINS relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s LIKE :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, "%" + condValues[0] + "%");
                        break;
                    case GREATER_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s > :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s >= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case LESS_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s < :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + i, condValues[0]);
                        break;
                    case BETWEEN:
                        if (condValues.length != 2) {
                            throw new ConditionException("BETWEEN relation needs two values");
                        }
                        hqlString.append(String.format(" AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), valueNum, valueNum));
                        queryParams.put("value" + i + "_1", condValues[0]);
                        queryParams.put("value" + i + "_2", condValues[1]);
                        break;
                    default:
                        throw new ConditionException("Unsupported relation " + cond.getRelation().toString());
                }
            }
        }
        if (orderItems != null) {
            if (orderItems.size() > 0) {
                hqlString.append(" order by ");
            }
            for (int i = 0; i < orderItems.size(); i++, valueNum++) {
                if (i != 0) {
                    hqlString.append(",");
                }
                hqlString.append(String.format("%s %s", orderItems.get(i).getKey(), orderItems.get(i).getOrder().toString()));
            }
        }
        //如果设置了分页条件，则生成分页语句
        if (this.page != -1) {
            if(returnCount){
                throw new WMSServiceException(String.format("returnCount时不允许设置页码(%d)",page));
            }
            if(page == 0){
                throw new WMSServiceException(String.format("page(%d)必须从1开始！",page));
            }
            hqlString.append(String.format(" limit %d,%d ", (page - 1) * pageSize, pageSize));
        }
        Query query = session.createQuery(hqlString.toString());
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    public ConditionItem[] getConditions() {
        ConditionItem[] results = new ConditionItem[this.conditions.size()];
        this.conditions.toArray(results);
        return results;
    }

    public Condition addCondition(String key, Object[] values, ConditionItem.Relation relation) {
        ConditionItem conditionItem = new ConditionItem();
        conditionItem.setKey(key);
        conditionItem.setRelation(relation);
        conditionItem.setValues(values);
        this.conditions.add(conditionItem);
        return this;
    }

    public Condition addCondition(String key, Object value, ConditionItem.Relation relation) {
        ConditionItem conditionItem = new ConditionItem();
        conditionItem.setKey(key);
        conditionItem.setRelation(relation);
        conditionItem.setValues(new Object[]{value});
        this.conditions.add(conditionItem);
        return this;
    }

    public Condition addCondition(String key, Object[] values) {
        return this.addCondition(key, values, ConditionItem.Relation.EQUAL);
    }

    public Condition addCondition(String key, Object value) {
        return this.addCondition(key, value, ConditionItem.Relation.EQUAL);
    }

    public Condition setConditions(ConditionItem[] conditions) {
        this.conditions = new ArrayList<ConditionItem>(Arrays.asList(conditions));
        return this;
    }

    public OrderItem[] getOrders() {
        OrderItem[] result = new OrderItem[this.orders.size()];
        this.orders.toArray(result);
        return result;
    }

    public Condition setOrders(OrderItem[] orders) {
        OrderItem[] orderItems = new OrderItem[this.orders.size()];
        this.orders = new ArrayList<>(Arrays.asList(orders));
        return this;
    }

    public Condition addOrder(String key, OrderItem.Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setKey(key);
        orderItem.setOrder(order);
        return this;
    }

    public Condition addOrder(String key) {
        this.addOrder(key, OrderItem.Order.ASC);
        return this;
    }
}
