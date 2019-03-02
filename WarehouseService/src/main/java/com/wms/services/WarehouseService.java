package com.wms.services;
import com.wms.services.ledger.datestructures.TreeViewData;
import com.wms.services.salary.service.GetBigDecimal;
import com.wms.services.settlement.service.SummaryNoteService;
import com.wms.services.warehouse.datastructures.DailyReportRequest;
import com.wms.services.warehouse.datastructures.DailyReports;
import com.wms.services.warehouse.datastructures.JudgeOldestBatch;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.service.RefreshGlobalDateService;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.GetTimeStampByTime;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.SummaryNote;
import com.wms.utilities.model.SummaryNoteView;
import com.wms.utilities.model.Supplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import com.wms.services.ledger.service.AccountRecordService;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        SupplierServices supplierServices=applicationContext.getBean(SupplierServices.class);
        DailyReportRequest dailyReportRequest=new DailyReportRequest();
        dailyReportRequest.setSupplierId(1);
        dailyReportRequest.setStartTime(GetTimeStampByTime.getTimestamoByTime("2018-5-27 0:0:0"));
        dailyReportRequest.setEndTime(GetTimeStampByTime.getTimestamoByTime("2018-5-27 24:0:0"));
        List<DailyReports> dailyReports=new ArrayList<>();
        supplierServices.generateDailyReportsByYear("WMS_Template",1203,dailyReportRequest,dailyReports);

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