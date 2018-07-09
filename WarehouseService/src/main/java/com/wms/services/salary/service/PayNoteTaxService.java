package com.wms.services.salary.service;

import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;
import com.wms.utilities.service.BaseService;

public interface PayNoteTaxService extends BaseService<PayNoteTax,PayNoteTaxView> {
    void chooseTax(String accountBook,PayNoteTax[] payNoteTaxes);
}
