package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Destination;
import com.wms.utilities.model.DestinationView;
import org.springframework.stereotype.Repository;

@Repository
public class DestinationDAOImpl
        extends BaseDAOImpl<Destination, DestinationView>
        implements DestinationDAO {
    public DestinationDAOImpl() {
        super(Destination.class, DestinationView.class, Destination::getId);
    }
}