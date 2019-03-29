package com.wms.services;
import com.wms.services.warehouse.datastructures.DailyReportRequest;
import com.wms.services.warehouse.datastructures.DailyReports;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.GetTimeStampByTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import java.util.*;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ComponentScan("com.wms")
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(String args[]) {
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class, args);
        System.out.println("仓库服务启动...");
//        StockRecordService stockRecordService=applicationContext.getBean(StockRecordService.class);
//        StockRecord stockRecord=new StockRecord();
//        stockRecord.setId(3380);
//        stockRecord.setBatchNo("3");
//        stockRecord.setSupplyId(5);
//        stockRecord.setWarehouseId(5587);
//        stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
//        stockRecord.setAvailableAmount(new BigDecimal(1));
//        stockRecord.setAmount(new BigDecimal(100));
//        stockRecord.setUnit("5");
//        stockRecord.setUnitAmount(new BigDecimal(1));
//        stockRecord.setState(1);
//        stockRecord.setStorageLocationId(23212);
//        stockRecord.setInventoryDate(new Timestamp(System.currentTimeMillis()));
//        TransferRecord transferRecord=new TransferRecord();
//        transferRecord.setTransferAmount(new BigDecimal(0));
//        TransferRecord[] transferRecords=new TransferRecord[]{transferRecord};
//        List<TransferRecord> list= Arrays.asList(transferRecords);
//        Iterator<TransferRecord> it=list.iterator();
//        //去除数组中"a"的元素
//        while(it.hasNext()){
//            TransferRecord transferRecord1=it.next();
//            if(transferRecord1.getTransferAmount().equals(new BigDecimal(0))){
//                it.remove();
//            }
//        }
//        list.toArray(transferRecords);
//        int a;
        //stockRecordService.add("WMS_Template",new StockRecord[]{stockRecord});


        SupplierServices supplierServices=applicationContext.getBean(SupplierServices.class);
        DailyReportRequest dailyReportRequest=new DailyReportRequest();
        dailyReportRequest.setSupplierId(1);
        dailyReportRequest.setStartTime(GetTimeStampByTime.getTimestamoByTime("2018-5-27 0:0:0"));
        dailyReportRequest.setEndTime(GetTimeStampByTime.getTimestamoByTime("2018-5-27 24:0:0"));
        List<DailyReports> dailyReports=new ArrayList<>();
        //supplierServices.generateDailyReportsByYear("WMS_Template",1203);
//        JudgeOldestBatch judgeOldestBatch=new JudgeOldestBatch();
//        judgeOldestBatch.setSupplyId(15);
//        judgeOldestBatch.setUnit("个");
//        judgeOldestBatch.setUnitAmount(new BigDecimal(1));
//        judgeOldestBatch.setBatchNo("20180603");
//        judgeOldestBatch.setWarehouseId(1);
//        StockRecordFind stockRecordFind=new StockRecordFind();
//        stockRecordFind.setState(2);
//        stockRecordFind.setUnit("个");
//        stockRecordFind.setSupplyId(15);
//        stockRecordFind.setUnitAmount(new BigDecimal(1));
//        stockRecordFind.setStorageLocationId(39);
//
//
//        SummaryNoteService summaryNoteService=applicationContext.getBean(SummaryNoteService.class);
//        StockRecordService stockRecordService=applicationContext.getBean(StockRecordService.class);
//        stockRecordService.findTableNewest("WMS_Template",stockRecordFind);
//        stockRecordService.judgeOldestBatch("WMS_Template",judgeOldestBatch);
        //SummaryNoteView[] summaryNoteViews=summaryNoteService.find("WMS_Template",new Condition().addCondition("id",4));
        //SummaryNote[] summaryNotes = ReflectHelper.createAndCopyFields(summaryNoteViews, SummaryNote.class);
        //summaryNoteService.summaryDelivery("WMS_Template",summaryNotes[0]);
        //summaryNoteService.generateSummaryNotes("WMS_Template",1,1);
        //RefreshGlobalDateService refreshGlobalDateService=applicationContext.getBean(RefreshGlobalDateService.class);
        //refreshGlobalDateService.refreshGlobalDate("WMS_Template",1);
    }
}