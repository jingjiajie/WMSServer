package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StockTakingOrderDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StockTakingOrderView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class StockTakingOrderServiceImpl implements StockTakingOrderService{
    @Autowired
    StockTakingOrderDAO stockTakingOrderDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;

    @Override
    public int[] add(String accountBook,StockTakingOrder[] stockTakingOrders) throws WMSServiceException {
        for(int i=0;i<stockTakingOrders.length;i++)
        {
           new Validator("盘点单号").notnull().notEmpty().validate(stockTakingOrders[i].getNo());
        }
        for(int i=0;i<stockTakingOrders.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{stockTakingOrders[i].getNo()});
            if(stockTakingOrderDAO.find(accountBook,cond).length > 0) {
                throw new WMSServiceException("盘点单代号重复：" + stockTakingOrders[i].getNo());
            }
        }
        //外键
        Stream.of(stockTakingOrders).forEach(
                (stockTakingOrder)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockTakingOrder.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockTakingOrder.getWarehouseId()));}
                      else if(this.personService.find(accountBook,
                                new Condition().addCondition("id",stockTakingOrder.getCreatePersonId())).length == 0){
                            throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getCreatePersonId()));
                    }
                    if(stockTakingOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("lastUpdateId",stockTakingOrder.getLastUpdatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getLastUpdatePersonId()));
                    }
                }
        );
        for(int i=0;i<stockTakingOrders.length;i++){
            stockTakingOrders[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        return stockTakingOrderDAO.add(accountBook,stockTakingOrders);
    }


    @Override
    public void update(String accountBook, StockTakingOrder[] stockTakingOrders) throws WMSServiceException {
        for(int i=0;i<stockTakingOrders.length;i++)
        {
            new Validator("盘点单号").notnull().notEmpty().validate(stockTakingOrders[i].getNo());
        }
        for(int i=0;i<stockTakingOrders.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{stockTakingOrders[i].getNo()}).addCondition("id",new Integer[]{stockTakingOrders[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(stockTakingOrderDAO.find(accountBook,cond).length > 0) {
                throw new WMSServiceException("盘点单代号重复：" + stockTakingOrders[i].getNo());
            }
          else if( stockTakingOrderDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{stockTakingOrders[i].getId()})).length!=1)
           {
               throw new WMSServiceException("没找到要修改的盘点单");
           }

        }

        //外键
        Stream.of(stockTakingOrders).forEach(
                (stockTakingOrder)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockTakingOrder.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockTakingOrder.getWarehouseId()));}
                    else if(this.personService.find(accountBook,
                            new Condition().addCondition("id",stockTakingOrder.getCreatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getCreatePersonId()));
                    }
                    if(stockTakingOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("lastUpdateId",stockTakingOrder.getLastUpdatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",stockTakingOrder.getLastUpdatePersonId()));
                    }
                }
        );
        for(int i=0;i<stockTakingOrders.length;i++){
            stockTakingOrders[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        stockTakingOrderDAO.update(accountBook,stockTakingOrders);
    }


    @Override
    public void remove(String accountBook,int ids[]) throws WMSServiceException{
        try {
            stockTakingOrderDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除盘点单失败，如果盘点单已经被引用，需要先删除引用的内容，才能删除该盘点单");
        }

    }

    @Override
    public StockTakingOrderView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderDAO.find(accountBook, cond);
    }
}
