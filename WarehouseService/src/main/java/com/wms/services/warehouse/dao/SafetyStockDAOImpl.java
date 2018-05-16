package com.wms.services.warehouse.dao;

import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;
import org.springframework.stereotype.Repository;
import com.wms.utilities.dao.BaseDAOImpl;


@Repository
public class SafetyStockDAOImpl
    extends BaseDAOImpl<SafetyStock,SafetyStockView>
    implements SafetyStockDAO{

    public SafetyStockDAOImpl(){
        super(SafetyStock.class,SafetyStockView.class,SafetyStock::getId);
    }
}
