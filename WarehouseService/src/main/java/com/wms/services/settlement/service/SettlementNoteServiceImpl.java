package com.wms.services.settlement.service;

import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.settlement.dao.SettlementNoteDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SettlementNote;
import com.wms.utilities.model.SettlementNoteView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;


@Service
@Transactional
public class SettlementNoteServiceImpl implements SettlementNoteService {
    @Autowired
    SettlementNoteDAO settlementNoteDAO;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNotes);

        Stream.of(settlementNotes).forEach((settlementNote -> {
            settlementNote.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }));
        return settlementNoteDAO.add(accountBook,settlementNotes);
    }

    @Override
    public void update(String accountBook, SettlementNote[] settlementNotes) throws WMSServiceException
    {
        this.validateEntities(accountBook,settlementNotes);
        settlementNoteDAO.update(accountBook, settlementNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (settlementNoteDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除结算单不存在，请重新查询！(%d)", id));
                }
            }

            settlementNoteDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除结算单信息失败，如果结算单信息已经被引用，需要先删除引用的内容，才能删除该汇总单");
        }
    }

    @Override
    public SettlementNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.settlementNoteDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SettlementNote[] settlementNotes) throws WMSServiceException{
        Stream.of(settlementNotes).forEach((settlementNote -> {
            new Validator("状态").min(0).max(2).validate(settlementNote.getState());
            new Validator("单号").notEmpty().validate(settlementNote.getNo());

            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitleIncomeId(), "收入科目ID");
            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitleReceivableId(), "应收款科目ID");
            this.idChecker.check(AccountTitleService.class, accountBook, settlementNote.getAccountTitlePropertyId(), "资产科目ID");
            this.idChecker.check(SummaryNoteService.class, accountBook, settlementNote.getSummaryNoteId(), "汇总单ID");

        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.settlementNoteDAO.findCount(database,cond);
    }


}
