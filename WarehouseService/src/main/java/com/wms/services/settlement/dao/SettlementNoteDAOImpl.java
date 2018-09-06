package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteView;
import org.springframework.stereotype.Repository;

@Repository
public class SettlementNoteDAOImpl
        extends BaseDAOImpl<SettlementNote,SettlementNoteView>
        implements SettlementNoteDAO{

    public SettlementNoteDAOImpl(){
        super(SettlementNote.class,SettlementNoteView.class,SettlementNote::getId);}

}
