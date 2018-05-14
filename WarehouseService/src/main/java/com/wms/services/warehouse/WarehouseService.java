package com.wms.services.warehouse;
import com.wms.services.warehouse.datastructures.StockRecordFind;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.services.warehouse.service.StockTakingOrderItemService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockTakingOrderItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import javax.management.relation.Relation;
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
    public static void main(java.lang.String args[]){
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class,args);
        System.out.println("仓库服务启动...");

        //Validator validator=new Validator("123");
        //int a[]={1 ,200};
        //validator.in(a);
        //validator.min(5).in(a).validate(1);
        //validator.validate("1000.1");
        StockRecordService stockRecordService= applicationContext.getBean(StockRecordService.class);

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR,2020);//设置年
        gc.set(Calendar.MONTH, 8);//这里0是1月..以此向后推
        gc.set(Calendar.DAY_OF_MONTH, 29);//设置天
        gc.set(Calendar.HOUR_OF_DAY,5);//设置小时
        gc.set(Calendar.MINUTE, 7);//设置分
        gc.set(Calendar.SECOND, 6);//设置秒
        gc.set(Calendar.MILLISECOND,200);//设置毫秒
        date = gc.getTime();
        long a=date.getTime();
        String timestamp = String.valueOf(date.getTime()/1000);
        Timestamp time2 =new Timestamp(date.getTime());
/*
        StockRecordFind stockRecordFind=new StockRecordFind();
        //stockRecordFind.setUnit("个");
        stockRecordFind.setSupplyId(7);
        stockRecordFind.setWarehouseId(5);
        stockRecordFind.setStorageLocationId(26);
        stockRecordFind.setTimeEnd(time2);
        //stockRecordFind.setUnitAmount(new BigDecimal(5));
        //stockRecordFind.setSupplyId(5);
        stockRecordService.find("WMS_Template",stockRecordFind);

*/
        Condition condition=new Condition().addCondition("warehouseId",new Integer[]{5}).addCondition("storageLocationId",new Integer[]{26}).addCondition("supplyId",new Integer[]{7});


        condition.addCondition("unit",new String[]{"个"}, ConditionItem.Relation.NOT_EQUAL);

        condition.addCondition("unitAmount",new BigDecimal[]{new BigDecimal(10)}, ConditionItem.Relation.NOT_EQUAL);

      condition.addCondition("batchNo",new String[]{"100"}, ConditionItem.Relation.NOT_EQUAL);

       StockRecordView[] stockRecordViews=stockRecordService.find("WMS_Template",condition);



      //  StockTakingOrderItemService stockTakingOrderItemService=applicationContext.getBean(StockTakingOrderItemService.class);
      // StockTakingOrderItemAdd stockTakingOrderItemAdd=new StockTakingOrderItemAdd();
       //stockTakingOrderItemAdd.setWarehouseId(-1);
       //stockTakingOrderItemAdd.setMode(0);
       //stockTakingOrderItemAdd.setStockTakingOrderId(1);
      // stockTakingOrderItemAdd.setSupplyId(5);


        //StockTakingOrderItem stockTakingOrderItem=new StockTakingOrderItem();
       // stockTakingOrderItem.setStockTakingOrderId(stockTakingOrderItemAdd.getStockTakingOrderId());
       // stockTakingOrderItem.setPersonId(stockTakingOrderItemAdd.getPersonId());
       // stockTakingOrderItem.setSupplyId(stockTakingOrderItemAdd.getSupplyId());
        //stockTakingOrderItemService.addStockTakingOrderItemSingle("WMS_Template", stockTakingOrderItemAdd);
       // StockRecordFind stockRecordFind=new StockRecordFind();


        //TransferStock transferStock=new TransferStock();
        //transferStock.setAmount(new BigDecimal(-100));
        //transferStock.setUnit("111");
       // transferStock.setUnitAmount(new BigDecimal(5));
        //transferStock.setSupplyId(5);
        //transferStock.setRelatedOrderNo("46416512653101");
        //transferStock.setSourceStorageLocationId(1);
       // transferStock.setNewStorageLocationId(5);
       // stockRecordService.addAmount("WMS_Template",transferStock);
        //stockRecordService.transformStock("WMS_Template",transferStock);
        //Supplier supplier=new Supplier();
        //supplier.setName("12345667777777");
        //supplier.setNo("aaaaa7777111111");
        //supplier.setWarehouseId(1);
        //supplier.setCreatePersonId(19);
        //supplier.setWarehouseId(12);
       // StockRecord stockRecord=new StockRecord();
       // stockRecord.setSupplyId(5);
        //stockRecord.setWarehouseId(-100);
       // stockRecord.setStorageLocationId( 1);
        //BigDecimal a=new BigDecimal(111);
       //stockRecord.setAmount(a);
        //stockRecord.setUnit("aaa");
        //stockRecord.setUnitAmount(a);
       // stockRecordService.add("WMS_Template",new StockRecord[]{stockRecord});

