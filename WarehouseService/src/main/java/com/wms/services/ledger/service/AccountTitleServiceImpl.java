package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.datestructures.FindBalance;
import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.AccountRecordView;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
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
                throw new WMSServiceException("科目名称：" + accountTitle.getName() +"在库存中已经存在!");
            }
            if(this.find(accountBook,new Condition().addCondition("no",new String[]{accountTitle.getNo()})).length > 0) {
                throw new WMSServiceException("科目编码：" + accountTitle.getNo() +"在库存中已经存在!");
            }
        });

        for(int i=0;i<accountTitles.length;i++){
            String No=accountTitles[i].getNo();
            for (int j = No.length();--j>=0;){
                if (!Character.isDigit(No.charAt(j))){
                    throw new WMSServiceException("科目编码必须为纯数字！出错编码："+No);
                }
            }
            List<FindLinkAccountTitle> findSonAccountTitleList=this.accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{accountTitles[i]});
            FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
            findSonAccountTitleList.toArray(sonAccountTitles);

            List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
            sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
//            if (curSonAccountTitleViews.length>0) {
//                throw new WMSServiceException(String.format("无法添加上级科目！当前编码存在子级科目，请检查再重新录入，当前科目名称(%s)，", accountTitles[0].getName()));
//            }
        }

        for(int i=0;i<accountTitles.length;i++){

            List<FindLinkAccountTitle> findParentAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,new AccountTitle[]{accountTitles[i]});
            FindLinkAccountTitle[] parentAccountTitles=new FindLinkAccountTitle[findParentAccountTitleList.size()];
            findParentAccountTitleList.toArray(parentAccountTitles);

            List<AccountTitleView> parentAccountTitleViewsList= parentAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curParentAccountTitleViews=new AccountTitleView[parentAccountTitleViewsList.size()];
            parentAccountTitleViewsList.toArray(curParentAccountTitleViews);

            Stream.of(curParentAccountTitleViews).forEach((curParentAccountTitleView)->{

                AccountTitle curAccountTitle =this.accountTitleDAO.findTable(accountBook,new Condition().addCondition("id",curParentAccountTitleView.getId()))[0];
                FindBalance findBalance=accountRecordService.findNewBalance(accountBook,curAccountTitle);

                if (findBalance.getExistBalance()){
                    throw new WMSServiceException(String.format("无法添加新的子级科目！当前科目的对应父级科目:(%s)中还有余额，如要继续需确认上级科目余额为0，当前科目名称(%s)，",curParentAccountTitleView.getName(), accountTitles[0].getName()));
                }

            });
        }

        int[]ids= accountTitleDAO.add(accountBook, accountTitles);
