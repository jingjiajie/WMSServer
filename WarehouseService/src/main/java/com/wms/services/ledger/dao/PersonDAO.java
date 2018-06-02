package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.Person;
import com.wms.utilities.model.PersonView;

import java.util.Map;

public interface PersonDAO
        extends BaseDAO<Person,PersonView>{
}
