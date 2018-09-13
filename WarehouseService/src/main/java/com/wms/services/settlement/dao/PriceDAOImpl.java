package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Price;
import com.wms.utilities.model.PriceView;
import org.springframework.stereotype.Repository;

@Repository
public class PriceDAOImpl
    extends BaseDAOImpl<Price,PriceView>
    implements PriceDAO{

    public PriceDAOImpl(){
        super(Price.class,PriceView.class,Price::getId);}
}
