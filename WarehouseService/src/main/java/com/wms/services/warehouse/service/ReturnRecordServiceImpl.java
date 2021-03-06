package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.ReturnRecordDAO;
import com.wms.services.warehouse.datastructures.ReturnAmount;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.controller.BaseController;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Service
@Transactional
public class ReturnRecordServiceImpl implements ReturnRecordService {

    @Autowired
    ReturnRecordDAO returnRecordDAO;

    @Transactional
    public int[] add(String accountBook, ReturnRecord[] returnRecords) throws WMSServiceException {
        this.validate(accountBook,returnRecords);
        return returnRecordDAO.add(accountBook, returnRecords);
    }

    @Transactional
    public void update(String accountBook, ReturnRecord[] returnRecords) throws WMSServiceException {
        this.validate(accountBook,returnRecords);
        returnRecordDAO.update(accountBook, returnRecords);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (returnRecordDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除退货记录不存在，请重新查询！(%d)", id));
            }
        }
        try {
            returnRecordDAO.remove(accountBook, ids);
        } catch (Exception ex) {
            throw new WMSServiceException("删除退货记录失败，如果退货记录已经被引用，需要先退货记录的内容，才能删除该退货记录信息");
        }
    }

    @Transactional
    public ReturnRecordView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.returnRecordDAO.find(accountBook, cond);
    }

    @Override
    @Transactional
    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.returnRecordDAO.findCount(database, cond);
    }

    @Override
    @Transactional
    public ReturnAmount findAmount(String database, int supplyId, Timestamp timestampStart, Timestamp timestampEnd) throws WMSServiceException {
        ReturnRecordView[] returnRecords=this.returnRecordDAO.find(database,new Condition().addCondition("supplyId",supplyId).
                addCondition("time",new Object[]{timestampStart,timestampEnd}, ConditionItem.Relation.BETWEEN));
        if(returnRecords.length==0){return new ReturnAmount();}
        BigDecimal amountAllQualified=new BigDecimal(0);
        BigDecimal amountAllUnqualified=new BigDecimal(0);
        for(int i=0;i<returnRecords.length;i++){
            if(returnRecords[i].getState()== TransferStock.QUALIFIED)
            amountAllQualified=amountAllQualified.add(returnRecords[i].getAmount());
            if(returnRecords[i].getState()== TransferStock.UNQUALIFIED)
                amountAllUnqualified=amountAllUnqualified.add(returnRecords[i].getAmount());
        }
        ReturnAmount returnAmount=new ReturnAmount();
        returnAmount.setMaterialName(returnRecords[0].getMaterialName());
        returnAmount.setMaterialNo(returnRecords[0].getMaterialNo());
        returnAmount.setMaterialProductLine(returnRecords[0].getMaterialProductLine());
        returnAmount.setSupplierName(returnRecords[0].getSupplierName());
        returnAmount.setSupplyId(supplyId);
        returnAmount.setAmountQualified(amountAllQualified);
        returnAmount.setAmountUnqualified(amountAllUnqualified);
        return  returnAmount;
    }

    private void validate(String accountBook, ReturnRecord[] returnRecords) {

    }
}