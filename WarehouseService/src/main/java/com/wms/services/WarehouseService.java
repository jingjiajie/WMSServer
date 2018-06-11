package com.wms.services;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.*;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
       // TestService testService = applicationContext.getBean(TestService.class);
        //testService.testTransfer();
        StockRecordService stockRecordService = applicationContext.getBean(StockRecordService.class);
        StockRecordFindByTime stockRecordFindByTime=new StockRecordFindByTime();
        stockRecordFindByTime.setStorageLocationId(new Integer(1));
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR,2018);//设置年
        gc.set(Calendar.MONTH, 4);//这里0是1月..以此向后推
        gc.set(Calendar.DAY_OF_MONTH, 32);//设置天
        gc.set(Calendar.HOUR_OF_DAY,5);//设置小时
        gc.set(Calendar.MINUTE, 7);//设置分
        gc.set(Calendar.SECOND, 6);//设置秒
        gc.set(Calendar.MILLISECOND,200);//设置毫秒
        date = gc.getTime();
        Timestamp time2 =new Timestamp(date.getTime());
        //stockRecordFindByTime.setEndTime(time2);
        //StockRecord[] stockRecords= stockRecordService.findByTime("WMS_Template",stockRecordFindByTime);
        //StockRecordViewNewest[] stockRecordViewNewest=stockRecordService.findNewest("WMS_Template",new Condition());
    }
}