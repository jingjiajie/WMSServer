package com.wms.services.warehouse.dao;

import com.netflix.discovery.converters.Auto;
import com.wms.services.warehouse.WarehouseService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.function.Function;
import java.util.function.Supplier;

@Repository
public class WarehouseServiceDAOTemplateFactory {
    @Autowired
    SessionFactory sessionFactory;

    public <T> WarehouseServiceDAOTemplate<T> createWarehouseServiceDAOTemplate(String tableName, Function<T,Integer> methodGetPK){
        return new WarehouseServiceDAOTemplate<>(sessionFactory,tableName,methodGetPK);
    }
}
