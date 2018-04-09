package com.wms.services.warehouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.model.Supplier;
import java.sql.Timestamp;
import java.util.Arrays;
import com.wms.services.warehouse.model.Warehouse;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.service.StorageAreaService;
import com.wms.services.warehouse.model.StorageArea;
import com.wms.services.warehouse.model.StorageLocation;
import com.wms.services.warehouse.service.StorageLocationService;
import com.wms.utilities.datastructures.Condition;
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(java.lang.String args[]){
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class,args);
        System.out.println("仓库服务启动...");
/*
        SupplierServices supplierServices= applicationContext.getBean(SupplierServices.class);
       // SupplierDAO supplierDAO=applicationContext.getBean(SupplierDAO.class);
        Supplier supplier=new Supplier();
        supplier.setName("1234566");
        supplier.setAddress("Asaddsadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        supplierServices.add("WMS_Template",new Supplier[]{supplier});
        Condition condition = Condition.fromJson("{'conditions':[{'key':'Name','values':['1'],'relation':'EQUAL'}],'orders':[{'key':'name','order':'ASC'}]}");
       //suppliers=supplierServices.find("WMS_Template",condition);
        //System.out.println(suppliers.length+"22929292929292929292929292929");
        */
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

        //com.wms.services.warehouse.service.WarehouseService warehouseService=applicationContext.getBean(com.wms.services.warehouse.service.WarehouseService.class);
       // Condition condition = Condition.fromJson("{'conditions':[{'key':'id','values':[1],'relation':'EQUAL'}]}");
       // Warehouse warehouses=new Warehouse();
       // warehouses=warehouseService.find("WMS_Template",condition);
        //System.out.printf(warehouses[0].getName());
       //  warehouses.setName("aaaa");
       // warehouses.setAddress("asaaaaaaaaaaaaaaaaaaa");
       // warehouseService.add("WMS_Template",new Warehouse[]{warehouses});
       // int[] ids={1};
       // warehouseService.remove("WMS_Template",ids);
    }
}
