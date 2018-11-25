package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.TaxItemService;
import com.wms.services.warehouse.dao.DeliveryOrderDAO;
import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.IDChecker;
import com.wms.utilities.OrderNoGenerator;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.utilities.model.TransferOrder;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.wms.utilities.ReflectHelper;

@Service
@Transactional
public class DeliveryOrderServiceImpl implements DeliveryOrderService{
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    DeliveryOrderDAO deliveryOrderDAO;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    PersonService personService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    TransferOrderService transferOrderService;
    @Autowired
    TransferOrderItemService transferOrderItemService;
    @Autowired
    SafetyStockService safetyStockService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Autowired
    PackageItemService packageItemService;
    @Autowired
    PackageService packageService;


    private static final String NO_PREFIX = "D";

    @Override
    public int[] add(String accountBook, DeliveryOrder[] deliveryOrders) throws WMSServiceException {
        //验证结构
        this.validateEntities(accountBook,deliveryOrders);
        //生成创建时间
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> deliveryOrder.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis())));

        //生成/检测单号
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> {
            //如果单号留空则自动生成
            if (deliveryOrder.getNo() == null||deliveryOrder.getNo().isEmpty()) {
                deliveryOrder.setNo(this.orderNoGenerator.generateNextNo(accountBook, DeliveryOrderServiceImpl.NO_PREFIX,deliveryOrder.getWarehouseId()));
            } else { //否则检查单号是否重复
                Condition cond = new Condition();
                cond.addCondition("no", new String[]{deliveryOrder.getNo()});
                if (deliveryOrderDAO.find(accountBook, cond).length > 0) {
                    throw new WMSServiceException("出库单单号重复：" + deliveryOrder.getNo());
                }
            }
        });
        return deliveryOrderDAO.add(accountBook, deliveryOrders);
    }

    @Override
    public void update(String accountBook, DeliveryOrder[] deliveryOrders) throws WMSServiceException {
        //数据验证
        Stream.of(deliveryOrders).forEach((deliveryOrder) -> {
            new Validator("出库单单号").notEmpty().validate(deliveryOrder.getNo());
        });

        //名称查重
        for (int i = 0; i < deliveryOrders.length; i++) {
            Condition cond = new Condition();
            cond.addCondition("no", new String[]{deliveryOrders[i].getNo()});
            cond.addCondition("id", new Integer[]{deliveryOrders[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if (deliveryOrderDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("出库单单号重复：" + deliveryOrders[i].getNo());
            }
        }
        Stream.of(deliveryOrders).forEach((deliveryOrder -> {
            deliveryOrder.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }));


        deliveryOrderDAO.update(accountBook, deliveryOrders);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (deliveryOrderDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除出库单不存在，请重新查询！(%d)", id));
            }
        }

        try {
            deliveryOrderDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除出库单失败，如果出库单已经被引用，需要先删除引用该出库单的内容，才能删除该出库单");
        }
    }

    @Override
    public DeliveryOrderView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return deliveryOrderDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, DeliveryOrder[] deliveryOrders) {
        //数据验证
        Stream.of(deliveryOrders).forEach(
                (deliveryOrder) -> {
                    new Validator("状态").min(0).max(5).validate(deliveryOrder.getState());
                    new Validator("出库类型").min(0).max(1).validate(deliveryOrder.getType());
//                    new Validator("司机").notEmpty().validate(deliveryOrder.getDriverName());
//                    new Validator("车牌号").notEmpty().validate(deliveryOrder.getLiscensePlateNumber());
                }
        );

        //外键检测
        Stream.of(deliveryOrders).forEach(
                (deliveryOrder) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getWarehouseId())).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", deliveryOrder.getWarehouseId()));
                    }else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getCreatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", deliveryOrder.getCreatePersonId()));
                    }if (deliveryOrder.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", deliveryOrder.getLastUpdatePersonId())).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", deliveryOrder.getLastUpdatePersonId()));
                    }
                }
        );
    }

    //翻包备货作业，生成移库单
    public void transferPakage(String accountBook, TransferArgs transferArgs) {
        TransferItem[] transferItems = transferArgs.getTransferItems();
        Stream.of(transferItems).forEach((transferItem) -> {
            //创建新的移库单
            TransferOrder transferOrder = transferItem.getTransferOrder();
            new Validator("移库单信息").notnull().validate(transferOrder);

            int newTransferOrderID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            //按照安全库存信息，生成移库单条目
            TransferOrderItem[] transferOrderItems=transferItem.getTransferOrderItems();

            Stream.of(transferOrderItems).forEach((transferOrderItem)->{
                //创建新的移库单条目
                // todo 等数据库修改确定
                //找到对应供货对应库位的安全库存信息
                if(transferArgs.isAutoCommit()) {
                    //SafetyStockView[] safetyStockViews = safetyStockService.find(accountBook,
                    //        new Condition().addCondition("targetStorageLocationId", new Integer[]{transferOrderItem.getTargetStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}));
                    //StockRecordView[] stockRecordViews = stockRecordService.find(accountBook,
                    //        new Condition().addCondition("storageLocationId", new Integer[]{transferOrderItem.getTargetStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}));
                    //StockRecordView[] stockRecordViews = stockRecordService.find(accountBook,
                    //        new Condition().addCondition("storageLocationId", new Integer[]{transferOrderItem.getTargetStorageLocationId()}).addCondition("supplyId", new Integer[]{transferOrderItem.getSupplyId()}).addCondition("unitAmount", new BigDecimal[]{transferOrderItem.getUnitAmount()}).addCondition("unit", new String[]{transferOrderItem.getUnit()}));

                    //if (stockRecordViews[0].getAmount().compareTo(safetyStockViews[0].getAmount()) == -1)//如果库存数量小于安全库存数量
                   // {
                   //     transferOrderItem.setScheduledAmount(safetyStockViews[0].getAmount().subtract(stockRecordViews[0].getAmount()));//设置计划数量为与安全库存的差值
                        transferOrderItem.setRealAmount(new BigDecimal(0));
                        transferOrderItem.setComment("成功一键移库");
                        transferOrderItem.setOperateTime(new Timestamp(System.currentTimeMillis()));
                        transferOrderItem.setTransferOrderId(newTransferOrderID);
                        transferOrderItem.setState(0);
                    //} else {
                        //库存充足取消备货
                     //   throw new WMSServiceException(String.format("当前备货区(%s)库存充足，不需要备货", safetyStockViews[0].getTargetStorageLocationName()));
                    //}
                }else{
                    transferOrderItem.setState(0);
                    transferOrderItem.setTransferOrderId(newTransferOrderID);

                }
            });
            this.transferOrderItemService.add(accountBook, transferOrderItems);
        });
    }

    public List<TransferOrderItemView> transferAuto(String accountBook, TransferAuto TransferAuto) throws WMSServiceException{
        new Validator("人员").notnull().validate(TransferAuto.getPersonId());
        new Validator("移库类型").min(0).max(2).validate(TransferAuto.getTransferType());

        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, TransferAuto.getWarehouseId(), " 仓库");
        //区分安全库存类型
        int transferType=TransferAuto.getTransferType();

        SafetyStockView[] AllSafetyStockViews=safetyStockService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{TransferAuto.getWarehouseId()}).addCondition("type",new Integer[]{transferType}));
        if(AllSafetyStockViews.length==0){throw new WMSServiceException("当前仓库无任何安全库存记录，无法自动添加作业单条目！");}
        TransferArgs transferArgs=new TransferArgs();
        TransferItem transferItem=new TransferItem();


        //新建列表存放条目
        List<TransferOrderItem> transferOrderItemsList=new ArrayList();
        List<TransferOrderItemView> falseTransferOrderItemsList=new ArrayList();
        //TODO 按供货商分组
        Map<Integer, List<SafetyStockView>> groupBySupplierIdMap =
                Stream.of(AllSafetyStockViews).collect(Collectors.groupingBy(SafetyStockView::getSupplierId));

        Iterator<Map.Entry<Integer,List<SafetyStockView>>> entries = groupBySupplierIdMap.entrySet().iterator();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<SafetyStockView>> entry = entries.next();
            Integer supplierId=entry.getKey();

            TransferOrder transferOrder=new TransferOrder();
            transferOrder.setType(transferType);
            transferOrder.setWarehouseId(TransferAuto.getWarehouseId());
            transferOrder.setDescription("自动移库");
            transferOrder.setCreatePersonId(TransferAuto.getPersonId());
            transferOrder.setSupplierId(supplierId);
            int newTransferOrderID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            List<SafetyStockView> safetyStockViewsList=entry.getValue();

            SafetyStockView[] safetyStockViews=null;
            safetyStockViews = (SafetyStockView[]) Array.newInstance(SafetyStockView.class,safetyStockViewsList.size());
            safetyStockViewsList.toArray(safetyStockViews);
            boolean succeedOrder=false;
            for(int i=0;i<safetyStockViews.length;i++){
                StockRecordViewNewest[] stockRecordViews3 = stockRecordService.findNewest(accountBook,
                        new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getTargetStorageLocationId()})
                                .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getUnitAmount()})
                                .addCondition("unit", new String[]{safetyStockViews[i].getUnit()})
                                .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));
                StockRecordViewNewest[] stockRecordViews4 = stockRecordService.findNewest(accountBook,
                        new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getSourceStorageLocationId()})
                                .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getSourceUnitAmount()})
                                .addCondition("unit", new String[]{safetyStockViews[i].getSourceUnit()})
                                .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));

                BigDecimal sourceAmount= new BigDecimal(0);
                for(int j=0;j<stockRecordViews3.length;j++) {
                    sourceAmount=sourceAmount.add(stockRecordViews3[j].getAvailableAmount());
                }

                BigDecimal sourceAmount1= new BigDecimal(0);
                for(int l=0;l<stockRecordViews4.length;l++) {
                    sourceAmount1=sourceAmount1.add(stockRecordViews4[l].getAvailableAmount());
                }

                TransferOrderItem transferOrderItem = new TransferOrderItem();
                TransferOrderItemView transferOrderItemView = new TransferOrderItemView();
                transferOrderItem.setTargetStorageLocationId(safetyStockViews[i].getTargetStorageLocationId());
                transferOrderItem.setUnit(safetyStockViews[i].getUnit());
                transferOrderItem.setUnitAmount(safetyStockViews[i].getUnitAmount());
                transferOrderItem.setSourceStorageLocationId(safetyStockViews[i].getSourceStorageLocationId());
                transferOrderItem.setSourceUnit(safetyStockViews[i].getSourceUnit());
                transferOrderItem.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                transferOrderItem.setSupplyId(safetyStockViews[i].getSupplyId());
                //预设计划数量
                transferOrderItem.setScheduledAmount(safetyStockViews[i].getAmountMin().subtract(sourceAmount));
                transferOrderItem.setPersonId(TransferAuto.getPersonId());
                transferOrderItem.setRealAmount(new BigDecimal(0));
                transferOrderItem.setOperateTime(new Timestamp(System.currentTimeMillis()));
                transferOrderItem.setTransferOrderId(newTransferOrderID);
                transferOrderItem.setState(0);

                if (stockRecordViews4.length>0 && sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) <0&& sourceAmount1.compareTo(safetyStockViews[i].getAmountMin())>=0) {
                    transferOrderItem.setComment("成功一键移库");
                    succeedOrder=true;
                    transferOrderItemsList.add(transferOrderItem);

                }else if (stockRecordViews4.length==0){
                    transferOrderItemView.setComment("源库位库存条目不存在！");

                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                    transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                    transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(0);


                    falseTransferOrderItemsList.add(transferOrderItemView);
                }else if (sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) >=0){
                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setTargetStorageLocationName(safetyStockViews[i].getTargetStorageLocationName());
                    transferOrderItemView.setUnit(safetyStockViews[i].getUnit());
                    transferOrderItemView.setUnitAmount(safetyStockViews[i].getUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(1);
                    transferOrderItem.setComment("库存充足！");

                    falseTransferOrderItemsList.add(transferOrderItemView);
                }else if (sourceAmount1.compareTo(safetyStockViews[i].getAmountMin())<0){
                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                    transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                    transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());

                    transferOrderItemView.setScheduledAmount(safetyStockViews[i].getAmountMin());
                    transferOrderItemView.setRealAmount(sourceAmount1);

                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(2);
                    transferOrderItem.setComment(safetyStockViews[i].getAmountMin().subtract(sourceAmount1).toString());
                    falseTransferOrderItemsList.add(transferOrderItemView);
                }

            }
            if (!succeedOrder){
                this.transferOrderService.remove(accountBook,new int[]{newTransferOrderID});
            }
        }


        TransferOrderItem[] transferOrderItems=null;
        transferOrderItems = (TransferOrderItem[]) Array.newInstance(TransferOrderItem.class,transferOrderItemsList.size());
        transferOrderItemsList.toArray(transferOrderItems);

        if (transferOrderItems.length==0){
            if(transferType==1){
            throw new WMSServiceException("当前备货库存充足或备货源库位上库存不足，未能自动生成备货，请检查备货库存设置和库存记录");
            }else if(transferType==0){
                throw new WMSServiceException("当前上架库存充足或备货源库位上库存不足，未能自动生成上架，请检查上架库存设置和库存记录");
            }
        }
        if (transferOrderItems.length!=0) {
            this.transferOrderItemService.add(accountBook, transferOrderItems);
        }
        return falseTransferOrderItemsList;
    }

    public List<TransferOrderItemView> transferAutoNew(String accountBook, TransferAuto TransferAuto) throws WMSServiceException{
        new Validator("人员").notnull().validate(TransferAuto.getPersonId());
        new Validator("移库类型").min(0).max(2).validate(TransferAuto.getTransferType());

        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, TransferAuto.getWarehouseId(), " 仓库");
        //区分安全库存类型
        int transferType=TransferAuto.getTransferType();

        SafetyStockView[] AllSafetyStockViews=safetyStockService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{TransferAuto.getWarehouseId()}).addCondition("type",new Integer[]{transferType}));
        if(AllSafetyStockViews.length==0){throw new WMSServiceException("当前仓库无任何安全库存记录，无法自动添加作业单条目！");}
        TransferArgs transferArgs=new TransferArgs();
        TransferItem transferItem=new TransferItem();


        //新建列表存放条目
        List<TransferOrderItem> transferOrderItemsList=new ArrayList();
        List<TransferOrderItemView> falseTransferOrderItemsList=new ArrayList();
        //TODO 按供货商分组
        Map<Integer, List<SafetyStockView>> groupBySupplierIdMap =
                Stream.of(AllSafetyStockViews).collect(Collectors.groupingBy(SafetyStockView::getSupplierId));

        Iterator<Map.Entry<Integer,List<SafetyStockView>>> entries = groupBySupplierIdMap.entrySet().iterator();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<SafetyStockView>> entry = entries.next();
            Integer supplierId=entry.getKey();

            TransferOrder transferOrder=new TransferOrder();
            transferOrder.setType(transferType);
            transferOrder.setWarehouseId(TransferAuto.getWarehouseId());
            transferOrder.setDescription("自动移库");
            transferOrder.setCreatePersonId(TransferAuto.getPersonId());
            transferOrder.setSupplierId(supplierId);
            int newTransferOrderID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            List<SafetyStockView> safetyStockViewsListFirst=entry.getValue();
            SafetyStockView[] safetyStockFirstViews=null;
            safetyStockFirstViews = (SafetyStockView[]) Array.newInstance(SafetyStockView.class,safetyStockViewsListFirst.size());
            safetyStockViewsListFirst.toArray(safetyStockFirstViews);

            //TODO 按目标库位分组
            Map<Integer, List<SafetyStockView>> groupByTargetStorageLocationIdMap =
                    Stream.of(safetyStockFirstViews).collect(Collectors.groupingBy(SafetyStockView::getTargetStorageLocationId));

            Iterator<Map.Entry<Integer,List<SafetyStockView>>> setItems = groupByTargetStorageLocationIdMap.entrySet().iterator();
            boolean succeedOrder=false;
            while (setItems.hasNext()) {
                Map.Entry<Integer, List<SafetyStockView>> setItem = entries.next();
                Integer targetStorageLocationId = entry.getKey();

                List<SafetyStockView> safetyStockViewsList=setItem.getValue();
                SafetyStockView[] safetyStockViews=null;
                safetyStockViews = (SafetyStockView[]) Array.newInstance(SafetyStockView.class,safetyStockViewsList.size());
                safetyStockViewsList.toArray(safetyStockViews);
                BigDecimal needAmount=safetyStockViews[0].getAmountMin();

                for(int i=0;i<safetyStockViews.length;i++){

                    StockRecordViewNewest[] stockRecordViews3 = stockRecordService.findNewest(accountBook,
                            new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getTargetStorageLocationId()})
                                    .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                    .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getUnitAmount()})
                                    .addCondition("unit", new String[]{safetyStockViews[i].getUnit()})
                                    .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));
                    StockRecordViewNewest[] stockRecordViews4 = stockRecordService.findNewest(accountBook,
                            new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getSourceStorageLocationId()})
                                    .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                    .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getSourceUnitAmount()})
                                    .addCondition("unit", new String[]{safetyStockViews[i].getSourceUnit()})
                                    .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));

                    BigDecimal sourceAmount= new BigDecimal(0);
                    for(int j=0;j<stockRecordViews3.length;j++) {
                        sourceAmount=sourceAmount.add(stockRecordViews3[j].getAvailableAmount());
                    }

                    BigDecimal sourceAmount1= new BigDecimal(0);
                    for(int l=0;l<stockRecordViews4.length;l++) {
                        sourceAmount1=sourceAmount1.add(stockRecordViews4[l].getAvailableAmount());
                    }

                    TransferOrderItem transferOrderItem = new TransferOrderItem();
                    TransferOrderItemView transferOrderItemView = new TransferOrderItemView();
                    transferOrderItem.setTargetStorageLocationId(safetyStockViews[i].getTargetStorageLocationId());
                    transferOrderItem.setUnit(safetyStockViews[i].getUnit());
                    transferOrderItem.setUnitAmount(safetyStockViews[i].getUnitAmount());
                    transferOrderItem.setSourceStorageLocationId(safetyStockViews[i].getSourceStorageLocationId());
                    transferOrderItem.setSourceUnit(safetyStockViews[i].getSourceUnit());
                    transferOrderItem.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                    transferOrderItem.setSupplyId(safetyStockViews[i].getSupplyId());
                    //预设计划数量
                    transferOrderItem.setScheduledAmount(safetyStockViews[i].getAmountMin().subtract(sourceAmount));
                    transferOrderItem.setPersonId(TransferAuto.getPersonId());
                    transferOrderItem.setRealAmount(new BigDecimal(0));
                    transferOrderItem.setOperateTime(new Timestamp(System.currentTimeMillis()));
                    transferOrderItem.setTransferOrderId(newTransferOrderID);
                    transferOrderItem.setState(0);
                    //有源库存，目标库位的数小于安全库存数，源库位库存数大于安全库存数
                    if (stockRecordViews4.length>0
                            && sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) <0
                            && sourceAmount1.compareTo(safetyStockViews[i].getAmountMin())>=0) {
                        transferOrderItem.setComment("成功一键移库");
                        succeedOrder=true;
                        transferOrderItemsList.add(transferOrderItem);
                        this.transferOrderItemService.add(accountBook, new TransferOrderItem[]{transferOrderItem});
                        needAmount=needAmount.subtract(transferOrderItem.getScheduledAmount());

                    }else if (stockRecordViews4.length==0){
                        transferOrderItemView.setComment("源库位库存条目不存在！");

                        transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                        transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                        transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                        transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                        transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                        transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                        transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                        transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                        transferOrderItemView.setState(0);


                        falseTransferOrderItemsList.add(transferOrderItemView);
                    }else if (sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) >=0){
                        transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                        transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                        transferOrderItemView.setTargetStorageLocationName(safetyStockViews[i].getTargetStorageLocationName());
                        transferOrderItemView.setUnit(safetyStockViews[i].getUnit());
                        transferOrderItemView.setUnitAmount(safetyStockViews[i].getUnitAmount());
                        transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                        transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                        transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                        transferOrderItemView.setState(1);
                        transferOrderItem.setComment("库存充足！");

                        falseTransferOrderItemsList.add(transferOrderItemView);
                    }else if (sourceAmount1.compareTo(needAmount)<0){
                        transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                        transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                        transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                        transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                        transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                        transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                        transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());

                        transferOrderItemView.setScheduledAmount(safetyStockViews[i].getAmountMin());
                        transferOrderItemView.setRealAmount(sourceAmount1);

                        transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                        transferOrderItemView.setState(2);
                        transferOrderItem.setComment(needAmount.subtract(sourceAmount1).toString());
                        falseTransferOrderItemsList.add(transferOrderItemView);
                    }

                }

            }
            if (!succeedOrder){
                this.transferOrderService.remove(accountBook,new int[]{newTransferOrderID});
            }
        }


        TransferOrderItem[] transferOrderItems=null;
        transferOrderItems = (TransferOrderItem[]) Array.newInstance(TransferOrderItem.class,transferOrderItemsList.size());
        transferOrderItemsList.toArray(transferOrderItems);

        if (transferOrderItems.length==0){
            if(transferType==1){
                throw new WMSServiceException("当前备货库存充足或备货源库位上库存不足，未能自动生成备货，请检查备货库存设置和库存记录");
            }else if(transferType==0){
                throw new WMSServiceException("当前上架库存充足或备货源库位上库存不足，未能自动生成上架，请检查上架库存设置和库存记录");
            }
        }
