package com.wms.services.ledger.service;

import com.wms.utilities.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PersonView;
import com.wms.utilities.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface PersonService
    extends BaseService<Person,PersonView>{
}
