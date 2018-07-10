package com.wms.services.salary.service;

import com.wms.services.ledger.dao.TaxDAO;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.dao.PayNoteTaxDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
@Service
public class PayNoteTaxServiceImpl implements PayNoteTaxService {
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
        for(int i=0;i<payNoteTaxes.length;i++){
            for(int j=i+1;j<payNoteTaxes.length;j++){
                int payNoteId=payNoteTaxes[i].getPayNoteId();
                int taxId=payNoteTaxes[i].getTaxId();
                if(payNoteId==payNoteTaxes[j].getPayNoteId()&&taxId==payNoteTaxes[j].getTaxId()){throw new WMSServiceException("相同的税务在列表中重复！");}
            }
        }
        //重复
        Stream.of(payNoteTaxes).forEach((payNoteTax)->{
            Condition cond = new Condition();
            cond.addCondition("taxId",payNoteTax.getPayNoteId());
            cond.addCondition("payNoteId",payNoteTax.getPayNoteId());
            if(payNoteTaxDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("同一税务只能添加一次！");
            }
        });
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
        for(int i=0;i<payNoteTaxes.length;i++){
            for(int j=i+1;j<payNoteTaxes.length;j++){
                int payNoteId=payNoteTaxes[i].getPayNoteId();
                int taxId=payNoteTaxes[i].getTaxId();
                if(payNoteId==payNoteTaxes[j].getPayNoteId()&&taxId==payNoteTaxes[j].getTaxId()){throw new WMSServiceException("相同的税务在列表中重复！");}
            }
        }
        //重复
        Stream.of(payNoteTaxes).forEach((payNoteTax)->{
            Condition cond = new Condition();
            cond.addCondition("taxId",payNoteTax.getPayNoteId());
            cond.addCondition("payNoteId",payNoteTax.getPayNoteId());
            cond.addCondition("id",payNoteTax.getId(), ConditionItem.Relation.NOT_EQUAL);
            if(payNoteTaxDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("同一税务只能添加一次！");
            }
        });
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

    public void chooseTax(String accountBook,PayNoteTax[] payNoteTaxes){
      if(payNoteTaxes.length!=1){throw new WMSServiceException("选择税务出现问题！");}
       PayNoteTaxView[] payNoteTaxViews= payNoteTaxDAO.find(accountBook,new Condition().addCondition("id",payNoteTaxes[0].getId()));
      if(payNoteTaxViews.length==0){
          this.add(accountBook,payNoteTaxes);
      }
      else {
          this.update(accountBook,payNoteTaxes);
      }
    }
}
