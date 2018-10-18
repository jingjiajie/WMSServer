package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.AccountPeriodService;
import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.service.PayNoteService;
import com.wms.services.salary.service.SalaryItemService;
import com.wms.services.salary.service.SalaryPeriodService;
import com.wms.services.salary.service.SalaryTypeService;
import com.wms.services.settlement.service.SummaryNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class RefreshGlobalDateServiceImpl implements RefreshGlobalDateService{
    @Autowired
    SupplierServices supplierServices;
    @Autowired
    MaterialService materialService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    StorageAreaService storageAreaService;
    @Autowired
    PersonService personService;
    @Autowired
    PackageService packageService;
    @Autowired
    SalaryItemService salaryItemService;
    @Autowired
    SalaryTypeService salaryTypeService;
    @Autowired
    SummaryNoteService summaryNoteService;
    @Autowired
    SalaryPeriodService salaryPeriodService;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    PayNoteService payNoteService;
    @Autowired
    TaxService taxService;
    @Autowired
    AccountPeriodService accountPeriodService;

    public Map<String,Object[]>  refreshGlobalDate(String accountBook, int warehouseId)
    {
        Map<String,Object[]> globalDateMap=new HashMap<String, Object[]>();
        globalDateMap.put("AllSuppliers",supplierServices.findNew(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllMaterial",materialService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllSupply",supplyService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllStorageLocation",storageLocationService.find(accountBook,
                new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllStorageArea",storageAreaService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllPersons",personService.find(accountBook,new Condition()));
        globalDateMap.put("AllPackage",packageService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllSalaryItem",salaryItemService.find(accountBook,
                new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllSalaryType",salaryTypeService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllSummaryNote",summaryNoteService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)));
        globalDateMap.put("AllSalaryPeriod",salaryPeriodService.find(accountBook,
                new Condition().addOrder("endTime", OrderItem.Order.DESC)));
        globalDateMap.put("AllAccountTitle",payNoteService.findSonTitleForAssociation(accountBook, new Condition()));
        globalDateMap.put("AllAccountTitleTrue",accountTitleService.find(accountBook,new Condition()));
        globalDateMap.put("AllTax",taxService.find(accountBook,new Condition()));
        globalDateMap.put("AllAccountPeriod",accountPeriodService.find(accountBook,
                new Condition().addCondition("warehouseId",warehouseId).addOrder("startTime", OrderItem.Order.DESC)));
        globalDateMap.put("AccountPeriod",accountPeriodService.find(accountBook,
                new Condition().addCondition("warehouseId",warehouseId).addCondition("ended",0).addOrder("startTime", OrderItem.Order.DESC)));
    return globalDateMap;
    }
}
