package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountPeriodDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
@Service
public class AccountPeriodServiceImpl implements AccountPeriodService{
    @Autowired
    AccountPeriodDAO accountPeriodDAO;
    @Autowired
    WarehouseService warehouseService;

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
}
