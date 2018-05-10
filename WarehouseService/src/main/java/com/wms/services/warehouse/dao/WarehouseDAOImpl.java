package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class WarehouseDAOImpl
        extends BaseDAOImpl<Warehouse, WarehouseView>
        implements WarehouseDAO{
    public WarehouseDAOImpl() {
        super(Warehouse.class, WarehouseView.class, Warehouse::getId);
    }
}