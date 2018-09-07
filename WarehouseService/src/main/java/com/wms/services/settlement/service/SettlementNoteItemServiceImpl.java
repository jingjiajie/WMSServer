package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SettlementNoteItemDAO;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.SettlementNoteItem;
import com.wms.utilities.model.SettlementNoteItemView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.stream.Stream;

@Service
@Transactional
public class SettlementNoteItemServiceImpl
    implements SettlementNoteItemService{
    @Autowired
    SettlementNoteItemDAO settlementNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, SettlementNoteItem[] settlementNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNoteItems);
        return settlementNoteItemDAO.add(accountBook,settlementNoteItems);
    }

    @Override
    public void update(String accountBook, SettlementNoteItem[] settlementNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNoteItems);
        settlementNoteItemDAO.update(accountBook, settlementNoteItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (settlementNoteItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除结算单条目不存在，请重新查询！(%d)", id));
                }
            }

            settlementNoteItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除结算单条目信息失败，如果结算单条目信息已经被引用，需要先删除引用的内容，才能删除该结算单条目");
        }
    }

    @Override
    public SettlementNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.settlementNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SettlementNoteItem[] settlementNoteItems) throws WMSServiceException{
        Stream.of(settlementNoteItems).forEach((settlementNoteItem -> {
            this.idChecker.check(SupplierServices.class, accountBook, settlementNoteItem.getSupplierId(), "供货商ID");
            this.idChecker.check(SettlementNoteService.class, accountBook, settlementNoteItem.getSettlementNoteId(), "结算单ID");

            new Validator("仓储费").min(0).max(1).validate(settlementNoteItem.getStorageCharge());
            new Validator("物流费").notEmpty().validate(settlementNoteItem.getLogisticFee());
            new Validator("实付金额").notEmpty().validate(settlementNoteItem.getActualPayment());
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.settlementNoteItemDAO.findCount(database,cond);
    }
}
