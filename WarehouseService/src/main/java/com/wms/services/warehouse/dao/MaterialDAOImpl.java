package com.wms.services.warehouse.dao;

import com.wms.utilities.model.Material;
import com.wms.utilities.model.MaterialView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MaterialDAOImpl implements MaterialDAO {
    @Autowired
    SessionFactory sessionFactory;

    private WarehouseServiceDAOTemplate<Material, MaterialView> getDAOTemplate() {
        return new WarehouseServiceDAOTemplate<>
                (this.sessionFactory, "Material", "MaterialView", Material::getId);
    }

    @Override
    public int[] add(String database,Material[] materials) throws WMSDAOException{
        return this.getDAOTemplate().add(database, materials);
    }

    @Override
    public void update(String database, Material materials[]) throws WMSDAOException{
        this.getDAOTemplate().update(database, materials);
    }

    public void remove(String database, int ids[]) throws WMSDAOException{
        this.getDAOTemplate().remove(database, ids);
    }

    @Override
    public MaterialView[] find(String database,Condition cond) throws WMSDAOException{
        return this.getDAOTemplate().find(database, cond, MaterialView.class);
    }
}
