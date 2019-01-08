package com.wms.services.salary.service;

import com.wms.services.salary.dao.SalaryItemDAO;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.SalaryItemTypeState;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.SalaryItem;
import com.wms.utilities.model.SalaryItemView;
import com.wms.utilities.model.SalaryType;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class SalaryItemServiceImpl implements SalaryItemService {
    @Autowired
    SalaryItemDAO salaryItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SalaryTypeService salaryTypeService;
    @Autowired
    PersonSalaryService personSalaryService;

    public int[] add(String accountBook, SalaryItem[] salaryItems) throws WMSServiceException {

        for (int i = 0; i < salaryItems.length; i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().validate(salaryItems[i].getName());
            if (salaryItems[i].getType() != SalaryItemTypeState.Formula) {
                Validator validator1 = new Validator("默认金额");
                validator1.notnull().notEmpty().validate(salaryItems[i].getDefaultAmount());
            }
        }

        for (int i = 0; i < salaryItems.length; i++) {
            for (int j = i + 1; j < salaryItems.length; j++) {
                String name = salaryItems[i].getName();
                if(name.equals("总金额")){throw new WMSServiceException("项目名称不允许为“总金额”");}
                if (name.equals(salaryItems[j].getName())) {
                    throw new WMSServiceException("薪金项目名称" + name + "在添加的列表中重复!");
                }
            }
        }
        //重复
        Stream.of(salaryItems).forEach((salaryItem) -> {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{salaryItem.getName()});
            cond.addCondition("warehouseId", salaryItem.getWarehouseId());
            cond.addCondition("salaryTypeId", salaryItem.getSalaryTypeId());
            if (salaryItemDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("薪金项目名称：" + salaryItem.getName() + "已经存在!");
            }
        });
        //判断标识符是否合法
        Stream.of(salaryItems).forEach((salaryItem) -> {
            if (salaryItem.getType() == SalaryItemTypeState.Formula) {
                if (salaryItem.getFormula() == null || salaryItem.getFormula().equals("")) {
                    throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的公式为空!");
                }
                if (salaryItem.getIdentifier() == null || salaryItem.getIdentifier().equals("")) {
                    throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的标识符为空!");
                }
                else {
                    if (!this.isString(salaryItem.getIdentifier())) {
                        throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的英文标识符不符合规则：以英文、下划线开头且只包括英文、下划线、数字组成!");
                    }
                    if (!this.isFormula(salaryItem.getFormula())) {
                        throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的公式不符合规则：以英文、下划线开头且只包括英文、下划线、数字和基本运算符合组成!");
                    }
                }
            }
        });
        //外键检测
        Stream.of(salaryItems).forEach(
                (salaryItem) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{salaryItem.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", salaryItem.getWarehouseId()));
                    }
                    if (this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{salaryItem.getSalaryTypeId()})).length == 0) {
                        throw new WMSServiceException(String.format("类别不存在，请重新提交！(%d)", salaryItem.getSalaryTypeId()));
                    }
                }
        );
          int[] ids = salaryItemDAO.add(accountBook, salaryItems);
        return ids;
    }

    @Transactional
    public void update(String accountBook, SalaryItem[] salaryItems) throws WMSServiceException {
        for (int i = 0; i < salaryItems.length; i++) {
            Validator validator = new Validator("薪金项目名称");
            validator.notnull().notEmpty().validate(salaryItems[i].getName());
            if (salaryItems[i].getType() != SalaryItemTypeState.Formula) {
                Validator validator1 = new Validator("默认金额");
                validator1.notnull().notEmpty().validate(salaryItems[i].getDefaultAmount());
            }
        }
        for (int i = 0; i < salaryItems.length; i++) {
            for (int j = i + 1; j < salaryItems.length; j++) {
                String name = salaryItems[i].getName();
                if(name.equals("总金额")){throw new WMSServiceException("项目名称不允许为“总金额”");}
                if (name.equals(salaryItems[j].getName())) {
                    throw new WMSServiceException("薪金项目名称" + name + "在添加的列表中重复!");
                }
            }
        }
        Stream.of(salaryItems).forEach(
                (salaryItem) -> {
                    if (this.salaryItemDAO.find(accountBook,
                            new Condition().addCondition("id", salaryItem.getId())).length == 0) {
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)", salaryItem.getId()));
                    }
                }
        );
        //判断标识符是否合法
        Stream.of(salaryItems).forEach((salaryItem) -> {
            if (salaryItem.getType() == SalaryItemTypeState.Formula) {
                if (salaryItem.getFormula() == null || salaryItem.getFormula().equals("")) {
                    throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的公式为空!");
                }
                if (salaryItem.getIdentifier() == null || salaryItem.getIdentifier().equals("")) {
                    throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的标识符为空!");
                }
                else {
                    if (!this.isString(salaryItem.getIdentifier())) {
                        throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的英文标识符不符合规则：以英文、下划线开头且只包括英文、下划线、数字组成!");
                    }
                    if (!this.isFormula(salaryItem.getFormula())) {
                        throw new WMSServiceException("薪金项目：" + salaryItem.getName() + "的公式不符合规则：以英文、下划线开头且只包括英文、下划线、数字和基本运算符合组成!");
                    }
                }
            }
        });
        for (int i = 0; i < salaryItems.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{salaryItems[i].getName()});
            cond.addCondition("warehouseId", salaryItems[i].getWarehouseId());
            cond.addCondition("id", new Integer[]{salaryItems[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            cond.addCondition("salaryTypeId", salaryItems[i].getSalaryTypeId());
            if (salaryItemDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("项目名称重复：" + salaryItems[i].getName());
            }
        }
        //外键检测
        Stream.of(salaryItems).forEach(
                (salaryItem) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{salaryItem.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", salaryItem.getWarehouseId()));
                    }
                    if (this.salaryTypeService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{salaryItem.getSalaryTypeId()})).length == 0) {
                        throw new WMSServiceException(String.format("类别不存在，请重新提交！(%d)", salaryItem.getWarehouseId()));
                    }
                }
        );
        salaryItemDAO.update(accountBook, salaryItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (salaryItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            salaryItemDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供薪金类别失败，如果薪金类别已经被引用，需要先删除引用的内容，才能删除薪金类别！");
        }
    }

    public SalaryItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.salaryItemDAO.find(accountBook, cond);
    }

    public SalaryItem[] findTable(String accountBook, Condition cond) throws WMSServiceException {
        return this.salaryItemDAO.findTable(accountBook, cond);
    }

    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.salaryItemDAO.findCount(database, cond);
    }

    private Boolean isString(String str) {
        Boolean bl = false;
        //首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
        Pattern pt = Pattern.compile("^[0-9a-zA-Z_]\\w*$");
        //然后使用Matcher来对比目标字符串与上面解释得结果
        Matcher mt = pt.matcher(str);
        //如果能够匹配则返回true。实际上还有一种方法mt.find()，某些时候，可能不是比对单一的一个字符串，
        //可能是一组，那如果只要求其中一个字符串符合要求就可以用find方法了.
        if (mt.matches()) {
            bl = true;
        }
        return bl;
    }

    private Boolean isFormula(String str) {
        Boolean bl = false;
        //首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
        Pattern pt = Pattern.compile("^[\\w\\.\\/\\*\\-\\+\\(\\)\\s\\=]*$");
        //然后使用Matcher来对比目标字符串与上面解释得结果
        Matcher mt = pt.matcher(str);
        //如果能够匹配则返回true。实际上还有一种方法mt.find()，某些时候，可能不是比对单一的一个字符串，
        //可能是一组，那如果只要求其中一个字符串符合要求就可以用find方法了.
        if (mt.matches()) {
            bl = true;
        }
        return bl;
    }
}
