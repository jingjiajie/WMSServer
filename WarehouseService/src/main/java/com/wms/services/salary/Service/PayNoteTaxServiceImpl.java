package com.wms.services.salary.Service;

import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.dao.PayNoteTaxDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
@Service
public class PayNoteTaxServiceImpl {
    @Autowired
    PayNoteTaxDAO payNoteTaxDAO;
    @Autowired
    TaxService taxService;
    @Autowired
    PayNoteService payNoteService;
    public int[] add(String accountBook, PayNoteTax[] payNoteTaxes) throws WMSServiceException
    {
        //外键检测
        Stream.of(payNoteTaxes).forEach(
                (payNoteTax)->{
                    if(this.payNoteService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteTax.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteTax.getPayNoteId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteTax.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format("税务不存在，请重新提交！(%d)",payNoteTax.getTaxId()));
                    }}
        );
        return payNoteTaxDAO.add(accountBook,payNoteTaxes);
    }

    @Transactional
    public void update(String accountBook, PayNoteTax[] payNoteTaxes) throws WMSServiceException{

//外键检测
        Stream.of(payNoteTaxes).forEach(
                (payNoteTax)->{
                    if(this.payNoteService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteTax.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteTax.getPayNoteId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteTax.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format("税务不存在，请重新提交！(%d)",payNoteTax.getTaxId()));
                    }}
        );
        payNoteTaxDAO.update(accountBook, payNoteTaxes);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (payNoteTaxDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            payNoteTaxDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除薪金发放单扣税失败，如果薪金发放单扣税已经被引用，需要先删除引用的内容，才能删除薪金发放单扣税！");
        }
    }

    public PayNoteTaxView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.payNoteTaxDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.payNoteTaxDAO.findCount(database,cond);
    }
}
