package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Package;
import com.wms.utilities.model.PackageView;
import org.springframework.stereotype.Repository;

@Repository
public class PackageDAOImpl
        extends BaseDAOImpl<Package,PackageView>
        implements PackageDAO{

        public PackageDAOImpl(){
        super(Package.class,PackageView.class,Package::getId);
    }
}
