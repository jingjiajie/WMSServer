package com.wms.utilities.service;

import com.wms.utilities.dao.CommonDataDAO;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.CommonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
@Transactional
public class CommonDataServiceImpl implements CommonDataService {
    @Autowired
    CommonDataDAO commonDataDAO;

    @Override
    public int[] add(String accountBook, CommonData[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj) -> {
            if (this.commonDataDAO.find(accountBook, new Condition().addCondition("key", obj.getKey())).length > 0) {
                throw new WMSServiceException(String.format("公共数据Key:%s 已存在，添加失败"));
            }
            if(obj.getTime() == null){
                obj.setTime(new Timestamp(System.currentTimeMillis()));
            }
        });
        return this.commonDataDAO.add(accountBook, objs);
    }

    @Override
    public void update(String accountBook, CommonData[] objs) throws WMSServiceException {
        Stream.of(objs).forEach((obj) -> {
            if (this.commonDataDAO.find(accountBook, new Condition()
                    .addCondition("key", obj.getKey())
                    .addCondition("id", obj.getId(), ConditionItem.Relation.NOT_EQUAL)).length > 0) {
                throw new WMSServiceException(String.format("公共数据Key:%s 已存在，修改失败"));
            }
        });
        this.commonDataDAO.update(accountBook, objs);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        this.commonDataDAO.remove(accountBook, ids);
    }

    @Override
    public CommonData[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.commonDataDAO.find(accountBook, cond);
    }
}
