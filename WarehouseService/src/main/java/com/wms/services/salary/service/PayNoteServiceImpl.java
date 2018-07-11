package com.wms.services.salary.service;

import com.wms.services.ledger.service.AccountRecordService;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PayNoteDAO;
import com.wms.services.salary.datestructures.CalculateTax;
import com.wms.services.salary.datestructures.PayNoteItemState;
import com.wms.services.salary.datestructures.PayNoteState;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
@Transactional
public class PayNoteServiceImpl implements PayNoteService{
    @Autowired
    PayNoteDAO payNoteDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    PayNoteItemService payNoteItemService;
    @Autowired
    AccountRecordService accountRecordService;

    public int[] add(String accountBook, PayNote[] payNotes) throws WMSServiceException
    {

        for(int i=0;i<payNotes.length;i++) {
            Validator validator = new Validator("薪资发放单单号");
            validator.notnull().notEmpty().validate(payNotes[i].getNo());
            payNotes[i].setState(PayNoteState.WAITING_FOR_CONFIRM);
        }

        for(int i=0;i<payNotes.length;i++){
            for(int j=i+1;j<payNotes.length;j++){
                String name=payNotes[i].getNo();
                if(name.equals(payNotes[j].getNo())){throw new WMSServiceException("薪资发放单单号"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(payNotes).forEach((payNote)->{
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{payNote.getNo()});
            cond.addCondition("warehouseId",payNote.getWarehouseId());
            if(payNoteDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪资发放单单号："+payNote.getNo()+"已经存在!");
            }
        });
        //外键检测
        Stream.of(payNotes).forEach(
                (payNote)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitleExpenseId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资费用科目不存在，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()})).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePropertyId()})).length == 0){
                        throw new WMSServiceException(String.format(" 资产科目不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                }
        );
        return payNoteDAO.add(accountBook,payNotes);
    }

    @Transactional
    public void update(String accountBook, PayNote[] payNotes) throws WMSServiceException{
        for(int i=0;i<payNotes.length;i++) {
            Validator validator = new Validator("薪资发放单单号");
            validator.notnull().notEmpty().validate(payNotes[i].getNo());
        }

        for(int i=0;i<payNotes.length;i++){
            for(int j=i+1;j<payNotes.length;j++){
                String name=payNotes[i].getNo();
                if(name.equals(payNotes[j].getNo())){throw new WMSServiceException("薪资发放单单号"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(payNotes).forEach((payNote)-> {
                    Condition cond = new Condition();
                    cond.addCondition("no", new String[]{payNote.getNo()});
                    cond.addCondition("warehouseId", payNote.getWarehouseId());
                    cond.addCondition("id", new Integer[]{payNote.getId()}, ConditionItem.Relation.NOT_EQUAL);
                    if (payNoteDAO.find(accountBook, cond).length > 0) {
                        throw new WMSServiceException("薪资发放单单号：" + payNote.getNo() + "已经存在!");
                    }
                });

        Stream.of(payNotes).forEach(
                (payNote)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitleExpenseId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资费用科目不存在，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()})).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePropertyId()})).length == 0){
                        throw new WMSServiceException(String.format(" 资产科目不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                }
        );
        payNoteDAO.update(accountBook,payNotes);

    }


    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (payNoteDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除薪资单不存在，请重新查询！(%d)", id));
            }
        }
        try {
            payNoteDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除薪资单失败，如果薪金单已经被引用，需要先删除引用的内容，才能删除薪金单！");
        }
    }

    public PayNoteView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.payNoteDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.payNoteDAO.findCount(database,cond);
    }

//确认支付到总账
   public void confirmToAccountTitle(String accountBook, int payNoteId ){
       PayNoteView[] payNoteViews=payNoteDAO.find(accountBook,new Condition().addCondition("id",payNoteId));
       if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
       if(payNoteViews[0].getState()!= PayNoteState.WAITING_FOR_CONFIRM){throw new WMSServiceException("此单不为未确认状态，无法执行确认操作！");}
       PayNoteItemView[] payNoteItemViews=payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
       //判断条目是否全部计算同时计算总金额
       BigDecimal totalAmount=new BigDecimal(0);
       for(int i=0;i<payNoteItemViews.length;i++){
           if(payNoteItemViews[i].getState()!= PayNoteItemState.CALCULATED_PAY){throw new WMSServiceException("操作的薪金发放单中条目未全部计算，无法同步到总账！");
           }
           totalAmount=totalAmount.add(payNoteItemViews[i].getAfterTaxAmount());
       }
       //应付薪资
       int accountTitlePayableID=payNoteViews[0].getAccountTitlePayableId();
       //管理费用
       int accountTitleExpenseID=payNoteViews[0].getAccountTitleExpenseId();
       //TODO 将总金额增加到 总账

       //将整单变为已确认待付款状态
       PayNote payNote=new PayNote();
       payNote=ReflectHelper.createAndCopyFields(payNoteViews[0],PayNote.class);
       payNote.setState(PayNoteState.CONFIRM_PAY);
       payNoteDAO.update(accountBook,new PayNote[]{payNote});
   }

   //实际支付同步到总账 同时将状态变为下一个状态
   public void realPayToAccountTitle(String accountBook,int payNoteId)
   {
       PayNoteView[] payNoteViews=payNoteDAO.find(accountBook,new Condition().addCondition("id",payNoteId));
       if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
       if(payNoteViews[0].getState()!= PayNoteState.CONFIRM_PAY){throw new WMSServiceException("此单不为等待支付状态，无法执行确认操作！");}
       PayNoteItemView[] payNoteItemViews=payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
       //判断条目是否全部计算同时计算总金额
       BigDecimal totalPaidAmount=new BigDecimal(0);
       for(int i=0;i<payNoteItemViews.length;i++){
           if(payNoteItemViews[i].getState()!= PayNoteItemState.PAYED){throw new WMSServiceException("操作的薪金发放单中条目未全部计算！");
           }
           totalPaidAmount=totalPaidAmount.add(payNoteItemViews[i].getPaidAmount());
       }
       //应付薪资
       int accountTitlePayableID=payNoteViews[0].getAccountTitlePayableId();
       //银行资产
       int accountTitlePropertyID=payNoteViews[0].getAccountTitlePropertyId();
       //同步到总账

       //将整单变为已付款
       PayNote payNote=new PayNote();
       payNote=ReflectHelper.createAndCopyFields(payNoteViews[0],PayNote.class);
       payNote.setState(PayNoteState.CONFIRM_REAL_PAY);
       payNoteDAO.update(accountBook,new PayNote[]{payNote});
   }
}
