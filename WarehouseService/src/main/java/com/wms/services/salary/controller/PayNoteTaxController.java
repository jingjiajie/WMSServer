package com.wms.services.salary.controller;

import com.wms.utilities.controller.BaseController;
import com.wms.utilities.model.PayNoteTax;
import com.wms.utilities.model.PayNoteTaxView;

public interface PayNoteTaxController extends BaseController<PayNoteTax,PayNoteTaxView> {
    void chooseTax(String accountBook,PayNoteTax[] payNoteTaxes);
}
