package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import org.springframework.stereotype.Repository;

@Repository
public class InspectionNoteItemDAOImpl
    extends BaseDAOImpl<InspectionNoteItem,InspectionNoteItemView>
    implements InspectionNoteItemDAO{

    public InspectionNoteItemDAOImpl(){
        super(InspectionNoteItem.class,InspectionNoteItemView.class,InspectionNoteItem::getId);
    }
}
