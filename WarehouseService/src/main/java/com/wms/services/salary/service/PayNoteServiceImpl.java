package com.wms.services.salary.service;

import com.wms.services.ledger.dao.AccountTitleDAO;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
    @Autowired
    SalaryTypeService salaryTypeService;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    AccountTitleDAO accountTitleDAO;

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
                        throw new WMSServiceException(String.format("科目一（借方科目）不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("科目二（贷方科目）不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format("税务不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资期间不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
                    }
                    if(this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryTypeId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资类型不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
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
                payNote.setNo(this.orderNoGenerator.generateNextNo(accountBook, PayNoteServiceImpl.NO_PREFIX,payNote.getWarehouseId()));}
                else {
            Condition cond = new Condition();
            cond.addCondition("no",new String[]{payNote.getNo()});
            cond.addCondition("warehouseId",payNote.getWarehouseId());
            if(payNoteDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("薪资发放单单号："+payNote.getNo()+"已经存在!");
            }}
            if(this.IsRepeat(new int[]{payNote.getAccountTitleExpenseId(),payNote.getAccountTitlePayableId()})){
                throw new WMSServiceException("薪资发放单薪科目不允许重复！!");
            }
            //判断是否最低级
            List<Integer> accountTitleId=new ArrayList<>();
            accountTitleId.add(payNote.getAccountTitleExpenseId());
            accountTitleId.add(payNote.getAccountTitlePayableId());
            this.HasSonAccountTitle(accountBook,accountTitleId);
        });
        int[] ids= payNoteDAO.add(accountBook,payNotes);
        for (int id:ids)
        {
            AddAllItem addAllItem=new AddAllItem();
            addAllItem.setPayNoteId(id);
            payNoteItemService.addAllItem(accountBook,addAllItem);
        }
        return ids;
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
                        throw new WMSServiceException(String.format("科目一（借方科目）不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitleExpenseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()}).addCondition("enabled",AccountTitleService.ENABLED_ON)).length == 0){
                        throw new WMSServiceException(String.format("科目二（贷方科目）不存在或已禁用，请重新提交！(%d)",payNote.getAccountTitlePayableId()));
                    }
                    if(this.taxService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getTaxId()})).length == 0){
                        throw new WMSServiceException(String.format(" 税务不存在，请重新提交！(%d)",payNote.getAccountTitlePropertyId()));
                    }
                    if(this.salaryPeriodService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryPeriodId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资期间不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
                    }
                    if(this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getSalaryTypeId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资类型不存在，请重新提交！(%d)",payNote.getSalaryPeriodId()));
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
            if(this.IsRepeat(new int[]{payNote.getAccountTitleExpenseId(),payNote.getAccountTitlePayableId()})){
                throw new WMSServiceException("薪资发放单薪科目不允许重复！!");
            }
            //判断是否最低级
            List<Integer> accountTitleId=new ArrayList<>();
            accountTitleId.add(payNote.getAccountTitleExpenseId());
            accountTitleId.add(payNote.getAccountTitlePayableId());
            this.HasSonAccountTitle(accountBook,accountTitleId);
        });
        payNoteDAO.update(accountBook,payNotes);
    }


    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            PayNote[] payNotes=payNoteDAO.findTable(accountBook, new Condition().addCondition("id", id));
            if ( payNotes.length == 0) {
                throw new WMSServiceException(String.format("删除薪资单不存在，请重新查询！(%d)", id));
            }
            if(payNotes[0].getState()!=PayNoteState.WAITING_FOR_CONFIRM){
                throw new WMSServiceException("删除薪资单不在等待确认状态无法删除！ 单号："+payNotes[0].getNo());
            }
        }
        Session session = this.sessionFactory.getCurrentSession();

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("(");
        for(int i=0;i<ids.length;i++){ stringBuffer.append(ids[i]);
            if(i!=ids.length-1)
            {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(")");
        try {
            Query query = null;
            String sql = "DELETE FROM PayNoteItem WHERE payNoteId in "+stringBuffer.toString();
            query = session.createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception e) {
            throw new WMSServiceException("删除薪资发放单条目失败！");
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

    public PayNote[] findTable(String accountBook, Condition cond) throws WMSServiceException{
        return this.payNoteDAO.findTable(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.payNoteDAO.findCount(database,cond);
    }


   //实际支付同步到总账 同时将状态变为下一个状态
   public void realPayToAccountTitle(String accountBook,AccountSynchronize accountSynchronize)
   {
       int payNoteId=accountSynchronize.getPayNoteId();
       int personId=accountSynchronize.getPersonId();
       PayNoteView[] payNoteViews=payNoteDAO.find(accountBook,new Condition().addCondition("id",payNoteId));
       if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
       if(payNoteViews[0].getState()!= PayNoteState.WAITING_FOR_CONFIRM){throw new WMSServiceException("此单不为等待支付状态，无法执行确认操作！");}
       PayNoteItemView[] payNoteItemViews=payNoteItemService.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
       if(payNoteItemViews.length==0){throw new WMSServiceException("操作的薪金发放单中无条目，无法进行同步！");}
       //判断条目是否全部计算同时计算总金额
       BigDecimal totalPaidAmount=new BigDecimal(0);
       for(int i=0;i<payNoteItemViews.length;i++){
           if(payNoteItemViews[i].getState()!= PayNoteItemState.PAYED){throw new WMSServiceException("操作的薪金发放单中条目未全部付款，无法同步到总账！");
           }
           //改为用税后应付 因为少了一个状态
           totalPaidAmount=totalPaidAmount.add(payNoteItemViews[i].getAfterTaxAmount());
       }
       //科目一 借方科目
       int accountTitle1=payNoteViews[0].getAccountTitleExpenseId();
       //科目二 贷方科目
       int accountTitle2=payNoteViews[0].getAccountTitlePayableId();
       AccountRecord accountRecord=new AccountRecord();
       accountRecord.setOwnAccountTitleId(accountTitle1);
       accountRecord.setOtherAccountTitleId(accountTitle2);
       accountRecord.setPersonId(personId);
       accountRecord.setDebitAmount(totalPaidAmount);//借方
       accountRecord.setCreditAmount(ZERO);//贷方
       accountRecord.setOtherBalance(new BigDecimal(0));
       accountRecord.setOwnBalance(new BigDecimal(0));
       accountRecord.setWarehouseId(accountSynchronize.getWarehouseId());
       accountRecord.setVoucherInfo(accountSynchronize.getVoucherInfo());
       accountRecord.setAccountPeriodId(accountSynchronize.getAccountPeriodId());
       accountRecord.setComment(accountSynchronize.getComment());
       accountRecord.setServiceTime(new Timestamp(System.currentTimeMillis()));
       try
       {
       accountRecordService.add(accountBook,new AccountRecord[]{accountRecord});
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

    public AccountTitleView[] findSonTitleForAssociation(String accountBook,Condition condition)
    {
        AccountTitleView[] accountTitleViews=accountTitleService.find(accountBook,condition);
        List<AccountTitleView> accountTitleViewList=new ArrayList<>();
        for(int i=0;i<accountTitleViews.length;i++){
            AccountTitle accountTitle=ReflectHelper.createAndCopyFields(accountTitleViews[i],AccountTitle.class);
            List<FindLinkAccountTitle> findSonAccountTitleList=accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{accountTitle});
            FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
            findSonAccountTitleList.toArray(sonAccountTitles);
            List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
            sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
            if(sonAccountTitleViewsList.size()==0)
            {
                accountTitleViewList.add(accountTitleViews[i]);
            }
        }
        AccountTitleView[] sonAccountTitle=new AccountTitleView[accountTitleViewList.size()];
        accountTitleViewList.toArray(sonAccountTitle);
        return sonAccountTitle;
    }
}
