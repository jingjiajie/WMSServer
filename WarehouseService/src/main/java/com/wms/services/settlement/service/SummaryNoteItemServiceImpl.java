package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SummaryNoteDAO;
import com.wms.services.settlement.dao.SummaryNoteItemDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class SummaryNoteItemServiceImpl
implements SummaryNoteItemService{
    @Autowired
    SummaryNoteItemDAO summaryNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException
    {
        return summaryNoteItemDAO.add(accountBook,summaryNoteItems);
    }

    @Override
    public void update(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException
    {
        summaryNoteItemDAO.update(accountBook, summaryNoteItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (summaryNoteItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总单条目不存在，请重新查询！(%d)", id));
                }
            }

            summaryNoteItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除汇总单条目信息失败，如果汇总单条目信息已经被引用，需要先删除引用的内容，才能删除该汇总单条目");
        }
    }

    @Override
    public SummaryNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,Material[] materials) throws WMSServiceException{
        Stream.of(materials).forEach((material -> {
            new Validator("是否启用").min(0).max(1).validate(material.getEnabled());
            new Validator("代号").notEmpty().validate(material.getNo());
            new Validator("物料名称").notEmpty().validate(material.getName());
            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",material.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",material.getWarehouseId()));
            }
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryNoteItemDAO.findCount(database,cond);
    }
}
