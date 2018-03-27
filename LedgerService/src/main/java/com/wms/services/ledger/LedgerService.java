package com.wms.services.ledger;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.dao.AccountTitleDAOImpl;
import com.wms.services.ledger.dao.PersonDAO;
import com.wms.services.ledger.dao.PersonDAOImpl;
import com.wms.services.ledger.model.AccountTitle;
import com.wms.services.ledger.model.Person;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.PersonServiceImpl;
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
        /*AccountTitleDAO accountTitleDAO = new AccountTitleDAOImpl();
        AccountTitle accountTitle = new AccountTitle();
        accountTitle.setDirection(1);
        accountTitle.setEnabled(1);
        accountTitle.setName("xxx");
        accountTitle.setNo("132");
        accountTitle.setType(1);
        AccountTitle[] accountTitles = new AccountTitle[]{accountTitle};
        accountTitleDAO.add("WMS_Template",accountTitles);*/
        PersonService personService = new PersonServiceImpl();
        Person person = new Person();
        person.setAuthorityString("xxx");
        person.setName("xxx");
        person.setPassword("123");
        person.setRole("2");
        Person[] persons = new Person[]{person};
        personService.add("WMS_Template",persons);
        System.out.println("总账服务启动...");
    }
}
