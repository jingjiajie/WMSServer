package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseEntryDAO;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryServiceImpl implements WarehouseEntryService {
    @Autowired
    WarehouseEntryDAO warehouseEntryDAO;
    @Autowired
    PersonService personService;
    @Autowired
    SupplierServices supplierService;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        //数据验证
        Stream.of(warehouseEntries).forEach(
                (warehouseEntry)->{
                    new Validator("创建用户").notnull().validate(warehouseEntry.getCreatePersonId());
                }
        );

        //编号查重
        Stream.of(warehouseEntries).forEach((warehouseEntry)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{warehouseEntry.getNo()});
            if(warehouseEntryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("入库单单号重复："+warehouseEntry.getNo());
            }
        });

        //外键检测
        Stream.of(warehouseEntries).forEach(
                (warehouseEntry)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",warehouseEntry.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",warehouseEntry.getWarehouseId()));
                    }else if(this.supplierService.find(accountBook,
                            new Condition().addCondition("id",warehouseEntry.getSupplierId())).length == 0){
                        throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)",warehouseEntry.getSupplierId()));
                    }else if(this.personService.find(accountBook,
                            new Condition().addCondition("id",warehouseEntry.getCreatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",warehouseEntry.getCreatePersonId()));
                    } if(warehouseEntry.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",warehouseEntry.getLastUpdatePersonId())).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",warehouseEntry.getLastUpdatePersonId()));
                    }
                }
        );

        //生成创建时间
        Stream.of(warehouseEntries).forEach((w)->w.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis())));

        return warehouseEntryDAO.add(accountBook, warehouseEntries);
    }

    @Override
    public void update(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        //编号查重
        for(int i=0;i<warehouseEntries.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{warehouseEntries[i].getNo()});
            cond.addCondition("id",new Integer[]{warehouseEntries[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(warehouseEntryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("入库单单号重复："+warehouseEntries[i].getNo());
            }
        }

        warehouseEntryDAO.update(accountBook, warehouseEntries);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            warehouseEntryDAO.remove(accountBook, ids);
        }catch (Throwable ex){
            throw new WMSServiceException("删除入库单失败，如果入库单已经被引用，需要先删除引用该入库单的内容，才能删除该入库单");
        }
    }

    @Override
    public WarehouseEntryView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return warehouseEntryDAO.find(accountBook, cond);
    }
}
