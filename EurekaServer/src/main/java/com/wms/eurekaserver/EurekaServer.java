package com.wms.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {
    public static void main(String args[]){
        ApplicationContext applicationContext = SpringApplication.run(EurekaServer.class,args);
        System.out.println("服务注册中心启动...");
    }
}
