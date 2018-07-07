package com.wms.services.ledger.dao;
import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.AccountRecord;
import com.wms.utilities.model.AccountRecordView;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRecordDAOImpl
        extends BaseDAOImpl<AccountRecord,AccountRecordView>
        implements AccountRecordDAO {
    public AccountRecordDAOImpl(){
        super(AccountRecord.class,AccountRecordView.class,AccountRecord::getId);}
}
