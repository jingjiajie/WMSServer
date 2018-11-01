package com.wms.services.settlement.service;

import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.CommonData;
import com.wms.utilities.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrayServiceImpl implements TrayService{
    @Autowired
    CommonDataService commonDataService;

    @Override
    public int[] add(String accountBook, CommonData[] commonData) throws WMSServiceException
    {
        int[] ids= commonDataService.add(accountBook,commonData);
        return ids;
    }

    @Override
    public void update(String accountBook, CommonData[] commonData) throws WMSServiceException
    {
        //this.validateEntities(accountBook,summaryNotes);
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
}
