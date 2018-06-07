package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.StockRecordView;

import java.lang.reflect.Array;
import java.util.List;

@Repository
public class StockRecordDAOImpl
        extends BaseDAOImpl<StockRecord,StockRecordView>
        implements StockRecordDAO{
    @Autowired
    private SessionFactory sessionFactory;
    public StockRecordDAOImpl(){
        super(StockRecord.class,StockRecordView.class,StockRecord::getId);
    }
    @Override
    public StockRecordViewNewest[] findNewest(String accountBook, Condition condition){

        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = condition.createQuery(StockRecordViewNewest.class, session);
            List<StockRecordViewNewest> resultList = query.list();
            StockRecordViewNewest[] resultArray = (StockRecordViewNewest[]) Array.newInstance(StockRecordViewNewest.class,resultList.size());
            resultList.toArray(resultArray);
            return resultArray;
        } catch (Throwable ex) {
            throw new WMSDAOException(ex.getMessage());
        }


    }

}
