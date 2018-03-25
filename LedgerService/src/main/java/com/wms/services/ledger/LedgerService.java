package com.wms.services.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class LedgerService {
    public static void main(String args[]){
        SpringApplication.run(LedgerService.class,args);
        System.out.println("总账服务启动...");
    }
}