//        List<FindLinkAccountTitle> findLinkAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,accountTitles);
//        List<FindLinkAccountTitle> findLinkAccountTitleList1=this.accountRecordService.FindSonAccountTitle(accountBook,accountTitles);
        this.updateAllTitle(accountBook);
        return ids;

    }

    @Transactional
    public void update(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException {
        this.validateEntities(accountBook,accountTitles);
        Stream.of(accountTitles).forEach((accountTitle)->{
            if(this.find(accountBook,new Condition().addCondition("name",new String[]{accountTitle.getName()})
                    .addCondition("id",new Integer[]{accountTitle.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("科目名称：" + accountTitle.getName() +"在库存中已经存在!");
            }
            if(this.find(accountBook,new Condition().addCondition("no",new String[]{accountTitle.getNo()})
                    .addCondition("id",new Integer[]{accountTitle.getId()}, ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException("科目编码：" + accountTitle.getNo() +"在库存中已经存在!");
            }
        });

        for(int i=0;i<accountTitles.length;i++){

            AccountTitleView[] oldAccountTitleViews=this.find(accountBook,new Condition().addCondition("id",new Integer[]{accountTitles[i].getId()}));
            AccountTitle[] oldAccountTitles = ReflectHelper.createAndCopyFields(oldAccountTitleViews,AccountTitle.class);
            if (!accountTitles[i].getNo().equals(oldAccountTitleViews[0].getNo()))
            {
                List<FindLinkAccountTitle> findSonAccountTitleList=this.accountRecordService.FindSonAccountTitle(accountBook,new AccountTitle[]{oldAccountTitles[0]});
                FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
                findSonAccountTitleList.toArray(sonAccountTitles);

                List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
                AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
                sonAccountTitleViewsList.toArray(curSonAccountTitleViews);


                //如果旧的编码属于父级科目
                if (curSonAccountTitleViews.length>0){
                    //啥事没有

                }else {

                    //找新的编码是否属于父级科目
                    List<FindLinkAccountTitle> findSonAccountTitleList1 = this.accountRecordService.FindSonAccountTitle(accountBook, new AccountTitle[]{accountTitles[i]});
                    FindLinkAccountTitle[] sonAccountTitles1 = new FindLinkAccountTitle[findSonAccountTitleList1.size()];
                    findSonAccountTitleList1.toArray(sonAccountTitles1);

                    List<AccountTitleView> sonAccountTitleViewsList1 = sonAccountTitles1[0].getAccountTitleViews();
                    AccountTitleView[] curSonAccountTitleViews1 = new AccountTitleView[sonAccountTitleViewsList1.size()];
                    sonAccountTitleViewsList1.toArray(curSonAccountTitleViews1);
                    //如果想变成别的父级科目，则自身余额需要为0

                    FindBalance findBalance=accountRecordService.findNewBalance(accountBook,oldAccountTitles[0]);

                    if (findBalance.getExistBalance()){
                        throw new WMSServiceException(String.format("无法修改为新的上级科目！原科目科目：(%s)还有余额，如要继续修改需确认科目余额为0，当前科目名称(%s)，",oldAccountTitleViews[i].getName(), accountTitles[0].getName()));
                    }

                }
                    //判断是否修改成为子级科目
                    List<FindLinkAccountTitle> findParentAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,new AccountTitle[]{accountTitles[i]});
                    FindLinkAccountTitle[] parentAccountTitles=new FindLinkAccountTitle[findParentAccountTitleList.size()];
                    findParentAccountTitleList.toArray(parentAccountTitles);

                    List<AccountTitleView> parentAccountTitleViewsList= parentAccountTitles[0].getAccountTitleViews();
                    AccountTitleView[] curParentAccountTitleViews=new AccountTitleView[parentAccountTitleViewsList.size()];
                    parentAccountTitleViewsList.toArray(curParentAccountTitleViews);

                    AccountTitle[] curParentAccountTitles = ReflectHelper.createAndCopyFields(curParentAccountTitleViews,AccountTitle.class);

                    Stream.of(curParentAccountTitles).forEach((curParentAccountTitle)->{
                        FindBalance findBalance=accountRecordService.findNewBalance(accountBook,oldAccountTitles[0]);
                        if (findBalance.getExistBalance()){
                            throw new WMSServiceException(String.format("无法修改为新的子级科目！当前科目编码的对应父级科目：(%s)还有余额，如要继续需确认上级科目余额为0，当前科目名称(%s)，",curParentAccountTitle.getName(), accountTitles[0].getName()));
                        }
                    });

            }
        }

        List<FindLinkAccountTitle> findLinkAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,accountTitles);
        List<FindLinkAccountTitle> findLinkAccountTitleList1=this.accountRecordService.FindSonAccountTitle(accountBook,accountTitles);
        accountTitleDAO.update(accountBook, accountTitles);
        this.updateAllTitle(accountBook);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            accountTitleDAO.remove(accountBook, ids);
            this.updateAllTitle(accountBook);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除科目失败，如果科目已经被引用，需要先删除引用内容，才能删除！");
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
            new Validator("科目编码长度").max(18).validate(accountTitle.getNo().length());
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

    public void updateAllTitle(String accountBook) throws WMSServiceException{
        AccountTitle[] accountTitles= this.accountTitleDAO.findTable(accountBook,new Condition());
        List<AccountTitle> updateAccountTitleList =new ArrayList<>();

        Stream.of(accountTitles).forEach((accountTitle)->{
            List<FindLinkAccountTitle> findParentAccountTitleList=this.accountRecordService.FindParentAccountTitle(accountBook,new AccountTitle[]{accountTitle});
            FindLinkAccountTitle[] parentAccountTitles=new FindLinkAccountTitle[findParentAccountTitleList.size()];
            findParentAccountTitleList.toArray(parentAccountTitles);

            List<AccountTitleView> parentAccountTitleViewsList= parentAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curParentAccountTitleViews=new AccountTitleView[parentAccountTitleViewsList.size()];
            parentAccountTitleViewsList.toArray(curParentAccountTitleViews);

            if (curParentAccountTitleViews.length>0) {
                StringBuilder parent=new StringBuilder();
                for (int i = 0; i < curParentAccountTitleViews.length-1; i++) {
                    for (int j = i+1; j < curParentAccountTitleViews.length ; j++) {
                        if (curParentAccountTitleViews[i].getNo().length() > curParentAccountTitleViews[j].getNo().length()) {
                            AccountTitleView temp = curParentAccountTitleViews[j];
                            curParentAccountTitleViews[j] = curParentAccountTitleViews[i];
                            curParentAccountTitleViews[i] = temp;
                        }
                    }
                }

                for (int i = 0; i < curParentAccountTitleViews.length; i++) {
                    parent.append(curParentAccountTitleViews[i].getName());
                    if (i != curParentAccountTitleViews.length - 1) {
                        parent.append(".");
                    }
                }

                if (accountTitle.getAccountTitleDdpendent() != null) {
                    if (!accountTitle.getAccountTitleDdpendent().equals(parent.toString())) {
                        accountTitle.setAccountTitleDdpendent(parent.toString());
                        updateAccountTitleList.add(accountTitle);
                    }
                } else {
                    accountTitle.setAccountTitleDdpendent(parent.toString());
                    updateAccountTitleList.add(accountTitle);
                }
            }else {
                accountTitle.setAccountTitleDdpendent("N/A");
                updateAccountTitleList.add(accountTitle);
            }

        });

        AccountTitle[] updateAccountTitles=new AccountTitle[updateAccountTitleList.size()];
        updateAccountTitleList.toArray(updateAccountTitles);

        this.accountTitleDAO.update(accountBook,updateAccountTitles);
    }
}
