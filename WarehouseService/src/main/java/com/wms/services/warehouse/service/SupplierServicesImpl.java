package com.wms.services.warehouse.service;

import com.sun.javafx.scene.layout.region.Margins;
import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SupplierServicesImpl implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    SupplyDAO supplyDAO;
    @Transactional
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException
    {
        for (int i=0;i<suppliers.length;i++)
        {
        Supplier[] suppliersRepeat;
        String supplierName = suppliers[i].getName();
        Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"name\",\"values\":[\"" + supplierName + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");
        try{
            suppliersRepeat = supplierDAO.find(accountBook,condition);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
        if(suppliersRepeat.length>0)
        {
            break;
        }
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
            Supplier[] suppliersRepet;
            String supplierName = suppliers[i].getName();
            Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"name\",\"values\":[\"" + supplierName + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");
            try{
                suppliersRepet = supplierDAO.find(accountBook,condition);
            }catch (DatabaseNotFoundException ex){
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if(suppliersRepet.length>0)
            {return;
             }
        }
        try {
            supplierDAO.update(accountBook, suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        Supply[] supplies;
        int idLength=ids.length;
        int[] idsModify=null;
        List<int[]>  idsList = Arrays.asList(ids);
        List<int[]> IDRemove=null;
        for (int i=0;i<idLength;i++)
        {
            Supplier supplierRefference;
            int SupplierID=ids[i];
            Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"SupplierID\",\"values\":[\"" + SupplierID + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");
            supplies =supplyDAO.find(accountBook,condition);
            if(supplies.length>0){
                    idsList.remove(i);
                    i--;
            }
        }

        for(int i=0;i<idsList.size();i++){
           idsModify=idsList.get(i);
}
        try {
            supplierDAO.remove(accountBook, idsModify);
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
