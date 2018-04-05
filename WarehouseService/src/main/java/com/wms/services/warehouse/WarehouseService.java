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
import com.wms.services.warehouse.dao.SupplierDAO;
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(java.lang.String args[]){
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class,args);
        System.out.println("仓库服务启动...");
/*

        java.lang.String a="asaas";

        SupplierServices supplierServices= applicationContext.getBean(SupplierServices.class);
        SupplierDAO supplierDAO=applicationContext.getBean(SupplierDAO.class);
        Supplier supplier=new Supplier();
        supplier.setName("asdadsads");
        supplier.setAddress("Asaddsadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        supplier.setWarehouseId(1);
        supplier.setCreatePersonId(19);
        supplier.setCreateTime(new Timestamp(System.currentTimeMillis()));
        supplierServices.add("WMS_Template",new Supplier[]{supplier});*/

    }
}
