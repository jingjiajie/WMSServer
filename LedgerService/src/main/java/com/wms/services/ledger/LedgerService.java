package com.wms.services.ledger;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.services.ledger.dao.AccountTitleDAOImpl;
import com.wms.services.ledger.dao.PersonDAO;
import com.wms.services.ledger.model.AccountTitle;
import com.wms.services.ledger.service.AccountTitleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.Scanner;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class LedgerService {
    public static void main(String args[]){
        ApplicationContext applicationContext = SpringApplication.run(LedgerService.class,args);
        System.out.println("总账服务启动...");
        /*AccountTitleService accountTitleService = applicationContext.getBean(AccountTitleService.class);
        AccountTitle accountTitle = new AccountTitle();
        accountTitle.setDirection(1);
        accountTitle.setEnabled(1);
        accountTitle.setName("aaaaaa");
        accountTitle.setNo("123456");
        accountTitle.setType(1);
        accountTitleService.add("WMS_Template", new AccountTitle[]{accountTitle});
        accountTitleService.remove("WMS_Template",new int[]{1});
        AccountTitle accountTitle = new AccountTitle();
        accountTitle.setDirection(1);
        accountTitle.setEnabled(1);
        accountTitle.setName("XXX");
        accountTitle.setNo("321");
        accountTitle.setType(1);
        accountTitle.setId(4);
        accountTitleService.update("WMS_Template",new AccountTitle[]{accountTitle});
        */
    }
}
