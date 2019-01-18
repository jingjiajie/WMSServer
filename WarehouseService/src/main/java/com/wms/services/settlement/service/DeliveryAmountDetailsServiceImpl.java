package com.wms.services.settlement.service;


import com.wms.services.settlement.dao.DeliveryAmountDetailsDAO;
import com.wms.services.warehouse.service.DestinationService;
import com.wms.services.warehouse.service.SupplyService;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryAmountDetails;
import com.wms.utilities.model.DeliveryAmountDetailsView;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryNoteItem;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DeliveryAmountDetailsServiceImpl implements DeliveryAmountDetailsService {
    @Autowired
    DeliveryAmountDetailsDAO deliveryAmountDetailsDAO;
    @Autowired
    IDChecker idChecker;
    @Autowired
    SummaryNoteItemService summaryNoteItemService;
    @Autowired
    SummaryDetailsService summaryDetailsService;

    @Override
    public int[] add(String accountBook, DeliveryAmountDetails[] deliveryAmountDetails) throws WMSServiceException {
        this.validateEntities(accountBook,deliveryAmountDetails);
        return deliveryAmountDetailsDAO.add(accountBook, deliveryAmountDetails);
    }

    @Override
    public void update(String accountBook, DeliveryAmountDetails[] deliveryAmountDetails) throws WMSServiceException {
        this.validateEntities(accountBook,deliveryAmountDetails);
        deliveryAmountDetailsDAO.update(accountBook, deliveryAmountDetails);
        this.updateSummary(accountBook,deliveryAmountDetails);
    }

    public void updateSummary(String accountBook,DeliveryAmountDetails[] deliveryAmountDetails)throws WMSServiceException{
        //按汇总单条目分组
        Map<Integer, List<DeliveryAmountDetails>> groupBySummaryNoteItemId =
                Stream.of(deliveryAmountDetails).collect(Collectors.groupingBy(DeliveryAmountDetails::getSummaryNoteItemId));

        Iterator<Map.Entry<Integer, List<DeliveryAmountDetails>>> groupBySummaryNoteItemIds = groupBySummaryNoteItemId.entrySet().iterator();
        while (groupBySummaryNoteItemIds.hasNext()) {
            Map.Entry<Integer, List<DeliveryAmountDetails>> groupByDestination = groupBySummaryNoteItemIds.next();
            Integer summaryNoteItemId = groupByDestination.getKey();
            List<DeliveryAmountDetails> curDeliveryAmountDetailsLists=groupByDestination.getValue();
            DeliveryAmountDetails[] curDeliveryAmountDetails=curDeliveryAmountDetailsLists.toArray(new DeliveryAmountDetails[curDeliveryAmountDetailsLists.size()]);

            //TODO 更新汇总单条目，供货商级
            SummaryNoteItem[] summaryNoteItems = summaryNoteItemService.findTable(accountBook, new Condition().addCondition("id", summaryNoteItemId));
            if (summaryNoteItems.length != 0) {
                BigDecimal newDeliveryAmount=BigDecimal.ZERO;
                DeliveryAmountDetails[] allSupplierDelivery= this.deliveryAmountDetailsDAO.findTable(accountBook,new Condition().addCondition("summaryNoteItemId",summaryNoteItemId));
                if (allSupplierDelivery.length>0){
                    for (int i=0;i<allSupplierDelivery.length;i++){
                        newDeliveryAmount=newDeliveryAmount.add(allSupplierDelivery[i].getDeliveryAmount());
                    }
                }
                summaryNoteItems[0].setTotalDeliveryAmount(newDeliveryAmount);
                this.summaryNoteItemService.update(accountBook,summaryNoteItems);
            }
            //TODO 更新面积详情，供货级
            //二级分组按供货id分组
            if (curDeliveryAmountDetails.length>0) {
                Map<Integer, List<DeliveryAmountDetails>> groupBySupplyId =
                        Stream.of(curDeliveryAmountDetails).collect(Collectors.groupingBy(DeliveryAmountDetails::getSupplyId));

                Iterator<Map.Entry<Integer, List<DeliveryAmountDetails>>> groupBySupplyIds = groupBySupplyId.entrySet().iterator();
                while (groupBySupplyIds.hasNext()) {
                    Map.Entry<Integer, List<DeliveryAmountDetails>> group = groupBySupplyIds.next();
                    Integer supplyId = group.getKey();

                    //供货级supplyId
                    SummaryDetails[] summaryDetails1 = this.summaryDetailsService.findTable(accountBook, new Condition()
                            .addCondition("summaryNoteItemId", summaryNoteItemId)
                            .addCondition("supplyId", supplyId));

                    if (summaryDetails1.length > 0) {
                        BigDecimal thDeliveryAmount=BigDecimal.ZERO;

                        DeliveryAmountDetails[] theAllSupplyDelivery= this.deliveryAmountDetailsDAO.findTable(accountBook,new Condition()
                                .addCondition("summaryNoteItemId",summaryNoteItemId)
                                .addCondition("supplyId", supplyId));

                        if (theAllSupplyDelivery.length>0){
                            for (int i=0;i<theAllSupplyDelivery.length;i++){
                                thDeliveryAmount=thDeliveryAmount.add(theAllSupplyDelivery[i].getDeliveryAmount());
                            }
                        }
                        summaryDetails1[0].setDeliveryAmount(thDeliveryAmount);
                        //调用更新
                        this.summaryDetailsService.update(accountBook,summaryDetails1);
                    }
                }
            }
        }
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (deliveryAmountDetailsDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除目的地不存在，请重新查询！(%d)", id));
            }
        }

        try {
            deliveryAmountDetailsDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除目的地失败，如果目的地已经被引用，需要先删除引用该目的地的内容，才能删除该目的地");
        }
    }

    @Override
    public DeliveryAmountDetailsView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return deliveryAmountDetailsDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.deliveryAmountDetailsDAO.findCount(accountBook,cond);
    }

    private void validateEntities(String accountBook, DeliveryAmountDetails[] deliveryAmountDetails) {
        Stream.of(deliveryAmountDetails).forEach((deliveryAmountDetails1 -> {
            //数据验证
            new Validator("出货数量").min(0).notEmpty().validate(deliveryAmountDetails1.getDeliveryAmount());

            //验证外键
            this.idChecker.check(DestinationService.class, accountBook, deliveryAmountDetails1.getDestinationId(), "关联目的地");
            this.idChecker.check(SupplyService.class, accountBook, deliveryAmountDetails1.getSupplyId(), "关联供货信息");
            this.idChecker.check(SummaryNoteItemService.class, accountBook, deliveryAmountDetails1.getSummaryNoteItemId(), "关联汇总单条目");

        }));
    }
}
