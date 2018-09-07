package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.model.SummaryNoteItemView;
import org.springframework.stereotype.Repository;

@Repository
public class SummaryNoteItemDAOImpl
        extends BaseDAOImpl<SummaryNoteItem,SummaryNoteItemView>
        implements SummaryNoteItemDAO{
    public SummaryNoteItemDAOImpl(){
        super(SummaryNoteItem.class,SummaryNoteItemView.class,SummaryNoteItem::getId);}
}
