package com.wms.services.warehouse.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SupplyDAOImpl
        extends BaseDAOImpl<Supply, SupplyView> implements SupplyDAO {
    public SupplyDAOImpl() { super(Supply.class, SupplyView.class,Supply::getId);}


    }
