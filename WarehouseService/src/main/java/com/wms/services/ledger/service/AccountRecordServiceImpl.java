package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountRecordDAO;
import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    AccountRecordService accountRecordService;

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

    public void RealTransformAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException
    {

        AccountTitleView[] OutAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getOutaccountTitleId()));
        AccountTitleView OutAccountTitleView=OutAccountTitleViews[0];
        AccountTitleView[] InAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getInaccountTitleId()));
        AccountTitleView InAccountTitleView=InAccountTitleViews[0];

        AccountRecord accountRecord=new AccountRecord();
        accountRecord.setAccountTitleId(OutAccountTitleView.getId());
        accountRecord.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord.setPersonId(transferAccount.getPersonId());
        accountRecord.setComment(transferAccount.getComment());
        accountRecord.setWarehouseId(transferAccount.getWarehouseId());
        accountRecord.setSummary(transferAccount.getSummary());
        accountRecord.setVoucherInfo(transferAccount.getVoucherInfo());

        AccountRecord accountRecord1=new AccountRecord();
        accountRecord1.setAccountTitleId(InAccountTitleView.getId());
        accountRecord1.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord1.setPersonId(transferAccount.getPersonId());
        accountRecord1.setComment(transferAccount.getComment());
        accountRecord1.setWarehouseId(transferAccount.getWarehouseId());
        accountRecord1.setSummary(transferAccount.getSummary());
        accountRecord1.setVoucherInfo(transferAccount.getVoucherInfo());

        AccountRecordView[] accountRecordViews= accountRecordService.find(accountBook,new Condition()
                .addCondition("warehouseId",new Integer[]{transferAccount.getWarehouseId()})
                .addCondition("accountPeriodId",new Integer[]{transferAccount.getAccountPeriodId()})
                .addCondition("accountTitleId",new Integer[]{OutAccountTitleView.getId()}));

        AccountRecordView newestAccountRecordView=new AccountRecordView();
        for (int i=0;i<accountRecordViews.length;i++){
            for (int j=0;j<accountRecordViews.length;j++){
                if (accountRecordViews[i].getTime().after(accountRecordViews[j].getTime())){
                    newestAccountRecordView=accountRecordViews[i];
                }else{
                    newestAccountRecordView=accountRecordViews[j];
                }
            }
        }

        AccountRecordView[] inaccountRecordViews= accountRecordService.find(accountBook,new Condition()
                .addCondition("warehouseId",new Integer[]{transferAccount.getWarehouseId()})
                .addCondition("accountPeriodId",new Integer[]{transferAccount.getAccountPeriodId()})
                .addCondition("accountTitleId",new Integer[]{InAccountTitleView.getId()}));

        AccountRecordView newestAccountRecordView1=new AccountRecordView();
        for (int i=0;i<inaccountRecordViews.length;i++){
            for (int j=0;j<inaccountRecordViews.length;j++){
                if (inaccountRecordViews[i].getTime().after(inaccountRecordViews[j].getTime())){
                    newestAccountRecordView1=inaccountRecordViews[i];
                }else{
                    newestAccountRecordView1=inaccountRecordViews[j];
                }
            }
        }

        if (OutAccountTitleView.getDirection()==InAccountTitleView.getDirection()){
            if (OutAccountTitleView.getDirection()==AccountTitleService.Debit){
                //todo 找出最新一条的余额扣掉
                accountRecord.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().add(transferAccount.getChangeAmount()));
            }else{
                accountRecord.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().add(transferAccount.getChangeAmount()));
            }
        }

        if (OutAccountTitleView.getDirection()!=InAccountTitleView.getDirection()){
            if (OutAccountTitleView.getDirection()==AccountTitleService.Debit){
                //todo 找出最新一条的余额扣掉
                accountRecord.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().subtract(transferAccount.getChangeAmount()));
            }else{
                accountRecord.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().subtract(transferAccount.getChangeAmount()));
            }
        }
        this.add(accountBook,new AccountRecord[]{accountRecord,accountRecord1});

    }
}
