package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.StockTakingOrderItemDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.StockTakingOrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockTakingOrderItemServiceImpl implements StockTakingOrderItemService{

    @Autowired
    StockTakingOrderItemDAO stockTakingOrderItemDAO;
    @Override
    public int[] add(String accountBook, StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {


        return stockTakingOrderItemDAO.add(accountBook,stockTakingOrderItems);
    }

    @Override
    public  void update(String accountBook,StockTakingOrderItem[] stockTakingOrderItems) throws WMSServiceException {

    }


    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            stockTakingOrderItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除盘点单条目失败，如果盘点单条目已经被引用，需要先删除引用的内容，才能删除该盘点单条目");
        }
    }


    @Override
    public StockTakingOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockTakingOrderItemDAO.find(accountBook, cond);
    }





}
