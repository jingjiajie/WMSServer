package com.wms.services.settlement.service;

import com.wms.services.settlement.datastructures.ValidateTray;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.CommonData;
import com.wms.utilities.model.StorageLocationView;
import com.wms.utilities.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class TrayServiceImpl implements TrayService{
    @Autowired
    CommonDataService commonDataService;
    @Autowired
    StorageLocationService storageLocationService;

    @Override
    public int[] add(String accountBook, CommonData[] commonData) throws WMSServiceException
    {
        int[] ids= commonDataService.add(accountBook,commonData);
        return ids;
    }

    @Override
    public void update(String accountBook, CommonData[] commonData) throws WMSServiceException
    {
        commonDataService.update(accountBook, commonData);
        //this.validateDuplication(accountBook,summaryNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (commonDataService.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除托位不存在，请重新查询！(%d)", id));
                }
            }
            commonDataService.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除托位信息失败，如果托位阙值信息已经被引用，需要先删除引用的内容，才能删除该托位阙值");
        }
    }

    @Override
    public CommonData[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.commonDataService.find(accountBook, cond);
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.commonDataService.findCount(database,cond);
    }

    public void validateEntities(String accountBook, ValidateTray validateTray){
        //Tray_Length_WarehouseId
            int warehouseId=validateTray.getWarehouseId();
            StorageLocationView[] storageLocationViews=storageLocationService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId));
            if(storageLocationViews.length==0){return;}
            BigDecimal lengthAvailableMin=new BigDecimal(-1);
            BigDecimal widthAvailableMin=new BigDecimal(-1);;
            BigDecimal areaMin=new BigDecimal(-1);;
            for(StorageLocationView storageLocationView:storageLocationViews){
                if(storageLocationView.getLength()!=null&&storageLocationView.getLengthPadding()!=null
                        &&storageLocationView.getWidth()!=null&&storageLocationView.getWidthPadding()!=null
                        &&storageLocationView.getReservedArea()!=null)
                {
                    //记录下最小的长度
                    if(lengthAvailableMin.compareTo(new BigDecimal(-1))==0||lengthAvailableMin.compareTo(storageLocationView.getLength().add(storageLocationView.getLengthPadding().negate()))>0)
                    {lengthAvailableMin=storageLocationView.getLength().add(storageLocationView.getLengthPadding().negate());}
                    //宽度
                    if(widthAvailableMin.compareTo(new BigDecimal(-1))==0||widthAvailableMin.compareTo(storageLocationView.getWidth().add(storageLocationView.getWidthPadding().negate()))>0)
                    { widthAvailableMin=storageLocationView.getWidth().add(storageLocationView.getWidthPadding().negate());}
                    //面积
                    BigDecimal area=(storageLocationView.getLength().add(storageLocationView.getLengthPadding().negate()).multiply(storageLocationView.getWidth().add(storageLocationView.getWidthPadding().negate()))).
                            add(storageLocationView.getReservedArea().negate());
                    if(areaMin.compareTo(new BigDecimal(-1))==0||areaMin.compareTo(area)>0)
                    { widthAvailableMin=storageLocationView.getWidth().add(storageLocationView.getWidthPadding().negate());}
                }
            }
            //进行比较
        if(validateTray.getWidth().compareTo(widthAvailableMin)>0){
                throw new WMSServiceException("托位宽度："+validateTray.getWidth()+" 不能大于所有库位宽度最小值");
        }
        if(validateTray.getLength().compareTo(lengthAvailableMin)>0){
            throw new WMSServiceException("托位长度："+validateTray.getWidth()+" 不能大于所有库位长度最小值");
        }
        if(validateTray.getLength().multiply(validateTray.getWidth()).compareTo(areaMin)>0){
            throw new WMSServiceException("托位面积："+validateTray.getWidth()+" 不能大于所有库位面积最小值");
        }
    }
}
