package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseEntryItemDAO;
import com.wms.utilities.model.WarehouseEntryItem;
import com.wms.utilities.model.WarehouseEntryItemView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryItemServiceImpl implements WarehouseEntryItemService {
    @Autowired
    WarehouseEntryItemDAO warehouseEntryItemDAO;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;

    @Override
    public int[] add(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        this.validateEntities(accountBook,warehouseEntryItems);
        return this.warehouseEntryItemDAO.add(accountBook,warehouseEntryItems);
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        this.validateEntities(accountBook,warehouseEntryItems);
        this.warehouseEntryItemDAO.update(accountBook,warehouseEntryItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            this.warehouseEntryItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public WarehouseEntryItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.warehouseEntryItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,WarehouseEntryItem[] warehouseEntryItems){
        //数据验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem)->{
                    new Validator("订单数量").min(0).validate(warehouseEntryItem.getExpectedAmount());
                    new Validator("实收数量").min(0).max(warehouseEntryItem.getExpectedAmount().intValue()).validate(warehouseEntryItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(warehouseEntryItem.getUnitAmount());
                    new Validator("已分配送检数量").min(0).max(warehouseEntryItem.getRealAmount().intValue()).validate(warehouseEntryItem.getInspectionAmount());
                    new Validator("拒收数量").min(0).max(warehouseEntryItem.getExpectedAmount().intValue()).validate(warehouseEntryItem.getRefuseAmount());
                    new Validator("拒收单位数量").min(0).validate(warehouseEntryItem.getRefuseUnitAmount());
                }
        );

        //外键验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    if (this.warehouseEntryService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getWarehouseEntryId())).length == 0) {
                        throw new WMSServiceException(String.format("入库单不存在，请重新提交！(%d)", warehouseEntryItem.getWarehouseEntryId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getStorageLocationId())).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (warehouseEntryItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getPersonId())).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    }
                }
        );
    }
}
