package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountRecordDAO;
import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.services.ledger.datestructures.TreeViewData;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.AccountTitleException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import com.wms.services.ledger.datestructures.SummaryAccountRecord;

import com.wms.services.ledger.datestructures.AccrualCheck;
import com.wms.services.ledger.datestructures.FindBalance;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class AccountRecordServiceImpl implements AccountRecordService{
    @Autowired
    AccountRecordDAO accountRecordDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    AccountPeriodService accountPeriodService;
    @Autowired
    AccountTitleService accountTitleService;
@Autowired
    AccountTitleDAO accountTitleDAO;
    /*
    @Autowired
    AccountRecordService accountRecordService;
*/
    public int[] add(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException
    {
        //获取当前仓库和期间
        int curWarehouseId= accountRecords[0].getWarehouseId();
        int curAccountPeriodId=accountRecords[0].getAccountPeriodId();
        int[] ids=new int[accountRecords.length];
        //数据验证
        this.validateEntities(accountBook,accountRecords);
        //设置时间
        Stream.of(accountRecords).forEach((accountRecord)->{
            accountRecord.setRecordingTime(new Timestamp(System.currentTimeMillis()));
        });
        //创建列表存放可能要更改余额的父级科目
        List<AccountRecord> updateParentRecordList=new ArrayList<>();


        for (int k=0;k<accountRecords.length;k++){
            //取得输入的发生额
            BigDecimal creditAmount = accountRecords[k].getCreditAmount();
            BigDecimal debitAmount = accountRecords[k].getDebitAmount();

            //先判断能不能往下走
            if ((creditAmount.compareTo(new BigDecimal(0)) != 0
                    || debitAmount.compareTo(new BigDecimal(0)) != 0)
                    && accountRecords[k].getOwnBalance().compareTo(BigDecimal.ZERO) != 0
                    && accountRecords[k].getOtherBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException(String.format("无法添加账目记录！输入发生额时请勿输入余额！凭证信息(%s)，", accountRecords[0].getVoucherInfo()));
            }
            if ((creditAmount.compareTo(new BigDecimal(0)) == 0
                    && debitAmount.compareTo(new BigDecimal(0)) == 0)
                    && accountRecords[k].getOwnBalance().compareTo(BigDecimal.ZERO) != 0
                    && accountRecords[k].getOtherBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException(String.format("无法添加账目记录！当前未录入非零发生额/余额！凭证信息(%s)，", accountRecords[0].getVoucherInfo()));
            }

            if (accountRecords[k].getOtherAccountTitleId()!=null&&accountRecords[k].getOtherAccountTitleId()!=-1) {
                //TODO 这里判断有对方科目，即为业务记录

                if (creditAmount.compareTo(new BigDecimal(0)) == 0
                        && debitAmount.compareTo(new BigDecimal(0)) == 0) {
                    throw new WMSServiceException(String.format("业务记录未输入业务发生额，请检查后重新录入！凭证信息(%s)，", accountRecords[0].getVoucherInfo()));
                }
                if (accountRecords[k].getOwnBalance().compareTo(BigDecimal.ZERO) != 0
                        || accountRecords[k].getOtherBalance().compareTo(BigDecimal.ZERO) != 0) {
                    throw new WMSServiceException(String.format("业务记录无需输入余额，凭证信息(%s)，", accountRecords[0].getVoucherInfo()));
                }

                //BigDecimal thisBalance=accountRecords[k].getOBalance();
                //找到对应的己方科目
                AccountTitleView[] ownAccountTitleViews = this.accountTitleService.find(accountBook, new Condition().addCondition("id", accountRecords[k].getOwnAccountTitleId()));
                AccountTitleView ownAccountTitleView = ownAccountTitleViews[0];
                AccountTitle[] ownAccountTitles = ReflectHelper.createAndCopyFields(ownAccountTitleViews, AccountTitle.class);

                //判断是否存在子级科目，存在的话将不能在当前科目新加科目记录
                List<FindLinkAccountTitle> findSonAccountTitleList = this.FindSonAccountTitle(accountBook, ownAccountTitles);
                FindLinkAccountTitle[] sonAccountTitles = new FindLinkAccountTitle[findSonAccountTitleList.size()];
                findSonAccountTitleList.toArray(sonAccountTitles);
                List<AccountTitleView> sonAccountTitleViewsList = sonAccountTitles[0].getAccountTitleViews();
                AccountTitleView[] curSonAccountTitleViews = new AccountTitleView[sonAccountTitleViewsList.size()];
                sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
                if (curSonAccountTitleViews.length > 0) {
                    throw new WMSServiceException(String.format("无法添加明细记录！当前己方科目存在子级科目，请在子级科目下记录，当前科目名称(%s)，", ownAccountTitles[0].getName()));
                }

                //找到对应的对方科目
                AccountTitleView[] otherAccountTitleViews = this.accountTitleService.find(accountBook, new Condition().addCondition("id", accountRecords[k].getOtherAccountTitleId()));
                AccountTitleView otherAccountTitleView = otherAccountTitleViews[0];
                AccountTitle[] otherAccountTitles = ReflectHelper.createAndCopyFields(otherAccountTitleViews, AccountTitle.class);

                //判断是否存在子级科目，存在的话将不能在当前科目新加科目记录
                List<FindLinkAccountTitle> findSonAccountTitleList1 = this.FindSonAccountTitle(accountBook, otherAccountTitles);
                FindLinkAccountTitle[] sonAccountTitles1 = new FindLinkAccountTitle[findSonAccountTitleList1.size()];
                findSonAccountTitleList1.toArray(sonAccountTitles1);
                List<AccountTitleView> sonAccountTitleViewsList1 = sonAccountTitles1[0].getAccountTitleViews();
                AccountTitleView[] curSonAccountTitleViews1 = new AccountTitleView[sonAccountTitleViewsList1.size()];
                sonAccountTitleViewsList1.toArray(curSonAccountTitleViews1);
                if (curSonAccountTitleViews1.length > 0) {
                    throw new WMSServiceException(String.format("无法添加明细记录！当前对方科目存在子级科目，请在子级科目下记录，当前科目名称(%s)，", otherAccountTitles[0].getName()));
                }

                AccountRecord[] oldAccountRecords = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("ownAccountTitleId", new Integer[]{accountRecords[k].getOwnAccountTitleId()}));

                AccountRecord[] oldAccountRecords1 = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("otherAccountTitleId", new Integer[]{accountRecords[k].getOwnAccountTitleId()}));

                AccountRecord[] oldAccountRecords2 = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("ownAccountTitleId", new Integer[]{accountRecords[k].getOtherAccountTitleId()}));

                AccountRecord[] oldAccountRecords3 = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("otherAccountTitleId", new Integer[]{accountRecords[k].getOtherAccountTitleId()}));

                //TODO 如果都有历史纪录有就存上，没有就看是不是有实际发生额，有的话要提示还没有录入期初余额
                if ((oldAccountRecords.length > 0 || oldAccountRecords1.length > 0)
                        && (oldAccountRecords2.length > 0 || oldAccountRecords3.length > 0)) {

                    BigDecimal curOwnBalance = BigDecimal.ZERO;
                    if (oldAccountRecords.length > 0) {
                        AccountRecord newestAccountRecord = oldAccountRecords[0];
                        for (int i = 0; i < oldAccountRecords.length; i++) {
                            if (oldAccountRecords[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = oldAccountRecords[i];
                            }
                        }
                        curOwnBalance = newestAccountRecord.getOwnBalance();
                        if (oldAccountRecords1.length > 0) {
                            for (int i = 0; i < oldAccountRecords1.length; i++) {
                                if (oldAccountRecords1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                    newestAccountRecord = oldAccountRecords[i];
                                }
                            }
                            curOwnBalance = newestAccountRecord.getOtherBalance();
                        }
                    }

                    //如果科目类型是借方
                    if (ownAccountTitleView.getDirection() == AccountTitleService.Debit) {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(creditAmount).add(debitAmount));
                    } else {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(debitAmount).add(creditAmount));
                    }

                    BigDecimal curOtherBalance = BigDecimal.ZERO;
                    if (oldAccountRecords2.length > 0) {
                        AccountRecord newestAccountRecord1 = oldAccountRecords2[0];
                        for (int i = 0; i < oldAccountRecords.length; i++) {
                            if (oldAccountRecords2[i].getRecordingTime().after(newestAccountRecord1.getRecordingTime())) {
                                newestAccountRecord1 = oldAccountRecords[i];
                            }
                        }
                        curOtherBalance = newestAccountRecord1.getOwnBalance();
                        if (oldAccountRecords3.length > 0) {
                            for (int i = 0; i < oldAccountRecords1.length; i++) {
                                if (oldAccountRecords3[i].getRecordingTime().after(newestAccountRecord1.getRecordingTime())) {
                                    newestAccountRecord1 = oldAccountRecords[i];
                                }
                            }
                            curOtherBalance = newestAccountRecord1.getOtherBalance();
                        }
                    }

                    //如果科目类型是借方
                    if (otherAccountTitleView.getDirection() == AccountTitleService.Debit) {
                        accountRecords[k].setOtherBalance(curOtherBalance.subtract(debitAmount).add(creditAmount));
                    } else {
                        accountRecords[k].setOtherBalance(curOtherBalance.subtract(creditAmount).add(debitAmount));
                    }
                }
                else if ((oldAccountRecords.length ==0 &&oldAccountRecords1.length ==0)){
                    //这里排除掉己方科目没有历史纪录/期初余额的情况
                    throw new WMSServiceException(String.format("当前业务记录的己方科目没有余额信息，请检查后重新录入！凭证信息(%s)，", accountRecords[0].getVoucherInfo()));
                }
                else {
                    //己方科目有历史余额，对方科目没有任何历史记录的情况

                    BigDecimal curOwnBalance = BigDecimal.ZERO;
                    if (oldAccountRecords.length > 0) {
                        AccountRecord newestAccountRecord = oldAccountRecords[0];
                        for (int i = 0; i < oldAccountRecords.length; i++) {
                            if (oldAccountRecords[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = oldAccountRecords[i];
                            }
                        }
                        curOwnBalance = newestAccountRecord.getOwnBalance();
                        if (oldAccountRecords1.length > 0) {
                            for (int i = 0; i < oldAccountRecords1.length; i++) {
                                if (oldAccountRecords1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                    newestAccountRecord = oldAccountRecords[i];
                                }
                            }
                            curOwnBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                    //如果科目类型是借方
                    if (ownAccountTitleView.getDirection() == AccountTitleService.Debit) {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(creditAmount).add(debitAmount));
                    } else {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(debitAmount).add(creditAmount));
                    }

                    BigDecimal curBalance = new BigDecimal(0);

                    //如果科目类型是借方
                    if (otherAccountTitleView.getDirection() == AccountTitleService.Debit) {

                        accountRecords[k].setOtherBalance(curBalance.subtract(debitAmount).add(creditAmount));
                    } else {
                        accountRecords[k].setOtherBalance(curBalance.subtract(creditAmount).add(debitAmount));
                    }

                }


                ids[k] = accountRecordDAO.add(accountBook, new AccountRecord[]{accountRecords[k]})[0];
            }
            else{
                //todo 这里是期初余额唯一的录入方式
                if (accountRecords[k].getOtherBalance()!= null) {
                    if (!accountRecords[k].getOtherBalance().equals(BigDecimal.ZERO)) {
                        throw new WMSServiceException(String.format("无法添加明细记录！当前未输入对方科目，请勿在输入对方科目余额，凭证信息(%s)，", accountRecords[k].getVoucherInfo()));
                    }
                }
                //找到对应的己方科目
                AccountTitleView[] ownAccountTitleViews = this.accountTitleService.find(accountBook, new Condition().addCondition("id", accountRecords[k].getOwnAccountTitleId()));
                AccountTitleView ownAccountTitleView = ownAccountTitleViews[0];
                AccountTitle[] ownAccountTitles = ReflectHelper.createAndCopyFields(ownAccountTitleViews, AccountTitle.class);

                //判断是否存在子级科目，存在的话将不能在当前科目新加科目记录
                List<FindLinkAccountTitle> findSonAccountTitleList = this.FindSonAccountTitle(accountBook, ownAccountTitles);
                FindLinkAccountTitle[] sonAccountTitles = new FindLinkAccountTitle[findSonAccountTitleList.size()];
                findSonAccountTitleList.toArray(sonAccountTitles);
                List<AccountTitleView> sonAccountTitleViewsList = sonAccountTitles[0].getAccountTitleViews();
                AccountTitleView[] curSonAccountTitleViews = new AccountTitleView[sonAccountTitleViewsList.size()];
                sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
                if (curSonAccountTitleViews.length > 0) {
                    throw new WMSServiceException(String.format("无法添加明细记录！当前己方科目存在子级科目，请在子级科目下记录，当前科目名称(%s)，", ownAccountTitles[0].getName()));
                }

                AccountRecord[] oldAccountRecords = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("ownAccountTitleId", new Integer[]{accountRecords[k].getOwnAccountTitleId()}));

                AccountRecord[] oldAccountRecords1 = accountRecordDAO.findTable(accountBook, new Condition()
                        .addCondition("warehouseId", new Integer[]{accountRecords[k].getWarehouseId()})
                        .addCondition("accountPeriodId", new Integer[]{accountRecords[k].getAccountPeriodId()})
                        .addCondition("otherAccountTitleId", new Integer[]{accountRecords[k].getOwnAccountTitleId()}));

                if ((oldAccountRecords.length > 0 || oldAccountRecords1.length > 0)) {

                    if (accountRecords[k].getOwnBalance() != null) {
                        if (!accountRecords[k].getOwnBalance().equals(BigDecimal.ZERO)) {
                            throw new WMSServiceException(String.format("无法添加明细记录！当前己方科目存在余额，请勿在输入己方科目余额，当前科目名称(%s)，", ownAccountTitles[0].getName()));
                        }
                    }

                    BigDecimal curOwnBalance = BigDecimal.ZERO;
                    if (oldAccountRecords.length > 0) {
                        AccountRecord newestAccountRecord = oldAccountRecords[0];
                        for (int i = 0; i < oldAccountRecords.length; i++) {
                            if (oldAccountRecords[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = oldAccountRecords[i];
                            }
                        }
                        curOwnBalance = newestAccountRecord.getOwnBalance();
                        if (oldAccountRecords1.length > 0) {
                            for (int i = 0; i < oldAccountRecords1.length; i++) {
                                if (oldAccountRecords1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                    newestAccountRecord = oldAccountRecords[i];
                                }
                            }
                            curOwnBalance = newestAccountRecord.getOtherBalance();
                        }
                    }

                    //如果科目类型是借方
                    if (ownAccountTitleView.getDirection() == AccountTitleService.Debit) {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(creditAmount).add(debitAmount));
                    } else {
                        accountRecords[k].setOwnBalance(curOwnBalance.subtract(debitAmount).add(creditAmount));
                    }
                }

                ids[k] = accountRecordDAO.add(accountBook, new AccountRecord[]{accountRecords[k]})[0];
            }
        }
        AccountRecord[] updateParentRecords=new AccountRecord[updateParentRecordList.size()];
        updateParentRecordList.toArray(updateParentRecords);
        if(updateParentRecords.length>0)
        {
            this.accountRecordDAO.update(accountBook,updateParentRecords);
        }
        return ids;
    }



    public int[] simpleAdd(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException
    {
        //数据验证
        this.validateEntities(accountBook,accountRecords);
        //设置时间
        Stream.of(accountRecords).forEach((accountRecord)->{
            //accountRecord.setTime(new Timestamp(System.currentTimeMillis()));
        });

        return accountRecordDAO.add(accountBook,accountRecords);
    }

    @Transactional
    public void update(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException{

        this.validateEntities(accountBook,accountRecords);
        for (int k=0;k<accountRecords.length;k++){

            AccountRecordView[] oidAccountTitleViews=this.find(accountBook,new Condition().addCondition("id",accountRecords[k].getId()));

            if(oidAccountTitleViews[0].getCreditAmount().compareTo(accountRecords[k].getCreditAmount())!=0
                    ||oidAccountTitleViews[0].getDebitAmount().compareTo(accountRecords[k].getDebitAmount())!=0
                    ||oidAccountTitleViews[0].getOwnBalance().compareTo(accountRecords[k].getOwnBalance())!=0
                    ||oidAccountTitleViews[0].getOtherBalance().compareTo(accountRecords[k].getOtherBalance())!=0
                    ||oidAccountTitleViews[0].getOwnAccountTitleId()!=accountRecords[k].getOwnAccountTitleId()
                    ||oidAccountTitleViews[0].getOtherAccountTitleId()!=accountRecords[k].getOtherAccountTitleId()){
                throw new WMSServiceException(String.format("无法修改账目记录！发生额/余额/科目名称无法修改！如需订正，请进行冲销操作，凭证信息(%s)", oidAccountTitleViews[0].getVoucherInfo()));
            }
        }

//        for (int k=0;k<accountRecords.length;k++){
//
//            AccountRecordView[] oidAccountTitleViews=this.find(accountBook,new Condition().addCondition("id",accountRecords[k].getId()));
//
//            BigDecimal oldCreditAmount=oidAccountTitleViews[0].getCreditAmount();
//            BigDecimal oldDebitAmount=oidAccountTitleViews[0].getDebitAmount();
//
//            BigDecimal creditAmount=accountRecords[k].getCreditAmount();
//            BigDecimal debitAmount=accountRecords[k].getDebitAmount();
//
//            AccountTitleView[] AccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",accountRecords[k].getAccountTitleId()));
//            AccountTitleView accountTitleView=AccountTitleViews[0];
//
//            //如果科目类型是借方
//            if (accountTitleView.getDirection()==AccountTitleService.Debit){
//                accountRecords[k].setBalance(oidAccountTitleViews[0].getBalance().subtract(oldDebitAmount).add(oldCreditAmount).subtract(creditAmount).add(debitAmount));
//
//            }else{
//                accountRecords[k].setBalance(oidAccountTitleViews[0].getBalance().subtract(oldCreditAmount).add(oldDebitAmount).subtract(debitAmount).add(creditAmount));
//            }
//        }

        accountRecordDAO.update(accountBook,accountRecords);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (accountRecordDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除期间不存在，请重新查询！(%d)", id));
            }
        }
        try {
            accountRecordDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除会计期间信息失败，如果会计期间信息已经被引用，需要先删除引用的内容，才能删除会计期间信息！");
        }
    }

    public AccountRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.accountRecordDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.accountRecordDAO.findCount(database,cond);
    }

    private void validateEntities(String accountBook,AccountRecord[] accountRecords) throws WMSServiceException{

        //外键检测
        Stream.of(accountRecords).forEach((accountRecord -> {

            //验证外键
            this.idChecker.check(WarehouseService.class, accountBook, accountRecord.getWarehouseId(), "仓库");
            this.idChecker.check(AccountPeriodService.class, accountBook, accountRecord.getAccountPeriodId(), "会计期间");
            this.idChecker.check(AccountTitleService.class, accountBook, accountRecord.getOwnAccountTitleId(), "科目");
            this.idChecker.check(PersonService.class, accountBook, accountRecord.getPersonId(), "记录人");
            new Validator("贷方金额").notEmpty().validate(accountRecord.getCreditAmount());
            new Validator("借方金额").notEmpty().validate(accountRecord.getDebitAmount());
//            new Validator("余额").notEmpty().validate(accountRecord.getBalance());


            AccountTitleView[] accountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",accountRecord.getOwnAccountTitleId()));
            if (accountTitleViews[0].getEnabled()!=AccountTitleService.ENABLED_ON){
                throw new WMSServiceException(String.format("该科目已被禁用，无法新添加账目记录，科目名称(%s)，", accountTitleViews[0].getName()));
            }

            if (accountRecord.getOtherAccountTitleId()!=null) {
                AccountTitleView[] accountTitleViews1 = this.accountTitleService.find(accountBook, new Condition().addCondition("id", accountRecord.getOtherAccountTitleId()));
                if (accountTitleViews1[0].getEnabled() != AccountTitleService.ENABLED_ON) {
                    throw new WMSServiceException(String.format("该科目已被禁用，无法新添加账目记录，科目名称(%s)，", accountTitleViews[0].getName()));
                }
            }
        }));

        Stream.of(accountRecords).forEach((accountRecord -> {

            if(accountRecord.getCreditAmount().compareTo(BigDecimal.ZERO)!=0
                    &&accountRecord.getDebitAmount().compareTo(BigDecimal.ZERO)!=0){
                throw new WMSServiceException("发生额只能填写借方或者贷方其中一个方向！无法添加账目记录!");
            }

        }));

    }

    @Deprecated
    public void RealTransferAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException
    {
        if (transferAccount.getChangeAmount().compareTo(BigDecimal.ZERO)<0){
            throw new WMSServiceException(String.format("转账申请发生额不能为负数，请重新检查后提交！凭证号：(%s)", transferAccount.getVoucherInfo()));
        }

        AccountTitleView[] OutAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getOutaccountTitleId()));
        AccountTitleView OutAccountTitleView=OutAccountTitleViews[0];
        AccountTitleView[] InAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getInaccountTitleId()));
        AccountTitleView InAccountTitleView=InAccountTitleViews[0];

        AccountRecord accountRecord=new AccountRecord();
        //accountRecord.setAccountTitleId(OutAccountTitleView.getId());
        accountRecord.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord.setPersonId(transferAccount.getPersonId());
        accountRecord.setComment("转账记录");
        accountRecord.setWarehouseId(transferAccount.getWarehouseId());
