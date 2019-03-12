package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.DestinationDAO;
import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DestinationServiceImpl implements DestinationService{
    @Autowired
    DestinationDAO destinationDAO;

    @Override
    public int[] add(String accountBook, Destination[] destinations) throws WMSServiceException {
        //数据验证
        Stream.of(destinations).forEach((destination) -> {
            new Validator("目的地名称").notEmpty().validate(destination.getName());
        });
        //名称查重
        for (int i = 0; i < destinations.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{destinations[i].getName()});
            if (destinationDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("目的地名称重复：" + destinations[i].getName());
            }
        }
        return destinationDAO.add(accountBook, destinations);
    }

    @Override
    public void update(String accountBook, Destination[] destinations) throws WMSServiceException {
        //数据验证
        Stream.of(destinations).forEach((destination) -> {
            new Validator("目的地名称").notEmpty().validate(destination.getName());
        });
        //名称查重
        for (int i = 0; i < destinations.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{destinations[i].getName()});
            cond.addCondition("id", new Integer[]{destinations[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if (destinationDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("目的地名称重复：" + destinations[i].getName());
            }
        }
        destinationDAO.update(accountBook, destinations);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            DestinationView[] destinationViews=destinationDAO.find(accountBook, new Condition().addCondition("id", id));
            if (destinationViews.length == 0) {
                throw new WMSServiceException(String.format("删除目的地不存在，请重新查询！(%d)", id));
            }
            if(destinationViews[0].getName().equals("供应商")){
                throw new WMSServiceException(String.format("“供应商”目的地不允许删除！"));
            }
        }

        try {
            destinationDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除目的地失败，如果目的地已经被引用，需要先删除引用该目的地的内容，才能删除该目的地");
        }
    }

    @Override
    public DestinationView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return destinationDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.destinationDAO.findCount(accountBook,cond);
    }




}
