package com.wms.services.warehouse;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.*;
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, 2018);//设置年
        gc.set(Calendar.MONTH, 5);//这里0是1月..以此向后推
        gc.set(Calendar.DAY_OF_MONTH, 5);//设置天
        date = gc.getTime();
        long a = date.getTime();
        Timestamp time2 = new Timestamp(date.getTime());
        /*
        StockRecordService stockRecordService = applicationContext.getBean(StockRecordService.class);
        SupplierServices supplierServices = applicationContext.getBean(SupplierServices.class);
        Supplier supplier=new Supplier();
        supplier.setId(1);
        supplier.setNo("111123123131");
        supplier.setName("adsdasdadsadadssa");
        supplier.setWarehouseId(1);
        supplier.setCreatePersonId(19);
        supplier.setCreateTime(time2);
        //supplierServices.updateHistory("WMS_Template",new Supplier[]{supplier});
        */
        DeliveryOrderService deliveryOrderService = applicationContext.getBean(DeliveryOrderService.class);
        TransferAuto transferAuto=new TransferAuto();
        transferAuto.setPersonId(19);
        transferAuto.setWarehouseId(1);
        deliveryOrderService.transferAuto("WMS_Template",transferAuto);

        /*
        Object[] o=new Object[]{5,0};
        stockRecordService.findBySql("WMS_Template","SELECT loading.* from DeliveryOrderItemView as loading \n" +
                "WHERE loading.SupplyID =:a0 and (SELECT d.state from DeliveryOrderView as d WHERE d.id=loading.DeliveryOrderID)=:a1",o);
        StockRecordFind stockRecordFind = new StockRecordFind();
        stockRecordFind.setSupplyId(5);
        stockRecordFind.setStorageLocationId(21);
        stockRecordFind.setWarehouseId(1);
        stockRecordFind.setUnitAmount(new BigDecimal(1));
        Object[] stockRecordSource1 = stockRecordService.findCheckSupply("WMS_Template", stockRecordFind);
        */

        /*
        StockTakingOrderService stockTakingOrderService = applicationContext.getBean(StockTakingOrderService.class);
        StockTakingOrderItemService stockTakingOrderServiceItem = applicationContext.getBean(StockTakingOrderItemService.class);
        StockTakingOrderItemAdd stockTakingOrderItemAdd=new StockTakingOrderItemAdd();
        stockTakingOrderItemAdd.setStockTakingOrderId(1);
        stockTakingOrderItemAdd.setPersonId(19);
        stockTakingOrderItemAdd.setSupplyId(5);
        stockTakingOrderItemAdd.setWarehouseId(1);
        stockTakingOrderServiceItem.addStockTakingOrderItemSingle("WMS_Template",stockTakingOrderItemAdd);
        StockTakingOrder stockTakingOrder=new StockTakingOrder();
        stockTakingOrder.setId(1);
        stockTakingOrder.setNo("5115111111");
        stockTakingOrder.setCreatePersonId(19);
        stockTakingOrder.setWarehouseId(-100);
        stockTakingOrder.setCreateTime(time2);
        stockTakingOrderService.update("WMS_Template",new StockTakingOrder[]{stockTakingOrder});
        */
    }
}