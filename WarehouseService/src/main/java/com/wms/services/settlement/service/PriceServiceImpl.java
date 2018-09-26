package com.wms.services.settlement.service;


import com.wms.services.settlement.dao.PriceDAO;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.Price;
import com.wms.utilities.model.PriceView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class PriceServiceImpl implements PriceService{
    @Autowired
    PriceDAO priceDAO;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, Price[] prices) throws WMSServiceException
    {
        this.validateEntities(accountBook,prices);
        return priceDAO.add(accountBook,prices);
    }

    @Override
    public void update(String accountBook, Price[] prices) throws WMSServiceException
    {
        this.validateEntities(accountBook,prices);
        priceDAO.update(accountBook, prices);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (priceDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除价格不存在，请重新查询！(%d)", id));
                }
            }

            priceDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除价格信息失败，如果价格信息已经被引用，需要先删除引用的内容，才能删除该价格信息！");
        }
    }

    @Override
    public PriceView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.priceDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,Price[] prices) throws WMSServiceException{
        Stream.of(prices).forEach((price -> {
            new Validator("物流阈值1").notEmpty().validate(price.getLogisticsThreshold1());
            new Validator("物流单价1").notEmpty().validate(price.getLogisticsUnitPrice1());
            new Validator("面积单价").notEmpty().validate(price.getAreaUnitPrice());
            this.idChecker.check(SupplyService.class,accountBook,price.getSupplyId(),"供货ID");

            if(price.getLogisticsThreshold2()!=null
                    &&price.getLogisticsThreshold1().compareTo(price.getLogisticsThreshold2())>=0){
                throw new WMSServiceException("物流阈值2的值不能小于物流阈值1！");
            }
            if(price.getLogisticsThreshold3()!=null
                    &&price.getLogisticsThreshold1().compareTo(price.getLogisticsThreshold3())>=0
                    &&price.getLogisticsThreshold2().compareTo(price.getLogisticsThreshold3())>=0){
                throw new WMSServiceException("物流阈值3的值不能小于物流阈值1！");
            }
        }));

    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.priceDAO.findCount(database,cond);
    }
}
