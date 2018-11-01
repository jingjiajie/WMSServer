package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.TrayThresholdsDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import com.wms.utilities.model.TrayThresholds;
import com.wms.utilities.model.TrayThresholdsView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class TrayThresholdsServiceImpl implements TrayThresholdsService{
    @Autowired
    TrayThresholdsDAO trayThresholdsDAO;
    @Autowired
    WarehouseService warehouseService;
    @Override
    public int[] add(String accountBook, TrayThresholds[] trayThresholds) throws WMSServiceException
    {

        this.validateEntities(accountBook,trayThresholds);
        int[] ids= trayThresholdsDAO.add(accountBook,trayThresholds);
        this.validateDuplication(accountBook,trayThresholds);
        return ids;
    }

    @Override
    public void update(String accountBook, TrayThresholds[] trayThresholds) throws WMSServiceException
    {
        this.validateEntities(accountBook,trayThresholds);
        trayThresholdsDAO.update(accountBook, trayThresholds);
        this.validateDuplication(accountBook,trayThresholds);
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

    private void validateEntities(String accountBook,TrayThresholds[] trayThreshold) throws WMSServiceException{
        Stream.of(trayThreshold).forEach((summaryNote -> {
            new Validator("阙值").notEmpty().min(0).validate(summaryNote.getThreshold());
            if(summaryNote.getThreshold().compareTo(new BigDecimal(1))>0){
                throw new WMSServiceException("阙值不能大于1");
            }
            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",summaryNote.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",summaryNote.getWarehouseId()));
            }
        }));
    }

    private void validateDuplication(String accountBook,TrayThresholds[] trayThresholds)
    {
        Condition cond = new Condition();
        cond.addCondition("warehouseId",trayThresholds[0].getWarehouseId());
        TrayThresholds[] trayThresholds1=trayThresholdsDAO.findTable(accountBook,cond);
        List<TrayThresholds> trayThresholds2= Arrays.asList(trayThresholds1);
        trayThresholds2.stream().reduce((last, cur) -> {
            if ((last.getThreshold().subtract(cur.getThreshold())).compareTo(new BigDecimal(0))==0){
                throw new WMSServiceException("阙值不能重复:"+cur.getThreshold());
            }
            return cur;
        });
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.trayThresholdsDAO.findCount(database,cond);
    }
}
