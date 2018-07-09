package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Tax;
import com.wms.utilities.model.TaxView;

import java.util.Map;

public interface TaxDAO extends BaseDAO<Tax,TaxView> {

}
