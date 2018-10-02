package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SummaryDetailsDAO;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryDetailsView;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.model.TrayThresholds;
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
public class SummaryDetailsServiceImpl
implements SummaryDetailsService{
    @Autowired
    SummaryDetailsDAO summaryDetailsDAO;
    @Autowired
    SupplyService supplyService;

    @Override
    public int[] add(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryDetails);
        return summaryDetailsDAO.add(accountBook,summaryDetails);
    }

    @Override
    public int[] addIn(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        return summaryDetailsDAO.add(accountBook,summaryDetails);
    }

    @Override
    public void update(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryDetails);
        summaryDetailsDAO.update(accountBook, summaryDetails);
    }

    @Override
    public void updateIn(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        summaryDetailsDAO.update(accountBook, summaryDetails);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (summaryDetailsDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总详情不存在，请重新查询！(%d)", id));
                }
            }

            summaryDetailsDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除汇总详情失败，如果汇总详情已经被引用，需要先删除引用的内容，才能删除该汇总详情");
        }
    }

    @Override
    public SummaryDetailsView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryDetailsDAO.find(accountBook, cond);
    }

    @Override
    public SummaryDetails[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryDetailsDAO.findTable(accountBook, cond);
    }

    private void validateEntities(String accountBook,SummaryDetails[] summaryDetails) throws WMSServiceException{
        Stream.of(summaryDetails).forEach((summaryDetail -> {
            new Validator("列数").greaterThan(0).notEmpty().notnull().validate(summaryDetail.getStorageLocations());
            new Validator("托数").notEmpty().notnull().greaterThan(0).validate(summaryDetail.getTrays());
            new Validator("面积").notEmpty().notnull().greaterThan(0).validate(summaryDetail.getArea());
            new Validator("出货数").notEmpty().notnull().greaterThan(0).validate(summaryDetail.getDeliveryAmount());
            if(this.supplyService.find(accountBook,
                    new Condition().addCondition("id",summaryDetail.getSupplyId())).length == 0){
                throw new WMSServiceException(String.format("供货不存在，请重新提交！(%d)",summaryDetail.getSupplyId()));
            }
        }));
    }

    private void validateDuplication(String accountBook,SummaryDetails[] summaryDetails)
    {
        Condition cond = new Condition();
        cond.addCondition("summaryNoteItemId",summaryDetails[0].getSummaryNoteItemId());
        SummaryDetails[] summaryDetails1=summaryDetailsDAO.findTable(accountBook,cond);
        List<SummaryDetails> summaryDetailsList= Arrays.asList(summaryDetails1);
        summaryDetailsList.stream().reduce((last, cur) -> {
            if ((last.getSupplyId()==cur.getSupplyId())){
                throw new WMSServiceException("详情中供货不能重复!");
            }
            return cur;
        });
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryDetailsDAO.findCount(database,cond);
    }
}
