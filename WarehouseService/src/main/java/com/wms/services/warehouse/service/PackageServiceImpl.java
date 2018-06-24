package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.PackageDAO;
import com.wms.utilities.model.Package;
import com.wms.utilities.model.PackageView;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;

import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PackageDAO packageDAO;

    @Override
    public int[] add(String accountBook, Package[] packages) throws WMSServiceException {

        for(int i=0;i<packages.length;i++){
            for(int j=i+1;j<packages.length;j++){
                String name=packages[i].getName();
                if(name.equals(packages[j].getName())){throw new WMSServiceException("套餐名称"+name+"在添加的列表中重复!");}
            }
        }
        //外键检测
        Stream.of(packages).forEach(
                (package1) -> {
                    new Validator("发货套餐名称").notEmpty().validate(package1.getName());
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", package1.getWarehouseId())).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", package1.getWarehouseId()));
                    }
                }
        );
        Stream.of(packages).forEach(
                (package1) -> {
                    Condition cond = new Condition();
                    cond.addCondition("name", new String[]{package1.getName()});
                    cond.addCondition("warehouseId",new Integer[]{package1.getWarehouseId()});
                    if (packageDAO.find(accountBook, cond).length > 0) {
                        throw new WMSServiceException("发货套餐名称重复：" + package1.getName());
                    }
                }
        );
        return packageDAO.add(accountBook, packages);
    }

    @Override
    public void update(String accountBook, Package[] packages) throws WMSServiceException {

        for(int i=0;i<packages.length;i++){
            for(int j=i+1;j<packages.length;j++){
                String name=packages[i].getName();
                if(name.equals(packages[j].getName())){throw new WMSServiceException("套餐名称"+name+"在添加的列表中重复!");}
            }
        }
        //数据验证
        Stream.of(packages).forEach((package1) -> {
            new Validator("套餐名称").notnull().notEmpty().validate(package1.getName());
        });

        //名称查重
        for (int i = 0; i < packages.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{packages[i].getName()});
            cond.addCondition("warehouseId",new Integer[]{packages[i].getWarehouseId()});
            cond.addCondition("id", new Integer[]{packages[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if (packageDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("发货套餐名称重复：" + packages[i].getName());
            }
        }


        packageDAO.update(accountBook, packages);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (packageDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除发货套餐不存在，请重新查询！(%d)", id));
            }
        }

        try {
            packageDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除发货套餐失败，如果发货套餐已经被引用，需要先删除引用该发货套餐的内容，才能删除该发货套餐");
        }
    }

    @Override
    public PackageView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return packageDAO.find(accountBook, cond);
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.packageDAO.findCount(accountBook,cond);
    }

}
