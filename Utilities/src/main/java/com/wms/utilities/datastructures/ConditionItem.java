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

public class ConditionItem {
    public enum Relation{
        EQUAL,GREATER_THAN,LESS_THAN,BETWEEN
    }

    private String key;
    private Relation relation;
    private Object[] values;

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
