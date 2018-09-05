package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SettlementNoteDAO;
import com.wms.services.warehouse.service.SafetyStockService;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;


@Service
@Transactional
public class SettlementNoteServiceImpl implements SettlementNoteService {
    @Autowired
    SettlementNoteDAO settlementNoteDAO;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {
        return settlementNoteDAO.add(accountBook,settlementNotes);
    }

    @Override
    public void update(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {
        settlementNoteDAO.update(accountBook, settlementNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (settlementNoteDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除结算单不存在，请重新查询！(%d)", id));
                }
            }

            settlementNoteDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除结算单信息失败，如果结算单信息已经被引用，需要先删除引用的内容，才能删除该汇总单");
        }
    }

    @Override
    public SettlementNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.settlementNoteDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,Material[] materials) throws WMSServiceException{
        Stream.of(materials).forEach((material -> {
            new Validator("是否启用").min(0).max(1).validate(material.getEnabled());
            new Validator("代号").notEmpty().validate(material.getNo());
            new Validator("物料名称").notEmpty().validate(material.getName());
            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",material.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",material.getWarehouseId()));
            }
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.settlementNoteDAO.findCount(database,cond);
    }
}
