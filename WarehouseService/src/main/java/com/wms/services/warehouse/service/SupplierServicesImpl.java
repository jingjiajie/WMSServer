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
            cond.addCondition("name",new String[]{supplier.getName()});
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商名："+supplier.getName()+"已经存在!");
            }
        });
        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{supplier.getNo()});
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
        cond.addCondition("name",new String[]{suppliers[i].getName()});
        cond.addCondition("id",new Integer[]{suppliers[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
        if(supplierDAO.find(accountBook,cond).length > 0){
            throw new WMSServiceException("供应商名称重复："+suppliers[i].getName());
        }
    }
    Stream.of(suppliers).forEach((supplier)->{
        Condition cond = new Condition();
        cond.addCondition("no",new String[]{supplier.getNo()});
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
            cond.addCondition("name",new String[]{suppliers[i].getName()});
            cond.addCondition("id",new Integer[]{suppliers[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(supplierDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("供应商名称重复："+suppliers[i].getName());
            }
        }
        Stream.of(suppliers).forEach((supplier)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{supplier.getNo()});
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
                    /*
                    else  if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getCreatePersonId()})).length == 0)
                    {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getCreatePersonId()));
                    }
                    if(supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{supplier.getLastUpdatePersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",supplier.getLastUpdatePersonId()));
                    }
                    */
                }
        );
        List<Supplier> supplierList=new ArrayList();
        //查出旧供应商
        for(int i=0;i<suppliers.length;i++){
            SupplierView[] supplierViews=supplierDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{suppliers[i].getId()}));
            if(supplierViews.length!=1){throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)",suppliers[i].getId()));}
            //新建一条存旧信息
            Supplier supplier=new Supplier();
            supplier.setCreateTime(suppliers[i].getCreateTime());
            supplier.setLastUpdatePersonId(suppliers[i].getLastUpdatePersonId());
            supplier.setAddress(suppliers[i].getAddress());
            supplier.setBalanceDelayMonth(suppliers[i].getBalanceDelayMonth());
            supplier.setBankAccount(suppliers[i].getBankAccount());
            supplier.setBankName(suppliers[i].getBankName());
            supplier.setBankNo(suppliers[i].getBankNo());
            supplier.setContractNo(suppliers[i].getContractNo());
            supplier.setContractEndTime(suppliers[i].getContractEndTime());
            supplier.setContractStartTime(suppliers[i].getContractStartTime());
            supplier.setContractState(suppliers[i].getContractState());
            supplier.setFullName(suppliers[i].getFullName());
            supplier.setInvoiceDelayMonth(suppliers[i].getInvoiceDelayMonth());
            supplier.setEnabled(suppliers[i].getEnabled());
            supplier.setLastUpdateTime(suppliers[i].getLastUpdateTime());
            supplier.setCreatePersonId(suppliers[i].getCreatePersonId());
            supplier.setContractStorageArea(suppliers[i].getContractStorageArea());
            supplier.setEnterpriseCode(suppliers[i].getEnterpriseCode());
            supplier.setFixedStorageCost(suppliers[i].getFixedStorageCost());
            supplier.setName(suppliers[i].getName());
            supplier.setNetArea(suppliers[i].getNetArea());
            supplier.setNo(suppliers[i].getNo());
            supplier.setRecipientName(suppliers[i].getRecipientName());
            supplier.setTaxpayerNumber(suppliers[i].getTaxpayerNumber());
            supplier.setTel(suppliers[i].getTel());
            supplier.setZipCode(suppliers[i].getZipCode());
            supplier.setIsHistory(1);
            supplier.setNewestSupplierId(suppliers[i].getId());
            supplierList.add(supplier);
        }
        //更新和保存
        Supplier[] resultArray=null;
        resultArray = (Supplier[]) Array.newInstance(StockRecordView.class,supplierList.size());
        //supplierList.toArray(resultArray);
        supplierDAO.update(accountBook, suppliers);
        supplierDAO.add(accountBook,resultArray);
    }
@Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
    for (int id : ids) {
        if (supplierDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
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
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.supplierDAO.findCount(database,cond);
    }
}
