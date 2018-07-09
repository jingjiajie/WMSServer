package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.TaxItem;
import com.wms.utilities.model.TaxItemView;

import java.util.Map;

public interface TaxItemDAO extends BaseDAO<TaxItem,TaxItemView>{

}
