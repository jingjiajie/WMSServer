package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.PersonDAO;
import com.wms.utilities.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonDAO personDAO;

    @Transactional
    public int[] add(String accountBook, Person[] persons) throws WMSServiceException {

        for (int i = 0; i < persons.length; i++) {

            String personName = persons[i].getName();
            if (personName == null || personName.trim().length() <= 0) {       //判断是否输入姓名
                throw new WMSServiceException("人员姓名不能为空!");
            }

            String personPassword = persons[i].getPassword();
            if (personPassword == null || personPassword.trim().length() <= 0) {       //判断是否输入密码
                throw new WMSServiceException("密码不能为空!");
            }

            String personRole = persons[i].getRole();
            if (personRole == null || personRole.trim().length() <= 0) {       //判断是否输入角色
                throw new WMSServiceException("角色不能为空!");
            }

            persons[i].setAuthorityString("AAA");//预设权限字符串

        }
        return personDAO.add(accountBook, persons);
    }

    @Transactional
    public void update(String accountBook, Person[] persons) throws WMSServiceException {

        for (int i = 0; i < persons.length; i++) {

            int actid = persons[i].getId();//获取要修改信息的Id
            if (actid == 0) {       //判断id，参考AccountTitle
                throw new WMSServiceException("修改失败，所选人员条目不存在!");
            }

            String personName = persons[i].getName();
            if (personName == null || personName.trim().length() <= 0) {       //判断是否输入姓名
                throw new WMSServiceException("人员姓名不能为空!");
            }

            String personPassword = persons[i].getPassword();
            if (personPassword == null || personPassword.trim().length() <= 0) {       //判断是否输入密码
                throw new WMSServiceException("密码不能为空!");
            }

            String personRole = persons[i].getRole();
            if (personRole == null || personRole.trim().length() <= 0) {       //判断是否输入角色
                throw new WMSServiceException("角色不能为空!");
            }

        }

        personDAO.update(accountBook, persons);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        personDAO.remove(accountBook, ids);
    }

    @Transactional
    public PersonView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.personDAO.find(accountBook, cond);
    }

    @Transactional
    public long findCount(String accountBook,Condition cond) throws WMSServiceException{
        return this.personDAO.findCount(accountBook,cond);
    }
}