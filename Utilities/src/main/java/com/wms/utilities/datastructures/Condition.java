package com.wms.utilities.datastructures;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.wms.utilities.exceptions.ConditionException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Condition {
    public enum Relation{
        EQUAL,GREATER_THAN,LESS_THAN,BETWEEN
    }

    private String key;
    private Relation relation;
    private Object[] values;

    private Condition(){}

    public static Condition[] fromJson(String jsonStr) throws ConditionException{
        Gson gson = new Gson();
        Condition[] results = gson.fromJson(jsonStr,new TypeToken<Condition[]>(){}.getType());
        return results;
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonElement = null;
//        try {
//            jsonElement = jsonParser.parse(jsonStr);
//        }catch (Exception ex){
//            throw new ConditionException("Invalid condition: "+jsonStr);
//        }
//        //要转换的JsonObject列表
//        List<JsonObject> listJsonObject = new ArrayList<JsonObject>();
//        //如果是单项，则作为数组的唯一一项
//        if(jsonElement.isJsonObject()){
//            listJsonObject.add(jsonElement.getAsJsonObject());
//        }else if(jsonElement.isJsonArray()){ //如果是数组，转为Condition数组
//            JsonArray jsonArray = jsonElement.getAsJsonArray();
//            for(JsonElement arrayElement : jsonArray) {
//                if (!arrayElement.isJsonObject()) {
//                    throw new ConditionException("Invalid condition: " + arrayElement.toString());
//                }
//                listJsonObject.add(arrayElement.getAsJsonObject());
//            }
//        }else{ //格式不对，抛出错误
//            throw new ConditionException("Invalid condition: " + jsonElement.toString());
//        }
//
//        //开始进行转换
//        List<Condition> results = new ArrayList<Condition>();
//        for(JsonObject jsonObject : listJsonObject) {
//            if (!jsonObject.has("key")) {
//                throw new ConditionException("condition must contain property \"key\" : " + jsonObject.toString());
//            } else if (!jsonObject.has("relation")) {
//                throw new ConditionException("condition must contain property \"relation\" : " + jsonObject.toString());
//            } else if (!jsonObject.has("values")) {
//                throw new ConditionException("condition must contain property \"values\" : " + jsonObject.toString());
//            }
//            String key = jsonObject.get("key");
//        }
    }

    public static Query createQuery(String entityName, Condition[] conditions, Session session){
        StringBuffer hqlString = new StringBuffer("from "+entityName+" ");
        Map<String,Object> queryParams = new HashMap<String, Object>();
        if(conditions.length != 0){
            hqlString.append("where 1=1 ");
        }

        for(int i=0;i<conditions.length;i++){
            Condition cond = conditions[i];
            switch (cond.relation) {
                case EQUAL:
                    if(cond.getValues().length != 1){
                        throw new ConditionException("EQUAL relation needs one value");
                    }
                    hqlString.append(String.format("AND %s = :value%d", cond.getKey(), i));
                    queryParams.put("value" + i, cond.getValues()[0]);
                    break;
                case GREATER_THAN:
                    if(cond.getValues().length != 1){
                        throw new ConditionException("GREATER_THAN relation needs one value");
                    }
                    hqlString.append(String.format("AND %s > :value%d", cond.getKey(), i));
                    queryParams.put("value" + i, cond.getValues()[0]);
                    break;
                case LESS_THAN:
                    if(cond.getValues().length != 1){
                        throw new ConditionException("LESS_THAN relation needs one value");
                    }
                    hqlString.append(String.format("AND %s < :value%d", cond.getKey(), i));
                    queryParams.put("value" + i, cond.getValues()[0]);
                    break;
                case BETWEEN:
                    if(cond.getValues().length != 2){
                        throw new ConditionException("BETWEEN relation needs two values");
                    }
                    hqlString.append(String.format("AND %s BETWEEN :value%d_1 AND :value%d_2", cond.getKey(), i,i));
                    queryParams.put("value" + i+"_1", cond.getValues()[0]);
                    queryParams.put("value" + i+"_2", cond.getValues()[1]);
                    break;
                default:
                    throw new ConditionException("Unsupported relation "+cond.relation.toString());
            }
        }
        Query query = session.createQuery(hqlString.toString());
        for(Map.Entry<String,Object> entry : queryParams.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        return query;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
