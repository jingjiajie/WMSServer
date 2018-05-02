package com.wms.services.warehouse;
import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.utilities.model.StockRecord;
import com.wms.utilities.model.SupplierView;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.vaildator.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import com.wms.utilities.model.Supplier;

import java.math.BigDecimal;

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
        //Supplier supplier=new Supplier();
        //supplier.setName("12345667777777");
        //supplier.setNo("aaaaa7777111111");
        //supplier.setWarehouseId(1);
        //supplier.setCreatePersonId(19);
        //supplier.setWarehouseId(12);
        StockRecord stockRecord=new StockRecord();
        stockRecord.setSupplyId(5);
        stockRecord.setWarehouseId(-100);
        stockRecord.setStorageLocationId( 1);
        BigDecimal a=new BigDecimal(111);
       stockRecord.setAmount(a);
        //stockRecord.setUnit("aaa");
        stockRecord.setUnitAmount(a);
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
