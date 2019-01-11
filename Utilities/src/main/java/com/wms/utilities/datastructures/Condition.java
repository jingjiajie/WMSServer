package com.wms.utilities.datastructures;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.exceptions.ConditionException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Condition {
    private List<ConditionItem> conditions = new ArrayList<>();
    private List<OrderItem> orders = new ArrayList<>();
    private int page = -1;
    private int pageSize = 50;

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

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

    @Deprecated
    public Query createQuery(String entityName, Session session) {
        return this.createQuery(entityName, session, false);
    }

    public Query createQuery(Class targetClass, Session session) {
        return this.createQuery(targetClass, session, false);
    }

    @SuppressWarnings("all")
    public Query createQuery(Class targetClass, Session session, boolean returnCount) {
        String className = targetClass.getName();
        List<ConditionItem> conditionItems = this.conditions;
        List<OrderItem> orderItems = this.orders;
        StringBuffer hqlString = new StringBuffer();
        if (returnCount) {
            hqlString.append("select count(*) ");
        }
        hqlString.append("from " + className + " ");
        Map<String, Object> queryParams = new HashMap<String, Object>();
        int valueNum = 0;
        if (conditionItems != null) {
            if (conditionItems.size() != 0) {
                hqlString.append("where 1=1 ");
            }

            for (int i = 0; i < conditionItems.size(); i++, valueNum++) {
                ConditionItem cond = conditionItems.get(i);
                String key = cond.getKey();
                Object[] condValues = cond.getValues();
                Field targetField = null;
                Class fieldType = null;
                try {
                    targetField = targetClass.getDeclaredField(key);
                    fieldType = targetField.getType();
                    for (int j = 0; j < condValues.length; j++) {
                        condValues[j] = this.changeType(condValues[j], fieldType);
                    }
                } catch (NoSuchFieldException ex) {
                    throw new WMSServiceException(className + " 类型中不存在\"" + key + "\"字段！查询失败");
                }
                switch (cond.getRelation()) {
                    case EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s = :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case NOT_EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("NOT_EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <> :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case CONTAINS:
                        if (condValues.length != 1) {
                            throw new ConditionException("CONTAINS relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s LIKE :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, "%" + condValues[0] + "%");
                        break;
                    case STARTS_WITH:
                        if (condValues.length != 1) {
                            throw new ConditionException("STARTS_WITH relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s LIKE :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]+"%");
                        break;
                    case ENDS_WITH:
                        if (condValues.length != 1) {
                            throw new ConditionException("ENDS_WITH relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s LIKE :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, "%" + condValues[0]);
                        break;
                    case GREATER_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s > :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s >= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case LESS_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s < :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case BETWEEN:
                        if (condValues.length != 2) {
                            throw new ConditionException("BETWEEN relation needs two values");
                        }
                        hqlString.append(String.format(" AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), valueNum, valueNum));
                        queryParams.put("value" + valueNum + "_1", condValues[0]);
                        queryParams.put("value" + valueNum + "_2", condValues[1]);
                        break;
                    case IN:
                        if (condValues.length == 0) {
                            throw new ConditionException("IN relation needs at least one value");
                        }
                        hqlString.append(String.format(" AND %s IN (",cond.getKey()));
                        for (int j = 0; j < condValues.length; j++) {
                            Object curValue = condValues[j];
                            if (j != 0) {
                                hqlString.append(",");
                            }
                            hqlString.append(String.format(":value%d_%d", valueNum, j));
                            queryParams.put(String.format("value%d_%d", valueNum, j), condValues[j]);
                        }
                        hqlString.append(") ");
                        break;
                    default:
                        throw new ConditionException("Unsupported relation " + cond.getRelation().toString());
                }
            }
        }
        //如果没有ID排序条件，则永远加一个按ID倒排。不需要理由
        boolean hasIDOrder = false;
        for (OrderItem orderItem : this.orders) {
            if (orderItem.getKey().equalsIgnoreCase("id")) {
                hasIDOrder = true;
            }
        }
        if (!hasIDOrder) {
            this.addOrder("id", OrderItem.Order.DESC);
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
        //创建查询
        Query query = session.createQuery(hqlString.toString());

        //如果设置了分页条件，则生成分页语句
        if (this.page != -1) {
            if (returnCount) {
                throw new WMSServiceException(String.format("returnCount时不允许设置页码(%d)", page));
            }
            if (page == 0) {
                throw new WMSServiceException(String.format("page(%d)必须从1开始！", page));
            }
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    @SuppressWarnings("all")
    @Deprecated
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
                    } else if (condValues[j] instanceof String) {
                        //如果有日期字符串，转换为日期类型，避免SQL查询类型错误
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            condValues[j] = sdf3.parse(condValues[j].toString());
                        } catch (Exception e) {
                            //do nothing
                        }
                        try {
                            condValues[j] = sdf2.parse(condValues[j].toString());
                        } catch (ParseException e) {
                            //do nothing
                        }
                        try {
                            condValues[j] = sdf1.parse(condValues[j].toString());
                        } catch (ParseException e) {
                            //do nothing
                        }
                    }
                }
                switch (cond.getRelation()) {
                    case EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s = :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case NOT_EQUAL:
                        if (condValues.length != 1) {
                            throw new ConditionException("NOT_EQUAL relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <> :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case CONTAINS:
                        if (condValues.length != 1) {
                            throw new ConditionException("CONTAINS relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s LIKE :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, "%" + condValues[0] + "%");
                        break;
                    case GREATER_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s > :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("GREATER_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s >= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case LESS_THAN:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s < :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        if (condValues.length != 1) {
                            throw new ConditionException("LESS_THAN_OR_EQUAL_TO relation needs one value");
                        }
                        hqlString.append(String.format(" AND %s <= :value%d", cond.getKey(), valueNum));
                        queryParams.put("value" + valueNum, condValues[0]);
                        break;
                    case BETWEEN:
                        if (condValues.length != 2) {
                            throw new ConditionException("BETWEEN relation needs two values");
                        }
                        hqlString.append(String.format(" AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), valueNum, valueNum));
                        queryParams.put("value" + valueNum + "_1", condValues[0]);
                        queryParams.put("value" + valueNum + "_2", condValues[1]);
                        break;
                    case IN:
                        if (condValues.length == 0) {
                            throw new ConditionException("IN relation needs at least one value");
                        }
                        hqlString.append(String.format(" AND %s IN (",cond.getKey()));
                        for (int j = 0; j < condValues.length; j++) {
                            Object curValue = condValues[j];
                            if (j != 0) {
                                hqlString.append(",");
                            }
                            hqlString.append(String.format("value%d_%d", valueNum, j));
                            queryParams.put(String.format("value%d_%d", valueNum, j), condValues[j]);
                        }
                        hqlString.append(") ");
                        break;
                    default:
                        throw new ConditionException("Unsupported relation " + cond.getRelation().toString());
                }
            }
        }
        //如果没有ID排序条件，则永远加一个按ID倒排。不需要理由
        boolean hasIDOrder = false;
        for (OrderItem orderItem : this.orders) {
            if (orderItem.getKey().equalsIgnoreCase("id")) {
                hasIDOrder = true;
            }
        }
        if (!hasIDOrder) {
            this.addOrder("id", OrderItem.Order.DESC);
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
        //创建查询
        Query query = session.createQuery(hqlString.toString());

        //如果设置了分页条件，则生成分页语句
        if (this.page != -1) {
            if (returnCount) {
                throw new WMSServiceException(String.format("returnCount时不允许设置页码(%d)", page));
            }
            if (page == 0) {
                throw new WMSServiceException(String.format("page(%d)必须从1开始！", page));
            }
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

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
        this.orders.add(orderItem);
        return this;
    }

    public Condition addOrder(String key) {
        this.addOrder(key, OrderItem.Order.ASC);
        return this;
    }

    @SuppressWarnings("all")
    private Object changeType(Object value, Class targetType) {
        if (value == null) return null;
        if (targetType == value.getClass()) return value;
        try {
            if (targetType == int.class || targetType == Integer.class) {
                double doubleValue = Double.parseDouble(value.toString());
                return (int) doubleValue;
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value.toString());
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value.toString());
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(value.toString());
            } else if (targetType == Timestamp.class) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf1.setLenient(true);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                sdf2.setLenient(true);
                try {
                    return sdf1.parse(value.toString());
                } catch (Exception e) {

                }
                try {
                    return sdf2.parse(value.toString());
                } catch (Exception e) {
                    throw new WMSServiceException("\"" + value.toString() + "\"不是合法的日期字符串！");
                }
            } else {
                throw new WMSServiceException("未被支持的Condition类型：" + targetType.getName());
            }
        } catch (WMSServiceException e) {
            throw e;
        }catch (Exception e){
            throw new WMSServiceException(String.format("\"%s\"不能转换为目标类型%s，请检查输入！",value.toString(),targetType.getName()));
        }
    }
}
