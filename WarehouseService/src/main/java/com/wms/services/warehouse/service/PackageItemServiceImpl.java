package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.PackageItemDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PackageItem;
import com.wms.utilities.model.PackageItemView;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        for(int i=0;i<packageItems.length;i++){
            for(int j=i+1;j<packageItems.length;j++){
                int supplyId=packageItems[i].getSupplyId();
                int locationId=packageItems[i].getDefaultDeliveryStorageLocationId();
                String unit=packageItems[i].getDefaultDeliveryUnit();
                BigDecimal unitAmount=packageItems[i].getDefaultDeliveryUnitAmount();
                if(unit.equals(packageItems[j].getDefaultDeliveryUnit())&&unitAmount.equals(packageItems[j].getDefaultDeliveryUnitAmount())
                        &&supplyId==packageItems[j].getSupplyId()&&locationId==packageItems[j].getDefaultDeliveryStorageLocationId()){throw new WMSServiceException("套餐条目在添加的列表中重复!");}
            }
        }
        this.validateEntities(accountBook,packageItems);
        return this.packageItemDAO.add(accountBook,packageItems);
    }

    @Override
    public void update(String accountBook, PackageItem[] packageItems) throws WMSServiceException {

        for(int i=0;i<packageItems.length;i++){
            for(int j=i+1;j<packageItems.length;j++){
                int supplyId=packageItems[i].getSupplyId();
                int locationId=packageItems[i].getDefaultDeliveryStorageLocationId();
                String unit=packageItems[i].getDefaultDeliveryUnit();
                BigDecimal unitAmount=packageItems[i].getDefaultDeliveryUnitAmount();
                if(unit.equals(packageItems[j].getDefaultDeliveryUnit())&&unitAmount.equals(packageItems[j].getDefaultDeliveryUnitAmount())
                        &&supplyId==packageItems[j].getSupplyId()&&locationId==packageItems[j].getDefaultDeliveryStorageLocationId()){throw new WMSServiceException("套餐条目在添加的列表中重复!");}
            }
        }
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
                    new Validator("默认出库库位ID").notEmpty().validate(packageItem.getDefaultDeliveryStorageLocationId());
                }
        );

        //外键验证
        Stream.of(packageItems).forEach(
                (packageItem) -> {
                    StorageLocationView[] storageLocationViews = storageLocationService.find(accountBook, new Condition().addCondition("id", packageItem.getDefaultDeliveryStorageLocationId()));
                    if (this.packageService.find(accountBook,
                            new Condition().addCondition("id", packageItem.getPackageId())).length == 0) {
                        throw new WMSServiceException(String.format("供货套餐不存在，请重新提交！(%d)", packageItem.getPackageId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", packageItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", packageItem.getSupplyId()));
                    }
                    else if (storageLocationViews.length == 0) {
                        throw new WMSServiceException("库位不存在，请重新提交！");
                    }
                }
        );
    }
    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.packageItemDAO.findCount(accountBook,cond);
    }
}
