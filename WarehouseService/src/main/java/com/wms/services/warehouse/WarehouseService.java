package com.wms.services.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(String args[]){
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class,args);
        System.out.println("仓库服务启动...");
    }
}
