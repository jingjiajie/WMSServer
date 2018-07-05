package com.wms.services.salary.Service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.salary.dao.PayNoteItemDAO;
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

@Transactional
@Service
public class PayNoteItemServiceImpl implements PayNoteItemService {
    @Autowired
    PayNoteItemDAO payNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    PayNoteService payNoteService;

    public int[] add(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException
    {
        //新建的条目状态应该为0
        for(int i=0;i<payNoteItems.length;i++){
            for(int j=i+1;j<payNoteItems.length;j++){
            payNoteItems[i].setState(0);
            }
        }

        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem)->{
                    if(this.payNoteItemDAO.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteItem.getPayNoteId()));
                    }

                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",payNoteItem.getPersonId()));
                    }
                }
        );
        return payNoteItemDAO.add(accountBook,payNoteItems);
    }

    @Transactional
    public void update(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException{

        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem)->{
                    if(this.payNoteItemDAO.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteItem.getPayNoteId()));
                    }

                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",payNoteItem.getPersonId()));
                    }
                }
        );
        payNoteItemDAO.update(accountBook,payNoteItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (payNoteItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            payNoteItemDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除薪金发放单条目失败，如果薪金发放单条目已经被引用，需要先删除引用的内容，才能删除薪金发放单条目！");
        }
    }

    public PayNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.payNoteItemDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.payNoteItemDAO.findCount(database,cond);
    }


}
