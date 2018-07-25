package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.vaildator.Validator;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class AccountTitleServiceImpl implements AccountTitleService {
    @Autowired
    AccountTitleDAO accountTitleDAO;
    @Autowired
    AccountRecordService accountRecordService;

    @Transactional
    public int[] add(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException {
        this.validateEntities(accountBook,accountTitles);
        Stream.of(accountTitles).forEach((accountTitle)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{accountTitle.getName()})).length > 0) {
                throw new WMSServiceException("科目名称：" + accountTitle.getNo() +"在库存中已经存在!");
            }
            if(this.find(accountBook,new Condition().addCondition("no",new String[]{accountTitle.getNo()})).length > 0) {
                throw new WMSServiceException("科目编码：" + accountTitle.getNo() +"在库存中已经存在!");
            }
        });

        for(int i=0;i<accountTitles.length;i++){

            List<FindLinkAccountTitle> findSonAccountTitleList=this.accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{accountTitles[i]});
            FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
            findSonAccountTitleList.toArray(sonAccountTitles);

            List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
            sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
            if (curSonAccountTitleViews.length>0) {
                throw new WMSServiceException(String.format("无法添加上级科目！当前编码存在子级科目，请检查再重新录入，当前科目名称(%s)，", accountTitles[0].getName()));
            }
        }
        int[]ids= accountTitleDAO.add(accountBook, accountTitles);
        List<FindLinkAccountTitle> findLinkAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,accountTitles);
        return ids;

    }

    @Transactional
    public void update(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException {
        this.validateEntities(accountBook,accountTitles);
        Stream.of(accountTitles).forEach((accountTitle)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{accountTitle.getName()})
                    .addCondition("id",new Integer[]{accountTitle.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("科目名称：" + accountTitle.getNo() +"在库存中已经存在!");
            }
            if(this.find(accountBook,new Condition().addCondition("no",new String[]{accountTitle.getNo()})
                    .addCondition("id",new Integer[]{accountTitle.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("科目编码：" + accountTitle.getNo() +"在库存中已经存在!");
            }
        });

        for(int i=0;i<accountTitles.length;i++){
            AccountTitleView oldAccountTitleView=new AccountTitleView();
            oldAccountTitleView=this.find(accountBook,new Condition().addCondition("id",new Integer[]{accountTitles[i].getId()}))[0];
            if (!accountTitles[i].getNo().equals(oldAccountTitleView.getNo()))
            {
                List<FindLinkAccountTitle> findSonAccountTitleList=this.accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{accountTitles[i]});
                FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
                findSonAccountTitleList.toArray(sonAccountTitles);

                List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
                AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
                sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
                if (curSonAccountTitleViews.length>0){
                    throw new WMSServiceException(String.format("无法添加上级科目！当前编码存在子级科目，请检查再重新录入，当前科目名称(%s)，", accountTitles[0].getName()));

                }
            }
        }

        try {
            accountTitleDAO.update(accountBook, accountTitles);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            accountTitleDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public AccountTitleView[] find(String accountBook, Condition cond) throws WMSServiceException {
        try {
            return this.accountTitleDAO.find(accountBook, cond);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public long findCount(String accountBook,Condition cond) throws WMSServiceException{
        return this.accountTitleDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook,AccountTitle[] accountTitles) throws WMSServiceException{
        Stream.of(accountTitles).forEach((accountTitle -> {
            new Validator("科目名称").notEmpty().validate(accountTitle.getName());
            new Validator("科目编码").notEmpty().validate(accountTitle.getNo());
            new Validator("余额方向").min(0).max(1).validate(accountTitle.getDirection());
            new Validator("启用").min(0).max(1).validate(accountTitle.getEnabled());
            new Validator("科目类型").min(0).max(5).validate(accountTitle.getType());
        }));

        for(int i=0;i<accountTitles.length;i++){
            for(int j=i+1;j<accountTitles.length;j++){
                String no=accountTitles[i].getNo();
                String name=accountTitles[i].getName();
                if(name.equals(accountTitles[j].getName()))
                {
                    throw new WMSServiceException("科目名称：" +name+"在添加的列表中重复!");
                }
                if(no.equals(accountTitles[j].getNo()))
                {
                    throw new WMSServiceException("科目编码：" +no+"在添加的列表中重复!");
                }
            }
        }
    }
}
