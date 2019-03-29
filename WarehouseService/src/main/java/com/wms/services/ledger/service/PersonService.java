package com.wms.services.ledger.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.Person;
import com.wms.utilities.model.PersonView;
import com.wms.utilities.service.BaseService;

public interface PersonService
    extends BaseService<Person,PersonView>{
    long findCount(String accountBook,Condition cond);
}
