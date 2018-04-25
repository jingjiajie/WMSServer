package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseEntryDAO;
import com.wms.services.warehouse.model.WarehouseEntry;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WarehouseEntryServiceImpl implements WarehouseEntryService {
    @Autowired
    WarehouseEntryDAO warehouseEntryDAO;

    @Override
    public int[] add(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        return warehouseEntryDAO.add(accountBook, warehouseEntries);
    }

    @Override
    public void update(String accountBook, WarehouseEntry[] warehouseEntries) throws WMSServiceException {
        warehouseEntryDAO.update(accountBook, warehouseEntries);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        warehouseEntryDAO.remove(accountBook, ids);
    }

    @Override
    public WarehouseEntry[] find(String accountBook, Condition cond) throws WMSServiceException {
        return warehouseEntryDAO.find(accountBook, cond);
    }
}
