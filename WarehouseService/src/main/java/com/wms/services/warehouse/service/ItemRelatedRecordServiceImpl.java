package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.ItemRelatedRecordDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.InspectionNote;
import com.wms.utilities.model.InspectionNoteView;
import com.wms.utilities.model.ItemRelatedRecord;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class ItemRelatedRecordServiceImpl
        implements ItemRelatedRecordService {
    @Autowired
    ItemRelatedRecordDAO itemRelatedRecordDAO;

    @Override
    public int[] add(String accountBook, ItemRelatedRecord[] itemRelatedRecords) throws WMSServiceException {
        this.setAmount(itemRelatedRecords);
        this.validateEntities(accountBook, itemRelatedRecords);
        return itemRelatedRecordDAO.add(accountBook, itemRelatedRecords);
    }

    @Override
    public void update(String accountBook, ItemRelatedRecord[] itemRelatedRecords) throws WMSServiceException {
        this.setAmount(itemRelatedRecords);
        this.validateEntities(accountBook, itemRelatedRecords);
        this.itemRelatedRecordDAO.update(accountBook, itemRelatedRecords);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (this.itemRelatedRecordDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("相关记录不存在，请重新查询(%d)", id));
            }
        }
        this.itemRelatedRecordDAO.remove(accountBook, ids);
    }

    @Override
    public ItemRelatedRecord[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.itemRelatedRecordDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException {
        return this.itemRelatedRecordDAO.findCount(accountBook, cond);
    }

    @Override
    public ItemRelatedRecord[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.itemRelatedRecordDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, ItemRelatedRecord[] itemRelatedRecords) throws WMSServiceException {
        Stream.of(itemRelatedRecords).forEach((itemRelatedRecord -> {
            new Validator("库存记录批次号").notnull().notEmpty().validate(itemRelatedRecord.getStockRecordBatchNo());
            new Validator("相关条目ID").notnull().notEmpty().validate(itemRelatedRecord.getRelatedItemId());
            new Validator("条目类型").notnull().notEmpty().validate(itemRelatedRecord.getItemType());
        }));
    }

    private void setAmount(ItemRelatedRecord[] itemRelatedRecords) throws WMSServiceException {
        Stream.of(itemRelatedRecords).forEach((itemRelatedRecord -> {
            if (itemRelatedRecord.getBatchAmount() == null) {
                itemRelatedRecord.setBatchAmount(new BigDecimal(0));
            }
            if (itemRelatedRecord.getBatchAvailableAmount() == null) {
                itemRelatedRecord.setBatchAvailableAmount(new BigDecimal(0));
            }
        }
        ));
    }
}
