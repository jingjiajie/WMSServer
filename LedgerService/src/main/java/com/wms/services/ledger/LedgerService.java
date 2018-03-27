package com.wms.services.ledger;

import com.wms.services.ledger.model.Person;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.PersonServiceImpl;
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
public class LedgerService {
    public static void main(String args[]){
        ApplicationContext applicationContext = SpringApplication.run(LedgerService.class,args);
        System.out.println("总账服务启动...");
        Person person = new Person();
        person.setName("小黑");
        person.setPassword("987654321");
        person.setRole("管理员");
        person.setAuthorityString("HEHE");
        PersonService personService = applicationContext.getBean(PersonService.class);
        personService.add("WMS_Template",new Person[]{person});
        System.out.println("添加完成！");
    }
}
