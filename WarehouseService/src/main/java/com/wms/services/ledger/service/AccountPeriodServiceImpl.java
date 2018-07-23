package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountPeriodDAO;
import com.wms.services.ledger.datestructures.CarryOver;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

import javax.persistence.OrderBy;
import java.sql.Timestamp;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class AccountPeriodServiceImpl implements AccountPeriodService{
    @Autowired
    AccountPeriodDAO accountPeriodDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    AccountRecordService accountRecordService;

    public int[] add(String accountBook, AccountPeriod[] accountPeriods) throws WMSServiceException
    {
        this.validateEntities(accountBook,accountPeriods);
        Stream.of(accountPeriods).forEach((accountPeriod)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{accountPeriod.getName()})
                    .addCondition("warehouseId",new Integer[]{accountPeriod.getWarehouseId()})).length > 0) {
                throw new WMSServiceException("物料名：" + accountPeriod.getName() +"已经存在!");
            }
        });
        return accountPeriodDAO.add(accountBook,accountPeriods);
    }

    @Transactional
    public void update(String accountBook, AccountPeriod[] accountPeriods) throws WMSServiceException{

        this.validateEntities(accountBook,accountPeriods);
        Stream.of(accountPeriods).forEach((accountPeriod)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{accountPeriod.getName()})
                    .addCondition("warehouseId",new Integer[]{accountPeriod.getWarehouseId()})
                    .addCondition("id",new Integer[]{accountPeriod.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("物料名：" + accountPeriod.getName() +"已经存在!");
            }
        });
        accountPeriodDAO.update(accountBook,accountPeriods);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (accountPeriodDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除期间不存在，请重新查询！(%d)", id));
            }
        }
        try {
            accountPeriodDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除会计期间信息失败，如果会计期间信息已经被引用，需要先删除引用的内容，才能删除会计期间信息！");
        }
    }

    public AccountPeriodView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.accountPeriodDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.accountPeriodDAO.findCount(database,cond);
    }

    private void validateEntities(String accountBook,AccountPeriod[] accountPeriods) throws WMSServiceException{

        for(int i=0;i<accountPeriods.length;i++){
            for(int j=i+1;j<accountPeriods.length;j++){
                String name=accountPeriods[i].getName();
                if(name.equals(accountPeriods[j].getName()))
                {
                    throw new WMSServiceException("期间名称：" +name+"在添加的列表中重复!");
                }
            }
        }
        //外键检测
        Stream.of(accountPeriods).forEach((accountPeriod -> {
            new Validator("是否已截止").min(0).max(1).validate(accountPeriod.getEnded());
            new Validator("起始时间").notEmpty().validate(accountPeriod.getStartTime());
            new Validator("期间名称").notEmpty().validate(accountPeriod.getStartTime());

            if(this.find(accountBook,
                    new Condition().addCondition("id",new Integer[]{ accountPeriod.getLastAccountPeriodId()})).length == 0){
                throw new WMSServiceException(String.format("上期期间不存在，请重新提交！(%d)",accountPeriod.getLastAccountPeriodId()));
            }

            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",accountPeriod.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",accountPeriod.getWarehouseId()));
            }
        }));

    }

    public   void carryOver(String accountBook,CarryOver carryOver) throws WMSServiceException{
        //取得需要的三个关键词
        int curWarehouseId=carryOver.getWarehouseId();
        int curPersonId=carryOver.getPersonId();
        //TODO 结转功能 为了保持会计工作的连续性，一定要把本会计年度末的余额转到下个会计年度

        //获得当前仓库，最新的没截止的期间,将其截止并启用新的期间
        AccountPeriodView accountPeriodView=this.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("ended",AccountPeriodService.ended_false).addOrder("startTime", OrderItem.Order.DESC))[0];
        AccountPeriod oldAccountPeriod = ReflectHelper.createAndCopyFields(accountPeriodView,AccountPeriod.class);
        oldAccountPeriod.setEndTime(new Timestamp(System.currentTimeMillis()));
        oldAccountPeriod.setEnded(AccountPeriodService.ended_ture);
        this.update(accountBook,new AccountPeriod[] {oldAccountPeriod});

        AccountPeriod newAccountPeriod=new AccountPeriod();
        newAccountPeriod.setStartTime(new Timestamp(System.currentTimeMillis()));
        newAccountPeriod.setLastAccountPeriodId(oldAccountPeriod.getId());
        newAccountPeriod.setEnded(AccountPeriodService.ended_false);
        newAccountPeriod.setName(new Timestamp(System.currentTimeMillis()).toString());
        newAccountPeriod.setWarehouseId(curWarehouseId);
        int newAccountPeriodID=this.add(accountBook,new AccountPeriod[] {newAccountPeriod})[0];

        List<AccountRecord> accountRecordList=new ArrayList();

        AccountRecordView[] accountRecordViews= accountRecordService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("accountPeriodId",new Integer[]{oldAccountPeriod.getId()}));
        Map<Integer, List<AccountRecordView>> groupBySupplierIdMap =
                Stream.of(accountRecordViews).collect(Collectors.groupingBy(AccountRecordView::getAccountTitleId));

        Iterator<Map.Entry<Integer,List<AccountRecordView>>> entries = groupBySupplierIdMap.entrySet().iterator();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<AccountRecordView>> entry = entries.next();
            Integer accountPeriodId = entry.getKey();
            List<AccountRecordView> accountRecordViewsList=entry.getValue();

            AccountRecordView[] curAccountRecordViews=null;
            curAccountRecordViews = (AccountRecordView[]) Array.newInstance(AccountRecordView.class,accountRecordViewsList.size());
            accountRecordViewsList.toArray(curAccountRecordViews);

            AccountRecordView newestAccountRecordView=new AccountRecordView();
            for (int i=0;i<curAccountRecordViews.length;i++){
                for (int j=0;j<curAccountRecordViews.length;j++){
                    if (curAccountRecordViews[i].getTime().after(curAccountRecordViews[j].getTime())){
                        newestAccountRecordView=curAccountRecordViews[i];
                    }else{
                        newestAccountRecordView=curAccountRecordViews[j];
                    }
                }
            }
            AccountRecord theAccountRecord=new AccountRecord();
            theAccountRecord.setWarehouseId(curWarehouseId);
            theAccountRecord.setAccountTitleId(accountPeriodId);
            theAccountRecord.setAccountPeriodId(newAccountPeriodID);
            theAccountRecord.setTime(new Timestamp(System.currentTimeMillis()));
            theAccountRecord.setPersonId(curPersonId);
            theAccountRecord.setBalance(newestAccountRecordView.getBalance());
            theAccountRecord.setComment("财务转结");
            theAccountRecord.setCreditAmount(new BigDecimal(0));
            theAccountRecord.setDebitAmount(new BigDecimal(0));
            theAccountRecord.setVoucherInfo("财务转结");
            accountRecordList.add(theAccountRecord);
        }
        AccountRecord[] curAccountRecords=null;
        curAccountRecords = (AccountRecord[]) Array.newInstance(AccountRecord.class,accountRecordList.size());
        accountRecordList.toArray(curAccountRecords);

        this.accountRecordService.add(accountBook,curAccountRecords);


    }
}
