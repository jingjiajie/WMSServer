package com.wms.services.salary.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;
import org.springframework.stereotype.Repository;

@Repository
public class PayNoteTaxDAOImpl extends BaseDAOImpl<PayNoteTax,PayNoteTaxView>
implements PayNoteTaxDAO{
    public PayNoteTaxDAOImpl(){
        super(PayNoteTax.class,PayNoteTaxView.class,PayNoteTax::getId);}
}
