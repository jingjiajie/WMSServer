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
                throw new WMSServiceException("期间名称：" + accountPeriod.getName() +"已经存在!");
            }
        });

        Stream.of(accountPeriods).forEach((accountPeriod)->{
            AccountPeriodView accountPeriodView=this.find(accountBook,new Condition().addCondition("id",new Integer[]{accountPeriod.getId()}))[0];
            AccountRecordView[] accountRecordViews= accountRecordService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{accountPeriod.getWarehouseId()}).addCondition("accountPeriodId",new Integer[]{accountPeriod.getId()}));
            AccountRecordView[] lastAccountRecordViews=null;
            if (accountPeriod.getLastAccountPeriodId()!=null) {
                lastAccountRecordViews = accountRecordService.find(accountBook, new Condition().addCondition("warehouseId", new Integer[]{accountPeriod.getWarehouseId()}).addCondition("accountPeriodId", new Integer[]{accountPeriod.getLastAccountPeriodId()}));
            }
            //如果期间没截止
            if (accountPeriod.getEnded()==AccountPeriodService.ended_false)
            {
                if (accountRecordViews.length>0) {
                    Stream.of(accountRecordViews).forEach((accountRecordView) -> {
                        if (accountRecordView.getServiceTime().before(accountPeriod.getStartTime())) {
                            throw new WMSServiceException(String.format("本期间的起始时间必须在本期间所有账目记录时间之前，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                        }
                    });
                }
                if (lastAccountRecordViews!=null) {
                    Stream.of(lastAccountRecordViews).forEach((lastAccountRecordView) -> {
                        if (lastAccountRecordView.getServiceTime().after(accountPeriod.getStartTime())) {
                            throw new WMSServiceException(String.format("本期间的起始时间必须在上一个会计期间所有账目记录时间之后，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                        }
                    });
                }
            }
            //如果期间截止
            if (accountPeriod.getEnded()==AccountPeriodService.ended_ture)
            {
                Stream.of(accountRecordViews).forEach((accountRecordView)->{
                    if (accountRecordView.getServiceTime().before(accountPeriod.getStartTime())){
                        throw new WMSServiceException(String.format("本期间的起始时间必须在本期间所有账目记录时间之前，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                    }
                    if (accountRecordView.getServiceTime().after(accountPeriod.getEndTime())){
                        throw new WMSServiceException(String.format("本期间的结束时间必须在本期间所有账目记录时间之后，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                    }
                });
                if (lastAccountRecordViews!=null) {
                    Stream.of(lastAccountRecordViews).forEach((lastAccountRecordView) -> {
                        if (lastAccountRecordView.getServiceTime().after(accountPeriod.getStartTime())) {
                            throw new WMSServiceException(String.format("本期间的起始时间必须在上一个会计期间所有账目记录时间之后，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                        }
                    });
                }
                AccountPeriodView[] decAccountPeriodViews=this.find(accountBook,new Condition().addCondition("lastAccountPeriodId",new Integer[]{accountPeriod.getId()}));
                if (decAccountPeriodViews.length>0) {
                    AccountRecordView[] decAccountRecordViews = accountRecordService.find(accountBook, new Condition().addCondition("warehouseId", new Integer[]{accountPeriod.getWarehouseId()}).addCondition("accountPeriodId", new Integer[]{decAccountPeriodViews[0].getId()}));
                    Stream.of(decAccountRecordViews).forEach((decAccountRecordView) -> {
                        if (decAccountRecordView.getServiceTime().before(accountPeriod.getEndTime())) {
                            throw new WMSServiceException(String.format("本期间的结束时间必须在后一个会计期间所有账目记录时间之前，请重新检查后输入！周期名称(%s)", accountPeriod.getName()));
                        }
                    });
                }

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

//            if(this.find(accountBook,
//                    new Condition().addCondition("id",new Integer[]{ accountPeriod.getLastAccountPeriodId()})).length == 0){
//                throw new WMSServiceException(String.format("上期期间不存在，请重新提交！(%d)",accountPeriod.getLastAccountPeriodId()));
//            }

            if(this.warehouseService.findTable(accountBook,
                    new Condition().addCondition("id",accountPeriod.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",accountPeriod.getWarehouseId()));
            }
        }));

    }

    public   void carryOver(String accountBook,CarryOver carryOver) throws WMSServiceException{
        //取得需要的三个关键词
        int curWarehouseId=carryOver.getWarehouseId();
        int curPersonId=carryOver.getPersonId();
        Timestamp cutTime=carryOver.getStartTime();
        //TODO 结转功能 为了保持会计工作的连续性，一定要把本会计年度末的余额转到下个会计年度

        //获得当前仓库，最新的没截止的期间,将其截止并启用新的期间
        AccountPeriodView accountPeriodView=this.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("ended",AccountPeriodService.ended_false).addOrder("startTime", OrderItem.Order.DESC))[0];
        AccountPeriod oldAccountPeriod = ReflectHelper.createAndCopyFields(accountPeriodView,AccountPeriod.class);
        oldAccountPeriod.setEndTime(cutTime);
        oldAccountPeriod.setEnded(AccountPeriodService.ended_ture);
        this.update(accountBook,new AccountPeriod[] {oldAccountPeriod});

        AccountPeriod newAccountPeriod=new AccountPeriod();
        newAccountPeriod.setStartTime(cutTime);
        newAccountPeriod.setLastAccountPeriodId(oldAccountPeriod.getId());
        newAccountPeriod.setEnded(AccountPeriodService.ended_false);
        newAccountPeriod.setName(new Timestamp(System.currentTimeMillis()).toString());
        newAccountPeriod.setWarehouseId(curWarehouseId);
        int newAccountPeriodID=this.add(accountBook,new AccountPeriod[] {newAccountPeriod})[0];

        List<AccountRecord> accountRecordList=new ArrayList();

        AccountRecordView[] accountRecordViews= accountRecordService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("accountPeriodId",new Integer[]{oldAccountPeriod.getId()}));
        if (accountRecordViews.length<=0)
        {
            throw new WMSServiceException("当前期间此仓库中无账目记录，无法结转");
        }
        Map<Integer, List<AccountRecordView>> groupBySupplierIdMap =
                Stream.of(accountRecordViews).collect(Collectors.groupingBy(AccountRecordView::getOwnAccountTitleId));

        Iterator<Map.Entry<Integer,List<AccountRecordView>>> entries = groupBySupplierIdMap.entrySet().iterator();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<AccountRecordView>> entry = entries.next();
            Integer accountPeriodId = entry.getKey();
            List<AccountRecordView> accountRecordViewsList=entry.getValue();

            AccountRecordView[] curAccountRecordViews=null;
            curAccountRecordViews = (AccountRecordView[]) Array.newInstance(AccountRecordView.class,accountRecordViewsList.size());
            accountRecordViewsList.toArray(curAccountRecordViews);

            AccountRecordView newestAccountRecordView=curAccountRecordViews[0];
            for (int i=0;i<curAccountRecordViews.length;i++){
//                    if (curAccountRecordViews[i].getTime().after(newestAccountRecordView.getTime())){
//                        newestAccountRecordView=curAccountRecordViews[i];
//                    }
//                if (curAccountRecordViews[i].getTime().after(cutTime)){
//                    throw new WMSServiceException("结转时间节点不能早于现存记录最后时间！");
//                }
            }
            AccountRecord theAccountRecord=new AccountRecord();
            theAccountRecord.setWarehouseId(curWarehouseId);
            //theAccountRecord.setAccountTitleId(accountPeriodId);
            theAccountRecord.setAccountPeriodId(newAccountPeriodID);
            //theAccountRecord.setTime(new Timestamp(System.currentTimeMillis()));
            theAccountRecord.setPersonId(curPersonId);
            //heAccountRecord.setBalance(newestAccountRecordView.getBalance());
            theAccountRecord.setComment("财务转结");
            theAccountRecord.setCreditAmount(new BigDecimal(0));
            theAccountRecord.setDebitAmount(new BigDecimal(0));
            theAccountRecord.setVoucherInfo("财务转结");
            accountRecordList.add(theAccountRecord);
        }
        AccountRecord[] curAccountRecords=null;
        curAccountRecords = (AccountRecord[]) Array.newInstance(AccountRecord.class,accountRecordList.size());
        accountRecordList.toArray(curAccountRecords);

        this.accountRecordService.simpleAdd(accountBook,curAccountRecords);


    }
}
