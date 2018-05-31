package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;
import com.wms.utilities.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Autowired
    SupplyService supplyService;
    @Autowired
    PersonService personService;
    @Autowired
    WarehouseService warehouseService;
@Override
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException
    {
        for (int i=0;i<suppliers.length;i++) {
            Validator validator=new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());
            Validator validator1=new Validator("供应商代号");
            validator1.notnull().validate(suppliers[i].getNo());
            new Validator("创建时间").validate(suppliers[i].getCreateTime());
            if(suppliers[i].getEnabled()!=0&&suppliers[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
            if(suppliers[i].getBalanceDelayMonth()!=null){if(suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO)<0)
            {throw new WMSServiceException("结算延迟月不能小于0！");}}
            if(suppliers[i].getInvoiceDelayMonth()!=null){if(suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO)<0)
            {throw new WMSServiceException("开票算延迟月不能小于0！");}}
            if(suppliers[i].getContractEndTime()!=null&&suppliers[i].getContractStartTime()!=null&&suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime())>=0)
            {throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");}
        }

        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{supplier.getName()}).addCondition("isHistory",new Integer[]{new Integer(0)});
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商名："+supplier.getName()+"已经存在!");
            }
        });
        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{supplier.getNo()}).addCondition("isHistory",new Integer[]{new Integer(0)});
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商代号："+supplier.getNo()+"已经存在!");
            }
        });

        //外键检测
        Stream.of(suppliers).forEach(
                (supplier)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ supplier.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supplier.getWarehouseId()));
                    }
                    else  if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getCreatePersonId()})).length == 0)
                    {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getCreatePersonId()));
                    }
                    if(supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getLastUpdatePersonId()));
                    }
                }
        );

        for (int i=0;i<suppliers.length;i++)
        {
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
            suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        return supplierDAO.add(accountBook,suppliers);
    }


    @Override
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException{

    for (int i=0;i<suppliers.length;i++) {
        Validator validator=new Validator("供应商名称");
        validator.notnull().validate(suppliers[i].getName());
        Validator validator1=new Validator("供应商代号");
        validator1.notnull().validate(suppliers[i].getNo());
        new Validator("创建时间").validate(suppliers[i].getCreateTime());
        if(suppliers[i].getEnabled()!=0&&suppliers[i].getEnabled()!=1){
            throw new WMSServiceException("是否启用只能为0和1！");
        }
        if(suppliers[i].getBalanceDelayMonth()!=null){if(suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO)<0)
        {throw new WMSServiceException("结算延迟月不能小于0！");}}
        if(suppliers[i].getInvoiceDelayMonth()!=null){if(suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO)<0)
        {throw new WMSServiceException("开票算延迟月不能小于0！");}}
        if(suppliers[i].getContractEndTime()!=null&&suppliers[i].getContractStartTime()!=null&&suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime())>=0)
        {throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");}
    }
    Stream.of(suppliers).forEach(
            (supplier)->{
                if(this.supplierDAO.find(accountBook,
                        new Condition().addCondition("id",supplier.getId())).length == 0){
                    throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",supplier.getId()));
                }
            }
    );


    for(int i=0;i<suppliers.length;i++){
        Condition cond = new Condition();
        cond.addCondition("name",new String[]{suppliers[i].getName()}).addCondition("isHistory",new Integer[]{new Integer(0)});
        cond.addCondition("id",new Integer[]{suppliers[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
        if(supplierDAO.find(accountBook,cond).length > 0){
            throw new WMSServiceException("供应商名称重复："+suppliers[i].getName());
        }
    }
    Stream.of(suppliers).forEach((supplier)->{
        Condition cond = new Condition();
        cond.addCondition("no",new String[]{supplier.getNo()}).addCondition("isHistory",new Integer[]{new Integer(0)});
        cond.addCondition("id",new Integer[]{supplier.getId()}, ConditionItem.Relation.NOT_EQUAL);
        if(supplierDAO.find(accountBook,cond).length > 0){
            throw new WMSServiceException("供应代号："+supplier.getNo()+"已经存在!");
        }
    });
        for (int i=0;i<suppliers.length;i++)
        {
                suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
                //TODO
                suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }

      //外键检测
    Stream.of(suppliers).forEach(
            (supplier)->{
                if(this.warehouseService.find(accountBook,
                        new Condition().addCondition("id",new Integer[]{ supplier.getWarehouseId()})).length == 0){
                    throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supplier.getWarehouseId()));
                }
                else  if(this.personService.find(accountBook,
                        new Condition().addCondition("id",new Integer[]{supplier.getCreatePersonId()})).length == 0)
                {
                    throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getCreatePersonId()));
                }
                if(supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                        new Condition().addCondition("id",new Integer[]{supplier.getLastUpdatePersonId()})).length == 0){
                    throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getLastUpdatePersonId()));
                }
            }
            );
            supplierDAO.update(accountBook, suppliers);

    }


    @Override
    public void updateHistory(String accountBook, Supplier[] suppliers) throws WMSServiceException{

        for (int i=0;i<suppliers.length;i++) {
            Validator validator=new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());
            Validator validator1=new Validator("供应商代号");
            validator1.notnull().validate(suppliers[i].getNo());
            if(suppliers[i].getEnabled()!=0&&suppliers[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
            if(suppliers[i].getBalanceDelayMonth()!=null){if(suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO)<0)
            {throw new WMSServiceException("结算延迟月不能小于0！");}}
            if(suppliers[i].getInvoiceDelayMonth()!=null){if(suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO)<0)
            {throw new WMSServiceException("开票算延迟月不能小于0！");}}
            if(suppliers[i].getContractEndTime()!=null&&suppliers[i].getContractStartTime()!=null&&suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime())>=0)
            {throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");}
            //suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        Stream.of(suppliers).forEach(
                (supplier)->{
                    if(this.supplierDAO.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getId()})).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",supplier.getId()));
                    }
                }
        );


        for(int i=0;i<suppliers.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{suppliers[i].getName()}).addCondition("isHistory",new Integer[]{new Integer(0)});
            cond.addCondition("id",new Integer[]{suppliers[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商名称重复："+suppliers[i].getName());
            }
        }
        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{supplier.getNo()}).addCondition("isHistory",new Integer[]{new Integer(0)});
            cond.addCondition("id",new Integer[]{supplier.getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应代号："+supplier.getNo()+"已经存在!");
            }
        });

        //外键检测
        Stream.of(suppliers).forEach(
                (supplier)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ supplier.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",supplier.getWarehouseId()));
                    }

                    else  if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getCreatePersonId()})).length == 0)
                    {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getCreatePersonId()));
                    }
                    if(supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getLastUpdatePersonId())); }

                }
        );
        List<Supplier> supplierList=new ArrayList();
        //查出旧供应商
        for(int i=0;i<suppliers.length;i++){
            SupplierView[] supplierViews=supplierDAO.find(accountBook,new Condition().addCondition("isHistory",new Integer[]{new Integer(0)}).addCondition("id",new Integer[]{suppliers[i].getId()}));
            if(supplierViews.length!=1){throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)",suppliers[i].getId()));}
            //新建一条存旧信息
            Supplier supplier=new Supplier();
            supplier.setCreateTime(supplierViews[0].getCreateTime());
            supplier.setLastUpdatePersonId(supplierViews[0].getLastUpdatePersonId());
            supplier.setAddress(supplierViews[0].getAddress());
            supplier.setBalanceDelayMonth(supplierViews[0].getBalanceDelayMonth());
            supplier.setBankAccount(supplierViews[0].getBankAccount());
            supplier.setBankName(supplierViews[0].getBankName());
            supplier.setBankNo(supplierViews[0].getBankNo());
            supplier.setContractNo(supplierViews[0].getContractNo());
            supplier.setContractEndTime(supplierViews[0].getContractEndTime());
            supplier.setContractStartTime(supplierViews[0].getContractStartTime());
            supplier.setContractState(supplierViews[0].getContractState());
            supplier.setFullName(supplierViews[0].getFullName());
            supplier.setInvoiceDelayMonth(supplierViews[0].getInvoiceDelayMonth());
            supplier.setEnabled(supplierViews[0].getEnabled());
            supplier.setLastUpdateTime(supplierViews[0].getLastUpdateTime());
            supplier.setCreatePersonId(supplierViews[0].getCreatePersonId());
            supplier.setContractStorageArea(supplierViews[0].getContractStorageArea());
            supplier.setEnterpriseCode(supplierViews[0].getEnterpriseCode());
            supplier.setFixedStorageCost(supplierViews[0].getFixedStorageCost());
            supplier.setName(supplierViews[0].getName());
            supplier.setNetArea(supplierViews[0].getNetArea());
            supplier.setNo(supplierViews[0].getNo());
            supplier.setRecipientName(supplierViews[0].getRecipientName());
            supplier.setTaxpayerNumber(supplierViews[0].getTaxpayerNumber());
            supplier.setTel(supplierViews[0].getTel());
            supplier.setZipCode(supplierViews[0].getZipCode());
            supplier.setWarehouseId(supplierViews[0].getWarehouseId());
            supplier.setIsHistory(1);
            supplier.setNewestSupplierId(suppliers[i].getId());
            supplierList.add(supplier);
        }
        //更新和保存
        Supplier[] resultArray=null;
        resultArray = (Supplier[]) Array.newInstance(Supplier.class,supplierList.size());
        supplierList.toArray(resultArray);
        supplierDAO.update(accountBook, suppliers);
        supplierDAO.add(accountBook,resultArray);
    }
@Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
    for (int id : ids) {
        if (supplierDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
            throw new WMSServiceException(String.format("删除供应商不存在，请重新查询！(%d)", id));
        }
    }

    try {
            supplierDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除供应商失败，如果供应商已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Override
    public SupplierView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public SupplierView[] findHistory(String accountBook, Condition cond) throws WMSServiceException{
        cond.addCondition("isHistory",new Integer[]{1});
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public SupplierView[] findNew(String accountBook, Condition cond) throws WMSServiceException{
    cond.addCondition("isHistory",new Integer[]{0});
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.supplierDAO.findCount(database,cond);
    }
}
