package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.InvoiceDAO;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Invoice;
import com.wms.utilities.model.InvoiceView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceDAO invoiceDAO;
    @Autowired
    WarehouseService warehouseService;

    @Override
    public int[] add(String accountBook, Invoice[] invoices) throws WMSServiceException
    {
        return invoiceDAO.add(accountBook,invoices);
    }

    @Override
    public void update(String accountBook, Invoice[] invoices) throws WMSServiceException
    {
        invoiceDAO.update(accountBook, invoices);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (invoiceDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除发票不存在，请重新查询！(%d)", id));
                }
            }

            invoiceDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除发票信息失败，如果发票信息已经被引用，需要先删除引用的内容，才能删除该发票");
        }
    }

    @Override
    public InvoiceView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.invoiceDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,Invoice[] invoices) throws WMSServiceException{
        Stream.of(invoices).forEach((invoice -> {
            new Validator("代号").notEmpty().validate(invoice.getNo());
            new Validator("物料名称").notEmpty().validate(invoice.getTrackingNumber());

        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.invoiceDAO.findCount(database,cond);
    }
}
