package com.wms.utilities;


import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.CommonData;
import com.wms.utilities.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class OrderNoGenerator {
    @Autowired
    CommonDataService commonDataService;

    @Transactional
    public <TOrder> String generateNextNo(String accountBook, String prefix, int warehouseID) {
        if(prefix == null || prefix.isEmpty()){
            throw new WMSServiceException("GenerateNextNo的prefix参数长度必须大于0！");
        }
        String key = "Today_Table_Count_"+prefix + "_" + warehouseID; //单据数量记录的key
        CommonData[] commonData = commonDataService.find(accountBook,
                new Condition().addCondition("key", key));
        long todayOrderCount = 0; //当日有几单
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarToday.set(Calendar.MINUTE, 0);
        calendarToday.set(Calendar.SECOND, 0);
        calendarToday.set(Calendar.MILLISECOND, 0);
        Timestamp timestampToday = new Timestamp(calendarToday.getTimeInMillis());
        if (commonData.length == 0) { //没有找到记录代表从来没有创建过此种单子，故新建记录
            CommonData newData = new CommonData();
            newData.setKey(key);
            newData.setValue("1");
            commonDataService.add(accountBook, new CommonData[]{newData});
        } else if (commonData[0].getTime().before(timestampToday)) {
            commonData[0].setTime(new Timestamp(System.currentTimeMillis()));
            commonData[0].setValue("1");
            commonDataService.update(accountBook, new CommonData[]{commonData[0]});
        } else {
            todayOrderCount = Long.parseLong(commonData[0].getValue());
            commonData[0].setValue(String.valueOf(todayOrderCount + 1));
            commonDataService.update(accountBook, new CommonData[]{commonData[0]});
        }
        Calendar calendarNow = Calendar.getInstance();
        String no = String.format("%s%04d%02d%02d%02d%02d-%d",
                prefix,
                calendarNow.get(Calendar.YEAR),
                calendarNow.get(Calendar.MONTH) + 1,
                calendarNow.get(Calendar.DAY_OF_MONTH),
                calendarNow.get(Calendar.HOUR_OF_DAY),
                calendarNow.get(Calendar.MINUTE),
                todayOrderCount + 1);
        return no;
    }
}
