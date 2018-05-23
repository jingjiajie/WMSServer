package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.StorageLocation;
import com.wms.utilities.model.StorageLocationView;
import org.springframework.stereotype.Repository;
import com.wms.utilities.model.StorageLocationView;
    @Repository

    public class StorageLocationDAOImpl
            extends BaseDAOImpl<StorageLocation ,StorageLocationView>
            implements StorageLocationDAO{
        public StorageLocationDAOImpl() {
            super(StorageLocation.class, StorageLocationView.class, StorageLocation::getId);
        }
    }