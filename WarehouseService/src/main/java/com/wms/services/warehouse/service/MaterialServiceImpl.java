package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.MaterialDAO;
import com.wms.services.warehouse.model.Material;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    MaterialDAO materialDAO;

    @Transactional
    public int[] add(String accountBook, Material[] materials) throws WMSServiceException{
        try{
            return materialDAO.add(accountBook,materials);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Material[] materials) throws WMSServiceException{
        try {
            materialDAO.update(accountBook, materials);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            materialDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public Material[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.materialDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
