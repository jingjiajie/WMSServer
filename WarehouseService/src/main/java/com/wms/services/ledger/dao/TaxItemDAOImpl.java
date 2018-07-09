package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAOImpl;

import com.wms.utilities.model.TaxItem;
import com.wms.utilities.model.TaxItemView;
import org.springframework.stereotype.Repository;



@Repository
public class TaxItemDAOImpl
        extends BaseDAOImpl<TaxItem,TaxItemView>
        implements TaxItemDAO {
    public TaxItemDAOImpl(){
        super(TaxItem.class,TaxItemView.class,TaxItem::getId);
    }


}
