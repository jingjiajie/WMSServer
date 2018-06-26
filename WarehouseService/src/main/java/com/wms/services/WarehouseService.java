package com.wms.services;
import com.wms.services.warehouse.datastructures.*;
import com.wms.services.warehouse.service.*;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        StockRecordService stockRecordService= applicationContext.getBean(StockRecordService.class);
        StockRecordFindByTime stockRecordFindByTime=new StockRecordFindByTime();
        stockRecordFindByTime.setSupplyId(5);
        StockRecordFindByTime stockRecordFindByTime1=new StockRecordFindByTime();
        stockRecordFindByTime1.setSupplyId(15);
        StockRecordFindByTime[] stockRecordFindByTimes={stockRecordFindByTime,stockRecordFindByTime1};

        TransferStock transferStock=new TransferStock();
transferStock.setSupplyId(36);
transferStock.setSourceStorageLocationId(33);
transferStock.setNewStorageLocationId(23180);
transferStock.setUnit("个");
transferStock.setAmount(new BigDecimal(2));
transferStock.setUnitAmount(new BigDecimal(10));
transferStock.setRelatedOrderNo("adasdadasdasdasdasdsada");
stockRecordService.RealTransformStock("WMS_Template",transferStock);
        stockRecordService.findByTime("WMS_Template",stockRecordFindByTimes);
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
        SessionFactory sessionFactory=applicationContext.getBean(SessionFactory.class);
        Session session=sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + "WMS_Template" + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException("WMS_Template");
        }
        Query query=null;
        String sql1="SELECT s1.* FROM StockRecord AS s1 \n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.storagelocationid,s2.supplyid  FROM StockRecord As s2\n" +
                "where   s2.SupplyID in (5,13,15)+and s2.time<=:endTime\n" +
                "GROUP BY s2.SupplyID,s2.BatchNo,s2.unit,s2.UnitAmount,s2.StorageLocationID) AS s3 \n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time\n" +
                "and s1.SupplyID=s3.supplyid and s1.StorageLocationID=s3.StorageLocationID   AND s1.BatchNo=s3.BatchNo";
        session.flush();
        query=session.createNativeQuery(sql1,StockRecord.class);
        query.setParameter("endTime",time2);
        StockRecord[] resultArray=null;
        List<StockRecord> resultList = query.list();
        resultArray = (StockRecord[]) Array.newInstance(StockRecord.class,resultList.size());
        resultList.toArray(resultArray);
        Map<Integer, List<StockRecord>> groupByPriceMap =
                Stream.of(resultArray).collect(Collectors.groupingBy(StockRecord::getSupplyId));

    }
}