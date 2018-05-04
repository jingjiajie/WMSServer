package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;
import org.springframework.stereotype.Repository;

@Repository
public class PackageItemDAOImpl
        extends BaseDAOImpl<PackageItem,PackageItemView>
        implements PackageItemDAO {

    public PackageItemDAOImpl(){ super(PackageItem.class, PackageItemView.class, PackageItem::getId); }
}
