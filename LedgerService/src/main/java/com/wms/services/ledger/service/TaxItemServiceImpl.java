package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.dao.TaxItemDAO;
import com.wms.utilities.model.Tax;
import com.wms.utilities.model.TaxItem;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
//---------------------税务ID总是判断为0---------------------
@Service
public class TaxItemServiceImpl implements TaxItemService {
    @Autowired
    TaxItemDAO taxItemDAO;

    @Autowired
    TaxDAO taxDAO;


    @Transactional
    public int[] add(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        for (int i = 0;i < taxItems.length;i ++) {

            //判断税务ID是否为空 判断所写入的税务ID是否存在
            Tax[] judgetaxitemtaxId = null;
            int taxitemtaxId = taxItems[i].getTaxId();

            System.out.print("-------"+taxitemtaxId+"------------");
            if (taxitemtaxId == 0) {       //判断是否输入税务ID
                throw new WMSServiceException("税务ID不能为空!");
            }
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Id','values':['"+taxitemtaxId+"'],'relation':'EQUAL'}],'orders':[{'key':'Id','order':'ASC'}]}");
            try {
                judgetaxitemtaxId = taxDAO.find(accountBook,condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if (judgetaxitemtaxId.length <= 0) {
                throw new WMSServiceException("没有找到税务ID为:"+taxitemtaxId+" 的税务");
            }

            //判断类型不能为空 是否符合条件(只能填入0 1)
            int taxitemType = taxItems[i].getType();
            if (taxitemType != 0 && taxitemType != 1) {
                throw new WMSServiceException("科目:"+taxitemType+" 错误!(只能填入0 1)");
            }

            BigDecimal taxitemStartamount = taxItems[i].getStartAmount();
            String strtaxitemStartamount = taxitemStartamount.toString();
            String strleftStartamount = strtaxitemStartamount.split(".")[0];
            String strrightStartamount = strtaxitemStartamount.split(".")[1];
            if (strleftStartamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightStartamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemEndamount = taxItems[i].getEndAmount();
            String strtaxitemEndamount = taxitemEndamount.toString();
            String strleftEndamount = strtaxitemEndamount.split(".")[0];
            String strrightEndamount = strtaxitemEndamount.split(".")[1];
            if (strleftEndamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightEndamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxamount = taxItems[i].getTaxAmount();
            String strtaxitemTaxamount = taxitemTaxamount.toString();
            String strleftTaxamount = strtaxitemTaxamount.split(".")[0];
            String strrightTaxamount = strtaxitemTaxamount.split(".")[1];
            if (strleftTaxamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxrate = taxItems[i].getTaxRate();
            String strtaxitemTaxrate = taxitemTaxrate.toString();
            String strleftTaxrate = strtaxitemTaxrate.split(".")[0];
            String strrightTaxrate = strtaxitemTaxrate.split(".")[1];
            if (strleftTaxrate.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxrate.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }
        }


        try{
            return taxItemDAO.add(accountBook,taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, TaxItem[] taxItems) throws WMSServiceException{
        for (int i = 0;i < taxItems.length;i ++) {
            TaxItem[] judgetaxitemId = null;
            int taxitemId = taxItems[i].getId();
            if (taxitemId == 0) {
                throw new WMSServiceException("若要修改必须输入ID!");
            }

            //判断税务ID是否为空 判断所写入的税务ID是否存在
            Tax[] judgetaxitemTaxid = null;
            int taxitemTaxid = taxItems[i].getTaxId();
            if (taxitemTaxid == 0) {       //判断是否输入税务ID
                throw new WMSServiceException("税务ID不能为空!");
            }
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Id','values':['"+taxitemTaxid+"'],'relation':'EQUAL'}],'orders':[{'key':'Id','order':'ASC'}]}");
            try {
                judgetaxitemTaxid = taxDAO.find("Tax",condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if (judgetaxitemTaxid.length <= 0) {
            throw new WMSServiceException("没有找到税务ID为:"+taxitemTaxid+" 的税务");
            }

            //判断类型不能为空 是否符合条件(只能填入0 1)
            int taxitemType = taxItems[i].getType();
            if (taxitemType != 0 && taxitemType != 1) {
                throw new WMSServiceException("科目:"+taxitemType+" 错误!(只能填入0 1)");
            }

            BigDecimal taxitemStartamount = taxItems[i].getStartAmount();
            String strtaxitemStartamount = taxitemStartamount.toString();
            String strleftStartamount = strtaxitemStartamount.split(".")[0];
            String strrightStartamount = strtaxitemStartamount.split(".")[1];
            if (strleftStartamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightStartamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemEndamount = taxItems[i].getEndAmount();
            String strtaxitemEndamount = taxitemEndamount.toString();
            String strleftEndamount = strtaxitemEndamount.split(".")[0];
            String strrightEndamount = strtaxitemEndamount.split(".")[1];
            if (strleftEndamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightEndamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxamount = taxItems[i].getTaxAmount();
            String strtaxitemTaxamount = taxitemTaxamount.toString();
            String strleftTaxamount = strtaxitemTaxamount.split(".")[0];
            String strrightTaxamount = strtaxitemTaxamount.split(".")[1];
            if (strleftTaxamount.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxamount.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

            BigDecimal taxitemTaxrate = taxItems[i].getTaxRate();
            String strtaxitemTaxrate = taxitemTaxrate.toString();
            String strleftTaxrate = strtaxitemTaxrate.split(".")[0];
            String strrightTaxrate = strtaxitemTaxrate.split(".")[1];
            if (strleftTaxrate.length() > 18) {
                throw new WMSServiceException("整数部分不能超过18位!");
            }
            if (strrightTaxrate.length() > 3) {
                throw new WMSServiceException("小数部分不能超过3位!");
            }

        }

        try {
            taxItemDAO.update(accountBook, taxItems);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            taxItemDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public TaxItem[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.taxItemDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
