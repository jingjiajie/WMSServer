package com.wms.services.salary.service;

import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PayNoteDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public int[] add(String accountBook, PayNote[] payNotes) throws WMSServiceException
    {

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
        Stream.of(payNotes).forEach((payNote)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{payNote.getNo()});
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
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePaidId()})).length == 0){
                        throw new WMSServiceException(String.format("实付款科目不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()})).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在，请重新提交！(%d)",payNote.getWarehouseId()));
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
                    cond.addCondition("name", new String[]{payNote.getNo()});
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
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePaidId()})).length == 0){
                        throw new WMSServiceException(String.format("实付款科目不存在，请重新提交！(%d)",payNote.getWarehouseId()));
                    }
                    if(this.accountTitleService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNote.getAccountTitlePayableId()})).length == 0){
                        throw new WMSServiceException(String.format("应付款科目不存在，请重新提交！(%d)",payNote.getWarehouseId()));
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
}