//        if (transferOrderItems.length!=0) {
//            this.transferOrderItemService.add(accountBook, transferOrderItems);
//        }
        return falseTransferOrderItemsList;
    }

    public List<TransferOrderItemView> putAwayAuto(String accountBook, TransferAuto TransferAuto) throws WMSServiceException{
        new Validator("人员").notnull().validate(TransferAuto.getPersonId());
        new Validator("移库类型").min(0).max(2).validate(TransferAuto.getTransferType());

        idChecker.check(com.wms.services.warehouse.service.WarehouseService.class, accountBook, TransferAuto.getWarehouseId(), " 仓库");
        //区分安全库存类型
        int transferType=TransferAuto.getTransferType();

        SafetyStockView[] AllSafetyStockViews=safetyStockService.find(accountBook,new Condition().addCondition("warehouseId",new Integer[]{TransferAuto.getWarehouseId()}).addCondition("type",new Integer[]{transferType}));
        if(AllSafetyStockViews.length==0){throw new WMSServiceException("当前仓库无任何安全库存记录，无法自动添加作业单条目！");}
        TransferArgs transferArgs=new TransferArgs();
        TransferItem transferItem=new TransferItem();


        //新建列表存放条目
        List<TransferOrderItem> transferOrderItemsList=new ArrayList();
        List<TransferOrderItemView> falseTransferOrderItemsList=new ArrayList();
        //TODO 按供货商分组
        Map<Integer, List<SafetyStockView>> groupBySupplierIdMap =
                Stream.of(AllSafetyStockViews).collect(Collectors.groupingBy(SafetyStockView::getSupplierId));

        Iterator<Map.Entry<Integer,List<SafetyStockView>>> entries = groupBySupplierIdMap.entrySet().iterator();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<SafetyStockView>> entry = entries.next();
            Integer supplierId=entry.getKey();

            TransferOrder transferOrder=new TransferOrder();
            transferOrder.setType(transferType);
            transferOrder.setWarehouseId(TransferAuto.getWarehouseId());
            transferOrder.setDescription("自动移库");
            transferOrder.setCreatePersonId(TransferAuto.getPersonId());
            transferOrder.setSupplierId(supplierId);
            int newTransferOrderID = this.transferOrderService.add(accountBook, new TransferOrder[]{transferOrder})[0];

            List<SafetyStockView> safetyStockViewsList=entry.getValue();

            SafetyStockView[] safetyStockViews=null;
            safetyStockViews = (SafetyStockView[]) Array.newInstance(SafetyStockView.class,safetyStockViewsList.size());
            safetyStockViewsList.toArray(safetyStockViews);
            boolean succeedOrder=false;
            for(int i=0;i<safetyStockViews.length;i++){
                StockRecordViewNewest[] stockRecordViews3 = stockRecordService.findNewest(accountBook,
                        new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getTargetStorageLocationId()})
                                .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getUnitAmount()})
                                .addCondition("unit", new String[]{safetyStockViews[i].getUnit()})
                                .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));
                StockRecordViewNewest[] stockRecordViews4 = stockRecordService.findNewest(accountBook,
                        new Condition().addCondition("storageLocationId", new Integer[]{safetyStockViews[i].getSourceStorageLocationId()})
                                .addCondition("supplyId", new Integer[]{safetyStockViews[i].getSupplyId()})
                                .addCondition("unitAmount", new BigDecimal[]{safetyStockViews[i].getSourceUnitAmount()})
                                .addCondition("unit", new String[]{safetyStockViews[i].getSourceUnit()})
                                .addCondition("state", new Integer[]{TransferOrderItemService.STATE_ALL_FINISH}));

                BigDecimal sourceAmount= new BigDecimal(0);
                for(int j=0;j<stockRecordViews3.length;j++) {
                    sourceAmount=sourceAmount.add(stockRecordViews3[j].getAvailableAmount());
                }

                BigDecimal sourceAmount1= new BigDecimal(0);
                for(int l=0;l<stockRecordViews4.length;l++) {
                    sourceAmount1=sourceAmount1.add(stockRecordViews4[l].getAvailableAmount());
                }

                TransferOrderItem transferOrderItem = new TransferOrderItem();
                TransferOrderItemView transferOrderItemView = new TransferOrderItemView();
                transferOrderItem.setTargetStorageLocationId(safetyStockViews[i].getTargetStorageLocationId());
                transferOrderItem.setUnit(safetyStockViews[i].getUnit());
                transferOrderItem.setUnitAmount(safetyStockViews[i].getUnitAmount());
                transferOrderItem.setSourceStorageLocationId(safetyStockViews[i].getSourceStorageLocationId());
                transferOrderItem.setSourceUnit(safetyStockViews[i].getSourceUnit());
                transferOrderItem.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                transferOrderItem.setSupplyId(safetyStockViews[i].getSupplyId());
                //预设计划数量
                transferOrderItem.setScheduledAmount(safetyStockViews[i].getAmountMax().subtract(sourceAmount));
                transferOrderItem.setPersonId(TransferAuto.getPersonId());
                transferOrderItem.setRealAmount(new BigDecimal(0));
                transferOrderItem.setOperateTime(new Timestamp(System.currentTimeMillis()));
                transferOrderItem.setTransferOrderId(newTransferOrderID);
                transferOrderItem.setState(0);

                if (stockRecordViews4.length>0 && sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) <0&& sourceAmount1.compareTo(safetyStockViews[i].getAmountMin())>=0) {
                    transferOrderItem.setComment("成功一键移库");
                    succeedOrder=true;
                    this.transferOrderItemService.add(accountBook, new TransferOrderItem[]{transferOrderItem});

                }else if (stockRecordViews4.length==0){
                    transferOrderItemView.setComment("源库位库存条目不存在！");

                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                    transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                    transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(0);


                    falseTransferOrderItemsList.add(transferOrderItemView);
                }else if (sourceAmount.compareTo(safetyStockViews[i].getAmountMin()) >=0){
                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setTargetStorageLocationName(safetyStockViews[i].getTargetStorageLocationName());
                    transferOrderItemView.setUnit(safetyStockViews[i].getUnit());
                    transferOrderItemView.setUnitAmount(safetyStockViews[i].getUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());
                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(1);
                    transferOrderItem.setComment("库存充足！");

                    falseTransferOrderItemsList.add(transferOrderItemView);
                }else if (sourceAmount1.compareTo(safetyStockViews[i].getAmountMin())<0){
                    transferOrderItemView.setSupplierName(safetyStockViews[i].getSupplierName());
                    transferOrderItemView.setSupplierNo(safetyStockViews[i].getSupplierNo());

                    transferOrderItemView.setSourceStorageLocationName(safetyStockViews[i].getSourceStorageLocationName());
                    transferOrderItemView.setSourceUnit(safetyStockViews[i].getSourceUnit());
                    transferOrderItemView.setSourceUnitAmount(safetyStockViews[i].getSourceUnitAmount());
                    transferOrderItemView.setMaterialName(safetyStockViews[i].getMaterialName());
                    transferOrderItemView.setMaterialNo(safetyStockViews[i].getMaterialNo());

                    transferOrderItemView.setScheduledAmount(safetyStockViews[i].getAmountMin());
                    transferOrderItemView.setRealAmount(sourceAmount1);

                    transferOrderItemView.setMaterialProductLine(safetyStockViews[i].getMaterialProductLine());
                    transferOrderItemView.setState(2);
                    transferOrderItem.setComment(safetyStockViews[i].getAmountMin().subtract(sourceAmount1).toString());
                    falseTransferOrderItemsList.add(transferOrderItemView);
                }

            }
            if (!succeedOrder){
                this.transferOrderService.remove(accountBook,new int[]{newTransferOrderID});
            }
        }


        TransferOrderItem[] transferOrderItems=null;
        transferOrderItems = (TransferOrderItem[]) Array.newInstance(TransferOrderItem.class,transferOrderItemsList.size());
        transferOrderItemsList.toArray(transferOrderItems);

        if (transferOrderItems.length==0){
            if(transferType==1){
                throw new WMSServiceException("当前备货库存充足或备货源库位上库存不足，未能自动生成备货，请检查备货库存设置和库存记录");
            }else if(transferType==0){
                throw new WMSServiceException("当前上架库存充足或备货源库位上库存不足，未能自动生成上架，请检查上架库存设置和库存记录");
            }
        }
