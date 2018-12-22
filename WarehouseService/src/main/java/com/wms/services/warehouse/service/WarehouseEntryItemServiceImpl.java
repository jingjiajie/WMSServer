package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.WarehouseEntryItemDAO;
import com.wms.services.warehouse.datastructures.ItemType;
import com.wms.services.warehouse.datastructures.RandomCode;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryItemServiceImpl implements WarehouseEntryItemService {
    @Autowired
    WarehouseEntryItemDAO warehouseEntryItemDAO;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    ItemRelatedRecordService itemRelatedRecordService;

    @Override
    public int[] add(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        if (warehouseEntryItems.length == 0) return new int[]{};
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        //验证字段
        this.validateEntities(accountBook, warehouseEntryItems);
        //增加库存
        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            TransferStock transferStock = new TransferStock();
            transferStock.setAmount(warehouseEntryItem.getRealAmount());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setInventoryDate(warehouseEntryItem.getInventoryDate());
            transferStock.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setManufactureDate(warehouseEntryItem.getManufactureDate());
            this.stockRecordService.addAmount(accountBook, transferStock);
        });
        //添加到数据库中
        int[] ids = this.warehouseEntryItemDAO.add(accountBook, warehouseEntryItems);
        return ids;
    }

    @Override
    public int[] add1(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        if (warehouseEntryItems.length == 0) return new int[]{};
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        //验证字段
        this.validateEntities(accountBook, warehouseEntryItems);
        //添加到数据库中
        int[] ids = this.warehouseEntryItemDAO.add(accountBook, warehouseEntryItems);
        //增加库存
        for (int i = 0; i < warehouseEntryItems.length; i++) {
            TransferStock transferStock = new TransferStock();
            transferStock.setAmount(warehouseEntryItems[i].getRealAmount());
            transferStock.setAvailableAmount(warehouseEntryItems[i].getRealAmount());
            transferStock.setUnit(warehouseEntryItems[i].getUnit());
            transferStock.setUnitAmount(warehouseEntryItems[i].getUnitAmount());
            transferStock.setInventoryDate(warehouseEntryItems[i].getInventoryDate());
            transferStock.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStock.setSourceStorageLocationId(warehouseEntryItems[i].getStorageLocationId());
            transferStock.setSupplyId(warehouseEntryItems[i].getSupplyId());
            transferStock.setManufactureDate(warehouseEntryItems[i].getManufactureDate());
            this.stockRecordService.addAmount(accountBook, transferStock,new TransferStock());
        }
        return ids;
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        this.update(accountBook, warehouseEntryItems, false);
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems, boolean allowUpdateInspectionAmount) throws WMSServiceException {
        //WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        this.validateEntities(accountBook, warehouseEntryItems);
        int[] ids = Stream.of(warehouseEntryItems).mapToInt(WarehouseEntryItem::getId).toArray();
        WarehouseEntryItemView[] oriItemViews = this.find(accountBook, new Condition().addCondition("id", ReflectHelper.intArrayToIntegerArray(ids), ConditionItem.Relation.IN));

        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            WarehouseEntryItemView oriItemView = Stream.of(oriItemViews).filter(item -> item.getId() == warehouseEntryItem.getId()).findFirst().orElse(null);
            if (oriItemView == null) {
                throw new WMSServiceException(String.format("入库单条目不存在，请重新提交！", warehouseEntryItem.getId()));
            }
            BigDecimal deltaRealAmount = warehouseEntryItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //不用管拒收数量 BigDecimal deltaRefuseAmount = warehouseEntryItem.getRefuseAmount().subtract(oriItemView.getRefuseAmount());
            //送检数量不能改
            BigDecimal deltaInspectionAmount = warehouseEntryItem.getInspectionAmount().subtract(oriItemView.getInspectionAmount());
            if (!allowUpdateInspectionAmount && deltaInspectionAmount.compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException("不允许修改入库单条目送检数量！");
            }
            //修改实收数量，单位或单位数量，或收货库区，更新库存
            if (deltaRealAmount.compareTo(BigDecimal.ZERO) != 0
                    || !oriItemView.getUnit().equals(warehouseEntryItem.getUnit())
                    || oriItemView.getUnitAmount().compareTo(warehouseEntryItem.getUnitAmount()) != 0
                    || oriItemView.getStorageLocationId() != warehouseEntryItem.getStorageLocationId()) {
                //如果已经送检，禁止修改入库数量
                if (warehouseEntryItem.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                    throw new WMSServiceException(String.format("不允许修改已送检/入库的入库单条目(%d)", warehouseEntryItem.getId()));
                }
                //冲抵原库存
                TransferStock transferStockAgainst = new TransferStock();
                transferStockAgainst.setAmount(oriItemView.getRealAmount().negate());
                transferStockAgainst.setUnit(oriItemView.getUnit());
                transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
                transferStockAgainst.setInventoryDate(oriItemView.getInventoryDate());
                transferStockAgainst.setRelatedOrderNo(oriItemView.getWarehouseEntryNo());
                transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
                transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
                transferStockAgainst.setManufactureDate(oriItemView.getManufactureDate());
                this.stockRecordService.addAmount(accountBook, transferStockAgainst);

                //增加新库存
                TransferStock transferStockNew = new TransferStock();
                transferStockNew.setAmount(warehouseEntryItem.getRealAmount());
                transferStockNew.setUnit(warehouseEntryItem.getUnit());
                transferStockNew.setUnitAmount(warehouseEntryItem.getUnitAmount());
                transferStockNew.setInventoryDate(warehouseEntryItem.getInventoryDate());
                transferStockNew.setRelatedOrderNo(oriItemView.getWarehouseEntryNo());
                transferStockNew.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
                transferStockNew.setSupplyId(warehouseEntryItem.getSupplyId());
                transferStockNew.setManufactureDate(warehouseEntryItem.getManufactureDate());
                this.stockRecordService.addAmount(accountBook, transferStockNew);
            }
        });
        this.warehouseEntryItemDAO.update(accountBook, warehouseEntryItems);
    }

    @Override
    public void update1(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        this.update(accountBook, warehouseEntryItems, false);
    }

    @Override
    public void update1(String accountBook, WarehouseEntryItem[] warehouseEntryItems, boolean allowUpdateInspectionAmount) throws WMSServiceException {
        //WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        this.validateEntities(accountBook, warehouseEntryItems);
        int[] ids = Stream.of(warehouseEntryItems).mapToInt(WarehouseEntryItem::getId).toArray();
        WarehouseEntryItemView[] oriItemViews = this.find(accountBook, new Condition().addCondition("id", ReflectHelper.intArrayToIntegerArray(ids), ConditionItem.Relation.IN));

        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            WarehouseEntryItemView oriItemView = Stream.of(oriItemViews).filter(item -> item.getId() == warehouseEntryItem.getId()).findFirst().orElse(null);
            if (oriItemView == null) {
                throw new WMSServiceException(String.format("入库单条目不存在，请重新提交！", warehouseEntryItem.getId()));
            }
            BigDecimal deltaRealAmount = warehouseEntryItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //不用管拒收数量 BigDecimal deltaRefuseAmount = warehouseEntryItem.getRefuseAmount().subtract(oriItemView.getRefuseAmount());
            //送检数量不能改
            BigDecimal deltaInspectionAmount = warehouseEntryItem.getInspectionAmount().subtract(oriItemView.getInspectionAmount());
            if (!allowUpdateInspectionAmount && deltaInspectionAmount.compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException("不允许修改入库单条目送检数量！");
            }
            //修改实收数量，单位或单位数量，或收货库区，更新库存
            if (deltaRealAmount.compareTo(BigDecimal.ZERO) != 0
                    || !oriItemView.getUnit().equals(warehouseEntryItem.getUnit())
                    || oriItemView.getUnitAmount().compareTo(warehouseEntryItem.getUnitAmount()) != 0
                    || oriItemView.getStorageLocationId() != warehouseEntryItem.getStorageLocationId()) {
                //如果已经送检，禁止修改入库数量
                if (warehouseEntryItem.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                    throw new WMSServiceException(String.format("不允许修改已送检/入库的入库单条目(%d)", warehouseEntryItem.getId()));
                }
                //冲抵原库存
                TransferStock transferStockAgainst = new TransferStock();
                transferStockAgainst.setUnit(oriItemView.getUnit());
                transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
                transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
                transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
                //默认是未送检的
                transferStockAgainst.setState(TransferStock.WAITING_FOR_INSPECTION);
                //增加新库存
                TransferStock transferStockNew = new TransferStock();
                transferStockNew.setAmount(warehouseEntryItem.getRealAmount());
                transferStockNew.setUnit(warehouseEntryItem.getUnit());
                transferStockNew.setUnitAmount(warehouseEntryItem.getUnitAmount());
                transferStockNew.setInventoryDate(warehouseEntryItem.getInventoryDate());
                transferStockNew.setRelatedOrderNo(oriItemView.getWarehouseEntryNo());
                transferStockNew.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
                transferStockNew.setSupplyId(warehouseEntryItem.getSupplyId());
                transferStockNew.setManufactureDate(warehouseEntryItem.getManufactureDate());
                this.stockRecordService.addAmount(accountBook, transferStockNew,transferStockAgainst);
            }
        });
        this.warehouseEntryItemDAO.update(accountBook, warehouseEntryItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        WarehouseEntryView warehouseEntryView = null;
        for (int id : ids) {
            WarehouseEntryItemView[] foundItemViews = this.warehouseEntryItemDAO.find(accountBook, new Condition().addCondition("id", id));
            if (foundItemViews.length == 0) {
                throw new WMSServiceException(String.format("无法找到入库单条目，请重新提交(%d)"));
            }
            WarehouseEntryItemView oriItemView = foundItemViews[0];
            if (warehouseEntryView == null) {
                final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", oriItemView.getWarehouseEntryId()));
                if (warehouseEntryViews.length == 0)
                    throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", oriItemView.getWarehouseEntryId()));
                warehouseEntryView = warehouseEntryViews[0];
            }
            if (oriItemView.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                throw new WMSServiceException(String.format("不允许删除已送检/已入库的入库单条目(%d)", oriItemView.getId()));
            }
            //冲抵原库存
            TransferStock transferStockAgainst = new TransferStock();
            transferStockAgainst.setAmount(oriItemView.getRealAmount().negate());
            transferStockAgainst.setUnit(oriItemView.getUnit());
            transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
            transferStockAgainst.setInventoryDate(oriItemView.getInventoryDate());
            transferStockAgainst.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
            transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
            transferStockAgainst.setManufactureDate(oriItemView.getManufactureDate());
            this.stockRecordService.addAmount(accountBook, transferStockAgainst);
            //删除入库时记录的随机码
            RandomCode randomCode=new RandomCode();
            randomCode.setEntryOrDeliver(0);//0代表入库
            randomCode.setItemId(id);
            this.stockRecordService.removeRandomCode(accountBook,randomCode);
        }
        try {
            this.warehouseEntryItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public void remove1(String accountBook, int[] ids) throws WMSServiceException {
        WarehouseEntryView warehouseEntryView = null;
        for (int id : ids) {
            WarehouseEntryItemView[] foundItemViews = this.warehouseEntryItemDAO.find(accountBook, new Condition().addCondition("id", id));
            if (foundItemViews.length == 0) {
                throw new WMSServiceException(String.format("无法找到入库单条目，请重新提交(%d)"));
            }
            WarehouseEntryItemView oriItemView = foundItemViews[0];
            if (warehouseEntryView == null) {
                final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", oriItemView.getWarehouseEntryId()));
                if (warehouseEntryViews.length == 0)
                    throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", oriItemView.getWarehouseEntryId()));
                warehouseEntryView = warehouseEntryViews[0];
            }
            if (oriItemView.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                throw new WMSServiceException(String.format("不允许删除已送检/已入库的入库单条目(%d)", oriItemView.getId()));
            }
            //冲抵原库存
            TransferStock transferStockAgainst = new TransferStock();
            transferStockAgainst.setUnit(oriItemView.getUnit());
            transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
            transferStockAgainst.setInventoryDate(oriItemView.getInventoryDate());
            transferStockAgainst.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
            transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
            transferStockAgainst.setItemId(oriItemView.getId());
            transferStockAgainst.setItemType(ItemType.entryItem);
            this.stockRecordService.restoreAmount(accountBook, transferStockAgainst);
            //删除入库时记录的随机码
            RandomCode randomCode=new RandomCode();
            randomCode.setEntryOrDeliver(0);//0代表入库
            randomCode.setItemId(id);
            this.stockRecordService.removeRandomCode(accountBook,randomCode);
        }
        try {
            this.warehouseEntryItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public WarehouseEntryItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.warehouseEntryItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, WarehouseEntryItem[] warehouseEntryItems) {
        //数据验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    new Validator("订单数量").min(0).validate(warehouseEntryItem.getExpectedAmount());
                    new Validator("实收数量").min(0).validate(warehouseEntryItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(warehouseEntryItem.getUnitAmount());
                    new Validator("已分配送检数量").min(0).max(warehouseEntryItem.getRealAmount()).validate(warehouseEntryItem.getInspectionAmount());
                    new Validator("拒收数量").min(0).max(warehouseEntryItem.getExpectedAmount()).validate(warehouseEntryItem.getRefuseAmount());
                    new Validator("拒收单位").notEmpty().validate(warehouseEntryItem.getRefuseUnit());
                    new Validator("拒收单位数量").min(0).validate(warehouseEntryItem.getRefuseUnitAmount());
                }
        );

        //外键验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    if (this.warehouseEntryService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getWarehouseEntryId())).length == 0) {
                        throw new WMSServiceException(String.format("入库单不存在，请重新提交！(%d)", warehouseEntryItem.getWarehouseEntryId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getStorageLocationId())).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", warehouseEntryItem.getStorageLocationId()));
                    } else if (warehouseEntryItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getPersonId())).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", warehouseEntryItem.getPersonId()));
                    }
                }
        );
    }

    private WarehouseEntryView getWarehouseEntryView(String accountBook, WarehouseEntryItem[] warehouseEntryItems) {
        //验证入库单号必须全部相同
        Stream.of(warehouseEntryItems).reduce((last, cur) -> {
            if (last.getWarehouseEntryId() != cur.getWarehouseEntryId())
                throw new WMSServiceException("入库单条目所属的入库单必须相同！");
            return cur;
        });
        //获取入库单
        int warehouseEntryID = warehouseEntryItems[0].getWarehouseEntryId();
        final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", warehouseEntryID));
        if (warehouseEntryViews.length == 0)
            throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", warehouseEntryID));
        WarehouseEntryView warehouseEntryView = warehouseEntryViews[0];
        return warehouseEntryView;
    }

    @Override
    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.warehouseEntryItemDAO.findCount(database, cond);
    }

    @Override
    @SuppressWarnings("all")
    public void receive(String accountBook, List<Integer> ids, Map<Integer, BigDecimal> idAndReturnAmount) {

        WarehouseEntryItem[] warehouseEntryItems = this.warehouseEntryItemDAO.findTable(accountBook, new Condition().addCondition("id", ReflectHelper.listToArray(ids, Integer.class), ConditionItem.Relation.IN));
        List<Integer> warehouseEntryIDs = new ArrayList<>();
        Stream.of(warehouseEntryItems).forEach((warehouseEntryItem -> {
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                throw new WMSServiceException("入库单已经入库，请不要重复入库！");
            }
            if (!warehouseEntryIDs.contains(warehouseEntryItem.getWarehouseEntryId())) {
                warehouseEntryIDs.add(warehouseEntryItem.getWarehouseEntryId());
            }
            WarehouseEntry warehouseEntry = this.warehouseEntryService.get(accountBook, warehouseEntryItem.getWarehouseEntryId());
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItem.getQualifiedStorageLocationId(), warehouseEntry.getNo() + " 入库单各条目的合格品库位");
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItem.getQualifiedStorageLocationId());
            if (idAndReturnAmount != null) {
                Integer warehouseEntryItemID = warehouseEntryItem.getId();
                if (!idAndReturnAmount.containsKey(warehouseEntryItemID)) {
                    throw new WMSServiceException("Receive返回数量参数未包含ID：" + warehouseEntryItemID);
                }
                transferStock.setAmount(warehouseEntryItem.getRealAmount().subtract(warehouseEntryItem.getInspectionAmount().subtract(idAndReturnAmount.get(warehouseEntryItemID))));
            } else {
                transferStock.setAmount(warehouseEntryItem.getRealAmount());
            }
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntry.getNo() + "(正品移库)");
            transferStock.setState(TransferStock.WAITING_FOR_INSPECTION);
            transferStock.setNewState(TransferStock.QUALIFIED);
            this.stockRecordService.RealTransformStock(accountBook, transferStock);

            warehouseEntryItem.setState(WarehouseEntryItemService.QUALIFIED);
        }));
        this.update(accountBook, warehouseEntryItems);
        //this.warehouseEntryService.updateState(accountBook, warehouseEntryIDs);
    }

    @Override
    @SuppressWarnings("all")
    public void receive1(String accountBook, List<Integer> ids, Map<Integer, BigDecimal> idAndReturnAmount) {

        WarehouseEntryItem[] warehouseEntryItems = this.warehouseEntryItemDAO.findTable(accountBook, new Condition().addCondition("id", ReflectHelper.listToArray(ids, Integer.class), ConditionItem.Relation.IN));
        List<Integer> warehouseEntryIDs = new ArrayList<>();
        Stream.of(warehouseEntryItems).forEach((warehouseEntryItem -> {
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                throw new WMSServiceException("入库单已经入库，请不要重复入库！");
            }
            if (!warehouseEntryIDs.contains(warehouseEntryItem.getWarehouseEntryId())) {
                warehouseEntryIDs.add(warehouseEntryItem.getWarehouseEntryId());
            }
            WarehouseEntry warehouseEntry = this.warehouseEntryService.get(accountBook, warehouseEntryItem.getWarehouseEntryId());
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItem.getQualifiedStorageLocationId(), warehouseEntry.getNo() + " 入库单各条目的合格品库位");
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItem.getQualifiedStorageLocationId());
            if (idAndReturnAmount != null) {
                Integer warehouseEntryItemID = warehouseEntryItem.getId();
                if (!idAndReturnAmount.containsKey(warehouseEntryItemID)) {
                    throw new WMSServiceException("Receive返回数量参数未包含ID：" + warehouseEntryItemID);
                }
                transferStock.setAmount(warehouseEntryItem.getRealAmount().subtract(warehouseEntryItem.getInspectionAmount().subtract(idAndReturnAmount.get(warehouseEntryItemID))));
            } else {
                transferStock.setAmount(warehouseEntryItem.getRealAmount());
            }
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntry.getNo() + "(正品移库)");
            transferStock.setState(TransferStock.WAITING_FOR_INSPECTION);
            transferStock.setNewState(TransferStock.QUALIFIED);
            this.stockRecordService.transferStock(accountBook, transferStock,new TransferStock());

            warehouseEntryItem.setState(WarehouseEntryItemService.QUALIFIED);
        }));
        this.update(accountBook, warehouseEntryItems);
        //this.warehouseEntryService.updateState(accountBook, warehouseEntryIDs);
    }

    @Override
    @SuppressWarnings("all")
    public void reject(String accountBook, List<Integer> ids, Map<Integer, BigDecimal> idAndReturnAmount) {
        WarehouseEntryItem[] warehouseEntryItems = this.warehouseEntryItemDAO.findTable(accountBook, new Condition().addCondition("id", ReflectHelper.listToArray(ids, Integer.class), ConditionItem.Relation.IN));
        List<Integer> warehouseEntryIDs = new ArrayList<>();
        Stream.of(warehouseEntryItems).forEach((warehouseEntryItem -> {
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                throw new WMSServiceException("入库单已经入库，请不要重复入库！");
            }
            if (!warehouseEntryIDs.contains(warehouseEntryItem.getWarehouseEntryId())) {
                warehouseEntryIDs.add(warehouseEntryItem.getWarehouseEntryId());
            }
            WarehouseEntry warehouseEntry = this.warehouseEntryService.get(accountBook, warehouseEntryItem.getWarehouseEntryId());
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItem.getUnqualifiedStorageLocationId(), warehouseEntry.getNo() + "入库单各条目的不良品库位");
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItem.getUnqualifiedStorageLocationId());
            if (idAndReturnAmount != null) {
                Integer warehouseEntryItemID = warehouseEntryItem.getId();
                if (!idAndReturnAmount.containsKey(warehouseEntryItemID)) {
                    throw new WMSServiceException("Receive返回数量参数未包含ID：" + warehouseEntryItemID);
                }
                transferStock.setAmount(warehouseEntryItem.getRealAmount().subtract(warehouseEntryItem.getInspectionAmount().subtract(idAndReturnAmount.get(warehouseEntryItemID))));
            } else {
                transferStock.setAmount(warehouseEntryItem.getRealAmount());
            }
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntry.getNo() + "(不良品移库)");
            transferStock.setState(TransferStock.WAITING_FOR_INSPECTION);
            transferStock.setNewState(TransferStock.UNQUALIFIED);
            transferStock.setItemTypeForBatchNo(ItemType.entryItem);
            transferStock.setItemType(ItemType.transferItem);
            this.stockRecordService.RealTransformStock(accountBook, transferStock);

            warehouseEntryItem.setState(WarehouseEntryItemService.UNQUALIFIED);
        }));
        this.update(accountBook, warehouseEntryItems);
        //this.warehouseEntryService.updateState(accountBook, warehouseEntryIDs);
    }

    @Override
    @SuppressWarnings("all")
    public void reject1(String accountBook, List<Integer> ids, Map<Integer, BigDecimal> idAndReturnAmount) {
        WarehouseEntryItem[] warehouseEntryItems = this.warehouseEntryItemDAO.findTable(accountBook, new Condition().addCondition("id", ReflectHelper.listToArray(ids, Integer.class), ConditionItem.Relation.IN));
        List<Integer> warehouseEntryIDs = new ArrayList<>();
        Stream.of(warehouseEntryItems).forEach((warehouseEntryItem -> {
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                throw new WMSServiceException("入库单已经入库，请不要重复入库！");
            }
            if (!warehouseEntryIDs.contains(warehouseEntryItem.getWarehouseEntryId())) {
                warehouseEntryIDs.add(warehouseEntryItem.getWarehouseEntryId());
            }
            WarehouseEntry warehouseEntry = this.warehouseEntryService.get(accountBook, warehouseEntryItem.getWarehouseEntryId());
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItem.getUnqualifiedStorageLocationId(), warehouseEntry.getNo() + "入库单各条目的不良品库位");
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItem.getUnqualifiedStorageLocationId());
            if (idAndReturnAmount != null) {
                Integer warehouseEntryItemID = warehouseEntryItem.getId();
                if (!idAndReturnAmount.containsKey(warehouseEntryItemID)) {
                    throw new WMSServiceException("Receive返回数量参数未包含ID：" + warehouseEntryItemID);
                }
                transferStock.setAmount(warehouseEntryItem.getRealAmount().subtract(warehouseEntryItem.getInspectionAmount().subtract(idAndReturnAmount.get(warehouseEntryItemID))));
            } else {
                transferStock.setAmount(warehouseEntryItem.getRealAmount());
            }
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntry.getNo() + "(不良品移库)");
            transferStock.setState(TransferStock.WAITING_FOR_INSPECTION);
            transferStock.setNewState(TransferStock.UNQUALIFIED);
            transferStock.setItemTypeForBatchNo(ItemType.entryItem);
            transferStock.setItemType(ItemType.transferItem);
            this.stockRecordService.transferStock(accountBook, transferStock,new TransferStock());

            warehouseEntryItem.setState(WarehouseEntryItemService.UNQUALIFIED);
        }));
        this.update(accountBook, warehouseEntryItems);
        //this.warehouseEntryService.updateState(accountBook, warehouseEntryIDs);
    }

    @Override
    public WarehouseEntryItem get(String accountBook, int id) throws WMSServiceException {
        return this.warehouseEntryItemDAO.get(accountBook, id);
    }
}