//        accountRecord.setSummary(transferAccount.getSummary());
        accountRecord.setVoucherInfo(transferAccount.getVoucherInfo());

        AccountRecord accountRecord1=new AccountRecord();
        //accountRecord1.setAccountTitleId(InAccountTitleView.getId());
        accountRecord1.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord1.setPersonId(transferAccount.getPersonId());
        accountRecord1.setComment("转账记录");
        accountRecord1.setWarehouseId(transferAccount.getWarehouseId());
//        accountRecord1.setSummary(transferAccount.getSummary());
        accountRecord1.setVoucherInfo(transferAccount.getVoucherInfo());


        if (OutAccountTitleView.getDirection()==AccountTitleService.Debit){
            accountRecord.setCreditAmount(transferAccount.getChangeAmount());
            accountRecord.setDebitAmount(BigDecimal.ZERO);
            //accountRecord.setBalance(BigDecimal.ZERO);

            accountRecord1.setDebitAmount(transferAccount.getChangeAmount());
            accountRecord1.setCreditAmount(BigDecimal.ZERO);
            //accountRecord1.setBalance(BigDecimal.ZERO);

        }else{
            accountRecord.setDebitAmount(transferAccount.getChangeAmount());
            accountRecord.setCreditAmount(BigDecimal.ZERO);
            //accountRecord.setBalance(BigDecimal.ZERO);

            accountRecord1.setCreditAmount(transferAccount.getChangeAmount());
            accountRecord1.setDebitAmount(BigDecimal.ZERO);
            //accountRecord1.setBalance(BigDecimal.ZERO);

        }
        this.add(accountBook,new AccountRecord[]{accountRecord,accountRecord1});
    }

    public void AddAccountRecord(String accountBook, AccountRecord[] accountRecords)throws WMSServiceException
    {
        BigDecimal allDebitAmount=new BigDecimal(0);
        BigDecimal allCreditAmount=new BigDecimal(0);
        for (int k=0;k<accountRecords.length;k++){

            allDebitAmount=allDebitAmount.add(accountRecords[k].getDebitAmount());
            allCreditAmount=allCreditAmount.add(accountRecords[k].getCreditAmount());
        }

        //总借贷平衡
        if (allDebitAmount.compareTo(allCreditAmount)!=0){
            throw new WMSServiceException(String.format("提交账目记录信息借贷不平，请重新检查后提交！凭证号：(%s)", accountRecords[0].getVoucherInfo()));
        }
        this.add(accountBook,accountRecords);
    }

    public List<FindLinkAccountTitle>  FindParentAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException
    {
        AccountTitle[] allAccountTitles=this.accountTitleDAO.findTable(accountBook,new Condition().addCondition("enabled",AccountTitleService.ENABLED_ON));
        List<FindLinkAccountTitle> result = new ArrayList<>();
        for (int i=0;i<accountTitles.length;i++){

            int curType=accountTitles[i].getType();
            int curDirection=accountTitles[i].getDirection();

            String curAccountTitleName=accountTitles[i].getName();

            FindLinkAccountTitle findLinkAccountTitle = new FindLinkAccountTitle();
            findLinkAccountTitle.setCurAccountTitleNo(accountTitles[i].getNo());
            findLinkAccountTitle.setAccountTitleViews(new ArrayList<>());
            result.add(findLinkAccountTitle);

            List<String> parentAccountTitleNoList=new ArrayList<>();
            String accountTitleNo=accountTitles[i].getNo();
            Stream.of(allAccountTitles).forEach((accountTitle)->{
                if (accountTitleNo.startsWith(accountTitle.getNo())&&!accountTitleNo.equals(accountTitle.getNo())){
                    parentAccountTitleNoList.add(accountTitle.getNo());
                    if (curType!=accountTitle.getType())
                    {
                        throw new WMSServiceException(String.format("科目与上级科目类型不一致，请重新检查后提交！科目名称：(%s)", curAccountTitleName));
                    }
                    if (curDirection!=accountTitle.getDirection())
                    {
                        throw new WMSServiceException(String.format("科目与上级科目余额方向不一致，请重新检查后提交！科目名称：(%s)", curAccountTitleName));
                    }
                }
            });
            String[] parentAccountTitleNos=new String[parentAccountTitleNoList.size()];
            parentAccountTitleNoList.toArray(parentAccountTitleNos);


            for (int j=0;j<parentAccountTitleNos.length;j++) {
                AccountTitleView[] curAccountTitleView = this.accountTitleService.find(accountBook, new Condition().addCondition("no", parentAccountTitleNos[j]));
                if (curAccountTitleView.length==0){
                    throw new WMSServiceException(String.format("上级科目不存在，请重新检查后提交！子科目编码：(%s)", parentAccountTitleNos[j]));
                }
                findLinkAccountTitle.getAccountTitleViews().add(curAccountTitleView[0]);
            }
        }
        return result;
    }

    public List<FindLinkAccountTitle>  FindSonAccountTitle(String accountBook, AccountTitle[] accountTitles)throws WMSServiceException
    {
        AccountTitle[] allAccountTitles=this.accountTitleDAO.findTable(accountBook,new Condition().addCondition("enabled",AccountTitleService.ENABLED_ON));
        List<FindLinkAccountTitle> result = new ArrayList<>();
        for (int i=0;i<accountTitles.length;i++){

            int curType=accountTitles[i].getType();
            int curDirection=accountTitles[i].getDirection();
            String curAccountTitleName=accountTitles[i].getName();

            FindLinkAccountTitle findLinkAccountTitle = new FindLinkAccountTitle();
            findLinkAccountTitle.setCurAccountTitleNo(accountTitles[i].getNo());
            findLinkAccountTitle.setAccountTitleViews(new ArrayList<>());
            result.add(findLinkAccountTitle);

            List<String> parentAccountTitleNoList=new ArrayList<>();
            String accountTitleNo=accountTitles[i].getNo();
            Stream.of(allAccountTitles).forEach((accountTitle)->{
                if (accountTitle.getNo().startsWith(accountTitleNo)&&!accountTitleNo.equals(accountTitle.getNo())){
                    parentAccountTitleNoList.add(accountTitle.getNo());
                    if (curType!=accountTitle.getType())
                    {
                        throw new WMSServiceException(String.format("科目与子级科目类型不一致，请重新检查后提交！科目名称：(%s)", curAccountTitleName));
                    }
                    if (curDirection!=accountTitle.getDirection())
                    {
                        throw new WMSServiceException(String.format("科目与子级科目余额方向不一致，请重新检查后提交！科目名称：(%s)", curAccountTitleName));
                    }
                }
            });
            String[] parentAccountTitleNos=new String[parentAccountTitleNoList.size()];
            parentAccountTitleNoList.toArray(parentAccountTitleNos);


            for (int j=0;j<parentAccountTitleNos.length;j++) {
                AccountTitleView[] curAccountTitleView = this.accountTitleService.find(accountBook, new Condition().addCondition("no", parentAccountTitleNos[j]));
                if (curAccountTitleView.length==0){
                    throw new WMSServiceException(String.format("子级科目不存在，请重新检查后提交！科目编码：(%s)", parentAccountTitleNos[j]));
                }
                findLinkAccountTitle.getAccountTitleViews().add(curAccountTitleView[0]);
            }
        }
        return result;
    }

    @Override
    public void writeOff(String accountBook,List<Integer> ids) throws WMSServiceException{
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个账目记录！");
        }

        List<AccountRecord> accountRecordList=new ArrayList();
        AccountRecordView[] accountRecordViews = this.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));



        Stream.of(accountRecordViews).forEach((accountRecordView)->{
            BigDecimal creditAmount=accountRecordView.getCreditAmount();
            BigDecimal debitAmount=accountRecordView.getDebitAmount();

            AccountRecord accountRecord=new AccountRecord();
            accountRecord.setOtherAccountTitleId(accountRecordView.getOtherAccountTitleId());
            accountRecord.setOtherAccountTitleDependence(accountRecordView.getOtherAccountTitleDependence());
            accountRecord.setOwnAccountTitleId(accountRecordView.getOwnAccountTitleId());
            accountRecord.setOwnAccountTitleDependence(accountRecordView.getOwnAccountTitleDependence());

            accountRecord.setVoucherInfo(accountRecordView.getVoucherInfo());
            accountRecord.setWarehouseId(accountRecordView.getWarehouseId());
            accountRecord.setPersonId(accountRecordView.getPersonId());
            accountRecord.setAccountPeriodId(accountRecordView.getAccountPeriodId());
            accountRecord.setServiceTime(accountRecordView.getServiceTime());
            accountRecord.setRecordingTime(new Timestamp(System.currentTimeMillis()));
            accountRecord.setSummary(accountRecordView.getSummary());
            accountRecord.setOwnBalance(BigDecimal.ZERO);
            accountRecord.setOtherBalance(BigDecimal.ZERO);

            accountRecord.setDebitAmount(creditAmount);
            accountRecord.setCreditAmount(debitAmount);
            accountRecord.setComment("冲销账目");

            accountRecordList.add(accountRecord);
        });
        this.add(accountBook,accountRecordList.toArray(new AccountRecord[accountRecordList.size()]));
    }

    @Override
    public List<AccrualCheck> accrualCheck(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException{
        int curWarehouseId=accrualCheck.getWarehouseId();
        int curPersonId=accrualCheck.getPersonId();
        int curAccountPeriodId=accrualCheck.getCurAccountPeriodId();
        List<AccrualCheck> returnAccrualCheckList=new ArrayList();

        AccountRecordView[] accountRecordViews= this.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));
        if (accountRecordViews.length<=0)
        {
            throw new WMSServiceException("当前期间此仓库中无账目记录!");
        }

        BigDecimal creditAmount=new BigDecimal(0);
        BigDecimal debitAmount=new BigDecimal(0);
        for (int i=0;i<accountRecordViews.length;i++){
            creditAmount=creditAmount.add(accountRecordViews[i].getCreditAmount());
            debitAmount=debitAmount.add(accountRecordViews[i].getDebitAmount());
        }
        AccrualCheck returnAccrualCheck=new AccrualCheck();
        returnAccrualCheck.setCreditAmount(creditAmount);
        returnAccrualCheck.setDebitAmount(debitAmount);
        returnAccrualCheckList.add(returnAccrualCheck);
        return returnAccrualCheckList;
    }

    @Override
    public List<AccrualCheck> deficitCheck(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException{
        //赤字提醒
        int curWarehouseId=accrualCheck.getWarehouseId();
        int curAccountPeriodId=accrualCheck.getCurAccountPeriodId();
//        List<AccountRecordView> returnAccountRecordViewList=new ArrayList();
        List<AccrualCheck> returnAccrualCheckList=new ArrayList();

        AccountRecordView[] tdAccountRecordViews= this.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{curWarehouseId}).addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));
        if (tdAccountRecordViews.length<=0)
        {
            throw new WMSServiceException("当前期间此仓库中无账目记录.");
        }

        AccountTitleView[] accountTitleViews =this.accountTitleService.find(accountBook,new Condition());


        for (int k=0;k<accountTitleViews.length;k++) {
            int curAccountTitleId= accountTitleViews[k].getId();
            BigDecimal balance=BigDecimal.ZERO;

            AccountRecordView[] accountRecordViews = this.find(accountBook, new Condition()
                    .addCondition("warehouseId", new Integer[]{curWarehouseId})
                    .addCondition("ownAccountTitleId", new Integer[]{curAccountTitleId})
                    .addCondition("accountPeriodId", new Integer[]{curAccountPeriodId}));

            AccountRecordView[] accountRecordViews1 = this.find(accountBook, new Condition()
                    .addCondition("warehouseId", new Integer[]{curWarehouseId})
                    .addCondition("otherAccountTitleId", new Integer[]{curAccountTitleId})
                    .addCondition("accountPeriodId", new Integer[]{curAccountPeriodId}));

            if (accountRecordViews.length > 0 || accountRecordViews1.length > 0) {
                //存在历史纪录
                if (accountRecordViews.length > 0) {
                    //己方科目
                    AccountRecordView newestAccountRecord = accountRecordViews[0];
                    for (int i = 0; i < accountRecordViews.length; i++) {
                        if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews[i];
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                    if (accountRecordViews1.length > 0) {
                        for (int i = 0; i < accountRecordViews1.length; i++) {
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                    }
                    balance=curBalance;
                } else {
                    //仅存在对方科目记录
                    AccountRecordView newestAccountRecord = accountRecordViews1[0];
                    BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                    for (int i = 0; i < accountRecordViews1.length; i++) {
                        if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews1[i];
                            curBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                    balance=curBalance;
                }
            }
            if (balance.compareTo(BigDecimal.ZERO)<0) {
                AccrualCheck returnAccrualCheck=new AccrualCheck();
                returnAccrualCheck.setBalance(balance);
                returnAccrualCheck.setCurAccountTitleName(accountTitleViews[k].getName());
                returnAccrualCheckList.add(returnAccrualCheck);          
            }
        }
//        //按科目分组
//        Map<Integer, List<AccountRecordView>> groupByAccountTitleId =
//                Stream.of(tdAccountRecordViews).collect(Collectors.groupingBy(AccountRecordView::getAccountPeriodId));
//
//        Iterator<Map.Entry<Integer,List<AccountRecordView>>> entries = groupByAccountTitleId.entrySet().iterator();
//        //将每组最新的加到一个列表中
//        while (entries.hasNext()) {
//            Map.Entry<Integer, List<AccountRecordView>> entry = entries.next();
//            Integer accountTitleId = entry.getKey();
//            List<AccountRecordView> accountRecordViewsList=entry.getValue();
//            AccountRecordView[] curAccountRecordViews=(AccountRecordView[]) Array.newInstance(AccountRecordView.class,accountRecordViewsList.size());
//            accountRecordViewsList.toArray(curAccountRecordViews);
//
//            AccountRecordView newestAccountRecordView=curAccountRecordViews[0];
//            for (int i=0;i<curAccountRecordViews.length;i++){
//                if (curAccountRecordViews[i].getServiceTime().after(newestAccountRecordView.getServiceTime())) {
//                    newestAccountRecordView = curAccountRecordViews[i];
//                }
//            }
//
//            if (newestAccountRecordView.getOwnBalance().compareTo(BigDecimal.ZERO)<0) {
//                returnAccountRecordViewList.add(newestAccountRecordView);
//            }
//        }


        return returnAccrualCheckList;
    }

    public FindBalance findNewBalance(String accountBook,AccountTitle accountTitle,Integer curWarehouseId) throws WMSServiceException {
        BigDecimal balance=BigDecimal.ZERO;

        int curAccountTitleId=accountTitle.getId();
        AccountRecordView[] accountRecordViews = this.find(accountBook, new Condition()
                .addCondition("warehouseId", new Integer[]{curWarehouseId})
                .addCondition("ownAccountTitleId", new Integer[]{curAccountTitleId}));

        AccountRecordView[] accountRecordViews1 = this.find(accountBook, new Condition()
                .addCondition("warehouseId", new Integer[]{curWarehouseId})
                .addCondition("otherAccountTitleId", new Integer[]{curAccountTitleId}));

        if (accountRecordViews.length > 0 || accountRecordViews1.length > 0) {
            //存在历史纪录
            if (accountRecordViews.length > 0) {
                //己方科目
                AccountRecordView newestAccountRecord = accountRecordViews[0];
                for (int i = 0; i < accountRecordViews.length; i++) {
                    if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                        newestAccountRecord = accountRecordViews[i];
                    }
                }
                BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                if (accountRecordViews1.length > 0) {
                    for (int i = 0; i < accountRecordViews1.length; i++) {
                        if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews1[i];
                            curBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                }
                balance=curBalance;
            } else {
                //仅存在对方科目记录
                AccountRecordView newestAccountRecord = accountRecordViews1[0];
                BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                for (int i = 0; i < accountRecordViews1.length; i++) {
                    if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                        newestAccountRecord = accountRecordViews1[i];
                        curBalance = newestAccountRecord.getOtherBalance();
                    }
                }
                balance=curBalance;
            }
        }
        FindBalance findBalance=new FindBalance();
        if (balance.compareTo(BigDecimal.ZERO)>0) {
            findBalance.setExistBalance(true);
            findBalance.setBalance(balance);
        }
        return findBalance;
    }

    public FindBalance findNewBalance(String accountBook,AccountTitle accountTitle) throws WMSServiceException {

        FindBalance findBalance=new FindBalance();

        BigDecimal returnBalance=BigDecimal.ZERO;
        WarehouseView[] warehouseViews= warehouseService.find(accountBook,new Condition());
        int curAccountTitleId=accountTitle.getId();
        for(int j=0;j<warehouseViews.length;j++){
            BigDecimal balance=BigDecimal.ZERO;
            AccountRecordView[] accountRecordViews = this.find(accountBook, new Condition()
                    .addCondition("warehouseId", new Integer[]{warehouseViews[j].getId()})
                    .addCondition("ownAccountTitleId", new Integer[]{curAccountTitleId}));

            AccountRecordView[] accountRecordViews1 = this.find(accountBook, new Condition()
                    .addCondition("warehouseId", new Integer[]{warehouseViews[j].getId()})
                    .addCondition("otherAccountTitleId", new Integer[]{curAccountTitleId}));

            if (accountRecordViews.length > 0 || accountRecordViews1.length > 0) {
                //存在历史纪录
                if (accountRecordViews.length > 0) {
                    //己方科目
                    AccountRecordView newestAccountRecord = accountRecordViews[0];
                    for (int i = 0; i < accountRecordViews.length; i++) {
                        if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews[i];
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                    if (accountRecordViews1.length > 0) {
                        for (int i = 0; i < accountRecordViews1.length; i++) {
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                    }
                    balance=curBalance;
                } else {
                    //仅存在对方科目记录
                    AccountRecordView newestAccountRecord = accountRecordViews1[0];
                    BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                    for (int i = 0; i < accountRecordViews1.length; i++) {
                        if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews1[i];
                            curBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                    balance=curBalance;
                }
            }

            if (balance.compareTo(BigDecimal.ZERO)>0) {
                findBalance.setExistBalance(true);
                findBalance.setBalance(balance);
            }
        }

        return findBalance;
    }

    @Override
    public List<AccrualCheck> showBalance(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException{

        int curWarehouseId=accrualCheck.getWarehouseId();
        int curAccountPeriodId=accrualCheck.getCurAccountPeriodId();
        int curAccountTitleId= accrualCheck.getCurAccountTitleId();
        List<AccrualCheck> returnAccrualCheckList=new ArrayList();
        AccrualCheck accrualCheck1=new AccrualCheck();


        AccountTitleView[] accountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",new Integer[]{curAccountTitleId}));
        AccountTitle[] accountTitles = ReflectHelper.createAndCopyFields(accountTitleViews,AccountTitle.class);

        //判断是否存在子级科目
        List<FindLinkAccountTitle> findSonAccountTitleList=this.FindSonAccountTitle(accountBook,accountTitles);
        FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
        findSonAccountTitleList.toArray(sonAccountTitles);
        List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
        AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
        sonAccountTitleViewsList.toArray(curSonAccountTitleViews);

        //没有子代就把自己科目最新的余额返回
        if (curSonAccountTitleViews.length==0){
            AccountRecordView[] accountRecordViews= this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{curWarehouseId})
                    .addCondition("ownAccountTitleId",new Integer[]{curAccountTitleId})
                    .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));

            AccountRecordView[] accountRecordViews1= this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{curWarehouseId})
                    .addCondition("otherAccountTitleId",new Integer[]{curAccountTitleId})
                    .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));

            if(accountRecordViews.length>0||accountRecordViews1.length>0) {
                //存在历史纪录
                if (accountRecordViews.length > 0) {
                    //己方科目
                    AccountRecordView newestAccountRecord = accountRecordViews[0];
                    for (int i = 0; i < accountRecordViews.length; i++) {
                        if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews[i];
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                    if (accountRecordViews1.length > 0) {
                        for (int i = 0; i < accountRecordViews1.length; i++) {
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                    }
                    accrualCheck1.setBalance(curBalance);
                }else{
                    //仅存在对方科目记录
                    AccountRecordView newestAccountRecord = accountRecordViews1[0];
                    BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                    for (int i = 0; i < accountRecordViews1.length; i++) {
                        if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews1[i];
                            curBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                    accrualCheck1.setBalance(curBalance);
                }
            }
            else{
                accrualCheck1.setBalance(BigDecimal.ZERO);
            }
        }else{
            //科目为上级科目，统计下级科目综合
            BigDecimal sumBalance=BigDecimal.ZERO;

            for(int k=0;k<curSonAccountTitleViews.length;k++){
                AccountRecordView[] accountRecordViews= this.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("ownAccountTitleId",new Integer[]{curSonAccountTitleViews[k].getId()})
                        .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));

                AccountRecordView[] accountRecordViews1= this.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("otherAccountTitleId",new Integer[]{curSonAccountTitleViews[k].getId()})
                        .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId}));

                if(accountRecordViews.length>0||accountRecordViews1.length>0) {
                    //存在历史纪录
                    if (accountRecordViews.length > 0) {
                        //己方科目
                        AccountRecordView newestAccountRecord = accountRecordViews[0];
                        for (int i = 0; i < accountRecordViews.length; i++) {
                            if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews[i];
                            }
                        }
                        BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                        if (accountRecordViews1.length > 0) {
                            for (int i = 0; i < accountRecordViews1.length; i++) {
                                if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                    newestAccountRecord = accountRecordViews1[i];
                                    curBalance = newestAccountRecord.getOtherBalance();
                                }
                            }
                        }
                        sumBalance=sumBalance.add(curBalance);
                    }else{
                        //仅存在对方科目记录
                        AccountRecordView newestAccountRecord = accountRecordViews1[0];
                        BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                        for (int i = 0; i < accountRecordViews1.length; i++) {
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                        sumBalance=sumBalance.add(curBalance);
                    }
                }
                else{
                    sumBalance=sumBalance.add(BigDecimal.ZERO);
                }
            }
            accrualCheck1.setBalance(sumBalance);
        }
        returnAccrualCheckList.add(accrualCheck1);
        return returnAccrualCheckList;

    }

    @Override
    public List<TreeViewData> buildAccountTitleTreeView(String accountBook) throws WMSServiceException{

        AccountTitleView[] accountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("enabled",1));

        for (int i=0;i<accountTitleViews.length;i++){
            accountTitleViews[i].setDirection(accountTitleViews[i].getNo().length());
        }

        List<AccountTitleView> accountTitleViewList= Arrays.asList(accountTitleViews);
        List<AccountTitleView> newAccountTitleViewList =accountTitleViewList.stream().sorted(Comparator.comparing(AccountTitleView::getDirection)).collect(Collectors.toList());
        AccountTitleView[] newAccountTitleViews =new AccountTitleView[newAccountTitleViewList.size()];
        newAccountTitleViewList.toArray(newAccountTitleViews);

        //替换成新的数组类型
        TreeViewData[] treeViewDatas =new TreeViewData[newAccountTitleViews.length+1];

        for (int i=0;i<treeViewDatas.length;i++){
            if(i==0){
                TreeViewData treeViewData =new TreeViewData();
                treeViewData.setAccountTitleNo("全部科目");
                treeViewData.setAccountTitleName("全部科目");
                treeViewData.setAccountTitleId(0);
                treeViewData.setParentAccountTitleId(0);
                treeViewDatas[i]=treeViewData;
            }
            if(i>0){
                TreeViewData treeViewData =new TreeViewData();
                treeViewData.setAccountTitleNo(newAccountTitleViews[i-1].getNo());
                treeViewData.setAccountTitleName(newAccountTitleViews[i-1].getName());
                treeViewData.setAccountTitleId(i);
                treeViewData.setParentAccountTitleId(0);
                treeViewDatas[i]=treeViewData;
            }

        }

        //合理编码
        for (int i=0;i<treeViewDatas.length;i++){
            String theNo=treeViewDatas[i].getAccountTitleNo();
            for (int j=i;j<treeViewDatas.length;j++){
                //如果后面有编码是以该编码开头，则替换掉PID
                if (treeViewDatas[j].getAccountTitleNo().startsWith(theNo)
                        &&!treeViewDatas[j].getAccountTitleNo().equals(theNo)){
                    treeViewDatas[j].setParentAccountTitleId(treeViewDatas[i].getAccountTitleId());
                }
            }
        }
        List<TreeViewData> returnAccountTitleTreeViewList=Arrays.asList(treeViewDatas);

        return returnAccountTitleTreeViewList;
    }

    public List<SummaryAccountRecord>  summaryAllTitle(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException{

        //必须有仓库，时间段
        if (accrualCheck.getStartTime()==null){
            throw new WMSServiceException("没有输入汇总起始时间！");
        }
        if (accrualCheck.getEndTime()==null){
            throw new WMSServiceException("没有输入汇总截止时间！");
        }
        if (accrualCheck.getEndTime().before(accrualCheck.getStartTime())){
            throw new WMSServiceException("时间输入错误！汇总截止时间不能早于汇总起始时间！");
        }

        AccountTitle[] accountTitles= this.accountTitleDAO.findTable(accountBook,new Condition());
        List<SummaryAccountRecord> summary=new ArrayList();


        Stream.of(accountTitles).forEach((accountTitle)->{
            SummaryAccountRecord summaryAccountRecord=new SummaryAccountRecord();
            summaryAccountRecord.setSummaryTime(accrualCheck.getStartTime());
            summaryAccountRecord.setAccountTitleDependent(accountTitle.getAccountTitleDdpendent());
            summaryAccountRecord.setAccountTitleName(accountTitle.getName());
            summaryAccountRecord.setAccountTitleNo(accountTitle.getNo());
            summaryAccountRecord.setBalance(new ArrayList<>());
            summaryAccountRecord.setCreditAmount(new ArrayList<>());
            summaryAccountRecord.setDebitAmount(new ArrayList<>());

            AccrualCheck checkAccrualCheck=new AccrualCheck();
            checkAccrualCheck.setWarehouseId(accrualCheck.getWarehouseId());
            checkAccrualCheck.setStartTime(accrualCheck.getStartTime());
            checkAccrualCheck.setEndTime(accrualCheck.getEndTime());

            //TODO 这里只查找一个时间段
            AccrualCheck returnAccrualCheck=this.showAccrualBalance(accountBook,checkAccrualCheck);
            summaryAccountRecord.getBalance().add(returnAccrualCheck.getBalance());
            summaryAccountRecord.getCreditAmount().add(returnAccrualCheck.getCreditAmount());
            summaryAccountRecord.getDebitAmount().add(returnAccrualCheck.getDebitAmount());

            summary.add(summaryAccountRecord);
        });

        return summary;
    }


    public AccrualCheck showAccrualBalance(String accountBook,AccrualCheck accrualCheck) throws WMSServiceException{

        //给定时间段，返回余额，发生额
        int curWarehouseId=accrualCheck.getWarehouseId();


        Timestamp startTime=accrualCheck.getStartTime();
        Timestamp endTime=accrualCheck.getEndTime();

        int curAccountTitleId= accrualCheck.getCurAccountTitleId();

        AccrualCheck accrualCheck1=new AccrualCheck();


        AccountTitleView[] accountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",new Integer[]{curAccountTitleId}));
        AccountTitle[] accountTitles = ReflectHelper.createAndCopyFields(accountTitleViews,AccountTitle.class);

        //判断是否存在子级科目
        List<FindLinkAccountTitle> findSonAccountTitleList=this.FindSonAccountTitle(accountBook,accountTitles);
        FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
        findSonAccountTitleList.toArray(sonAccountTitles);
        List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
        AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
        sonAccountTitleViewsList.toArray(curSonAccountTitleViews);

        //没有子代就把自己科目最新的余额返回
        if (curSonAccountTitleViews.length==0){

            BigDecimal sumDebitAmount=BigDecimal.ZERO;
            BigDecimal sumCreditAmount=BigDecimal.ZERO;
            AccountRecordView[] accountRecordViews= this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{curWarehouseId})
                    .addCondition("ownAccountTitleId",new Integer[]{curAccountTitleId})
                    .addCondition("recordingTime",new Timestamp[]{startTime,endTime}, ConditionItem.Relation.BETWEEN));

            AccountRecordView[] accountRecordViews1= this.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{curWarehouseId})
                    .addCondition("otherAccountTitleId",new Integer[]{curAccountTitleId})
                    .addCondition("recordingTime",new Timestamp[]{startTime,endTime}, ConditionItem.Relation.BETWEEN));

            if(accountRecordViews.length>0||accountRecordViews1.length>0) {
                //存在历史纪录
                if (accountRecordViews.length > 0) {
                    //己方科目
                    AccountRecordView newestAccountRecord = accountRecordViews[0];
                    for (int i = 0; i < accountRecordViews.length; i++) {

                        sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getDebitAmount());
                        sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getCreditAmount());
                        if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews[i];
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                    if (accountRecordViews1.length > 0) {
                        for (int i = 0; i < accountRecordViews1.length; i++) {

                            sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getCreditAmount());
                            sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getDebitAmount());
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                    }
                    accrualCheck1.setBalance(curBalance);
                }else{
                    //仅存在对方科目记录
                    AccountRecordView newestAccountRecord = accountRecordViews1[0];
                    BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                    for (int i = 0; i < accountRecordViews1.length; i++) {
                        sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getCreditAmount());
                        sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getDebitAmount());
                        if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                            newestAccountRecord = accountRecordViews1[i];
                            curBalance = newestAccountRecord.getOtherBalance();
                        }
                    }
                    accrualCheck1.setBalance(curBalance);
                }
            }
            else{
                accrualCheck1.setBalance(BigDecimal.ZERO);
            }
            accrualCheck1.setCreditAmount(sumCreditAmount);
            accrualCheck1.setDebitAmount(sumDebitAmount);
        }else{
            //科目为上级科目，统计下级科目综合
            BigDecimal sumBalance=BigDecimal.ZERO;
            BigDecimal sumDebitAmount=BigDecimal.ZERO;
            BigDecimal sumCreditAmount=BigDecimal.ZERO;

            for(int k=0;k<curSonAccountTitleViews.length;k++){
                AccountRecordView[] accountRecordViews= this.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("ownAccountTitleId",new Integer[]{curSonAccountTitleViews[k].getId()})
                        .addCondition("recordingTime",new Timestamp[]{startTime,endTime}, ConditionItem.Relation.BETWEEN));

                AccountRecordView[] accountRecordViews1= this.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("otherAccountTitleId",new Integer[]{curSonAccountTitleViews[k].getId()})
                        .addCondition("recordingTime",new Timestamp[]{startTime,endTime}, ConditionItem.Relation.BETWEEN));

                if(accountRecordViews.length>0||accountRecordViews1.length>0) {
                    //存在历史纪录
                    if (accountRecordViews.length > 0) {
                        //己方科目
                        //找余额
                        AccountRecordView newestAccountRecord = accountRecordViews[0];
                        for (int i = 0; i < accountRecordViews.length; i++) {
                            sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getDebitAmount());
                            sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getCreditAmount());

                            if (accountRecordViews[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews[i];
                            }
                        }
                        BigDecimal curBalance = newestAccountRecord.getOwnBalance();
                        if (accountRecordViews1.length > 0) {
                            for (int i = 0; i < accountRecordViews1.length; i++) {
                                sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getCreditAmount());
                                sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getDebitAmount());

                                if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                    newestAccountRecord = accountRecordViews1[i];
                                    curBalance = newestAccountRecord.getOtherBalance();
                                }
                            }
                        }
                        sumBalance=sumBalance.add(curBalance);

                    }else{
                        //仅存在对方科目记录
                        AccountRecordView newestAccountRecord = accountRecordViews1[0];
                        BigDecimal curBalance = accountRecordViews1[0].getOtherBalance();
                        for (int i = 0; i < accountRecordViews1.length; i++) {
                            sumDebitAmount=sumDebitAmount.add(accountRecordViews[i].getCreditAmount());
                            sumCreditAmount=sumCreditAmount.add(accountRecordViews[i].getDebitAmount());
                            if (accountRecordViews1[i].getRecordingTime().after(newestAccountRecord.getRecordingTime())) {
                                newestAccountRecord = accountRecordViews1[i];
                                curBalance = newestAccountRecord.getOtherBalance();
                            }
                        }
                        sumBalance=sumBalance.add(curBalance);
                    }
                }
                else{
                    sumBalance=sumBalance.add(BigDecimal.ZERO);
                }
            }
            accrualCheck1.setBalance(sumBalance);
            accrualCheck1.setCreditAmount(sumCreditAmount);
            accrualCheck1.setDebitAmount(sumDebitAmount);
        }

        return accrualCheck1;

    }
}

