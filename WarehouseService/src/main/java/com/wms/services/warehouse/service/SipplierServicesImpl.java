package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class SipplierServicesImpl  implements SupplierServices{
    @Autowired
    SupplierDAO  supplierDAO;
    @Transactional
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException{
        try{
            return supplierDAO.add(accountBook,suppliers);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }






}
