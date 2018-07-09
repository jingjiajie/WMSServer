package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.Tax;
import com.wms.utilities.model.TaxView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class TaxDAOImpl
        extends BaseDAOImpl<Tax,TaxView>
        implements TaxDAO {
    public TaxDAOImpl(){
        super(Tax.class,TaxView.class,Tax::getId);
    }

}