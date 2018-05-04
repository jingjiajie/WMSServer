package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.TransferRecordDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

public class TransferRecordServiceImpl implements TransferRecordService {

    @Autowired
    TransferRecordDAO transferRecordDAO;
    @Autowired
    IDChecker idChecker;

    @Override
    @Transactional
    public int[] add(String accountBook, TransferRecord[] transferRecords) throws WMSServiceException {
        Stream.of(transferRecords).forEach(transferRecord -> {
            this.idChecker.check(WarehouseService.class,accountBook,transferRecord.getWarehouseId(),"仓库").
                    check(StockRecordService.class,accountBook,transferRecord.getSourceStockRecordId(),"原库存").
                    check(StockRecordService.class,accountBook,transferRecord.getNewStockRecordId(),"新库存");
        });
        return this.transferRecordDAO.add(accountBook,transferRecords);

    }

    @Transactional
    public TransferRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.transferRecordDAO.find(accountBook, cond);
    }
}