//        if (transferOrderItems.length!=0) {
//            this.transferOrderItemService.add(accountBook, transferOrderItems);
//        }
        return falseTransferOrderItemsList;
    }

    @Override
    public void deliveryFinish(String accountBook,DeliveryFinish deliveryFinish) throws WMSServiceException{

        List<Integer>ids=deliveryFinish.getDeliveryOrderIds();
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个出库单！");
        }
        //DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderDAO.find(accountBook, new Condition().addCondition("id", ids.toArray()));

        DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderDAO.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));
        if (deliveryOrderViews.length == 0) return;
        DeliveryOrder[] deliveryOrders = ReflectHelper.createAndCopyFields(deliveryOrderViews,DeliveryOrder.class);
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_DELIVER_FINNISH) {
                throw new WMSServiceException(String.format("当前出库单（%s）已经确认核减，无法进行发运操作", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_IN_DELIVER) {
                throw new WMSServiceException(String.format("当前出库单（%s）已经发运在途，无法重复发运", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_IN_LOADING||deliveryOrder.getState() ==DeliveryOrderService.STATE_PARTIAL_LOADING) {
                throw new WMSServiceException(String.format("当前出库单（%s）未完成装车，无法发运", deliveryOrder.getNo()));
            }


        });

        DeliveryOrderItemView[] itemViews = this.deliveryOrderItemService.find(accountBook,new Condition().addCondition("deliveryOrderId",ids.toArray(), ConditionItem.Relation.IN));
        DeliveryOrderItem[] deliveryOrderItems = ReflectHelper.createAndCopyFields(itemViews,DeliveryOrderItem.class);
        if (deliveryOrderItems.length == 0) return;

        Stream.of(deliveryOrderItems).forEach(deliveryOrderItem -> {
            if (deliveryOrderItem.getState() !=DeliveryOrderService.STATE_ALL_LOADING) {
                throw new WMSServiceException(String.format("当前出库单（%d）未完成装车，无法发运", deliveryOrderItem.getDeliveryOrderId()));
            }
        });
        this.deliveryOrderItemService.update(accountBook,deliveryOrderItems);
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            if (deliveryOrder.getState() !=DeliveryOrderService.STATE_IN_DELIVER) {
                deliveryOrder.setState(DeliveryOrderService.STATE_IN_DELIVER);
            }
            deliveryOrder.setLiscensePlateNumber(deliveryFinish.getLiscensePlateNumber());
            deliveryOrder.setDriverName(deliveryFinish.getDriverName());
            deliveryOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));
        });
        this.update(accountBook,deliveryOrders);

    }

    @Override
    public void decreaseInAccounting(String accountBook,List<Integer> ids) throws WMSServiceException{

        //TODO 人员id没往下传
        if (ids.size() == 0) {
            throw new WMSServiceException("请选择至少一个出库单！");
        }
        //DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderDAO.find(accountBook, new Condition().addCondition("id", ids.toArray()));

        DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderDAO.find(accountBook,new Condition().addCondition("id",ids.toArray(), ConditionItem.Relation.IN));
        if (deliveryOrderViews.length == 0) return;
        DeliveryOrder[] deliveryOrders = ReflectHelper.createAndCopyFields(deliveryOrderViews,DeliveryOrder.class);
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            if (deliveryOrder.getState() ==DeliveryOrderService.STATE_DELIVER_FINNISH) {
                throw new WMSServiceException(String.format("当前出库单（%s）已经核减完成，无法重复操作", deliveryOrder.getNo()));
            }
            if (deliveryOrder.getState() !=DeliveryOrderService.STATE_IN_DELIVER) {
                throw new WMSServiceException(String.format("当前出库单（%s）未完成发运，无法核减", deliveryOrder.getNo()));
            }


        });
        Stream.of(deliveryOrders).forEach(deliveryOrder -> {
            deliveryOrder.setState(DeliveryOrderService.STATE_DELIVER_FINNISH);
            deliveryOrder.setReturnNoteTime(new Timestamp(System.currentTimeMillis()));
        });
        this.update(accountBook,deliveryOrders);

    }

    @Override
    public List<DeliveryOrderAndItems> getPreviewData(String accountBook, List<Integer> deliveryOrderIDs) throws WMSServiceException{
        DeliveryOrderView[] deliveryOrderViews = this.deliveryOrderDAO.find(accountBook,new Condition().addCondition("id",deliveryOrderIDs.toArray(), ConditionItem.Relation.IN));
        DeliveryOrderItemView[] itemViews = this.deliveryOrderItemService.find(accountBook,new Condition().addCondition("deliveryOrderId",deliveryOrderIDs.toArray(), ConditionItem.Relation.IN));
        List<DeliveryOrderAndItems> result = new ArrayList<>();
        for(DeliveryOrderView deliveryOrderView : deliveryOrderViews){
            DeliveryOrderAndItems deliveryOrderAndItems = new DeliveryOrderAndItems();
            deliveryOrderAndItems.setDeliveryOrder(deliveryOrderView);
            deliveryOrderAndItems.setDeliveryOrderItems(new ArrayList<>());
            result.add(deliveryOrderAndItems);
            for(DeliveryOrderItemView itemView : itemViews){
                if(itemView.getDeliveryOrderId() == deliveryOrderView.getId()){
                    deliveryOrderAndItems.getDeliveryOrderItems().add(itemView);
                }
            }
        }
        return result;
    }

    @Override
    public List<DeliveryOrderItemView> deliveryByPakage(String accountBook,DeliveryByPakage deliveryByPakage) throws WMSServiceException{

        //TODO 传的是发货套餐ID
        Integer curId=deliveryByPakage.getPackageId();

        PackageView[] curPakageViews = this.packageService.find(accountBook,new Condition().addCondition("id",curId, ConditionItem.Relation.IN));
        PackageItemView[] itemViews = this.packageItemService.find(accountBook,new Condition().addCondition("packageId",curId, ConditionItem.Relation.IN));
        if (itemViews.length == 0) {
            throw new WMSServiceException(String.format("当前发货套餐未包括条目信息，套餐名称（%S），无法创建出库单", curPakageViews[0].getName()));
        }
        int curDeliveryOrderId=deliveryByPakage.getDeliveryOrderId();
        if (deliveryByPakage.getDeliveryOrderId()==-1) {
            //新建出库单
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setCreatePersonId(deliveryByPakage.getPersonId());
            deliveryOrder.setWarehouseId(deliveryByPakage.getWarehouseId());
            deliveryOrder.setState(DeliveryOrderService.STATE_IN_LOADING);
            deliveryOrder.setDescription("套餐一键发货");
            curDeliveryOrderId = this.add(accountBook, new DeliveryOrder[]{deliveryOrder})[0];
        }

        List<DeliveryOrderItem> deliveryOrderItemList=new ArrayList();
        List<DeliveryOrderItemView> falseDeliveryOrderItemList=new ArrayList();
        for(int i=0;i<itemViews.length;i++){
            //寻找供货
            //SupplyView[] supplyViews=supplyService.find(accountBook, new Condition().addCondition("id", new Integer[]{itemViews[i].getSupplyId()}));
            StockRecordViewNewest[] stockRecordViews = stockRecordService.findNewest(accountBook,
                    new Condition().addCondition("storageLocationId", new Integer[]{itemViews[i].getDefaultDeliveryStorageLocationId()})
                            .addCondition("supplyId", new Integer[]{itemViews[i].getSupplyId()})
                            .addCondition("unitAmount", new BigDecimal[]{itemViews[i].getDefaultDeliveryUnitAmount()})
                            .addCondition("unit", new String[]{itemViews[i].getDefaultDeliveryUnit()}));

            BigDecimal sourceAmount= new BigDecimal(0);
            for(int j=0;j<stockRecordViews.length;j++) {
                sourceAmount=sourceAmount.add(stockRecordViews[j].getAvailableAmount());
            }

            if (stockRecordViews.length!=0&&sourceAmount.compareTo(itemViews[i].getDefaultDeliveryAmount()) >=0) {


                    DeliveryOrderItem deliveryOrderItem = new DeliveryOrderItem();
                    deliveryOrderItem.setSourceStorageLocationId(itemViews[i].getDefaultDeliveryStorageLocationId());
                    deliveryOrderItem.setUnit(itemViews[i].getDefaultDeliveryUnit());
                    deliveryOrderItem.setUnitAmount(itemViews[i].getDefaultDeliveryUnitAmount());
                    deliveryOrderItem.setSupplyId(itemViews[i].getSupplyId());
                    deliveryOrderItem.setScheduledAmount(itemViews[i].getDefaultDeliveryAmount());
                    deliveryOrderItem.setPersonId(deliveryByPakage.getPersonId());
                    deliveryOrderItem.setDeliveryOrderId(curDeliveryOrderId);
                    deliveryOrderItem.setRealAmount(BigDecimal.ZERO);
                    deliveryOrderItem.setComment("套餐发货项");
                    deliveryOrderItemList.add(deliveryOrderItem);

            }
            else{
                DeliveryOrderItemView falseDeliveryOrderItemView = new DeliveryOrderItemView();
                falseDeliveryOrderItemView.setSourceStorageLocationId(itemViews[i].getDefaultDeliveryStorageLocationId());
                falseDeliveryOrderItemView.setSourceStorageLocationName(itemViews[i].getDefaultDeliveryStorageLocationName());
                falseDeliveryOrderItemView.setUnit(itemViews[i].getDefaultDeliveryUnit());
                falseDeliveryOrderItemView.setUnitAmount(itemViews[i].getDefaultDeliveryUnitAmount());
                falseDeliveryOrderItemView.setSupplyId(itemViews[i].getSupplyId());

                falseDeliveryOrderItemView.setSupplierName(itemViews[i].getSupplierName());
                falseDeliveryOrderItemView.setSupplierNo(itemViews[i].getSupplierNo());
                falseDeliveryOrderItemView.setMaterialName(itemViews[i].getMaterialName());
                falseDeliveryOrderItemView.setMaterialNo(itemViews[i].getMaterialNo());
                falseDeliveryOrderItemView.setMaterialProductLine(itemViews[i].getMaterialProductLine());

                falseDeliveryOrderItemView.setScheduledAmount(itemViews[i].getDefaultDeliveryAmount());
                falseDeliveryOrderItemView.setRealAmount(sourceAmount);
                falseDeliveryOrderItemView.setComment("失败发货项");
                falseDeliveryOrderItemList.add(falseDeliveryOrderItemView);
            }

        }
        DeliveryOrderItem[] deliveryOrderItems=null;
        deliveryOrderItems = (DeliveryOrderItem[]) Array.newInstance(DeliveryOrderItem.class,deliveryOrderItemList.size());
        deliveryOrderItemList.toArray(deliveryOrderItems);
        if (deliveryOrderItems.length==0) {
            throw new WMSServiceException(String.format("当前发货套餐无可正常发货项，套餐名称（%S），无法创建出库单，请检查发货套餐设置再试！", curPakageViews[0].getName()));
        }
        this.deliveryOrderItemService.add(accountBook,deliveryOrderItems);
        return falseDeliveryOrderItemList;
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.deliveryOrderDAO.findCount(accountBook,cond);
    }
}
