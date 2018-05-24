package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.PackageItemDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class PackageItemServiceImpl implements PackageItemService  {
    @Autowired
    PackageItemDAO packageItemDAO;
    @Autowired
    PackageService packageService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;

    @Override
    public int[] add(String accountBook, PackageItem[] packageItems) throws WMSServiceException {
        this.validateEntities(accountBook,packageItems);
        return this.packageItemDAO.add(accountBook,packageItems);
    }

    @Override
    public void update(String accountBook, PackageItem[] packageItems) throws WMSServiceException {
        this.validateEntities(accountBook,packageItems);
        this.packageItemDAO.update(accountBook,packageItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            this.packageItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public PackageItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.packageItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,PackageItem[] packageItems){
        //数据验证
        Stream.of(packageItems).forEach(
                (packageItem)->{
                    new Validator("默认发货数量（个）").min(0).validate(packageItem.getDefaultDeliveryAmount());
                    new Validator("默认发货单位数量").min(0).validate(packageItem.getDefaultDeliveryUnitAmount());

                }
        );

        //外键验证
        Stream.of(packageItems).forEach(
                (packageItem) -> {
                    if (this.packageService.find(accountBook,
                            new Condition().addCondition("id", packageItem.getPackageId())).length == 0) {
                        throw new WMSServiceException(String.format("供货套餐不存在，请重新提交！(%d)", packageItem.getPackageId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", packageItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", packageItem.getSupplyId()));
                    }
                }
        );
    }
    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.packageItemDAO.findCount(accountBook,cond);
    }
}
