package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.PersonDAO;
import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonDAO personDAO;

    @Transactional
    public int[] add(String accountBook, Person[] persons) throws WMSServiceException{

        for (int i = 0;i < persons.length;i ++) {

            String personName = persons[i].getName();
            if (personName == null && personName.trim().length() <=0) {       //判断是否输入姓名
                throw new WMSServiceException("人员姓名不能为空!");
            }

            String personPassword = persons[i].getPassword();
            if (personPassword == null &&personPassword.trim().length() <=0) {       //判断是否输入密码
                throw new WMSServiceException("密码不能为空!");
            }

            String personRole = persons[i].getRole();
            if (personRole == null && personRole.trim().length() <=0) {       //判断是否输入角色
                throw new WMSServiceException("角色不能为空!");
            }

            persons[i].setAuthorityString("asd");//预设权限字符串

        }
        try{
            return personDAO.add(accountBook,persons);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("账套 "+accountBook+" 不存在!");
        }
    }

    @Transactional
    public void update(String accountBook, Person[] persons) throws WMSServiceException{

        for (int i = 0;i < persons.length;i ++) {

            int actid = persons[i].getId();//获取要修改信息的Id
            if (actid == 0) {       //判断id，参考AccountTitle
                throw new WMSServiceException("修改失败，所选人员条目不存在!");
            }

            String personName = persons[i].getName();
            if (personName == null && personName.trim().length() <=0) {       //判断是否输入姓名
                throw new WMSServiceException("人员姓名不能为空!");
            }

            String personPassword = persons[i].getPassword();
            if (personPassword == null &&personPassword.trim().length() <=0) {       //判断是否输入密码
                throw new WMSServiceException("密码不能为空!");
            }

            String personRole = persons[i].getRole();
            if (personRole == null && personRole.trim().length() <=0) {       //判断是否输入角色
                throw new WMSServiceException("角色不能为空!");
            }

        }

        try {
            personDAO.update(accountBook, persons);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("账套 "+accountBook+" 不存在!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            personDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("账套 " + accountBook + " 不存在!");
        }
    }

    @Transactional
    public Person[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.personDAO.find(accountBook, cond);
    }
}
