package com.wms.services.warehouse;

import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.service.SupplierServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.sql.Timestamp;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(String args[]){
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class,args);
        System.out.println("仓库服务启动...");
        double d1 = 133999441.132;
        double d2 = 44123.031;
        java.lang.String a="asaas";

        SupplierServices supplierServices= applicationContext.getBean(SupplierServices.class);
        SupplierDAO supplierDAO=applicationContext.getBean(SupplierDAO.class);
        Supplier supplier=new Supplier();
        supplier.setName("asdadsads");
        supplier.setAddress("Asaddsada");
        supplier.setCreateTime(new Timestamp(System.currentTimeMillis()));
        supplierServices.add("WMS_Template",new Supplier[]{supplier});
    }
}
