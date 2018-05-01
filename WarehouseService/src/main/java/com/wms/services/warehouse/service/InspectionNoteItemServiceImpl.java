package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.InspectionNoteItemDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNoteItem;
import com.wms.utilities.model.InspectionNoteItemView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class InspectionNoteItemServiceImpl
    implements InspectionNoteItemService{

    @Autowired
    InspectionNoteItemDAO inspectionNoteItemDAO;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        this.validateEntities(accountBook,objs);
        return this.inspectionNoteItemDAO.add(accountBook,objs);
    }

    @Override
    public void update(String accountBook, InspectionNoteItem[] objs) throws WMSServiceException {
        this.validateEntities(accountBook,objs);
        this.update(accountBook,objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for(int id : ids){
            idChecker.check(this.getClass(),accountBook,id,"删除的送检单条目");
        }
        this.remove(accountBook,ids);
    }

    @Override
    public InspectionNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.inspectionNoteItemDAO.find(accountBook,cond);
    }

    private void validateEntities(String accountBook, InspectionNoteItem[] inspectionNoteItems){
        Stream.of(inspectionNoteItems).forEach((inspectionNoteItem -> {
            //数据验证
            new Validator("状态").min(0).max(2).validate(inspectionNoteItem.getState());
            new Validator("送检数量").min(0).validate(inspectionNoteItem.getAmount());
            new Validator("送检单位数量").min(0).validate(inspectionNoteItem.getUnitAmount());
            if(inspectionNoteItem.getReturnAmount() != null){
                //TODO max不支持double以及bigDecimal?
                new Validator("送检返回数量").min(0)/*.max(inspectionNoteItem.getAmount())*/.validate(inspectionNoteItem.getAmount());
            }
            if(inspectionNoteItem.getReturnUnitAmount() != null){
                new Validator("送检返回单位数量").min(0).validate(inspectionNoteItem.getReturnUnitAmount());
            }
            //验证外键
            this.idChecker.check(InspectionNoteService.class,accountBook,inspectionNoteItem.getInspectionNoteId(),"关联送检单");
            this.idChecker.check(WarehouseEntryItemService.class,accountBook,inspectionNoteItem.getWarehouseEntryItemId(),"关联入库单条目");
            this.idChecker.check(StorageLocationService.class,accountBook,inspectionNoteItem.getInspectionStorageLocationId(),"送检库位");
            if(inspectionNoteItem.getReturnStorageLocationId() != null){
                this.idChecker.check(StorageLocationService.class,accountBook,inspectionNoteItem.getReturnStorageLocationId(),"返回库位");
            }
            if(inspectionNoteItem.getPersonId() != null){
                this.idChecker.check(PersonService.class,accountBook,inspectionNoteItem.getPersonId(),"作业人员");
            }
        }));
    }
}
