package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.InspectionNoteDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class InspectionNoteServiceImpl
    implements InspectionNoteService{
    @Autowired
    InspectionNoteDAO inspectionNoteDAO;
    @Autowired
    IDChecker idChecker;
    @Autowired
    OrderNoGenerator orderNoGenerator;

    private static final String PREFIX = "J";

    @Override
    public int[] add(String accountBook, InspectionNote[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj)->{
            if(obj.getNo() == null || obj.getNo().isEmpty()) {
                obj.setNo(this.orderNoGenerator.generateNextNo(accountBook, PREFIX));
            }
            obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        return inspectionNoteDAO.add(accountBook,objs);
    }

    @Override
    public void update(String accountBook, InspectionNote[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj)->{
            new Validator("最后更新人员").notnull().validate(obj.getLastUpdatePersonId());
            obj.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        });
        this.validateEntities(accountBook,objs);
        this.inspectionNoteDAO.update(accountBook,objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for(int id : ids){
            if(this.inspectionNoteDAO.find(accountBook,new Condition().addCondition("id",id)).length == 0){
                throw new WMSServiceException(String.format("送检单不存在，请重新查询(%d)",id));
            }
        }
        this.inspectionNoteDAO.remove(accountBook,ids);
    }

    @Override
    public InspectionNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.inspectionNoteDAO.find(accountBook,cond);
    }

    private void validateEntities(String accpountBook,InspectionNote[] inspectionNotes) throws WMSServiceException{
        Stream.of(inspectionNotes).forEach((inspectionNote -> {
            new Validator("状态").min(0).max(2).validate(inspectionNote.getState());
            new Validator("创建时间").notnull().validate(inspectionNote.getCreateTime());

            idChecker.check(WarehouseEntryService.class,accpountBook,inspectionNote.getWarehouseEntryId(),"关联入库单")
                    .check(WarehouseService.class,accpountBook,inspectionNote.getWarehouseId(),"仓库")
                    .check(PersonService.class,accpountBook,inspectionNote.getCreatePersonId(),"创建人员");
            if(inspectionNote.getLastUpdatePersonId() != null){
                idChecker.check(PersonService.class,accpountBook,inspectionNote.getLastUpdatePersonId(),"最后更新人员");
            }
        }));
    }
}
