package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SummaryNoteDAO;
import com.wms.services.settlement.dao.SummaryNoteItemDAO;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class SummaryNoteItemServiceImpl
        implements SummaryNoteItemService {
    @Autowired
    SummaryNoteItemDAO summaryNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SupplierServices supplierServices;
    @Autowired
    SummaryDetailsService summaryDetailsService;

    @Override
    public int[] add(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException {
        this.validateEntities(accountBook, summaryNoteItems);
        for (SummaryNoteItem summaryNoteItem : summaryNoteItems) {
            summaryNoteItem.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        int[] ids= summaryNoteItemDAO.add(accountBook, summaryNoteItems);
        this.validateDucpicate(accountBook,summaryNoteItems);
        return ids;
    }

    @Override
    public void update(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException {
        this.validateEntities(accountBook, summaryNoteItems);
        Stream.of(summaryNoteItems).forEach((summaryNoteItem) -> {
            int summaryNoteItemId = summaryNoteItem.getId();
            SummaryDetailsView[] summaryDetails = summaryDetailsService.find(accountBook, new Condition().addCondition("summaryNoteItemId", summaryNoteItemId));
            if (summaryDetails.length != 0)
            {
                if (summaryDetails[0].getSupplierId() != summaryNoteItem.getSupplierId())
                {
                 throw new WMSServiceException("条目供应商必须与详情供应商相同！");
                }
            }
        });
        summaryNoteItemDAO.update(accountBook, summaryNoteItems);
        this.validateDucpicate(accountBook,summaryNoteItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {

        try {
            for (int id : ids) {
                if (summaryNoteItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总单条目不存在，请重新查询！(%d)", id));
                }
            }

            summaryNoteItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除汇总单条目信息失败，如果汇总单条目信息已经被引用，需要先删除引用的内容，才能删除该汇总单条目");
        }
    }

    @Override
    public SummaryNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteItemDAO.find(accountBook, cond);
    }

    @Override
    public SummaryNoteItem[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteItemDAO.findTable(accountBook, cond);
    }

    private void validateEntities(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException {
        Stream.of(summaryNoteItems).forEach((summaryNoteItem -> {
            //new Validator("使用面积").greaterThan(0).notEmpty().notnull().validate(summaryNoteItem.getTotalArea());
            //new Validator("放置天数").notEmpty().notnull().greaterThan(0).validate(summaryNoteItem.getDays());
            if (this.supplierServices.find(accountBook,
                    new Condition().addCondition("id", summaryNoteItem.getSupplierId())).length == 0) {
                throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)", summaryNoteItem.getSupplierId()));
            }
        }));
    }

    private void validateDucpicate(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException {
        Stream.of(summaryNoteItems).forEach((summaryNoteItem -> {
            SummaryNoteItemView[] summaryNoteItemViews = this.summaryNoteItemDAO.find(accountBook,
                    new Condition().addCondition("supplierId", summaryNoteItem.getSupplierId()).addCondition("summaryNoteId", summaryNoteItem.getSummaryNoteId()));
            if (summaryNoteItemViews.length > 1) {
                SupplierView[] supplierViews = this.supplierServices.find(accountBook, new Condition().addCondition("id", summaryNoteItemViews[0].getSupplierId()));
                if (supplierViews.length != 1) {
                    throw new WMSServiceException("数据验证中查询供应商出错,可能已经删除！");
                }
                throw new WMSServiceException(String.format("供应商(%s)在此单内重复！(%d)", supplierViews[0].getName(), summaryNoteItem.getSupplierId()));
            }
        }));
    }

    @Override
    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.summaryNoteItemDAO.findCount(database, cond);
    }
}
