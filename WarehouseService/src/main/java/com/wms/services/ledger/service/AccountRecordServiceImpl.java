package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountRecordDAO;
import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.datestructures.TransferAccount;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.services.ledger.datestructures.FindLinkAccountTitle;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        //数据验证
        this.validateEntities(accountBook,accountRecords);
        //设置时间
        Stream.of(accountRecords).forEach((accountRecord)->{
            accountRecord.setTime(new Timestamp(System.currentTimeMillis()));
        });
        //创建列表存放可能要更改余额的父级科目
        List<AccountRecord> updateParentRecordList=new ArrayList<>();
        for (int k=0;k<accountRecords.length;k++){
            //取得输入的发生额
            BigDecimal creditAmount=accountRecords[k].getCreditAmount();
            BigDecimal debitAmount=accountRecords[k].getDebitAmount();
            BigDecimal thisBalance=accountRecords[k].getBalance();
            //找到对应的科目
            AccountTitleView[] AccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",accountRecords[k].getAccountTitleId()));
            AccountTitleView accountTitleView=AccountTitleViews[0];
            AccountTitle[] accountTitles = ReflectHelper.createAndCopyFields(AccountTitleViews,AccountTitle.class);

            //判断是否存在子级科目，存在的话将不能在当前科目新加科目记录
            List<FindLinkAccountTitle> findSonAccountTitleList=this.FindSonAccountTitle(accountBook,accountTitles);
            FindLinkAccountTitle[] sonAccountTitles=new FindLinkAccountTitle[findSonAccountTitleList.size()];
            findSonAccountTitleList.toArray(sonAccountTitles);
            List<AccountTitleView> sonAccountTitleViewsList= sonAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curSonAccountTitleViews=new AccountTitleView[sonAccountTitleViewsList.size()];
            sonAccountTitleViewsList.toArray(curSonAccountTitleViews);
            if (curSonAccountTitleViews.length>0
                    &&accountRecords[k].getCreditAmount().compareTo(new BigDecimal(0))!=0
                    &&accountRecords[k].getDebitAmount().compareTo(new BigDecimal(0))!=0){
                throw new WMSServiceException(String.format("无法添加明细记录！当前账目存在子级科目，请在子级科目下记录，当前科目名称(%s)，", accountTitles[0].getName()));
            }

            //TODO 找出当前科目的父代科目
            List<FindLinkAccountTitle> findLinkAccountTitleList=this.FindParentAccountTitle(accountBook,accountTitles);
            FindLinkAccountTitle[] parentAccountTitles=new FindLinkAccountTitle[findLinkAccountTitleList.size()];
            findLinkAccountTitleList.toArray(parentAccountTitles);

            List<AccountTitleView> accountTitleViewsList= parentAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curParentAccountTitleViews=new AccountTitleView[accountTitleViewsList.size()];
            accountTitleViewsList.toArray(curParentAccountTitleViews);

            AccountTitle[] curParentAccountTitles = ReflectHelper.createAndCopyFields(curParentAccountTitleViews,AccountTitle.class);

            //TODO 对当前科目的父代科目最新的账目记录余额进行更新
            Stream.of(curParentAccountTitles).forEach((curParentAccountTitle)->{

                AccountRecordView[] accountRecordViews= accountRecordDAO.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId})
                        .addCondition("accountTitleId",new Integer[]{curParentAccountTitle.getId()}));

                AccountRecord[] allParentAccountRecords = ReflectHelper.createAndCopyFields(accountRecordViews,AccountRecord.class);

                //TODO 如果存在父级科目的账目记录，直接把余额按照借贷方向增/减发生额
                if (allParentAccountRecords.length>0) {
                    AccountRecord newestAccountRecord = new AccountRecord();
                    for (int i = 0; i < allParentAccountRecords.length; i++) {
                        for (int j = 0; j < allParentAccountRecords.length; j++) {
                            if (allParentAccountRecords[i].getTime().after(allParentAccountRecords[j].getTime())) {
                                newestAccountRecord = allParentAccountRecords[i];
                            } else {
                                newestAccountRecord = allParentAccountRecords[j];
                            }
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getBalance();

                    if (creditAmount.compareTo(new BigDecimal(0))==0
                            &&debitAmount.compareTo(new BigDecimal(0))==0){
                        newestAccountRecord.setBalance(curBalance.add(thisBalance));
                    }
                    else{
                    //如果科目类型是借方
                    if (curParentAccountTitle.getDirection() == AccountTitleService.Debit) {
                        newestAccountRecord.setBalance(curBalance.subtract(creditAmount).add(debitAmount));

                    } else {
                        newestAccountRecord.setBalance(curBalance.subtract(debitAmount).add(creditAmount));
                    }
                    }
                    updateParentRecordList.add(newestAccountRecord);
                }
            });

            AccountRecordView[] accountRecordViews= accountRecordDAO.find(accountBook,new Condition()
                    .addCondition("warehouseId",new Integer[]{accountRecords[k].getWarehouseId()})
                    .addCondition("accountPeriodId",new Integer[]{accountRecords[k].getAccountPeriodId()})
                    .addCondition("accountTitleId",new Integer[]{accountRecords[k].getAccountTitleId(),}));

            //如果有就存上，没有就拉倒
            if (accountRecordViews.length>0){
                AccountRecordView newestAccountRecordView=new AccountRecordView();
                for (int i=0;i<accountRecordViews.length;i++){
                    for (int j=0;j<accountRecordViews.length;j++){
                        if (accountRecordViews[i].getTime().after(accountRecordViews[j].getTime())){
                            newestAccountRecordView=accountRecordViews[i];
                        }else{
                            newestAccountRecordView=accountRecordViews[j];
                        }
                    }
                }
                BigDecimal curBalance=newestAccountRecordView.getBalance();
                //如果科目类型是借方
                if (accountTitleView.getDirection()==AccountTitleService.Debit){
                    accountRecords[k].setBalance(curBalance.subtract(creditAmount).add(debitAmount));
                }else{
                    accountRecords[k].setBalance(curBalance.subtract(debitAmount).add(creditAmount));
                }
            }

        }
        AccountRecord[] updateParentRecords=new AccountRecord[updateParentRecordList.size()];
        updateParentRecordList.toArray(updateParentRecords);
        if(updateParentRecords.length>0)
        {
            this.update(accountBook,updateParentRecords);
        }
        return accountRecordDAO.add(accountBook,accountRecords);
    }

    @Transactional
    public void update(String accountBook, AccountRecord[] accountRecords) throws WMSServiceException{
        int curWarehouseId= accountRecords[0].getWarehouseId();
        int curAccountPeriodId=accountRecords[0].getAccountPeriodId();

        this.validateEntities(accountBook,accountRecords);
        List<AccountRecord> updateParentRecordList=new ArrayList<>();
        for (int k=0;k<accountRecords.length;k++){

            AccountRecordView[] oidAccountTitleViews=this.find(accountBook,new Condition().addCondition("id",accountRecords[k].getId()));

            BigDecimal oldCreditAmount=oidAccountTitleViews[0].getCreditAmount();
            BigDecimal oldDebitAmount=oidAccountTitleViews[0].getDebitAmount();

            BigDecimal creditAmount=accountRecords[k].getCreditAmount();
            BigDecimal debitAmount=accountRecords[k].getDebitAmount();

            AccountTitleView[] AccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",accountRecords[k].getAccountTitleId()));
            AccountTitleView accountTitleView=AccountTitleViews[0];
            AccountTitle[] accountTitles = ReflectHelper.createAndCopyFields(AccountTitleViews,AccountTitle.class);

            //TODO 找出当前科目的父代科目
            List<FindLinkAccountTitle> findLinkAccountTitleList=this.FindParentAccountTitle(accountBook,accountTitles);
            FindLinkAccountTitle[] parentAccountTitles=new FindLinkAccountTitle[findLinkAccountTitleList.size()];
            findLinkAccountTitleList.toArray(parentAccountTitles);

            List<AccountTitleView> accountTitleViewsList= parentAccountTitles[0].getAccountTitleViews();
            AccountTitleView[] curParentAccountTitleViews=new AccountTitleView[accountTitleViewsList.size()];
            accountTitleViewsList.toArray(curParentAccountTitleViews);

            AccountTitle[] curParentAccountTitles = ReflectHelper.createAndCopyFields(curParentAccountTitleViews,AccountTitle.class);

            //TODO 对当前科目的父代科目最新的账目记录余额进行更新
            Stream.of(curParentAccountTitles).forEach((curParentAccountTitle)->{

                AccountRecordView[] accountRecordViews= accountRecordDAO.find(accountBook,new Condition()
                        .addCondition("warehouseId",new Integer[]{curWarehouseId})
                        .addCondition("accountPeriodId",new Integer[]{curAccountPeriodId})
                        .addCondition("accountTitleId",new Integer[]{curParentAccountTitle.getId()}));

                AccountRecord[] allParentAccountRecords = ReflectHelper.createAndCopyFields(accountRecordViews,AccountRecord.class);

                //TODO 返回上级科目的列表

                if (allParentAccountRecords.length>0) {
                    AccountRecord newestAccountRecord = new AccountRecord();
                    for (int i = 0; i < allParentAccountRecords.length; i++) {
                        for (int j = 0; j < allParentAccountRecords.length; j++) {
                            if (allParentAccountRecords[i].getTime().after(allParentAccountRecords[j].getTime())) {
                                newestAccountRecord = allParentAccountRecords[i];
                            } else {
                                newestAccountRecord = allParentAccountRecords[j];
                            }
                        }
                    }
                    BigDecimal curBalance = newestAccountRecord.getBalance();

                    //如果科目类型是借方
                    if (curParentAccountTitle.getDirection() == AccountTitleService.Debit) {
                        newestAccountRecord.setBalance(curBalance.subtract(oldDebitAmount).add(oldCreditAmount).subtract(creditAmount).add(debitAmount));

                    } else {
                        newestAccountRecord.setBalance(curBalance.subtract(oldCreditAmount).add(oldDebitAmount).subtract(debitAmount).add(creditAmount));
                    }
                    updateParentRecordList.add(newestAccountRecord);
                }
            });
            //如果科目类型是借方
            if (accountTitleView.getDirection()==AccountTitleService.Debit){
                accountRecords[k].setBalance(oidAccountTitleViews[0].getBalance().subtract(oldDebitAmount).add(oldCreditAmount).subtract(creditAmount).add(debitAmount));

            }else{
                accountRecords[k].setBalance(oidAccountTitleViews[0].getBalance().subtract(oldCreditAmount).add(oldDebitAmount).subtract(debitAmount).add(creditAmount));
            }
        }
        AccountRecord[] updateParentRecords=new AccountRecord[updateParentRecordList.size()];
        updateParentRecordList.toArray(updateParentRecords);

        accountRecordDAO.update(accountBook,updateParentRecords);

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
            new Validator("余额").min(0).validate(accountRecord.getBalance());


            //验证外键
            this.idChecker.check(WarehouseService.class, accountBook, accountRecord.getWarehouseId(), "仓库");
            this.idChecker.check(AccountPeriodService.class, accountBook, accountRecord.getAccountPeriodId(), "会计期间");
            this.idChecker.check(AccountTitleService.class, accountBook, accountRecord.getAccountTitleId(), "科目");
            this.idChecker.check(PersonService.class, accountBook, accountRecord.getPersonId(), "记录人");
        }));


    }

    public void RealTransformAccount(String accountBook, TransferAccount transferAccount)throws WMSServiceException
    {

        AccountTitleView[] OutAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getOutaccountTitleId()));
        AccountTitleView OutAccountTitleView=OutAccountTitleViews[0];
        AccountTitleView[] InAccountTitleViews=this.accountTitleService.find(accountBook,new Condition().addCondition("id",transferAccount.getInaccountTitleId()));
        AccountTitleView InAccountTitleView=InAccountTitleViews[0];

        AccountRecord accountRecord=new AccountRecord();
        accountRecord.setAccountTitleId(OutAccountTitleView.getId());
        accountRecord.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord.setPersonId(transferAccount.getPersonId());
        accountRecord.setComment(transferAccount.getComment());
        accountRecord.setWarehouseId(transferAccount.getWarehouseId());
        accountRecord.setSummary(transferAccount.getSummary());
        accountRecord.setVoucherInfo(transferAccount.getVoucherInfo());

        AccountRecord accountRecord1=new AccountRecord();
        accountRecord1.setAccountTitleId(InAccountTitleView.getId());
        accountRecord1.setAccountPeriodId(transferAccount.getAccountPeriodId());
        accountRecord1.setPersonId(transferAccount.getPersonId());
        accountRecord1.setComment(transferAccount.getComment());
        accountRecord1.setWarehouseId(transferAccount.getWarehouseId());
        accountRecord1.setSummary(transferAccount.getSummary());
        accountRecord1.setVoucherInfo(transferAccount.getVoucherInfo());
