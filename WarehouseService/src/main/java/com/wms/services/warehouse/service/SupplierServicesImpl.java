package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.warehouse.model.Supplier;
import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;

@Service
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Autowired
    SupplyService supplyService;
    @Transactional
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException
    {

        for (int i=0;i<suppliers.length;i++) {
            if(suppliers[i].getName()==null)
            {
                throw new WMSServiceException("供应商名不能为空！");
            }
            if(suppliers[i].getNo()==null)
            {
                throw new WMSServiceException("供应商代号不能为空！");
            }
            if(suppliers[i].getCreatePersonId()==0)
            {
                throw new WMSServiceException("人员信息无法找到！");
            }
            if(suppliers[i].getWarehouseId()==0)
            {
                throw new WMSServiceException("仓库信息无法找到！");
            }
            Supplier[] suppliersRepeat=null;
            String supplierName = suppliers[i].getName();;
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Name','values':['"+supplierName+"'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
            try {
                suppliersRepeat = supplierDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (suppliersRepeat.length > 0) {
               throw new WMSServiceException("供应商名 " + supplierName + " 已经存在！");
            }
        }
        for (int i=0;i<suppliers.length;i++)
        {
            //suppliers[i].setWarehouseId(1);
            //suppliers[i].setCreatePersonId(19);
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        try
        { return supplierDAO.add(accountBook,suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }

    }

    @Transactional
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException{
        for (int i=0;i<suppliers.length;i++)
        {
            if(suppliers[i].getName()==null)
            {
                throw new WMSServiceException("供应商名不能为空！");
            }
            if(suppliers[i].getNo()==null)
            {
                throw new WMSServiceException("供应商代号不能为空！");
            }
            Supplier[] suppliersRepet;
            String supplierName = suppliers[i].getName();
            Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"name\",\"values\":[\"" + supplierName + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");
            try{
                suppliersRepet = supplierDAO.find(accountBook,condition);
            }catch (DatabaseNotFoundException ex){
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if(suppliersRepet.length>0)
            {
                if(suppliersRepet[1].getId()!=suppliers[i].getId())
                { throw new WMSServiceException("供应商名 " + supplierName + " 已经存在！");}
            }

                //suppliers[i].setWarehouseId(1);
                //suppliers[i].setCreatePersonId(19);
                suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        }
        try {
            supplierDAO.update(accountBook, suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        int idLength=ids.length;
        int[] idsModify=null;
        List<int[]>  idsList = Arrays.asList(ids);
        Supply[] supplies;
        for (int i=0;i<idLength;i++)
        {
            Supplier[] supplierRefference=null;
            int SupplierID=ids[i];
            Condition condition = Condition.fromJson("{'conditions':[{'key':'supplierID','values':[" + SupplierID + "],'relation':'EQUAL'}]}");
            supplies =supplyService.find(accountBook,condition);
            if(supplies.length>0){
                Condition conditionSupplier= Condition.fromJson("{'conditions':[{'key':'id','values':[" + SupplierID + "],'relation':'EQUAL'}]}");
                supplierRefference=supplierDAO.find(accountBook,conditionSupplier);
                throw new WMSServiceException("供应商名 " +supplierRefference[0].getName() + " 被引用无法删除!");
            }
        }
        try {
            supplierDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public Supplier[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.supplierDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
