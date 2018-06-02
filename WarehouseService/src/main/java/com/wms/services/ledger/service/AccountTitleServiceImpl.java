package com.wms.services.ledger.service;

import com.wms.services.ledger.dao.AccountTitleDAO;
import com.wms.utilities.model.AccountTitle;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountTitleServiceImpl implements AccountTitleService {
    @Autowired
    AccountTitleDAO accountTitleDAO;

    @Transactional
    public int[] add(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException {
        for (int i = 0; i < accountTitles.length; i++) {


            //判断科目名称是否为空
            String accounttitleName = accountTitles[i].getName();        //获取科目名称
            if (accounttitleName == null || accounttitleName.trim().length() <= 0) {       //判断是否为空
                throw new WMSServiceException("科目名称不能为空！");
            }

            //判断科目编号是否为空 是否唯一
            AccountTitle[] judgeaccounttitleName = null;       //judgeactno用于判断科目编号是否存在
            String actno = accountTitles[i].getNo();        //获取科目编号
            if (actno == null || actno.trim().length() <= 0) {      //判断是否为空
                throw new WMSServiceException("科目编号不能为空！");
            }
            Condition condition = Condition.fromJson("{'conditions':[{'key':'No','values':['" + actno + "'],'relation':'EQUAL'}],'orders':[{'key':'No','order':'ASC'}]}");      //查找已有的科目编号中相同的
            judgeaccounttitleName = accountTitleDAO.find(accountBook, condition);       //将查询结果赋值给用于判断的judgeactno
            if (judgeaccounttitleName.length > 0) {        //长度 >0 说明有相同的 说明不唯一
                throw new WMSServiceException("科目编号:" + judgeaccounttitleName + " 已存在!");
            }

            //(判断科目类型是否为空) 是否符合条件 (只能填入0 1 3 4 5 6)
            int accounttitleType = accountTitles[i].getType();
            if (accounttitleType <= 0 || accounttitleType > 5) {
                throw new WMSServiceException("科目类型:" + accounttitleType + " 错误!(只能填入0 1 3 4 5 6)");
            }

            //(判断余额方向是否为空) 是否符合条件 (只能填入0 1)
            int accounttitleDirection = accountTitles[i].getDirection();
            if (accounttitleDirection != 0 && accounttitleDirection != 1) {
                throw new WMSServiceException("余额方向:" + accounttitleDirection + " 错误!(只能填入0 1)");
            }

            //判断启用是否符合条件(只能填入0 1) 默认为 1
            int accounttitleEnabled = accountTitles[i].getEnabled();
            if (accounttitleEnabled != 0 && accounttitleEnabled != 1) {
                throw new WMSServiceException("余额方向:" + accounttitleEnabled + " 错误!(只能填入0 1)");
            }
        }

        return accountTitleDAO.add(accountBook, accountTitles);
    }

    @Transactional
    public void update(String accountBook, AccountTitle[] accountTitles) throws WMSServiceException {

        for (int i = 0; i < accountTitles.length; i++) {
            AccountTitle[] judgeaccounttitleId = null;
            int accounttitleId = accountTitles[i].getId();
            if (accounttitleId == 0) {       //判断是否输入所要查询科目的ID int类型默认为0 数据库id默认从1开始 所以可以用0判断
                throw new WMSServiceException("若要修改必须输入科目ID!");
            }
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Id','values':['" + accounttitleId + "'],'relation':'EQUAL'}],'orders':[{'key':'Id','order':'ASC'}]}");      //查找已有ID中是否存在相同的
            try {
                judgeaccounttitleId = accountTitleDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (judgeaccounttitleId.length == 0) {        //用于判断的judgeactid若 <= 0 说明没有所要查询的ID
                throw new WMSServiceException("没有找到ID为:" + accounttitleId + " 的科目");
            }

            //判断科目编号是否为空 是否唯一
            String accounttitleNo = accountTitles[i].getNo();        //获取科目编号
            if (accounttitleNo == null || accounttitleNo.trim().length() == 0) {      //判断是否为空
                throw new WMSServiceException("科目编号不能为空！");
            }

            //id和科目编号都相同可以修改
            String actno1 = judgeaccounttitleId[0].getNo();
            if (!accounttitleNo.equals(actno1)) {        //科目编号不同也可以 只要不重复就行
                Condition acondition = Condition.fromJson("{'conditions':[{'key':'No','values':['" + accounttitleNo + "'],'relation':'EQUAL'}],'orders':[{'key':'No','order':'ASC'}]}");      //查找已有的科目编号中相同的
                AccountTitle[] judgeaccounttitleNo = null;       //judgeactno用于判断科目编号是否重复
                try {
                    judgeaccounttitleNo = accountTitleDAO.find(accountBook, acondition);       //将查询结果赋值给用于判断的judgeactno
                } catch (DatabaseNotFoundException ex) {
                    throw new WMSServiceException("Accountbook " + accountBook + " not found!");
                }
                if (judgeaccounttitleNo.length > 0) throw new WMSServiceException("科目编号:" + judgeaccounttitleNo + " 已存在!");
            }


            //判断科目名称是否为空
            String accounttitleName = accountTitles[i].getName();        //获取科目名称
            if (accounttitleName == null || accounttitleName.trim().length() <= 0) {       //判断是否为空
                throw new WMSServiceException("科目名称不能为空！");
            }


            //判断科目类型是否为空 是否符合条件 (只能填入0 1 2 3 4 5)
            int accounttitleType = accountTitles[i].getType();
            if (accounttitleType < 0 || accounttitleType > 5) {
                throw new WMSServiceException("科目类型:" + accounttitleType + " 错误!");
            }

            //判断余额方向是否为空 是否符合条件 (只能填入0 1)
            int accounttitleDirection = accountTitles[i].getDirection();
            if (accounttitleDirection != 0 && accounttitleDirection != 1) {
                throw new WMSServiceException("余额方向:" + accounttitleDirection + " 错误!(只能填入0 1 2 3 4 5)");
            }

            //判断启用是否符合条件(只能填入0 1) 默认为 1
            int accounttitleEnabled = accountTitles[i].getEnabled();
            if (accounttitleEnabled != 0 && accounttitleEnabled != 1) {
                throw new WMSServiceException("启用:" + accounttitleEnabled + " 错误!(只能填入0 1)");
            }

        }

        try {
            accountTitleDAO.update(accountBook, accountTitles);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            accountTitleDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }

    @Transactional
    public AccountTitle[] find(String accountBook, Condition cond) throws WMSServiceException {
        try {
            return this.accountTitleDAO.find(accountBook, cond);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook" + accountBook + "not found!");
        }
    }
}
