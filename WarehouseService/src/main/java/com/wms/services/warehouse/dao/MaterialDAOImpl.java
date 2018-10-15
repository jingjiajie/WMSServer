package com.wms.services.warehouse.dao;

import com.wms.utilities.dao.BaseDAOImpl;
import com.wms.utilities.model.Material;
import com.wms.utilities.model.MaterialView;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MaterialDAOImpl
        extends BaseDAOImpl<Material, MaterialView> implements MaterialDAO {
    public MaterialDAOImpl() { super(Material.class, MaterialView.class,Material::getId); }


}
