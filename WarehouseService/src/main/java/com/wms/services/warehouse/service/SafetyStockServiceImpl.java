package com.wms.services.warehouse.service;


import com.wms.services.warehouse.dao.SafetyStockDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SafetyStock;
import com.wms.utilities.model.SafetyStockView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;


@Service
@Transactional
public class SafetyStockServiceImpl implements SafetyStockService{
    @Autowired
    SafetyStockDAO safetyStockDAO;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;

    private static final String NO_PREFIX = "R";

    @Override
    public int[] add(String accountBook, SafetyStock[] safetyStocks) throws WMSServiceException {
        //数据验证
        Stream.of(safetyStocks).forEach(
                (safetyStock) -> {
                    new Validator("安全库存数量(个)").min(0).validate(safetyStock.getAmount());
                    new Validator("单位").notnull().validate(safetyStock.getUnit());
                    new Validator("单位数量").min(0).validate(safetyStock.getUnitAmount());
                    new Validator("类型").min(0).max(2).validate(safetyStock.getType());
                }
        );

        //外键检测
        Stream.of(safetyStocks).forEach(
                (safetyStock) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", safetyStock.getWarehouseId())).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", safetyStock.getWarehouseId()));
                    } else if (this.supplyService.find(accountBook,
                            new Condition().addCondition("id", safetyStock.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供应信息不存在，请重新提交！(%d)", safetyStock.getSupplyId()));
                    }
                    //TODO else if (this.storageLocationService.find(accountBook,
                         //TODO   new Condition().addCondition("id", safetyStock.getStorageLocationId())).length == 0) {
                       //TODO throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", safetyStock.getStorageLocationId()));
                    //}
                }
        );



        return safetyStockDAO.add(accountBook, safetyStocks);
    }

    @Override
    public void update(String accountBook, SafetyStock[] safetyStocks) throws WMSServiceException {
        //数据验证
        Stream.of(safetyStocks).forEach((safetyStock) -> {
            new Validator("安全库存数量(个)").min(0).validate(safetyStock.getAmount());
            new Validator("单位").notnull().validate(safetyStock.getUnit());
            new Validator("单位数量").min(0).validate(safetyStock.getUnitAmount());
            new Validator("类型").min(0).max(2).validate(safetyStock.getType());
        });



        safetyStockDAO.update(accountBook, safetyStocks);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (safetyStockDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除安全库存信息不存在，请重新查询！(%d)", id));
            }
        }

        try {
            safetyStockDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除安全库存信息失败，如果安全库存信息已经被引用，需要先删除引用该安全库存信息的内容，才能删除该安全库存信息");
        }
    }

    @Override
    public SafetyStockView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return safetyStockDAO.find(accountBook, cond);
    }


}
