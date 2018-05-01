package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.StorageArea;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.StorageAreaView;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.model.WarehouseView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class StorageAreaDAOImpl
        extends BaseDAOImpl<StorageArea, StorageAreaView>
        implements StorageAreaDAO{
    public StorageAreaDAOImpl() {
        super(StorageArea.class, StorageAreaView.class, StorageArea::getId);
    }
}
