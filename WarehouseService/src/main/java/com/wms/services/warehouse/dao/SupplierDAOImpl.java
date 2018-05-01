package com.wms.services.warehouse.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository

public class SupplierDAOImpl
        extends BaseDAOImpl<Supplier, SupplierView> implements SupplierDAO {
    public SupplierDAOImpl() { super(Supplier.class, SupplierView.class,Supplier::getId); }
}










