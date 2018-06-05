package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.DeliveryOrderItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.model.Warehouse;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;
import java.sql.Timestamp;

@Service
@Transactional
public class DeliveryOrderItemServiceImpl implements DeliveryOrderItemService{
    @Autowired
    DeliveryOrderItemDAO deliveryOrderItemDAO;
    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;
    @Autowired
    StockRecordService stockRecordService;

    @Override
    public int[] add(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        if (deliveryOrderItems.length == 0) return new int[]{};
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        //验证字段
        this.validateEntities(accountBook,deliveryOrderItems);
        //修改库存
        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            if (deliveryOrderItem.getRealAmount().compareTo(new BigDecimal(0))==0) {

                //没有实际数输入就只改变可用数量
                TransferStock transferStock = new TransferStock();
                BigDecimal amount = new BigDecimal(0);
                BigDecimal realAmount = amount.subtract(deliveryOrderItem.getScheduledAmount());//计划出库数量or实际出库数量
                transferStock.setModifyAvailableAmount(realAmount);
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                //transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);//仅修改可用数量
            }
            else{
                //先移动
                TransferStock transferStock = new TransferStock();
                transferStock.setAmount(new BigDecimal(0).subtract(deliveryOrderItem.getRealAmount()));
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                this.stockRecordService.addAmount(accountBook, transferStock);//直接改数

                //再改可用数量
                TransferStock rdTransferStock = new TransferStock();
                rdTransferStock.setModifyAvailableAmount(deliveryOrderItem.getScheduledAmount().subtract(deliveryOrderItem.getRealAmount()));
                rdTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                rdTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                rdTransferStock.setUnit(deliveryOrderItem.getUnit());
                rdTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, rdTransferStock);//直接改可用数量
            }
        });
        //添加到数据库中
        int[] ids = this.deliveryOrderItemDAO.add(accountBook, deliveryOrderItems);
        return ids;//仅返回iDs
        //return this.deliveryOrderItemDAO.add(accountBook,deliveryOrderItems);
    }

    @Override
    public void update(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        //数据验证
        this.validateEntities(accountBook,deliveryOrderItems);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            DeliveryOrderItemView[] foundOriItems = this.deliveryOrderItemDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{deliveryOrderItem.getId()}));
            if(foundOriItems.length == 0) throw new WMSServiceException(String.format("出库单条目不存在，请重新提交！",deliveryOrderItem.getId()));//排除异常
            DeliveryOrderItemView oriItemView = foundOriItems[0];

            //无法中途修改出库库位
            if (deliveryOrderItem.getSourceStorageLocationId()!=oriItemView.getSourceStorageLocationId())
            {
                throw new WMSServiceException("无法修改移库单条目的目标库位:(%s)，如要操作请新建移库单"+oriItemView.getSourceStorageLocationName());
            }
            if (deliveryOrderItem.getRealAmount().compareTo(oriItemView.getRealAmount())<0)
            {
                throw new WMSServiceException("无法修改移库单条目的实际移动数量，如要操作请新建移库单");
            }

            if (deliveryOrderItem.getScheduledAmount().equals(oriItemView.getScheduledAmount()))//如果计划移库数量发生变化
            {
                if (deliveryOrderItem.getScheduledAmount().subtract(oriItemView.getRealAmount()).compareTo(new BigDecimal(0))<=0)//如果新修改时计划数量小于当前实际已经移动的数量
                {
                    throw new WMSServiceException(String.format("无法修改移库单条目计划数量单号：(%s)，移库操作基本完成，如需操作请新建移库单作业",deliveryOrderView.getNo()));
                }
                //否则修改计划移库数量并同步到库存记录可用数量
                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount().subtract(deliveryOrderItem.getScheduledAmount()));//计算要修改的计划移库数量
                fixTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                fixTransferStock.setUnit(deliveryOrderItem.getUnit());
                fixTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);
            }


            BigDecimal deltaRealAmount = deliveryOrderItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //修改实收数量，更新库存
            if(!deltaRealAmount.equals(new BigDecimal(0))){

                //todo 是移库前先把当前一步实际数量加回去可用数量
                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(deltaRealAmount);//实际要移动的数量加回到可用数量
                fixTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                fixTransferStock.setUnit(deliveryOrderItem.getUnit());
                fixTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);

                TransferStock transferStock=new TransferStock();
                transferStock.setAmount(deltaRealAmount);//TODO 待定
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                this.stockRecordService.addAmount(accountBook, transferStock);
            }
            deliveryOrderItem.setState(1);
            if (deliveryOrderItem.getScheduledAmount().equals(deliveryOrderItem.getRealAmount())){
                deliveryOrderItem.setState(2);
            }
        });
        this.deliveryOrderItemDAO.update(accountBook,deliveryOrderItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        try {
            this.deliveryOrderItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
        for (int id : ids) {
            //todo idChecker.check(this.getClass(), accountBook, id, "删除的移库单条目");

            DeliveryOrderItemView[] oriItemViews = this.deliveryOrderItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("出库单条目不存在，删除失败(%d)", id));
            }
            DeliveryOrderItemView oriItemView=oriItemViews[0];
            if (oriItemView.getState()==0&&oriItemView.getRealAmount().equals(new BigDecimal(0)))
            {
                //删除了未经过操作的单，更新库存可用数量
                TransferStock transferStock = new TransferStock();
                transferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount());//计划数量
                transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位可用数量
                transferStock.setSupplyId(oriItemView.getSupplyId());
                transferStock.setUnit(oriItemView.getUnit());
                transferStock.setUnitAmount(oriItemView.getUnitAmount());
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);

            }else
            {
                throw new WMSServiceException(String.format("出库单正在作业，无法删除(%s)", oriItemView.getId()));

            }
        }
    }

    @Override
    public DeliveryOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.deliveryOrderItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,DeliveryOrderItem[] deliveryOrderItems){
        //数据验证
        Stream.of(deliveryOrderItems).forEach(
                (deliveryOrderItem)->{
                    new Validator("计划装车数量（个）").min(0).validate(deliveryOrderItem.getScheduledAmount());
                    new Validator("实际装车数量（个）").min(0).max(deliveryOrderItem.getScheduledAmount().intValue()).validate(deliveryOrderItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(deliveryOrderItem.getUnitAmount());
                    new Validator("状态").min(0).max(5).validate(deliveryOrderItem.getState());
                }
        );

        //外键验证
        Stream.of(deliveryOrderItems).forEach(
                (deliveryOrderItem) -> {
                    if (this.deliveryOrderService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{deliveryOrderItem.getDeliveryOrderId()})).length == 0) {
                        throw new WMSServiceException(String.format("出库单不存在，请重新提交！(%d)", deliveryOrderItem.getDeliveryOrderId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{deliveryOrderItem.getSupplyId()})).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", deliveryOrderItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{deliveryOrderItem.getSourceStorageLocationId()})).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", deliveryOrderItem.getSourceStorageLocationId()));
                    } else if (deliveryOrderItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{deliveryOrderItem.getPersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", deliveryOrderItem.getPersonId()));
                    }
                }
        );
    }

    private DeliveryOrderView getDeliveryOrderView(String accountBook,DeliveryOrderItem[] deliveryOrderItems){
        //验证各出库单条目，出库单号必须全部相同
        Stream.of(deliveryOrderItems).reduce((last, cur) -> {
            if (last.getDeliveryOrderId() != cur.getDeliveryOrderId())
                throw new WMSServiceException("出库单条目所属的出库单必须相同！");
            return cur;
        });
        //获取出库单
        int deliveryOrderId = deliveryOrderItems[0].getDeliveryOrderId();
        final DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderService.find(accountBook, new Condition().addCondition("id", new Integer[]{deliveryOrderId}));
        if (deliveryOrderViews.length == 0)
            throw new WMSServiceException(String.format("出库单(%d)不存在，请重新提交！", deliveryOrderId));
        DeliveryOrderView deliveryOrderView = deliveryOrderViews[0];
        return deliveryOrderView;
    }


    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.deliveryOrderItemDAO.findCount(accountBook,cond);
    }
}
