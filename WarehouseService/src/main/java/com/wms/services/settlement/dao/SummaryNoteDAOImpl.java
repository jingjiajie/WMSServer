package com.wms.services.settlement.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import org.springframework.stereotype.Repository;

@Repository
public class SummaryNoteDAOImpl
        extends BaseDAOImpl<SummaryNote,SummaryNoteView>
        implements SummaryNoteDAO {
    public SummaryNoteDAOImpl(){
        super(SummaryNote.class,SummaryNoteView.class,SummaryNote::getId);}
}
