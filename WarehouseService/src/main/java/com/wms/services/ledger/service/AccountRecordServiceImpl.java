package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountRecordDAO;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Transactional
@Service
public class AccountRecordServiceImpl implements AccountRecordService{
    @Autowired
    AccountRecordDAO accountRecordDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    AccountPeriodService accountPeriodService;

    public int[] add(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException
    {
        this.validateEntities(accountBook,accountRecords);
        Stream.of(accountRecords).forEach((accountRecord)->{
            accountRecord.setTime(new Timestamp(System.currentTimeMillis()));
        });
        return accountRecordDAO.add(accountBook,accountRecords);
    }

    @Transactional
    public void update(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException{

        this.validateEntities(accountBook,accountRecords);
        accountRecordDAO.update(accountBook,accountRecords);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (accountRecordDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除期间不存在，请重新查询！(%d)", id));
            }
        }
        try {
            accountRecordDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除会计期间信息失败，如果会计期间信息已经被引用，需要先删除引用的内容，才能删除会计期间信息！");
        }
    }

    public AccountRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.accountRecordDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.accountRecordDAO.findCount(database,cond);
    }

    private void validateEntities(String accountBook,AccountRecord[] accountRecords) throws WMSServiceException{

        //外键检测
        Stream.of(accountRecords).forEach((accountRecord -> {
            new Validator("余额").notnull().min(0).validate(accountRecord.getBalance());


            //验证外键
            this.idChecker.check(WarehouseService.class, accountBook, accountRecord.getWarehouseId(), "仓库");
            this.idChecker.check(AccountPeriodService.class, accountBook, accountRecord.getAccountPeriodId(), "会计期间");
            this.idChecker.check(AccountTitleService.class, accountBook, accountRecord.getAccountTitleId(), "科目");
            this.idChecker.check(PersonService.class, accountBook, accountRecord.getPersonId(), "记录人");
        }));


    }
}
