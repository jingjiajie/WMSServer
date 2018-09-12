package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SummaryDetailsDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryDetailsView;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;

@Service
@Transactional
public class SummaryDetailsServiceImpl
implements SummaryDetailsService{
    @Autowired
    SummaryDetailsDAO summaryDetailsDAO;

    @Override
    public int[] add(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        //this.validateEntities(accountBook,summaryDetails);
        return summaryDetailsDAO.add(accountBook,summaryDetails);
    }

    @Override
    public void update(String accountBook, SummaryDetails[] summaryDetails) throws WMSServiceException
    {
        //this.validateEntities(accountBook,summaryNoteItems);
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

    private void validateEntities(String accountBook,SummaryNoteItem[] summaryNoteItems) throws WMSServiceException{
        Stream.of(summaryNoteItems).forEach((summaryNoteItem -> {
            new Validator("使用面积").greaterThan(0).notEmpty().notnull().validate(summaryNoteItem.getArea());
            new Validator("放置天数").notEmpty().notnull().greaterThan(0).validate(summaryNoteItem.getDays());
            if(this.summaryDetailsDAO.find(accountBook,
                    new Condition().addCondition("id",summaryNoteItem.getSupplierId())).length == 0){
                throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)",summaryNoteItem.getSupplierId()));
            }
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryDetailsDAO.findCount(database,cond);
    }
}
