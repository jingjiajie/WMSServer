package com.wms.services.settlement.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.settlement.dao.SummaryNoteDAO;
import com.wms.services.warehouse.service.StockTakingOrderServiceImpl;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class SummaryNoteServiceImpl implements SummaryNoteService {

    @Autowired
    SummaryNoteDAO summaryNoteDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    OrderNoGenerator orderNoGenerator;

    private static final String NO_PREFIX = "H";

    @Override
    public int[] add(String accountBook, SummaryNote[] summaryNotes) throws WMSServiceException
    {
        //生成/检测单号
        Stream.of(summaryNotes).forEach((summaryNote) -> {
            //如果单号留空则自动生成
            if (summaryNote.getNo() == null) {
                summaryNote.setNo(this.orderNoGenerator.generateNextNo(accountBook, SummaryNoteServiceImpl.NO_PREFIX,summaryNote.getWarehouseId()));
            }
        });
        this.validateEntities(accountBook,summaryNotes);
        int[] ids= summaryNoteDAO.add(accountBook,summaryNotes);
        this.validateDuplication(accountBook,summaryNotes);
        return ids;
    }

    @Override
    public void update(String accountBook, SummaryNote[] summaryNotes) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryNotes);
        summaryNoteDAO.update(accountBook, summaryNotes);
        this.validateDuplication(accountBook,summaryNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (summaryNoteDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总单不存在，请重新查询！(%d)", id));
                }
            }

            summaryNoteDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除汇总单信息失败，如果汇总单信息已经被引用，需要先删除引用的内容，才能删除该汇总单");
        }
    }

    @Override
    public SummaryNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SummaryNote[] summaryNotes) throws WMSServiceException{
        Stream.of(summaryNotes).forEach((summaryNote -> {
            new Validator("代号").notEmpty().validate(summaryNote.getNo());
            new Validator("起始时间").notEmpty().validate(summaryNote.getStartTime());
            new Validator("截止时间").notEmpty().validate(summaryNote.getEndTime());
            if(summaryNote.getStartTime().compareTo(summaryNote.getEndTime())>=0)
            {
                throw new WMSServiceException("汇总单的截止时间必须在起始时间之后！单号："+summaryNote.getNo());
            }
            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",summaryNote.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",summaryNote.getWarehouseId()));
            }
            if(this.personService.find(accountBook,
                    new Condition().addCondition("id",summaryNote.getCreatePersonId())).length == 0){
                throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",summaryNote.getCreatePersonId()));
            }
        }));
    }

    private void validateDuplication(String accountBook,SummaryNote[] summaryNotes)
    {
        Condition cond = new Condition();
        cond.addCondition("warehouseId",summaryNotes[0].getWarehouseId());
        SummaryNote[] summaryNotesCheck=summaryNoteDAO.findTable(accountBook,cond);
        List<SummaryNote> summaryNoteList= Arrays.asList(summaryNotesCheck);
        summaryNoteList.stream().reduce((last, cur) -> {
            if (last.getNo().equals(cur.getNo())){
                throw new WMSServiceException("汇总单单号重复:"+cur.getNo());
            }
            return cur;
        });
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryNoteDAO.findCount(database,cond);
    }
}