//        supplier.setAddress("Asaddsadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
      //supplierServices.add("WMS_Template",new Supplier[]{supplier});
        //int[] a={31, 32};
        //supplierServices.remove("WMS_Template",a);
        //Condition condition = new Condition();
       // condition.addCondition("name",new String[]{"asdadsads11"});
        //suppliers=supplierDAO.find("WMS_Template",condition);
       // Validator validator=new Validator("qqq");
        //validator.min(111).validate(suppliers[0].getInvoiceDelayMonth());
        //System.out.println(suppliers.length+"22929292929292929292929292929");

/*
        MaterialService materialService= applicationContext.getBean(MaterialService.class);
        Material material=new Material();
        material.setName("1222");
        material.setWarehouseId(3);
        material.setNo("32323");
        materialService.add("WMS_Template",new Material[]{material});
        System.out.println("添加完成！");
        */


        /*
                SupplyService supplyService= applicationContext.getBean(SupplyService.class);
        Supply supply=new Supply();
        //supply.setWarehouseId(4);
        supply.setSupplierId(4);
        supply.setMaterialId(3);
        //supply.setCreatePersonId(19);
        //supply.setCreateTime(new Timestamp(System.currentTimeMillis()));
        supplyService.add("WMS_Template",new Supply[]{supply});
        System.out.println("添加完成！");
        MaterialService materialService= applicationContext.getBean(MaterialService.class);
        MaterialDAO materialDAO=applicationContext.getBean(MaterialDAO.class);
        Material material=new Material();
        material.setName("ewe");
        material.setWarehouseId(3);
        material.setNo("32323");
        materialService.add("WMS_Template",new Material[]{material});
        */
        //Condition condition = Condition.fromJson("{'conditions':[{'key':'WarehouseId','values':['1'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
        //suppliers=supplierServices.find("WMS_Template",condition);
/*

        StorageAreaService storageAreaService= applicationContext.getBean(StorageAreaService.class);
        if(storageAreaService==null){
            System.out.printf("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        StorageArea[] storageAreas=null;
        Condition condition1 = Condition.fromJson("{'conditions':[{'key':'id','values':[1],'relation':'EQUAL'}]}");
        storageAreas=storageAreaService.find("WMS_Template",condition1);
        if(storageAreas==null){
            System.out.printf("saaaaaaaaaaaaaaaaaaaaa");
        }
        //storageArea.setNo("111");
        //storageArea.setName("111");
       // storageAreaService.add("WMS_Template", new StorageArea[]{storageArea});
        //Condition condition = Condition.fromJson("{'conditions':[{'key':'name','values':['1'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
        //storageAreas= storageAreaService.find("WMS_Template",condition);

*/
        //StorageLocationService storageLocationService=applicationContext.getBean(StorageLocationService.class);
        //StorageLocation storageLocation=new StorageLocation();
        //storageLocation.setName("adasasasd135");
       // storageLocation.setNo("12211356");
        //storageLocation.setStorageAreaId(1);
        //storageLocationService.add("WMS_Template",new StorageLocation[] {storageLocation});
        //Condition condition = Condition.fromJson("{'conditions':[{'key':'No','values':['122'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
       // storageLocations=storageLocationService.find("WMS_Template",condition);
       // for(int i=0;i<storageLocations.length;i++)
        //{System.out.println(storageLocations[i].getName());}
    }
}
