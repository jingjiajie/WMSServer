package com.wms.services.ledger.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.AccountPeriod;
import com.wms.utilities.model.AccountPeriodView;
import org.springframework.stereotype.Repository;

@Repository
public class AccountPeriodDAOImpl
        extends BaseDAOImpl<AccountPeriod,AccountPeriodView>
        implements AccountPeriodDAO{
    public AccountPeriodDAOImpl(){
        super(AccountPeriod.class,AccountPeriodView.class,AccountPeriod::getId);}
}
