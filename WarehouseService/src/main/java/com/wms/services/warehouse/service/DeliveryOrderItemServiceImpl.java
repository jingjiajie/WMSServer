package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.DeliveryOrderItemDAO;
import com.wms.services.warehouse.datastructures.ItemType;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.DeliveryOrder;
import com.wms.utilities.model.DeliveryOrderItem;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.DeliveryOrderView;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    @Autowired
    ItemRelatedRecordService itemRelatedRecordService;

    @Override
    public int[] add(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        if (deliveryOrderItems.length == 0) return new int[]{};
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_IN_DELIVER){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运在途，无法再添加出库单条目", deliveryOrderView.getNo()));
        }
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_DELIVER_FINNISH){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运核减，无法再添加出库单条目", deliveryOrderView.getNo()));
        }
        //验证字段
        this.validateEntities(accountBook,deliveryOrderItems);

        //修改库存
        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            if (deliveryOrderItem.getRealAmount().compareTo(new BigDecimal(0))==0) {
                int deliveryType=2;
                if(deliveryOrderView.getType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                    deliveryType=1;
                }
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
                transferStock.setState(deliveryType);
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);//仅修改可用数量
                deliveryOrderItem.setState(DeliveryOrderService.STATE_IN_LOADING);
            }
            else{

                int deliveryType=2;
                if(deliveryOrderView.getType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                    deliveryType=1;
                }
                //先移动
                TransferStock transferStock = new TransferStock();
                transferStock.setAmount(new BigDecimal(0).subtract(deliveryOrderItem.getRealAmount()));
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                transferStock.setState(deliveryType);
                this.stockRecordService.addAmount(accountBook, transferStock);//直接改数

                //再改可用数量
                TransferStock rdTransferStock = new TransferStock();
                rdTransferStock.setModifyAvailableAmount(deliveryOrderItem.getRealAmount().subtract(deliveryOrderItem.getScheduledAmount()));
                rdTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                rdTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                rdTransferStock.setUnit(deliveryOrderItem.getUnit());
                rdTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                rdTransferStock.setState(deliveryType);
                this.stockRecordService.modifyAvailableAmount(accountBook, rdTransferStock);//直接改可用数量
                deliveryOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
            }
        });
        //添加到数据库中
        int[] ids = this.deliveryOrderItemDAO.add(accountBook, deliveryOrderItems);
        this.updateDeliveryOrder(accountBook,deliveryOrderItems[0].getDeliveryOrderId() ,-1);
        return ids;//仅返回iDs

    }

    @Override
    public int[] add2(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        if (deliveryOrderItems.length == 0) return new int[]{};
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_IN_DELIVER){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运在途，无法再添加出库单条目", deliveryOrderView.getNo()));
        }
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_DELIVER_FINNISH){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运核减，无法再添加出库单条目", deliveryOrderView.getNo()));
        }
        //验证字段
        this.validateEntities(accountBook,deliveryOrderItems);

        //修改库存

        int[] ids=new int[deliveryOrderItems.length];
        for (int i=0;i<ids.length;i++) {
            int id = this.deliveryOrderItemDAO.add(accountBook, new DeliveryOrderItem[]{deliveryOrderItems[i]})[0];

            int deliveryType = 2;
            if (deliveryOrderView.getType() == DeliveryOrderService.DELIVERY_TYPE_Unqualified) {
                deliveryType = 1;
            }
            //先移动
            TransferStock transferStock = new TransferStock();
            transferStock.setAmount(deliveryOrderItems[i].getRealAmount());
            transferStock.setAvailableAmount(deliveryOrderItems[i].getScheduledAmount());

            transferStock.setSourceStorageLocationId(deliveryOrderItems[i].getSourceStorageLocationId());

            transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
            transferStock.setItemId(id);
            transferStock.setItemType(ItemType.delierItem);

            transferStock.setSupplyId(deliveryOrderItems[i].getSupplyId());
            transferStock.setUnit(deliveryOrderItems[i].getUnit());
            transferStock.setUnitAmount(deliveryOrderItems[i].getUnitAmount());
            transferStock.setState(deliveryType);

            this.stockRecordService.reduceAmount(accountBook, transferStock, new TransferStock());//直接改数
            if (deliveryOrderItems[i].getScheduledAmount().compareTo(deliveryOrderItems[i].getRealAmount())==0){
                deliveryOrderItems[i].setState(DeliveryOrderService.STATE_ALL_LOADING);
            }else if (deliveryOrderItems[i].getRealAmount().compareTo(BigDecimal.ZERO)==0){
                deliveryOrderItems[i].setState(DeliveryOrderService.STATE_IN_LOADING);
            }else{
                deliveryOrderItems[i].setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
            }
            ids[i]=id;
        }
        //添加到数据库中

        this.updateDeliveryOrder(accountBook,deliveryOrderItems[0].getDeliveryOrderId() ,-1);
        return ids;//仅返回iDs
    }

    @Override
    public void update(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);

        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_IN_DELIVER){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运在途，无法再修改出库单条目", deliveryOrderView.getNo()));
        }
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_DELIVER_FINNISH){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运核减，无法再修改出库单条目", deliveryOrderView.getNo()));
        }

        //数据验证
        this.validateEntities(accountBook,deliveryOrderItems);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {

            int deliveryType=2;
            if(deliveryOrderView.getType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                deliveryType=1;
            }
            DeliveryOrderItemView[] foundOriItems = this.deliveryOrderItemDAO.find(accountBook,new Condition().addCondition("id",new Integer[]{deliveryOrderItem.getId()}));
            if(foundOriItems.length == 0) throw new WMSServiceException(String.format("出库单条目不存在，请重新提交！",deliveryOrderItem.getId()));//排除异常
            DeliveryOrderItemView oriItemView = foundOriItems[0];

            if (deliveryOrderItem.getScheduledAmount().subtract(oriItemView.getRealAmount()).compareTo(new BigDecimal(0))<0)//如果新修改时计划数量小于当前实际已经移动的数量
            {
                throw new WMSServiceException(String.format("出库单条目计划数量不能小于实际数量！出库单号：(%s)",deliveryOrderView.getNo()));
            }
            //TODO 中途修改出库库位/单位/单位数量
            if(!deliveryOrderItem.getSourceStorageLocationId().equals(oriItemView.getSourceStorageLocationId())
                   ||deliveryOrderItem.getUnitAmount().compareTo(oriItemView.getUnitAmount())!=0
                    ||!deliveryOrderItem.getUnit().equals(oriItemView.getUnit()))
            {
                if (oriItemView.getState()==0)
                {
                    //删除了未经过操作的单，更新库存可用数量
                    TransferStock transferStock = new TransferStock();
                    transferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount());//计划数量
                    transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位可用数量
                    transferStock.setSupplyId(oriItemView.getSupplyId());
                    transferStock.setUnitAmount(oriItemView.getUnitAmount());
                    transferStock.setUnit(oriItemView.getUnit());
                    transferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);

                }else{

                    TransferStock transferStock=new TransferStock();
                    transferStock.setAmount(oriItemView.getRealAmount());//TODO 待定
                    transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());
                    transferStock.setRelatedOrderNo(oriItemView.getDeliveryOrderNo());
                    transferStock.setSupplyId(oriItemView.getSupplyId());
                    transferStock.setUnit(oriItemView.getUnit());
                    transferStock.setUnitAmount(oriItemView.getUnitAmount());
                    transferStock.setState(deliveryType);
                    this.stockRecordService.addAmountToNewestBatchNo(accountBook, transferStock);


                    TransferStock thefixTransferStock = new TransferStock();
                    thefixTransferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount().subtract(oriItemView.getRealAmount()));//实际要回到可用数量
                    thefixTransferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位
                    thefixTransferStock.setSupplyId(oriItemView.getSupplyId());
                    thefixTransferStock.setUnit(oriItemView.getUnit());
                    thefixTransferStock.setUnitAmount(oriItemView.getUnitAmount());
                    thefixTransferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, thefixTransferStock);


                }


                if (deliveryOrderItem.getRealAmount().compareTo(new BigDecimal(0))==0) {

                    //没有实际数输入就只改变可用数量
                    TransferStock transferStock = new TransferStock();
                    transferStock.setModifyAvailableAmount(new BigDecimal(0).subtract(deliveryOrderItem.getScheduledAmount()));
                    transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                    //transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                    transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    transferStock.setUnit(deliveryOrderItem.getUnit());
                    transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    transferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);//仅修改可用数量
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_IN_LOADING);
                }
                else{
                    //先移动
                    TransferStock transferStock = new TransferStock();
                    transferStock.setAmount(new BigDecimal(0).subtract(deliveryOrderItem.getRealAmount()));
                    transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                    transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                    transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    transferStock.setUnit(deliveryOrderItem.getUnit());
                    transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                    transferStock.setState(deliveryType);
                    this.stockRecordService.addAmount(accountBook, transferStock);//直接改数

                    //再改可用数量
                    TransferStock rdTransferStock = new TransferStock();
                    rdTransferStock.setModifyAvailableAmount(deliveryOrderItem.getScheduledAmount().subtract(deliveryOrderItem.getRealAmount()));
                    rdTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                    rdTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    rdTransferStock.setUnit(deliveryOrderItem.getUnit());
                    rdTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    rdTransferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, rdTransferStock);//直接改可用数量
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
                }
            }
            else{

                BigDecimal deltaRealAmount = deliveryOrderItem.getRealAmount().subtract(oriItemView.getRealAmount());
                //修改实收数量，更新库存
                //TODO 如果输入有实际数量变化
                if(deltaRealAmount.compareTo(BigDecimal.ZERO)>0){

                    //todo 是移库前先把当前一步实际数量加回去可用数量
                    TransferStock fixTransferStock = new TransferStock();
                    fixTransferStock.setModifyAvailableAmount(deltaRealAmount);//实际要移动的数量加回到可用数量
                    fixTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());//修改源库位
                    fixTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    fixTransferStock.setUnit(deliveryOrderItem.getUnit());
                    fixTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    fixTransferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);

                    TransferStock transferStock=new TransferStock();
                    transferStock.setAmount(new BigDecimal(0).subtract(deltaRealAmount));//TODO 待定
                    transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                    transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                    transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    transferStock.setUnit(deliveryOrderItem.getUnit());
                    transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());

                    transferStock.setState(deliveryType);
                    if (deltaRealAmount.compareTo(BigDecimal.ZERO)>0){
                        transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                    this.stockRecordService.addAmount(accountBook, transferStock);}
                    else if(deltaRealAmount.compareTo(BigDecimal.ZERO)<0)
                    {
                        this.stockRecordService.addAmountToNewestBatchNo(accountBook, transferStock);
                    }
                }else if(deltaRealAmount.compareTo(BigDecimal.ZERO)<0){

                    //todo 是移库前先把当前一步实际数量加回去,再调整可用数量
                    TransferStock transferStock=new TransferStock();
                    transferStock.setAmount(new BigDecimal(0).subtract(deltaRealAmount));//TODO 待定
                    transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());
                    transferStock.setRelatedOrderNo(deliveryOrderView.getNo());
                    transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    transferStock.setUnit(deliveryOrderItem.getUnit());
                    transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());

                    transferStock.setState(deliveryType);
                    if (deltaRealAmount.compareTo(BigDecimal.ZERO)>0){
                        transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                        this.stockRecordService.addAmount(accountBook, transferStock);}
                    else if(deltaRealAmount.compareTo(BigDecimal.ZERO)<0)
                    {
                        this.stockRecordService.addAmountToNewestBatchNo(accountBook, transferStock);
                    }

                    TransferStock fixTransferStock = new TransferStock();
                    fixTransferStock.setModifyAvailableAmount(deltaRealAmount);//实际要移动的数量加回到可用数量
                    fixTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());//修改源库位
                    fixTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    fixTransferStock.setUnit(deliveryOrderItem.getUnit());
                    fixTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    fixTransferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);
                }

                deliveryOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
                if (deliveryOrderItem.getScheduledAmount().equals(deliveryOrderItem.getRealAmount())){
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_ALL_LOADING);
                }


                if (deliveryOrderItem.getScheduledAmount().compareTo(oriItemView.getScheduledAmount())!=0)//如果计划移库数量发生变化
                {
                    //否则修改计划移库数量并同步到库存记录可用数量
                    TransferStock fixTransferStock = new TransferStock();
                    fixTransferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount().subtract(deliveryOrderItem.getScheduledAmount()));//计算要修改的计划移库数量
                    fixTransferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());//修改源库位
                    fixTransferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                    fixTransferStock.setUnit(deliveryOrderItem.getUnit());
                    fixTransferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                    fixTransferStock.setState(deliveryType);
                    this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);
                }
            }
        });
        this.deliveryOrderItemDAO.update(accountBook,deliveryOrderItems);
        this.updateDeliveryOrder(accountBook,deliveryOrderItems[0].getDeliveryOrderId() ,-1);
    }

    @Override
    public void update2(String accountBook, DeliveryOrderItem[] deliveryOrderItems) throws WMSServiceException {
        DeliveryOrderView deliveryOrderView = this.getDeliveryOrderView(accountBook,deliveryOrderItems);

        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_IN_DELIVER){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运在途，无法再修改出库单条目", deliveryOrderView.getNo()));
        }
        if(deliveryOrderView.getState()==DeliveryOrderService.STATE_DELIVER_FINNISH){
            throw new WMSServiceException(String.format("当前出库单（%s）已经发运核减，无法再修改出库单条目", deliveryOrderView.getNo()));
        }

        //数据验证
        this.validateEntities(accountBook,deliveryOrderItems);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {

            int deliveryType=2;
            if(deliveryOrderView.getType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                deliveryType=1;
            }
            DeliveryOrderItemView[] foundOriItems = this.find(accountBook,new Condition().addCondition("id",new Integer[]{deliveryOrderItem.getId()}));
            if(foundOriItems.length == 0) throw new WMSServiceException(String.format("出库单条目不存在，请重新提交！",deliveryOrderItem.getId()));//排除异常
            DeliveryOrderItemView oriItemView = foundOriItems[0];

            if (deliveryOrderItem.getScheduledAmount().subtract(oriItemView.getRealAmount()).compareTo(new BigDecimal(0))<0)//如果新修改时计划数量小于当前实际已经移动的数量
            {
                throw new WMSServiceException(String.format("出库单条目计划数量不能小于实际数量！出库单号：(%s)",deliveryOrderView.getNo()));
            }



            if (itemRelatedRecordService.findTable(accountBook,new Condition()
                    .addCondition("relatedItemId",deliveryOrderItem.getId())
                    .addCondition("itemType", ItemType.delierItem)).length > 0) {

                //实际移库操作
                TransferStock transferStock = new TransferStock();
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());

                transferStock.setAmount(deliveryOrderItem.getRealAmount());//实际数量
                transferStock.setAvailableAmount(deliveryOrderItem.getScheduledAmount());

                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());//获取单号
                transferStock.setItemId(deliveryOrderItem.getId());
                transferStock.setItemType(ItemType.delierItem);//必须区分条目类型

                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());
                transferStock.setState(deliveryType);

                //旧的信息
                TransferStock transferStockRestore = new TransferStock();
                transferStockRestore.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());
                transferStock.setAmount(oriItemView.getRealAmount());//实际数量
                transferStock.setAvailableAmount(oriItemView.getScheduledAmount());

                transferStockRestore.setRelatedOrderNo(deliveryOrderView.getNo());//获取单号
                transferStockRestore.setItemId(deliveryOrderItem.getId());
                transferStockRestore.setItemType(ItemType.delierItem);//必须区分条目类型

                transferStockRestore.setSupplyId(oriItemView.getSupplyId());
                transferStockRestore.setState(deliveryType);
                transferStockRestore.setUnit(oriItemView.getUnit());
                transferStockRestore.setUnitAmount(oriItemView.getUnitAmount());


                this.stockRecordService.reduceAmount(accountBook, transferStock,transferStockRestore);//使用更新单位的库存修改


            }else{
                //实际移库操作
                TransferStock transferStock = new TransferStock();
                transferStock.setSourceStorageLocationId(deliveryOrderItem.getSourceStorageLocationId());

                transferStock.setAmount(deliveryOrderItem.getRealAmount());//实际数量
                transferStock.setAvailableAmount(deliveryOrderItem.getScheduledAmount());

                transferStock.setRelatedOrderNo(deliveryOrderView.getNo());//获取单号
                transferStock.setItemId(deliveryOrderItem.getId());
                transferStock.setItemType(ItemType.delierItem);//必须区分条目类型

                transferStock.setSupplyId(deliveryOrderItem.getSupplyId());
                transferStock.setState(deliveryType);
                transferStock.setUnit(deliveryOrderItem.getUnit());
                transferStock.setUnitAmount(deliveryOrderItem.getUnitAmount());

                this.stockRecordService.reduceAmount(accountBook, transferStock,new TransferStock());//使用更新单位的库存修改

            }

                if (deliveryOrderItem.getScheduledAmount().equals(deliveryOrderItem.getRealAmount())){
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_ALL_LOADING);
                }else if (deliveryOrderItem.getRealAmount().compareTo(BigDecimal.ZERO)==0) {
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_IN_LOADING);
                }else{
                    deliveryOrderItem.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
                }

        });
        this.deliveryOrderItemDAO.update(accountBook,deliveryOrderItems);
        this.updateDeliveryOrder(accountBook,deliveryOrderItems[0].getDeliveryOrderId() ,-1);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        int curdeliveryOrderId=-1;
        for (int i=0;i<ids.length;i++) {

            DeliveryOrderItemView[] oriItemViews = this.find(accountBook, new Condition().addCondition("id", new Integer[]{ids[i]}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("出库单条目不存在，删除失败(%d)", ids[i]));
            }
            int deliveryType=2;
            if(oriItemViews[0].getDeliveryOrderType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                deliveryType=1;
            }
            DeliveryOrderItemView oriItemView=oriItemViews[0];
            curdeliveryOrderId=oriItemView.getDeliveryOrderId();
            if (oriItemView.getState()==0)
            {
                //删除了未经过操作的单，更新库存可用数量
                TransferStock transferStock = new TransferStock();
                transferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount());//计划数量
                transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位可用数量
                transferStock.setSupplyId(oriItemView.getSupplyId());
                transferStock.setUnit(oriItemView.getUnit());
                transferStock.setUnitAmount(oriItemView.getUnitAmount());
                transferStock.setState(deliveryType);
                this.stockRecordService.modifyAvailableAmount(accountBook, transferStock);

            }else{

                TransferStock transferStock=new TransferStock();
                transferStock.setAmount(oriItemView.getRealAmount());//TODO 待定
                transferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());
                transferStock.setRelatedOrderNo(oriItemView.getDeliveryOrderNo());
                transferStock.setSupplyId(oriItemView.getSupplyId());
                transferStock.setUnit(oriItemView.getUnit());
                transferStock.setUnitAmount(oriItemView.getUnitAmount());
                transferStock.setInventoryDate(new Timestamp(System.currentTimeMillis()));
                transferStock.setState(deliveryType);
                this.stockRecordService.addAmountToNewestBatchNo(accountBook, transferStock);


                TransferStock fixTransferStock = new TransferStock();
                fixTransferStock.setModifyAvailableAmount(oriItemView.getScheduledAmount().subtract(oriItemView.getRealAmount()));//实际要移动的数量加回到可用数量
                fixTransferStock.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());//修改源库位
                fixTransferStock.setSupplyId(oriItemView.getSupplyId());
                fixTransferStock.setUnit(oriItemView.getUnit());
                fixTransferStock.setUnitAmount(oriItemView.getUnitAmount());
                fixTransferStock.setState(deliveryType);
                this.stockRecordService.modifyAvailableAmount(accountBook, fixTransferStock);


            }
        }
        try {
            this.deliveryOrderItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
        this.updateDeliveryOrder(accountBook,curdeliveryOrderId ,-1);
    }

    @Override
    public void remove2(String accountBook, int[] ids) throws WMSServiceException {
        int curdeliveryOrderId=-1;
        for (int i=0;i<ids.length;i++) {

            DeliveryOrderItemView[] oriItemViews = this.find(accountBook, new Condition().addCondition("id", new Integer[]{ids[i]}));
            if (oriItemViews.length == 0) {
                throw new WMSServiceException(String.format("出库单条目不存在，删除失败(%d)", ids[i]));
            }
            int deliveryType=2;
            if(oriItemViews[0].getDeliveryOrderType()==DeliveryOrderService.DELIVERY_TYPE_Unqualified){
                deliveryType=1;
            }
            DeliveryOrderItemView oriItemView=oriItemViews[0];
            curdeliveryOrderId=oriItemView.getDeliveryOrderId();
            //旧的信息
            TransferStock transferStockRestore = new TransferStock();
            transferStockRestore.setSourceStorageLocationId(oriItemView.getSourceStorageLocationId());

            transferStockRestore.setRelatedOrderNo(oriItemView.getDeliveryOrderNo());//获取单号
            transferStockRestore.setItemId(oriItemView.getId());
            //必须区分条目类型
            transferStockRestore.setItemType(ItemType.delierItem);

            transferStockRestore.setSupplyId(oriItemView.getSupplyId());
            transferStockRestore.setState(deliveryType);
            transferStockRestore.setUnit(oriItemView.getUnit());
            transferStockRestore.setUnitAmount(oriItemView.getUnitAmount());

            this.stockRecordService.restoreAmount(accountBook,transferStockRestore);//使用更新单位的库存修改
        }
        try {
            this.deliveryOrderItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
        this.updateDeliveryOrder(accountBook,curdeliveryOrderId ,-1);
    }

    @Override
    public DeliveryOrderItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.deliveryOrderItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,DeliveryOrderItem[] deliveryOrderItems){
        //数据验证
        Stream.of(deliveryOrderItems).forEach(
                (deliveryOrderItem)->{
                    new Validator("计划装车数量（个）").greaterThan(0).validate(deliveryOrderItem.getScheduledAmount());
                    new Validator("实际装车数量（个）").min(0).validate(deliveryOrderItem.getRealAmount());
                    if (deliveryOrderItem.getRealAmount().compareTo(deliveryOrderItem.getScheduledAmount())>0){
                        throw new WMSServiceException(String.format("实际装车数量的值(%f)不能大于计划装车数量(%f)",deliveryOrderItem.getRealAmount(),deliveryOrderItem.getScheduledAmount()));
                    }

                    new Validator("单位数量").greaterThan(0).validate(deliveryOrderItem.getUnitAmount());
                    new Validator("单位").notnull().validate(deliveryOrderItem.getUnit());
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
            if (!last.getDeliveryOrderId().equals(cur.getDeliveryOrderId()))
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


    @Override
    public void loadingALL(String accountBook,List<Integer> ids) throws WMSServiceException{

        //TODO 这里传的ID是出库单ID
        //先找出库单
        DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderService.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));
        if (deliveryOrderViews.length == 0) return;
        DeliveryOrder[] deliveryOrders = ReflectHelper.createAndCopyFields(deliveryOrderViews,DeliveryOrder.class);
        //再找条目
        DeliveryOrderItemView[] itemViews = this.find(accountBook,new Condition().addCondition("deliveryOrderId",ids.toArray(), ConditionItem.Relation.IN));
        DeliveryOrderItem[] deliveryOrderItems = ReflectHelper.createAndCopyFields(itemViews,DeliveryOrderItem.class);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            if (deliveryOrderItem.getState() ==TransferOrderItemService.STATE_IN_TRANSFER) {
                deliveryOrderItem.setRealAmount(deliveryOrderItem.getScheduledAmount());
                deliveryOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
            }
            deliveryOrderItem.setLoadingTime(new Timestamp(System.currentTimeMillis()));
        });
        this.update(accountBook,deliveryOrderItems);
        //更新出库单状态
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_IN_DELIVER) {
                throw new WMSServiceException(String.format("当前出库单（%s）正在发运，无法重复装车工作", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_DELIVER_FINNISH) {
                throw new WMSServiceException(String.format("当前出库单（%s）已经发运完成，无法重复装车工作", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_ALL_LOADING) {
                throw new WMSServiceException(String.format("当前出库单（%s）已经完成整单装车，无法重复装车工作", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_IN_LOADING) {
                deliveryOrder.setState(DeliveryOrderService.STATE_ALL_LOADING);
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_PARTIAL_LOADING) {
                deliveryOrder.setState(DeliveryOrderService.STATE_ALL_LOADING);
            }

        });
        this.deliveryOrderService.update(accountBook,deliveryOrders);

    }

    @Override
    public void loadingSome(String accountBook,List<Integer> ids) throws WMSServiceException{

        //TODO 这里传的是出库单条目ID，人员id没往下传
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个出库单条目！");
        }
        //先找条目
        DeliveryOrderItemView[] itemViews = this.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));
        DeliveryOrderItem[] deliveryOrderItems = ReflectHelper.createAndCopyFields(itemViews,DeliveryOrderItem.class);
        //再找对应出库单
        DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{deliveryOrderItems[0].getDeliveryOrderId()}));
        if (deliveryOrderViews.length == 0) return;
        DeliveryOrder[] deliveryOrders = ReflectHelper.createAndCopyFields(deliveryOrderViews,DeliveryOrder.class);

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            if (deliveryOrderItem.getState() ==TransferOrderItemService.STATE_IN_TRANSFER) {
                deliveryOrderItem.setRealAmount(deliveryOrderItem.getScheduledAmount());
                deliveryOrderItem.setState(TransferOrderItemService.STATE_ALL_FINISH);
            }
        });
        this.update(accountBook,deliveryOrderItems);
        //TODO 如果部分更新的时候是全部完成，需要补逻辑
        //更新出库单状态,应该是只有一个单子
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            boolean judgeState =true;
            DeliveryOrderItem[] allItems = this.deliveryOrderItemDAO.findTable(accountBook,new Condition().addCondition("deliveryOrderId",deliveryOrder.getId(), ConditionItem.Relation.IN));
            for (int i=0;i<allItems.length;i++){
                if (allItems[i].getState()==DeliveryOrderService.STATE_ALL_LOADING){

                }
                else {
                    judgeState=false;
                }
            }
            if (judgeState){
                deliveryOrder.setState(DeliveryOrderService.STATE_ALL_LOADING);
            }else{
                deliveryOrder.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
            }

        });
        this.deliveryOrderService.update(accountBook, deliveryOrders);

    }

    public void updateDeliveryOrder( String accountBook,int deliveryOrderId ,int lastUpdatePersonId){
        //TODO idChecker.check(PersonService.class,accountBook,lastUpdatePersonId," 人员");
        DeliveryOrderView[] deliveryOrderViews= deliveryOrderService.find(accountBook,new Condition().addCondition("id",new Integer[]{deliveryOrderId}));
        DeliveryOrder[] deliveryOrders = ReflectHelper.createAndCopyFields(deliveryOrderViews,DeliveryOrder.class);
        if(deliveryOrderViews.length==0){
            throw new WMSServiceException("没有找到要更新的出库单！");
        }
        DeliveryOrder deliveryOrder=deliveryOrders[0];
        if (lastUpdatePersonId!=-1) {
        deliveryOrder.setLastUpdatePersonId(lastUpdatePersonId);
        }

        deliveryOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        //更新移库单状态,应该是只有一个单子
        DeliveryOrderItem[] allItems = this.deliveryOrderItemDAO.findTable(accountBook,new Condition()
                .addCondition("deliveryOrderId",deliveryOrder.getId(), ConditionItem.Relation.IN));

        DeliveryOrderItem[] allLoadingItems = this.deliveryOrderItemDAO.findTable(accountBook,new Condition()
                .addCondition("deliveryOrderId",deliveryOrder.getId(), ConditionItem.Relation.IN)
                .addCondition("state",DeliveryOrderService.STATE_ALL_LOADING, ConditionItem.Relation.IN));

        DeliveryOrderItem[] zeroItems = this.deliveryOrderItemDAO.findTable(accountBook,new Condition()
                .addCondition("deliveryOrderId",deliveryOrder.getId(), ConditionItem.Relation.IN)
                .addCondition("state",DeliveryOrderService.STATE_IN_LOADING, ConditionItem.Relation.IN));

        if (allItems.length>0&&allLoadingItems.length==allItems.length){
            deliveryOrder.setState(DeliveryOrderService.STATE_ALL_LOADING);
        }else if (allItems.length==0||zeroItems.length==allItems.length){
            deliveryOrder.setState(DeliveryOrderService.STATE_IN_LOADING);
        }
        else{
            deliveryOrder.setState(DeliveryOrderService.STATE_PARTIAL_LOADING);
        }


        deliveryOrderService.update(accountBook,new DeliveryOrder[]{deliveryOrder});
    }

    public void updateSleep(){
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    private void validateRandomDuplication(String accountBook) {
        Condition cond = new Condition();
        DeliveryOrderItem[] deliveryOrderItemsCheck = deliveryOrderItemDAO.findTable(accountBook, cond);
        List<DeliveryOrderItem> deliveryOrderItemList=new ArrayList<>();
        for(int i=0;i<deliveryOrderItemsCheck.length;i++){
            if(deliveryOrderItemsCheck[i].getDeliveryRandomCode()!=null){
                if(!deliveryOrderItemsCheck[i].getDeliveryRandomCode().equals(""))
                {
                    deliveryOrderItemList.add(deliveryOrderItemsCheck[i]);
                }}
        }
        deliveryOrderItemList.stream().sorted(Comparator.comparing(DeliveryOrderItem::getDeliveryRandomCode)).reduce((last, cur) -> {
            if (last.getDeliveryRandomCode().equals(cur.getDeliveryRandomCode()) && last.getDeliveryRandomCode() != null && !(last.getDeliveryRandomCode().equals(""))) {
                throw new WMSServiceException("随机码重复！");
            }
            return cur;
        });
    }


}
