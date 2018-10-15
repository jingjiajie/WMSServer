package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Invoice;
import com.wms.utilities.model.InvoiceView;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDAOImpl
    extends BaseDAOImpl<Invoice,InvoiceView>
    implements InvoiceDAO{

    public InvoiceDAOImpl(){
        super(Invoice.class,InvoiceView.class,Invoice::getId);}

}
