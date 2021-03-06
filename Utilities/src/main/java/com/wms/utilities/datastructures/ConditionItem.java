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
        EQUAL,NOT_EQUAL,GREATER_THAN,GREATER_THAN_OR_EQUAL_TO,LESS_THAN,LESS_THAN_OR_EQUAL_TO,BETWEEN,CONTAINS,STARTS_WITH,ENDS_WITH,IN
    }

    private String key;
    private Relation relation = Relation.EQUAL;
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
