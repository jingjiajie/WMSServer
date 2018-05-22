package com.wms.services.warehouse;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.DeliveryOrderService;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.services.warehouse.service.StockTakingOrderItemService;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ComponentScan("com.wms")
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(java.lang.String args[]) {
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class, args);
        System.out.println("仓库服务启动...");

        StockRecordService stockRecordService = applicationContext.getBean(StockRecordService.class);
        IDChecker idChecker = applicationContext.getBean(IDChecker.class);
        SupplyService supplyService=applicationContext.getBean(SupplyService.class);

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR,2018);//设置年
        gc.set(Calendar.MONTH, 5);//这里0是1月..以此向后推
        gc.set(Calendar.DAY_OF_MONTH, 5);//设置天
        date = gc.getTime();
        long a=date.getTime();
        String timestamp = String.valueOf(date.getTime()/1000);
        Timestamp time2 =new Timestamp(date.getTime());
/*
        StockRecordFind stockRecordFind = new StockRecordFind();
        stockRecordFind.setSupplyId(5);
        stockRecordFind.setStorageLocationId(21);
        stockRecordFind.setWarehouseId(1);
        stockRecordFind.setUnit("个");
        stockRecordFind.setReturnMode("new");
        stockRecordFind.setUnitAmount(new BigDecimal(1));
        stockRecordFind.setInventoryDate(time2);
        StockRecordView[] stockRecordSource1 = stockRecordService.find("WMS_Template", stockRecordFind);
*/
        TransferStock transferStock=new TransferStock();
        transferStock.setAmount(new BigDecimal(1));
        transferStock.setSourceStorageLocationId(20);
        transferStock.setUnit("个");
        transferStock.setUnitAmount(new BigDecimal(100));
        transferStock.setSupplyId(5);
        transferStock.setInventoryDate(time2);
        transferStock.setRelatedOrderNo("123456");
        transferStock.setNewStorageLocationId(4);


        stockRecordService.RealTransformStock("WMS_Template",transferStock);
        /*
        Condition condition = new Condition().addCondition("warehouseId", new Integer[]{5}).addCondition("storageLocationId", new Integer[]{26}).addCondition("supplyId", new Integer[]{7});

        // condition.addCondition("unit",new String[]{"个"}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("unitAmount", new BigDecimal[]{new BigDecimal(10)}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("batchNo", new String[]{"100"}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("time", stockRecordFind.getTimeEnd(), ConditionItem.Relation.LESS_THAN);

        StockRecordView[] stockRecordViews = stockRecordService.find("WMS_Template", condition);
        */
    }
}
