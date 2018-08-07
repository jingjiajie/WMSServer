package com.wms.services.salary.service;

import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import com.wms.services.ledger.service.AccountRecordService;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.dao.PayNoteDAO;
import com.wms.services.salary.datestructures.*;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.AccountTitleException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    TaxService taxService;
    @Autowired
    SalaryPeriodService salaryPeriodService;

    private static final String NO_PREFIX = "X";
    private static final BigDecimal ZERO= new BigDecimal(0);

    public int[] add(String accountBook, PayNote[] payNotes) throws WMSServiceException
    {

        //外键检测
        Stream.of(payNotes).forEach(
                (payNote)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitleExpenseId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("薪资费用科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePropertyId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format(" 资产科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format(" 税务不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资期间不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
                    }
                }
        );
        for(int i=0;i<payNotes.length;i++) {
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
            if (payNote.getNo() == null) {
                payNote.setNo(this.orderNoGenerator.generateNextNo(accountBook, PayNoteServiceImpl.NO_PREFIX));}
                else {
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{payNote.getNo()});
            cond.addCondition("warehouseId",payNote.getWarehouseId());
            if(payNoteDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪资发放单单号："+payNote.getNo()+"已经存在!");
            }}
            if(this.IsRepeat(new int[]{payNote.getAccountTitlePropertyId(),payNote.getAccountTitleExpenseId(),payNote.getAccountTitlePayableId()})){
                throw new WMSServiceException("薪资发放单薪资费用科目、应付款科目、资产科目不允许重复！!");
            }
            //判断是否最低级
            List<Integer> accountTitleId=new ArrayList<>();
            accountTitleId.add(payNote.getAccountTitlePropertyId());
            accountTitleId.add(payNote.getAccountTitleExpenseId());
            accountTitleId.add(payNote.getAccountTitlePayableId());
            this.HasSonAccountTitle(accountBook,accountTitleId);
        });
        return payNoteDAO.add(accountBook,payNotes);
    }

    @Transactional
    public void update(String accountBook, PayNote[] payNotes) throws WMSServiceException{
        for(int i=0;i<payNotes.length;i++) {
            Validator validator = new Validator("薪资发放单单号");
            validator.notnull().notEmpty().validate(payNotes[i].getNo());
        }
        Stream.of(payNotes).forEach(
                (payNote)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getWarehouseId()})).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitleExpenseId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("薪资费用科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePropertyId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format(" 资产科目不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format(" 税务不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资期间不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
                    }
                }
        );

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
            if(this.IsRepeat(new int[]{payNote.getAccountTitlePropertyId(),payNote.getAccountTitleExpenseId(),payNote.getAccountTitlePayableId()})){
                throw new WMSServiceException("薪资发放单薪资费用科目、应付款科目、资产科目不允许重复！!");
            }
            //判断是否最低级
            List<Integer> accountTitleId=new ArrayList<>();
            accountTitleId.add(payNote.getAccountTitlePropertyId());
            accountTitleId.add(payNote.getAccountTitleExpenseId());
            accountTitleId.add(payNote.getAccountTitlePayableId());
            this.HasSonAccountTitle(accountBook,accountTitleId);
        });
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
   public void confirmToAccountTitle(String accountBook, AccountSynchronize accountSynchronize){
        int payNoteId=accountSynchronize.getPayNoteId();
        int personId=accountSynchronize.getPersonId();
        int warehouseId=accountSynchronize.getWarehouseId();
       PayNoteView[] payNoteViews=payNoteDAO.find(accountBook,new Condition().addCondition("id",payNoteId));
       if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
       if(payNoteViews[0].getState()!= PayNoteState.WAITING_FOR_CONFIRM){throw new WMSServiceException("此单不为未确认状态，无法执行确认操作！");}
       PayNoteItemView[] payNoteItemViews=payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
       if(payNoteItemViews.length==0){throw new WMSServiceException("操作的薪金发放单中无条目，无法进行同步！");}
       //判断条目是否全部计算同时计算总金额
       BigDecimal totalAmount=new BigDecimal(0);
       for(int i=0;i<payNoteItemViews.length;i++){
           if(payNoteItemViews[i].getState()!= PayNoteItemState.CALCULATED_PAY){throw new WMSServiceException("操作的薪金发放单中条目未全部计算应付，无法同步到总账！");
           }
           totalAmount=totalAmount.add(payNoteItemViews[i].getAfterTaxAmount());
       }
       //应付薪资
       int accountTitlePayableID=payNoteViews[0].getAccountTitlePayableId();
       AccountRecord accountRecord=new AccountRecord();
       accountRecord.setAccountTitleId(accountTitlePayableID);
       accountRecord.setPersonId(personId);
       accountRecord.setCreditAmount(totalAmount);
       accountRecord.setDebitAmount(ZERO);
       accountRecord.setWarehouseId(accountSynchronize.getWarehouseId());
       accountRecord.setVoucherInfo(accountSynchronize.getVoucherInfo());
       accountRecord.setAccountPeriodId(accountSynchronize.getAccountPeriodId());
       accountRecord.setComment(accountSynchronize.getComment());
       accountRecord.setTime(new Timestamp(System.currentTimeMillis()));
       accountRecord.setBalance(new BigDecimal(1));
       //管理费用
       int accountTitleExpenseID=payNoteViews[0].getAccountTitleExpenseId();
       AccountRecord accountRecord1=new AccountRecord();
       accountRecord1.setAccountTitleId(accountTitleExpenseID);
       accountRecord1.setPersonId(personId);
       accountRecord1.setDebitAmount(totalAmount);
       accountRecord1.setCreditAmount(ZERO);
       accountRecord1.setWarehouseId(warehouseId);
       accountRecord1.setVoucherInfo(accountSynchronize.getVoucherInfo());
       accountRecord1.setAccountPeriodId(accountSynchronize.getAccountPeriodId());
       accountRecord1.setComment(accountSynchronize.getComment());
       accountRecord1.setBalance(new BigDecimal(1));
       accountRecord1.setTime(new Timestamp(System.currentTimeMillis()));
       //TODO 将总金额增加到 总账
       try{
       accountRecordService.add(accountBook,new AccountRecord[]{accountRecord,accountRecord1});
       }
       catch (AccountTitleException e){
           throw new WMSServiceException("无法向非子级科目记录账目，请将应付款科目和薪资费用科目修改为子级科目！");
       }
       //将整单变为已确认待付款状态
       PayNote payNote=ReflectHelper.createAndCopyFields(payNoteViews[0],PayNote.class);
       payNote.setState(PayNoteState.CONFIRM_PAY);
       payNoteDAO.update(accountBook,new PayNote[]{payNote});
   }

   //实际支付同步到总账 同时将状态变为下一个状态
   public void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize)
   {
       int payNoteId=accountSynchronize.getPayNoteId();
       int personId=accountSynchronize.getPersonId();
       int warehouseId=accountSynchronize.getWarehouseId();
       PayNoteView[] payNoteViews=payNoteDAO.find(accountBook,new Condition().addCondition("id",payNoteId));
       if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
       if(payNoteViews[0].getState()!= PayNoteState.CONFIRM_PAY){throw new WMSServiceException("此单不为等待支付状态，无法执行确认操作！");}
       PayNoteItemView[] payNoteItemViews=payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
       if(payNoteItemViews.length==0){throw new WMSServiceException("操作的薪金发放单中无条目，无法进行同步！");}
       //判断条目是否全部计算同时计算总金额
       BigDecimal totalPaidAmount=new BigDecimal(0);
       for(int i=0;i<payNoteItemViews.length;i++){
           if(payNoteItemViews[i].getState()!= PayNoteItemState.PAYED){throw new WMSServiceException("操作的薪金发放单中条目未全部付款，无法同步到总账！");
           }
           totalPaidAmount=totalPaidAmount.add(payNoteItemViews[i].getPaidAmount());
       }
       //应付薪资
       int accountTitlePayableID=payNoteViews[0].getAccountTitlePayableId();
       AccountRecord accountRecord=new AccountRecord();
       accountRecord.setAccountTitleId(accountTitlePayableID);
       accountRecord.setPersonId(personId);
       accountRecord.setDebitAmount(totalPaidAmount);
       accountRecord.setCreditAmount(ZERO);
       accountRecord.setWarehouseId(accountSynchronize.getWarehouseId());
       accountRecord.setVoucherInfo(accountSynchronize.getVoucherInfo());
       accountRecord.setAccountPeriodId(accountSynchronize.getAccountPeriodId());
       accountRecord.setComment(accountSynchronize.getComment());
       accountRecord.setBalance(new BigDecimal(1));
       accountRecord.setTime(new Timestamp(System.currentTimeMillis()));
       //银行资产
       int accountTitlePropertyID=payNoteViews[0].getAccountTitlePropertyId();
       //同步到总账
       AccountRecord accountRecord1=new AccountRecord();
       accountRecord1.setAccountTitleId(accountTitlePropertyID);
       accountRecord1.setPersonId(personId);
       accountRecord1.setCreditAmount(totalPaidAmount);
       accountRecord1.setDebitAmount(ZERO);
       accountRecord1.setWarehouseId(accountSynchronize.getWarehouseId());
       accountRecord1.setVoucherInfo(accountSynchronize.getVoucherInfo());
       accountRecord1.setAccountPeriodId(accountSynchronize.getAccountPeriodId());
       accountRecord1.setComment(accountSynchronize.getComment());
       accountRecord1.setBalance(new BigDecimal(1));
       accountRecord1.setTime(new Timestamp(System.currentTimeMillis()));
       try
       {
       accountRecordService.add(accountBook,new AccountRecord[]{accountRecord,accountRecord1});
       }
       catch (AccountTitleException e)
       {
           throw new WMSServiceException("无法非子级科目记录账目，请应付款科目和资产科目修改为子级科目");
       }
       //将整单变为已付款
       PayNote payNote=ReflectHelper.createAndCopyFields(payNoteViews[0],PayNote.class);
       payNote.setState(PayNoteState.CONFIRM_REAL_PAY);
       payNoteDAO.update(accountBook,new PayNote[]{payNote});
   }

    @Override
    public List<PayNoteAndItems> getPreviewData(String accountBook, List<Integer> payNoteIds) throws WMSServiceException{
        PayNoteView[] payNoteViews = this.payNoteDAO.find(accountBook,new Condition().addCondition("id", payNoteIds.toArray(), ConditionItem.Relation.IN));
        PayNoteItemView[] itemViews = this.payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId", payNoteIds.toArray(), ConditionItem.Relation.IN));
        List<PayNoteAndItems> result = new ArrayList<>();
        for(PayNoteView payNoteView : payNoteViews){
            PayNoteAndItems payNoteAndItems= new PayNoteAndItems();
            payNoteAndItems.setPayNoteView(payNoteView);
            payNoteAndItems.setPayNoteItemViews(new ArrayList<>());
            result.add(payNoteAndItems);
            for(PayNoteItemView itemView : itemViews){
                if(itemView.getPayNoteId() == payNoteView.getId()){
                    payNoteAndItems.getPayNoteItemViews().add(itemView);
                }
            }
        }
        return result;
    }

    private  boolean IsRepeat(int[] array)
    {
        List<Integer> list= new ArrayList<>();
        for (int i = 0; i < array.length; i++)
        {
            if (list.contains(array[i]))
            {
                return true;
            }
            else
            {
                list.add(array[i]);
            }
        }
        return false;
    }

    private void HasSonAccountTitle(String accountBook,List<Integer> accountTitleId)
    {
      AccountTitleView[] accountTitleViews=accountTitleService.find(accountBook,new Condition().addCondition("id",accountTitleId.toArray(), ConditionItem.Relation.IN));
      for(int i=0;i<accountTitleViews.length;i++){
          AccountTitle accountTitle=ReflectHelper.createAndCopyFields(accountTitleViews[i],AccountTitle.class);

          List<FindLinkAccountTitle> findSonAccountTitleList=accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{accountTitle});
          FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
          findSonAccountTitleList.toArray(sonAccountTitles);
          List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
          AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
          sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
          if(sonAccountTitleViewsList.size()>0)
          {
              throw new WMSServiceException("科目:("+accountTitle.getName()+")不是最低级科目，请修改！");
          }
      }
    }
}
