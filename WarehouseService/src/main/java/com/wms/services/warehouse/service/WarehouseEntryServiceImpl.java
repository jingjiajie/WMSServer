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

@Service
@Transactional
public class WarehouseEntryServiceImpl implements WarehouseEntryService {
    @Autowired
    WarehouseEntryDAO warehouseEntryDAO;
    @Autowired
    PersonController personController;

    @Override
    public int[] add(String accountBook, WarehouseEntryView[] warehouseEntryViews) throws WMSServiceException {
        //编号查重
        for(int i=0;i<warehouseEntryViews.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{warehouseEntryViews[i].getNo()});
            if(warehouseEntryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("入库单单号重复："+warehouseEntryViews[i].getNo());
            }
        }

        //视图转基本表对象
        WarehouseEntry[] warehouseEntries = ReflectHelper.createAndCopyFields(warehouseEntryViews,WarehouseEntry.class);
        //将创建人姓名转为创建人ID
        for(int i=0;i<warehouseEntries.length;i++){
            WarehouseEntryView warehouseEntryView = warehouseEntryViews[i];
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{warehouseEntryView.getCreatePersonName()});
            Person[] persons = personController.find(accountBook,cond.toJson());
            if(persons.length == 0){
                throw new WMSServiceException("无法找到对应人员："+warehouseEntryView.getCreatePersonName());
            }
            warehouseEntries[i].setCreatePersonId(persons[0].getId());
            warehouseEntries[i].setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        return warehouseEntryDAO.add(accountBook, warehouseEntries);
    }

    @Override
    public void update(String accountBook, WarehouseEntryView[] warehouseEntryViews) throws WMSServiceException {
        //编号查重
        for(int i=0;i<warehouseEntryViews.length;i++){
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{warehouseEntryViews[i].getNo()});
            cond.addCondition("id",new Integer[]{warehouseEntryViews[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(warehouseEntryDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("入库单单号重复："+warehouseEntryViews[i].getNo());
            }
        }
        //视图转基本表对象
        WarehouseEntry[] warehouseEntries = ReflectHelper.createAndCopyFields(warehouseEntryViews,WarehouseEntry.class);

        //将最后修改人姓名转为创建人ID
        for(int i=0;i<warehouseEntries.length;i++){
            WarehouseEntryView warehouseEntryView = warehouseEntryViews[i];
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{warehouseEntryView.getLastUpdatePersonName()});
            Person[] persons = personController.find(accountBook,cond.toJson());
            if(persons.length == 0){
                throw new WMSServiceException("修改入库单失败，无法找到对应人员："+warehouseEntryView.getLastUpdatePersonName());
            }
            warehouseEntries[i].setLastUpdatePersonId(persons[0].getId());
            warehouseEntries[i].setLastUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
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
