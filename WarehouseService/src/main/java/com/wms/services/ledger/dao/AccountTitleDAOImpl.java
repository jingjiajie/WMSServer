package com.wms.services.ledger.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.AccountTitleView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class AccountTitleDAOImpl
        extends BaseDAOImpl<AccountTitle,AccountTitleView>
        implements AccountTitleDAO {
    public AccountTitleDAOImpl(){ super(AccountTitle.class,AccountTitleView.class,AccountTitle::getId);}

}