//之前用了service 我改成了DAO 用service 无法注入
        AccountRecordView[] accountRecordViews= accountRecordDAO.find(accountBook,new Condition()
                .addCondition("warehouseId",new Integer[]{transferAccount.getWarehouseId()})
                .addCondition("accountPeriodId",new Integer[]{transferAccount.getAccountPeriodId()})
                .addCondition("accountTitleId",new Integer[]{OutAccountTitleView.getId()}));

        AccountRecordView newestAccountRecordView=new AccountRecordView();
        for (int i=0;i<accountRecordViews.length;i++){
            for (int j=0;j<accountRecordViews.length;j++){
                if (accountRecordViews[i].getTime().after(accountRecordViews[j].getTime())){
                    newestAccountRecordView=accountRecordViews[i];
                }else{
                    newestAccountRecordView=accountRecordViews[j];
                }
            }
        }

//之前用了service 我改成了DAO 用service 无法注入
        AccountRecordView[] inaccountRecordViews= accountRecordDAO.find(accountBook,new Condition()
                .addCondition("warehouseId",new Integer[]{transferAccount.getWarehouseId()})
                .addCondition("accountPeriodId",new Integer[]{transferAccount.getAccountPeriodId()})
                .addCondition("accountTitleId",new Integer[]{InAccountTitleView.getId()}));

        AccountRecordView newestAccountRecordView1=new AccountRecordView();
        for (int i=0;i<inaccountRecordViews.length;i++){
            for (int j=0;j<inaccountRecordViews.length;j++){
                if (inaccountRecordViews[i].getTime().after(inaccountRecordViews[j].getTime())){
                    newestAccountRecordView1=inaccountRecordViews[i];
                }else{
                    newestAccountRecordView1=inaccountRecordViews[j];
                }
            }
        }

        if (OutAccountTitleView.getDirection()==InAccountTitleView.getDirection()){
            if (OutAccountTitleView.getDirection()==AccountTitleService.Debit){
                //todo 找出最新一条的余额扣掉
                accountRecord.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().add(transferAccount.getChangeAmount()));
            }else{
                accountRecord.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().add(transferAccount.getChangeAmount()));
            }
        }

        if (OutAccountTitleView.getDirection()!=InAccountTitleView.getDirection()){
            if (OutAccountTitleView.getDirection()==AccountTitleService.Debit){
                //todo 找出最新一条的余额扣掉
                accountRecord.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().subtract(transferAccount.getChangeAmount()));
            }else{
                accountRecord.setDebitAmount(transferAccount.getChangeAmount());
                accountRecord.setBalance(newestAccountRecordView.getBalance().subtract(transferAccount.getChangeAmount()));
                accountRecord1.setCreditAmount(transferAccount.getChangeAmount());
                accountRecord1.setBalance(newestAccountRecordView1.getBalance().subtract(transferAccount.getChangeAmount()));
            }
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
                }
            });
            String[] parentAccountTitleNos=new String[parentAccountTitleNoList.size()];
            parentAccountTitleNoList.toArray(parentAccountTitleNos);

            AccountTitleView[] accountTitleViews=new AccountTitleView[parentAccountTitleNos.length];
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
                        throw new WMSServiceException(String.format("科目与上级科目类型不一致，请重新检查后提交！科目名称：(%s)", curAccountTitleName));
                    }
                }
            });
            String[] parentAccountTitleNos=new String[parentAccountTitleNoList.size()];
            parentAccountTitleNoList.toArray(parentAccountTitleNos);

            AccountTitleView[] accountTitleViews=new AccountTitleView[parentAccountTitleNos.length];
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
}

