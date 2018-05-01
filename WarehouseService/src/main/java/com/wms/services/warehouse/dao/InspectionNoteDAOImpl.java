package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import org.springframework.stereotype.Repository;

@Repository
public class InspectionNoteDAOImpl
    extends BaseDAOImpl<InspectionNote,InspectionNoteView>
    implements InspectionNoteDAO{

    public InspectionNoteDAOImpl(){
        super(InspectionNote.class,InspectionNoteView.class,InspectionNote::getId);
    }

}

