package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.TrayThresholdsDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import com.wms.utilities.model.TrayThresholds;
import com.wms.utilities.model.TrayThresholdsView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TrayThresholdsServiceImpl implements TrayThresholdsService{
    @Autowired
    TrayThresholdsDAO trayThresholdsDAO;
    @Override
    public int[] add(String accountBook, TrayThresholds[] trayThresholds) throws WMSServiceException
    {

        //his.validateEntities(accountBook,trayThresholds);
        int[] ids= trayThresholdsDAO.add(accountBook,trayThresholds);
        //this.validateDuplication(accountBook,summaryNotes);
        return ids;
    }

    @Override
    public void update(String accountBook, TrayThresholds[] trayThresholds) throws WMSServiceException
    {
        //this.validateEntities(accountBook,summaryNotes);
        trayThresholdsDAO.update(accountBook, trayThresholds);
        //this.validateDuplication(accountBook,summaryNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (trayThresholdsDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除托位阙值不存在，请重新查询！(%d)", id));
                }
            }
            trayThresholdsDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除托位阙值信息失败，如果托位阙值信息已经被引用，需要先删除引用的内容，才能删除该托位阙值");
        }
    }

    @Override
    public TrayThresholdsView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.trayThresholdsDAO.find(accountBook, cond);
    }
/*
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
*/
    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.trayThresholdsDAO.findCount(database,cond);
    }
}
