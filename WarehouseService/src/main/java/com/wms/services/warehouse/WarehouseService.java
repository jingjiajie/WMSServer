package com.wms.services.warehouse;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.services.warehouse.datastructures.TransferItem;
import com.wms.services.warehouse.service.DeliveryOrderService;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.services.warehouse.service.StockTakingOrderItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockTakingOrderItem;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import com.wms.services.warehouse.datastructures.TransferArgs;

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


        StockRecordFind stockRecordFind = new StockRecordFind();
        stockRecordFind.setSupplyId(7);
        stockRecordFind.setStorageLocationId(26);
        stockRecordFind.setWarehouseId(5);
        stockRecordFind.setUnit("个");
        stockRecordFind.setUnitAmount(new BigDecimal(10));
        StockRecordView[] stockRecordSource1 = stockRecordService.find("WMS_Template", stockRecordFind);


        Condition condition = new Condition().addCondition("warehouseId", new Integer[]{5}).addCondition("storageLocationId", new Integer[]{26}).addCondition("supplyId", new Integer[]{7});

        // condition.addCondition("unit",new String[]{"个"}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("unitAmount", new BigDecimal[]{new BigDecimal(10)}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("batchNo", new String[]{"100"}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("time", stockRecordFind.getTimeEnd(), ConditionItem.Relation.LESS_THAN);

        StockRecordView[] stockRecordViews = stockRecordService.find("WMS_Template", condition);
    }
}
