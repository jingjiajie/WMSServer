package com.wms.services.warehouse.service;

import com.sun.jmx.snmp.Timestamp;
import com.wms.services.warehouse.controller.PersonController;
import com.wms.services.warehouse.dao.WarehouseEntryDAO;
import com.wms.services.warehouse.model.Person;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.services.warehouse.model.WarehouseEntryView;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
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
    PersonController personController;

    @Override
    public int[] add(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        //编号查重
        Stream.of(warehouseEntries).forEach((warehouseEntry)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{warehouseEntry.getNo()});
            if(warehouseEntryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("入库单单号重复："+warehouseEntry.getNo());
            }
        });

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
