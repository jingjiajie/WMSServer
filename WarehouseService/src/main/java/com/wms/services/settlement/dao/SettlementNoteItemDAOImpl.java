package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;


public class SettlementNoteItemDAOImpl
    extends BaseDAOImpl<SettlementNoteItem,SettlementNoteItemView>
    implements SettlementNoteItemDAO{

    public SettlementNoteItemDAOImpl(){
        super(SettlementNoteItem.class,SettlementNoteItemView.class,SettlementNoteItem::getId);}

}
