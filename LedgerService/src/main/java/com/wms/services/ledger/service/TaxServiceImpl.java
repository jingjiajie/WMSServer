package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.TaxDAO;
import com.wms.utilities.model.Tax;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class TaxServiceImpl implements TaxService {
    @Autowired
    TaxDAO taxDAO;

    @Transactional
    public int[] add(String accountBook, Tax[] taxes) throws WMSServiceException{
        for (int i = 0;i < taxes.length;i ++) {
            //判断税务名称是否为空
            String taxName = taxes[i].getName();
            if (taxName == null || taxName.trim().length() <= 0) {
                throw new WMSServiceException("税务名称不能为空！");
            }

            //税务代码默认为""
        }

        try{
            return taxDAO.add(accountBook,taxes);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Tax[] taxes) throws WMSServiceException{
        for (int i = 0;i < taxes.length;i ++) {
            Tax[] judgetaxId = null;
            int taxid = taxes[i].getId();
            if (taxid == 0) {       //判断是否输入所要查询税务的ID
                throw new WMSServiceException("若要修改必须输入税务ID!");
            }
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Id','values':['"+taxid+"'],'relation':'EQUAL'}],'orders':[{'key':'Id','order':'ASC'}]}");      //查找已有ID中是否存在相同的
            try {
                judgetaxId = taxDAO.find(accountBook,condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if(judgetaxId.length <= 0) {        //用于判断的judgetaxid若 <= 0 说明没有所要查询的ID
                throw new WMSServiceException("没有找到ID为:"+taxid+" 的税务");
            }

            //判断税务名称是否为空
            String taxName = taxes[i].getName();
            if (taxName == null || taxName.trim().length() <= 0) {
                throw new WMSServiceException("税务名称不能为空！");
            }

            //税务代码默认为""
            String taxNo = taxes[i].getNo();
            if (taxNo == null) {
                throw new WMSServiceException("税务代码不能为空！");
            }
        }

        try {
            taxDAO.update(accountBook, taxes);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            taxDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public Tax[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.taxDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
