package com.wms.services.warehouse.dao;
import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.model.Supply;
import com.wms.utilities.model.SupplyView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;

public interface SupplyDAO  extends BaseDAO<Supply,SupplyView> {

}
