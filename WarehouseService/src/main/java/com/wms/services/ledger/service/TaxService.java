package com.wms.services.ledger.service;

import com.wms.services.ledger.datestructures.TaxCalculation;
import com.wms.utilities.model.Tax;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TaxView;
import com.wms.utilities.service.BaseService;

import java.math.BigDecimal;
import java.util.Map;

public interface TaxService extends BaseService<Tax,TaxView>{
    public BigDecimal taxCalculation(String accountBook, TaxCalculation taxCalculation) throws WMSServiceException;

}
