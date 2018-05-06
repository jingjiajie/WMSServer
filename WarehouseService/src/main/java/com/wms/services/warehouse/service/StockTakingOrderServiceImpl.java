package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StockTakingOrderDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockTakingOrder;
import com.wms.utilities.model.StorageArea;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
public class StockTakingOrderServiceImpl implements StockRecordService{
    @Autowired
    StockTakingOrderDAO stockTakingOrderDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;

    @Transactional
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
        return stockTakingOrderDAO.add(accountBook,stockTakingOrders);
    }


}
