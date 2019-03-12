package com.wms.services.warehouse.service;
import com.wms.services.warehouse.datastructures.DailyReportRequest;
import com.wms.services.warehouse.datastructures.DailyReports;
import com.wms.services.warehouse.datastructures.SupplierAmount;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Supplier;
import com.wms.utilities.model.SupplierView;

import java.util.List;

public interface SupplierServices {
    int[] add(String accountBook, Supplier suppliers[]) throws WMSServiceException;
    void update(String accountBook,Supplier suppliers[]) throws WMSServiceException;
    void updateHistory(String accountBook,Supplier suppliers[]) throws WMSServiceException;
    void remove(String accountBook,int ids[]) throws WMSServiceException;
    SupplierView[] find(String accountBook, Condition cond) throws WMSServiceException;
    SupplierView[] findNew(String accountBook, Condition cond) throws WMSServiceException;
    SupplierView[] findHistory(String accountBook, Condition cond) throws WMSServiceException;
    long findCount(String database,Condition cond) throws WMSServiceException;
    long findCountNew(String database,Condition cond) throws WMSServiceException;
    long findCountHistory(String database,Condition cond) throws WMSServiceException;
    SupplierAmount[] supplierRemind(String accountBook, int supplierId);
    List<DailyReports> generateDailyReports(String accountBook, DailyReportRequest dailyReportRequest);
    List<DailyReports> generateDailyReportsByYear(String accountBook, int supplyId, DailyReportRequest dailyReportRequest, List<DailyReports> dailyReportsList);
    //List<DailyReports> generateDailyReportsByYear(String accountBook, int supplyId);
    List<DailyReports> generateDailyReportsByYear(String accountBook,int supplyId);
}
