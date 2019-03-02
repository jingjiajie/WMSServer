package com.wms.services.warehouse.datastructures;

import java.sql.Timestamp;
import java.util.Comparator;

public class DailyReportsComparator implements Comparator<DailyReports> {
    @Override
    public int compare(DailyReports dailyReports1, DailyReports dailyReports2) {
        int cr = 0;
        //按时间升序排列
        int a = dailyReports1.getTimestamp().compareTo(dailyReports2.getTimestamp());
        if (a != 0) {
            cr = (a > 0) ? 3 : -1;
        } else {
            //按类型降序排列
            a = dailyReports2.getType() - dailyReports1.getType();
            if (a != 0) {
                cr = (a > 0) ? 2 : -2;
            } else {
                //TODO
                a = dailyReports2.getType() - dailyReports1.getType();
                if (a != 0) {
                    cr = (a > 0) ? 1 : -3;
                }
            }
        }
        return cr;
    }
}
