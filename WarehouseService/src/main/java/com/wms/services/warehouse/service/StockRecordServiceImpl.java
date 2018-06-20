package com.wms.services.warehouse.service;


import com.wms.services.warehouse.dao.StockRecordDAO;
import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.OrderItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.sql.Timestamp;
//@Transactional(propagation = Propagation.MANDATORY)
@Transactional()
@Service
public class StockRecordServiceImpl implements StockRecordService {
    @Autowired
    StockRecordDAO stockRecordDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    TransferRecordService transformRecordService;
    @Autowired
    StorageAreaService storageAreaService;
    @Autowired
    IDChecker idChecker;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int[] add(String accountBook, StockRecord[] stockRecords) throws WMSServiceException {
        for(int i=0;i<stockRecords.length;i++) {
            new Validator("数量").notnull().notEmpty().min(0).validate(stockRecords[i].getAmount());
            new Validator("单位").notnull().notEmpty().validate(stockRecords[i].getUnit());
            new Validator("单位数量").notnull().notEmpty().min(0).validate(stockRecords[i].getUnitAmount());
            new Validator("存货日期").notnull().validate(stockRecords[i].getInventoryDate());
        }
        //外键检测
        Stream.of(stockRecords).forEach(
                (stockRecord)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockRecord.getWarehouseId()));
                    }
                    else  if(this.storageLocationService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getStorageLocationId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)",stockRecord.getStorageLocationId()));
                    }
                    else if( this.supplyService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getSupplyId())).length == 0){
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)",stockRecord.getSupplyId()));
                    }
                }
        );

        for (int i=0;i<stockRecords.length;i++)
        {
            stockRecords[i].setTime(new Timestamp(System.currentTimeMillis()));
            stockRecords[i].setBatchNo(this.batchTransfer(stockRecords[i].getInventoryDate()));
        }
        return stockRecordDAO.add(accountBook,stockRecords);
    }

    @Override
    public void returnSupply(String accountBook, StockRecord[] stockRecords) throws WMSServiceException {
        int[] addId={};
        for(int i=0;i<stockRecords.length;i++) {
            new Validator("数量").notnull().notEmpty().min(0).validate(stockRecords[i].getAmount());
            new Validator("单位").notnull().notEmpty().validate(stockRecords[i].getUnit());
            new Validator("单位数量").notnull().notEmpty().min(0).validate(stockRecords[i].getUnitAmount());
            new Validator("存货日期").notnull().validate(stockRecords[i].getInventoryDate());
        }
        //外键检测
        Stream.of(stockRecords).forEach(
                (stockRecord)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockRecord.getWarehouseId()));
                    }
                    else  if(this.storageLocationService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getStorageLocationId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)",stockRecord.getStorageLocationId()));
                    }
                    else if( this.supplyService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getSupplyId())).length == 0){
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)",stockRecord.getSupplyId()));
                    }
                }
        );
        for (int i=0;i<stockRecords.length;i++)
        {
            stockRecords[i].setTime(new Timestamp(System.currentTimeMillis()));
            stockRecords[i].setBatchNo(this.batchTransfer(new Timestamp(System.currentTimeMillis())));
            stockRecords[i].setInventoryDate(new Timestamp(System.currentTimeMillis()));
        }
        for(int i=0;i<stockRecords.length;i++){
            int StorageLocationId=stockRecords[i].getStorageLocationId();
            int supplyId=stockRecords[i].getSupplyId();
            Integer[] warehouseId=warehouseIdFind(accountBook,StorageLocationId);//至少能返回一个
            idChecker.check(StorageLocationService.class,accountBook,StorageLocationId,"库位");
            idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
            idChecker.check(SupplyService.class,accountBook,supplyId,"供货");
            StockRecordFind stockRecordFind=new StockRecordFind();
            stockRecordFind.setSupplyId(stockRecords[i].getSupplyId());
            stockRecordFind.setStorageLocationId(stockRecords[i].getStorageLocationId());
            stockRecordFind.setWarehouseId(warehouseId[0]);
            stockRecordFind.setUnit(stockRecords[i].getUnit());
            stockRecordFind.setUnitAmount(stockRecords[i].getUnitAmount());
            stockRecordFind.setInventoryDate(stockRecords[i].getInventoryDate());
            stockRecordFind.setReturnMode("batch");
            StockRecord[]   stockRecords1=this.find(accountBook,stockRecordFind);
            if(stockRecords1.length==0) {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(stockRecords[i].getUnit());
                stockRecord.setUnitAmount(stockRecords[i].getUnitAmount());
                stockRecord.setRelatedOrderNo(stockRecords[i].getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(stockRecords[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecords[i].getInventoryDate());
                stockRecord.setStorageLocationId(StorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecords[i].getAmount());
                stockRecord.setAvailableAmount(stockRecords[i].getAvailableAmount());
                addId=stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setWarehouseId(warehouseId[0].intValue());
                transferRecord.setNewStockRecordId(addId[0]);
                transferRecord.setSupplyId(supplyId);
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
            //找到一条记录，则可以合并
            else if(stockRecords1.length==1){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(stockRecords[i].getUnit());
                stockRecord.setUnitAmount(stockRecords[i].getUnitAmount());
                stockRecord.setRelatedOrderNo(stockRecords[i].getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(stockRecords[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecords[i].getInventoryDate());
                stockRecord.setStorageLocationId(StorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecords1[0].getAmount().add(stockRecords[i].getAmount()));
                stockRecord.setAvailableAmount(stockRecords1[0].getAvailableAmount().add(stockRecords[i].getAvailableAmount()));
                addId= stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setWarehouseId(warehouseId[0].intValue());
                transferRecord.setNewStockRecordId(addId[0]);
                transferRecord.setSupplyId(supplyId);
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
            else
            {
                throw new WMSServiceException("查询库存记录出现问题，请检查输入条件!");
            }
        }
    }

@Override
public  void update(String accountBook,StockRecord[] stockRecords) throws WMSServiceException {
    for(int i=0;i<stockRecords.length;i++) {
        new Validator("数量").notnull().notEmpty().min(0).validate(stockRecords[i].getAmount());
        new Validator("单位").notnull().notEmpty().validate(stockRecords[i].getUnit());
        new Validator("单位数量").notnull().notEmpty().min(0).validate(stockRecords[i].getUnitAmount());
        new Validator("存货日期").notnull().validate(stockRecords[i].getInventoryDate());
    }
        //外键检测
        Stream.of(stockRecords).forEach(
                (stockRecord)->{
                    if(this.warehouseService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getWarehouseId())).length == 0){
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",stockRecord.getWarehouseId()));
                    }
                    else  if(this.storageLocationService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getStorageLocationId())).length == 0)
                    {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)",stockRecord.getStorageLocationId()));
                    }
                    else if( this.supplyService.find(accountBook,
                            new Condition().addCondition("id",stockRecord.getSupplyId())).length == 0){
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)",stockRecord.getSupplyId()));
                    }
                }
        );
    for (int i=0;i<stockRecords.length;i++)
    {
        stockRecords[i].setBatchNo(this.batchTransfer(stockRecords[i].getInventoryDate()));
    }
        stockRecordDAO.update(accountBook,stockRecords);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            stockRecordDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除库存记录信息失败，如果库存记录信息已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Override
    public StockRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockRecordDAO.find(accountBook, cond);
    }

    @Override
    public StockRecordViewNewest[] findNewest(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockRecordDAO.findNewest(accountBook,cond);
        /*StockRecordView[] stockRecordViews=this.stockRecordDAO.find(accountBook, cond);
        List<StockRecordViewAndSumGroupBySupplyId> stockRecordGroupList=new ArrayList<>();
        for(int i=0;i<stockRecordViews.length;i++){
            StockRecordViewAndSumGroupBySupplyId stockRecordGroup=new StockRecordViewAndSumGroupBySupplyId();
            StringBuffer stringBuffer=new StringBuffer();
            //if(stockRecordViews[i].getUnit()==null||stockRecordViews[i].getUnit().equals("")){stringBuffer.append("empty");}
            stringBuffer.append(stockRecordViews[i].getUnit());
            stringBuffer.append(";");
            //if(stockRecordViews[i].getUnitAmount()==null){stringBuffer.append("empty");}
            stringBuffer.append(stockRecordViews[i].getUnitAmount());
            stringBuffer.append(";");
            stringBuffer.append(stockRecordViews[i].getStorageLocationId());
            stringBuffer.append(";");
            stringBuffer.append(stockRecordViews[i].getSupplyId());
            stringBuffer.append(";");
            stringBuffer.append(stockRecordViews[i].getWarehouseId());
            stringBuffer.append(";");
            stringBuffer.append(stockRecordViews[i].getBatchNo());
            stockRecordGroup.setSupplyId(stringBuffer.toString());
            stockRecordGroup.setStockRecordView(stockRecordViews[i]);
            stockRecordGroupList.add(stockRecordGroup);
        }
        StockRecordViewAndSumGroupBySupplyId[] resultArray=null;
        resultArray = (StockRecordViewAndSumGroupBySupplyId[]) Array.newInstance(StockRecordViewAndSumGroupBySupplyId.class,stockRecordGroupList.size());
        stockRecordGroupList.toArray(resultArray);
        Map<String,List<StockRecordViewAndSumGroupBySupplyId>> stockRecordGroup = Stream.of(resultArray).collect(Collectors.groupingBy(StockRecordViewAndSumGroupBySupplyId::getSupplyId));
        Iterator<Map.Entry<String,List<StockRecordViewAndSumGroupBySupplyId>>> entries = stockRecordGroup.entrySet().iterator();
        List<StockRecordView> stockRecordViewList=new ArrayList<>();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<String, List<StockRecordViewAndSumGroupBySupplyId>> entry = entries.next();
            List<StockRecordViewAndSumGroupBySupplyId> stockRecordGroup1=entry.getValue();
            StockRecordViewAndSumGroupBySupplyId[] resultArray1=null;
            resultArray1 = (StockRecordViewAndSumGroupBySupplyId[]) Array.newInstance(StockRecordViewAndSumGroupBySupplyId.class,stockRecordGroup1.size());
            stockRecordGroup1.toArray(resultArray1);
            StockRecordView stockRecordViewNewest = resultArray1[0].getStockRecordView();
            for(int i=1;i<resultArray1.length-1;i++){
                if(stockRecordViewNewest.getTime().compareTo(resultArray1[i].getStockRecordView().getTime())<0){
                    stockRecordViewNewest = resultArray1[i].getStockRecordView();
                }
            }
            stockRecordViewList.add(stockRecordViewNewest);
        }
        StockRecordView[] result=null;
        result = (StockRecordView[]) Array.newInstance(StockRecordView.class,stockRecordViewList.size());
        stockRecordViewList.toArray(result);
        return result;*/
    }

    @Override
    public void RealTransformStock(String accountBook, TransferStock transferStock)
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        int newStorageLocationId=transferStock.getNewStorageLocationId();
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        String relatedOrderNo=transferStock.getRelatedOrderNo();
        idChecker.check(StorageLocationService.class,accountBook,newStorageLocationId,"库位");
        idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
        idChecker.check(StorageLocationService.class,accountBook,sourceStorageLocationId,"库位");
        idChecker.check(SupplyService.class,accountBook,supplyId,"供货");
        //先查出最新源库存记录和新库位
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setWarehouseId(warehouseId[0]);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        stockRecordFind.setReturnMode("new");
        StockRecord[]   stockRecordSource1=this.find(accountBook,stockRecordFind);
        //StockRecordView[]   stockRecordSource2=this.find(accountBook,new Condition().addCondition("id",new Integer[]{512}));
        StockRecord[]   stockRecordSource3=stockRecordDAO.findTable(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordSource1[0].getId()}));
        if(stockRecordSource1.length==0)
        {
            throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
        }

        //按批次（存货时间）进行排序
        for(int i=0;i<stockRecordSource1.length;i++)
        {
            for(int j=i+1;j<stockRecordSource1.length;j++)
            {
                if(stockRecordSource1[i].getInventoryDate().compareTo(stockRecordSource1[j].getInventoryDate())<=0)
                {
                    StockRecord temp=stockRecordSource1[i];
                    stockRecordSource1[i]=stockRecordSource1[j];
                    stockRecordSource1[j]=temp;
                }
            }
        }
        //排序之后最后一条为最久的
        BigDecimal amountAvailableAll=BigDecimal.ZERO;
        int iNeed=-1;
        for(int i=stockRecordSource1.length-1;i>=0;i--){
            amountAvailableAll=amountAvailableAll.add(stockRecordSource1[i].getAvailableAmount());
            //如果加到某个记录够移出数量 则跳出并记录下i
            if(amountAvailableAll.subtract(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                iNeed=i;
                break;}
        }
        if(iNeed==-1){
            StorageLocationView[] storageLocationViews1= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
            SupplyView[] supplyViews1=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
            throw new WMSServiceException("物料“"+supplyViews1[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews1[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getAmount()+"，现有库存："+amountAvailableAll); }

        //判断新库位和源库位是否相同
        //如果相同新建一条完全相同的 然后增加移位记录
        if(sourceStorageLocationId==newStorageLocationId){
            for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
                StockRecord stockRecordNewSave=new StockRecord();
                if(stockRecordSource1[i].getAvailableAmount().compareTo(new BigDecimal(0))==0){continue;}
                {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                    stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                    stockRecord.setAmount(stockRecordSource1[i].getAmount());
                    stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount());
                    int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecord});
                    if(newStockRecordId.length!=1)
                    {
                        throw new WMSServiceException("添加新库存记录失败！");
                    }
                    //添加一条移位记录
                    TransferRecord transferRecord=new TransferRecord();
                    transferRecord.setNewStockRecordId(newStockRecordId[0]);
                    transferRecord.setSourceStockRecordId(stockRecordSource1[i].getId());
                    transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                    transferRecord.setSupplyId(supplyId);
                    transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
                }
            }
        }
        //不相同则正常移动
        else{
        for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
            StockRecord stockRecordNewSave=new StockRecord();
            if(stockRecordSource1[i].getAvailableAmount().compareTo(new BigDecimal(0))==0){continue;}
            if(i>iNeed)
            {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount()));
                stockRecord.setAvailableAmount(BigDecimal.ZERO);
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{unit}).addCondition("unitAmount",new BigDecimal[]{unitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );

                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(stockRecordSource1[i].getAvailableAmount());
                    stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAvailableAmount());
                    stockRecordNewSave.setUnit(unit);
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }

            }
            else if(i==iNeed){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                //stockRecord.setAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                //stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                stockRecord.setAvailableAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{unit}).addCondition("unitAmount",new BigDecimal[]{unitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );
                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    //stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    //stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setUnit(unit);
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    //stockRecordNewSave.setAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    //stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setUnit(unit);
                    stockRecordNewSave.setUnitAmount(unitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }

                int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave});
                if(newStockRecordId.length!=1)
                {
                    throw new WMSServiceException("添加新库存记录失败！");
                }
                //添加一条移位记录
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setNewStockRecordId(newStockRecordId[0]);
                transferRecord.setSourceStockRecordId(stockRecordSource1[i].getId());
                transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                transferRecord.setSupplyId(supplyId);
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
        }
        }
    }

    @Override
    public void addAmount(String accountBook,TransferStock transferStock )
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        String batchNo="";
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
        idChecker.check(StorageLocationService.class,accountBook,sourceStorageLocationId,"库位");
        idChecker.check(SupplyService.class,accountBook,supplyId,"供货");
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            new Validator("存货日期").notnull().validate(transferStock.getInventoryDate());
            batchNo=this.batchTransfer(transferStock.getInventoryDate());}
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();

        //先查出源库存记录
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        stockRecordFind.setWarehouseId(warehouseId[0]);
        stockRecordFind.setReturnMode("new");
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            stockRecordFind.setInventoryDate(transferStock.getInventoryDate());
            stockRecordFind.setReturnMode("batch");
        }
        //根据以上条件如果为增加只应该有一条 如果为减少则应该为所有批次
        StockRecord[] stockRecordSource= this.find(accountBook,stockRecordFind);

        //移入的情况
        if(transferStock.getAmount().compareTo(new BigDecimal(0)) >=0){
            int addId[]={};
            // 没有相关记录，就新建一条,这种记录没有生产日期和失效日期
            if(stockRecordSource.length==0) {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(batchNo);
                stockRecord.setInventoryDate(transferStock.getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(amount);
                stockRecord.setAvailableAmount(amount);
                addId=stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setWarehouseId(warehouseId[0].intValue());
                transferRecord.setNewStockRecordId(addId[0]);
                transferRecord.setSupplyId(supplyId);
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
            //找到一条记录，则可以合并
            else if(stockRecordSource.length==1){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(warehouseId[0]);
                stockRecord.setBatchNo(batchNo);
                stockRecord.setInventoryDate(transferStock.getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource[0].getAmount().add(amount));
                stockRecord.setAvailableAmount(stockRecordSource[0].getAmount().add(amount));
                addId= stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setWarehouseId(warehouseId[0].intValue());
                transferRecord.setNewStockRecordId(addId[0]);
                transferRecord.setSupplyId(supplyId);
                //transferRecord.setSourceStockRecordId(stockRecordSource[0].getId());
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
            else
            {
                throw new WMSServiceException("查询库存记录出现问题，请检查输入条件!");
            }
        }
        //如果为移出
        else
        {
            int addId[]={};
           StorageLocationView[] storageLocationViews= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
           SupplyView[] supplyViews=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
           if (stockRecordSource.length==0)
            {
                throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getAmount().negate()+"，现有库存：0");
            }
            //首先找到最久的库存记录
            for(int i=0;i<stockRecordSource.length;i++)
            {
                for(int j=i+1;j<stockRecordSource.length;j++)
                {
                    if(stockRecordSource[i].getInventoryDate().compareTo(stockRecordSource[j].getInventoryDate())<=0)
                    {
                        StockRecord temp=stockRecordSource[i];
                        stockRecordSource[i]=stockRecordSource[j];
                        stockRecordSource[j]=temp;
                    }
                }
            }
            //排序之后最后一条为最久的
            BigDecimal amountAvailableAll= BigDecimal.ZERO;
            int iNeed=-1;
            for(int i=stockRecordSource.length-1;i>=0;i--){
                 amountAvailableAll=amountAvailableAll.add(stockRecordSource[i].getAvailableAmount());
                 //如果加到某个记录够移出数量 则跳出并记录下i
                 if(amountAvailableAll.add(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                     iNeed=i;
                     break;}
            }
            if(iNeed==-1){
                StorageLocationView[] storageLocationViews1= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
                SupplyView[] supplyViews1=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
                throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getAmount().negate()+"，现有库存："+amountAvailableAll);}
            for(int i=stockRecordSource.length-1;i>=iNeed;i--){
                if(stockRecordSource[i].getAvailableAmount().compareTo(new BigDecimal(0))==0){continue;}
                if(i>iNeed)
                {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                    stockRecord.setWarehouseId(warehouseId[0]);
                    stockRecord.setBatchNo(stockRecordSource[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                    stockRecord.setAmount(stockRecordSource[i].getAmount().subtract(stockRecordSource[i].getAvailableAmount()));
                    stockRecord.setAvailableAmount(new BigDecimal(0));
                    addId= stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});
                    TransferRecord transferRecord=new TransferRecord();
                    transferRecord.setWarehouseId(warehouseId[0].intValue());
                    //transferRecord.setNewStockRecordId(addId[0]);
                    transferRecord.setSourceStockRecordId(stockRecordSource[i].getId());
                    transferRecord.setSupplyId(supplyId);
                    transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
                }
                else{
                    StockRecord stockRecord1 = new StockRecord();
                    stockRecord1.setUnit(unit);
                    stockRecord1.setUnitAmount(unitAmount);
                    stockRecord1.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                    stockRecord1.setWarehouseId(warehouseId[0]);
                    stockRecord1.setBatchNo(stockRecordSource[i].getBatchNo());
                    stockRecord1.setInventoryDate(stockRecordSource[i].getInventoryDate());
                    stockRecord1.setStorageLocationId(sourceStorageLocationId);
                    stockRecord1.setSupplyId(supplyId);
                    stockRecord1.setTime(new Timestamp(System.currentTimeMillis()));
                    //stockRecord.setAmount(amountAvailableAll.add(transferStock.getAmount()));
                    //stockRecord.setAvailableAmount(stockRecordSource[i].getAvailableAmount().subtract(stockRecordSource[i].getAmount().subtract(amountAvailableAll.add(transferStock.getAmount()))));
                    stockRecord1.setAmount(stockRecordSource[i].getAmount().subtract(stockRecordSource[i].getAvailableAmount().subtract(amountAvailableAll.add(transferStock.getAmount()))));
                    stockRecord1.setAvailableAmount(amountAvailableAll.add(transferStock.getAmount()));
                    addId= stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord1});
                    TransferRecord transferRecord=new TransferRecord();
                    transferRecord.setWarehouseId(warehouseId[0].intValue());
                    //transferRecord.setNewStockRecordId(addId[0]);
                    transferRecord.setSourceStockRecordId(stockRecordSource[i].getId());
                    transferRecord.setSupplyId(supplyId);
                    transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
                }
            }
        }
    }
/*
    @Override
     public  StockRecordView[] find(String accountBook, StockRecordFind stockRecordFind)
    {
        Condition condition=new Condition();
         List<StockRecordView> stockRecordViews=new ArrayList<StockRecordView>();
        StockRecordView[] stockRecordViews2=null;
        if(stockRecordFind.getTimeEnd()!=null) {
            condition.addCondition("time", stockRecordFind.getTimeEnd(),ConditionItem.Relation.LESS_THAN);
            if(stockRecordFind.getTimeStart()!=null){
                condition.addCondition("time",stockRecordFind.getTimeStart(),ConditionItem.Relation.GREATER_THAN);}
        }
        condition.addOrder("time", OrderItem.Order.DESC);
        if(stockRecordFind.getSupplyId()!=null)
        { condition.addCondition("supplyId",new Integer[]{stockRecordFind.getSupplyId()});}

        if(stockRecordFind.getStorageLocationId()!=null)
        {condition.addCondition("storageLocationId",new Integer[]{stockRecordFind.getStorageLocationId()});}

        if(stockRecordFind.getWarehouseId()!=null)
        {condition.addCondition("warehouseId",new Integer[]{stockRecordFind.getWarehouseId()});}

        if(!stockRecordFind.getUnit().equals(""))
        {condition.addCondition("unit",new String[]{stockRecordFind.getUnit()});}

        if(stockRecordFind.getUnitAmount()!=null)
        {condition.addCondition("unitAmount",new BigDecimal[]{stockRecordFind.getUnitAmount()});}

        //同一天批号相同，所以用批号查询？
        if(stockRecordFind.getInventoryDate()!=null) {
            condition.addCondition("batchNo",new String[]{this.batchTransfer(stockRecordFind.getInventoryDate())}); }

        if(stockRecordDAO.find(accountBook, condition).length == 0)
        {throw new WMSServiceException("此条件下无库存记录！");}

        if(stockRecordFind.getReturnMode().equals("newest")) {
            while (stockRecordDAO.find(accountBook, condition).length > 0) {
                StockRecordView[] stockRecordTemp = stockRecordDAO.find(accountBook, condition);
                stockRecordViews.add(stockRecordTemp[0]);
                if (stockRecordFind.getStorageLocationId() == null) {
                    condition.addCondition("storageLocationId", new Integer[]{stockRecordTemp[0].getStorageLocationId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getSupplyId() == null) {
                    condition.addCondition("supplyId", new Integer[]{stockRecordTemp[0].getSupplyId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getWarehouseId() == null) {
                    condition.addCondition("warehouseId", new Integer[]{stockRecordTemp[0].getWarehouseId()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getUnit().equals("")) {
                    condition.addCondition("unit", new String[]{stockRecordTemp[0].getUnit()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if (stockRecordFind.getUnitAmount() == null) {
                    condition.addCondition("unitAmount", new BigDecimal[]{stockRecordTemp[0].getUnitAmount()}, ConditionItem.Relation.NOT_EQUAL);
                }
                if(stockRecordFind.getInventoryDate()==null) {
                    condition.addCondition("batchNo",new String[]{stockRecordTemp[0].getBatchNo()},ConditionItem.Relation.NOT_EQUAL );
                    }
            }
            StockRecordView[] stockRecordViews1 = new StockRecordView[stockRecordViews.size()];
            stockRecordViews2 = stockRecordViews.toArray(stockRecordViews1);
        }
        else
        {
            stockRecordViews2=stockRecordDAO.find(accountBook, condition);
        }
        return stockRecordViews2;
    }
*/

    private Integer[] warehouseIdFind(String accountBook,int storageLocationId) {
        StorageLocationView[] storageLocationViews = storageLocationService.find(accountBook, new Condition().addCondition("id", new Integer[]{storageLocationId}));
        if (storageLocationViews.length != 1) {
            throw new WMSServiceException("没有找到相关库位！");}
            StorageAreaView[] storageAreaViews = storageAreaService.find(accountBook, new Condition().addCondition("id", new Integer[]{storageLocationViews[0].getStorageAreaId()}));
            if (storageAreaViews.length != 1) {
                throw new WMSServiceException("没有找到相关库区!");
            }
        return    new Integer[] {storageAreaViews[0].getWarehouseId()};
    }

    public void RealTransferStockUnitFlexible(String accountBook,TransferStock transferStock)
    {
        new Validator("相关单号").notEmpty().notnull().validate(transferStock.relatedOrderNo);
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        new Validator("数量").notnull().validate(transferStock.getAmount());
        new Validator("新单位").notnull().notEmpty().validate(transferStock.getNewUnit());
        new Validator("新单位数量").notnull().min(0).validate(transferStock.getNewUnitAmount());

        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        int newStorageLocationId=transferStock.getNewStorageLocationId();
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        BigDecimal amount=transferStock.getAmount();
        String unit=transferStock.getUnit();
        String newUnit=transferStock.getNewUnit();
        BigDecimal newUnitAmount=transferStock.getNewUnitAmount();
        BigDecimal unitAmount=transferStock.getUnitAmount();

        String relatedOrderNo=transferStock.getRelatedOrderNo();
        idChecker.check(StorageLocationService.class,accountBook,newStorageLocationId,"库位");
        idChecker.check(WarehouseService.class,accountBook,warehouseId[0],"仓库");
        idChecker.check(StorageLocationService.class,accountBook,sourceStorageLocationId,"库位");
        idChecker.check(SupplyService.class,accountBook,supplyId,"供货");
        //先查出最新源库存记录和新库位
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setWarehouseId(warehouseId[0]);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        stockRecordFind.setReturnMode("new");
        StockRecord[]   stockRecordSource1=this.find(accountBook,stockRecordFind);
        if(stockRecordSource1.length==0)
        {
            StorageLocationView[] storageLocationViews= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
            SupplyView[] supplyViews=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
            //throw new WMSServiceException("没查到符合要求的源库存记录，请检查相关信息！");
            throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getAmount()+"，现有库存：0");
        }

        //进行排序
        for(int i=0;i<stockRecordSource1.length;i++)
        {
            for(int j=i+1;j<stockRecordSource1.length;j++)
            {
                if(stockRecordSource1[i].getInventoryDate().compareTo(stockRecordSource1[j].getInventoryDate())<=0)
                {
                    StockRecord temp=stockRecordSource1[i];
                    stockRecordSource1[i]=stockRecordSource1[j];
                    stockRecordSource1[j]=temp;
                }
            }
        }
        //排序之后最后一条为最久的
        BigDecimal amountAvailableAll=BigDecimal.ZERO;
        int iNeed=-1;
        for(int i=stockRecordSource1.length-1;i>=0;i--){
            amountAvailableAll=amountAvailableAll.add(stockRecordSource1[i].getAvailableAmount());
            //如果加到某个记录够移出数量 则跳出并记录下i
            if(amountAvailableAll.subtract(transferStock.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                iNeed=i;
                break;}
        }
        StorageLocationView[] storageLocationViews= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
        SupplyView[] supplyViews=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
        if(iNeed==-1){ throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getAmount()+"，现有库存："+amountAvailableAll); }

        //相同的情况
        if(newStorageLocationId==sourceStorageLocationId&&newUnit.equals(unit)&&newUnitAmount.equals(unitAmount)){
            if(newUnit.equals(unit)&&newUnitAmount.equals(unitAmount)){
                //如果完全相同
                for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
                    StockRecord stockRecordNewSave=new StockRecord();
                    if(stockRecordSource1[i].getAvailableAmount().compareTo(new BigDecimal(0))==0){continue;}
                    {
                        StockRecord stockRecord = new StockRecord();
                        stockRecord.setUnit(unit);
                        stockRecord.setUnitAmount(unitAmount);
                        stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                        stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                        stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                        stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                        stockRecord.setStorageLocationId(sourceStorageLocationId);
                        stockRecord.setSupplyId(supplyId);
                        stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                        stockRecord.setAmount(stockRecordSource1[i].getAmount());
                        stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount());
                        int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecord});
                        if(newStockRecordId.length!=1)
                        {
                            throw new WMSServiceException("添加新库存记录失败！");
                        }
                        //添加一条移位记录
                        TransferRecord transferRecord=new TransferRecord();
                        transferRecord.setNewStockRecordId(newStockRecordId[0]);
                        transferRecord.setSourceStockRecordId(stockRecordSource1[i].getId());
                        transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                        transferRecord.setSupplyId(supplyId);
                        transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
                    }
                }
            }
        }
        else{
        for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
            if(stockRecordSource1[i].getAvailableAmount().compareTo(new BigDecimal(0))==0){continue;}
            StockRecord stockRecordNewSave=new StockRecord();
            if(i>iNeed)
            {
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount()));
                stockRecord.setAvailableAmount(BigDecimal.ZERO);
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{newUnit}).addCondition("unitAmount",new BigDecimal[]{newUnitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );

                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount()));
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    stockRecordNewSave.setAmount(stockRecordSource1[i].getAvailableAmount());
                    stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAvailableAmount());
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
            }
            else if(i==iNeed){
                StockRecord stockRecord = new StockRecord();
                stockRecord.setUnit(unit);
                stockRecord.setUnitAmount(unitAmount);
                stockRecord.setRelatedOrderNo(transferStock.getRelatedOrderNo());
                stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                stockRecord.setStorageLocationId(sourceStorageLocationId);
                stockRecord.setSupplyId(supplyId);
                stockRecord.setTime(new Timestamp(System.currentTimeMillis()));
                //stockRecord.setAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                //stockRecord.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                stockRecord.setAmount(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                stockRecord.setAvailableAmount(amountAvailableAll.subtract(transferStock.getAmount()));
                stockRecordDAO.add(accountBook, new StockRecord[]{stockRecord});

                //查到新库位上最新的相同供货的记录
                StockRecordView[] stockRecordViews=stockRecordDAO.find(accountBook, new Condition().addCondition( "storageLocationId",new Integer[]{newStorageLocationId}).
                        addCondition("supplyId",new Integer[]{supplyId}).addCondition("unit",new String[]{newUnit}).addCondition("unitAmount",new BigDecimal[]{newUnitAmount}).
                        addCondition("batchNo",new String[]{stockRecordSource1[i].getBatchNo()}).addOrder("time",OrderItem.Order.DESC)
                );
                //已经找到最新的可以叠加的记录，则第二条为叠加
                if(stockRecordViews.length>0){
                    //stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    //stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setAmount(stockRecordViews[0].getAmount().add(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setAvailableAmount(stockRecordViews[0].getAvailableAmount().add(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount()))));
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }
                else
                {
                    //stockRecordNewSave.setAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    //stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));

                    stockRecordNewSave.setAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setAvailableAmount(stockRecordSource1[i].getAvailableAmount().subtract(amountAvailableAll.subtract(transferStock.getAmount())));
                    stockRecordNewSave.setUnit(newUnit);
                    stockRecordNewSave.setUnitAmount(newUnitAmount);
                    stockRecordNewSave.setRelatedOrderNo(relatedOrderNo);
                    stockRecordNewSave.setExpiryDate(stockRecordSource1[i].getExpiryDate());
                    stockRecordNewSave.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecordNewSave.setManufactureDate(stockRecordSource1[i].getManufactureDate());
                    stockRecordNewSave.setStorageLocationId(newStorageLocationId);
                    stockRecordNewSave.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecordNewSave.setSupplyId(supplyId);
                    stockRecordNewSave.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecordNewSave.setTime(new Timestamp(System.currentTimeMillis()));
                }

                int[] newStockRecordId =stockRecordDAO.add(accountBook,new StockRecord[]{stockRecordNewSave});
                if(newStockRecordId.length!=1)
                {
                    throw new WMSServiceException("添加新库存记录失败！");
                }
                //添加一条移位记录
                TransferRecord transferRecord=new TransferRecord();
                transferRecord.setNewStockRecordId(newStockRecordId[0]);
                transferRecord.setSourceStockRecordId(stockRecordSource1[i].getId());
                transferRecord.setWarehouseId(stockRecordSource1[0].getWarehouseId());
                transferRecord.setSupplyId(supplyId);
                transformRecordService.add(accountBook,new TransferRecord[]{transferRecord});
            }
        }}
    }

    public String batchTransfer(Timestamp timestamp)
    {
        Timestamp timestamp1 =timestamp;
        String batchNo=null;
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMMdd");
        try {
             batchNo = format.format(timestamp1);
        }
        catch (Exception e){throw new WMSServiceException("转换成批号失败，请检查输入时间！");}
        return batchNo;
    }

    public void modifyAvailableAmount(String accountBook,TransferStock transferStock){
        //只改变可用数量
        new Validator("可用数量变化值").notnull().validate(transferStock.getModifyAvailableAmount());
        BigDecimal modifyAvailableAmoun=transferStock.getModifyAvailableAmount();
        new Validator("单位数量").notnull().min(0).validate(transferStock.getUnitAmount());
        new Validator("单位").notnull().notEmpty().validate(transferStock.getUnit());
        //new Validator("存货时间").notnull().validate(transferStock.getInventoryDate());
        idChecker.check(StorageLocationService.class,accountBook,transferStock.getSourceStorageLocationId(),"库位");
        idChecker.check(SupplyService.class,accountBook,transferStock.getSupplyId(),"供货信息");
        int sourceStorageLocationId=transferStock.getSourceStorageLocationId();
        int supplyId=transferStock.getSupplyId();
        Integer[] warehouseId=warehouseIdFind(accountBook,sourceStorageLocationId);//至少能返回一个
        String unit=transferStock.getUnit();
        BigDecimal unitAmount=transferStock.getUnitAmount();
        //先查出最新源库存记录和新库位
        StockRecordFind stockRecordFind=new StockRecordFind();
        stockRecordFind.setSupplyId(supplyId);
        stockRecordFind.setStorageLocationId(sourceStorageLocationId);
        stockRecordFind.setWarehouseId(warehouseId[0]);
        stockRecordFind.setUnit(unit);
        stockRecordFind.setUnitAmount(unitAmount);
        stockRecordFind.setReturnMode("new");
        StockRecord[]   stockRecordSource1=this.find(accountBook,stockRecordFind);

        //进行排序
        for(int i=0;i<stockRecordSource1.length;i++)
        {
            for(int j=i+1;j<stockRecordSource1.length;j++)
            {
                if(stockRecordSource1[i].getInventoryDate().compareTo(stockRecordSource1[j].getInventoryDate())<=0)
                {
                    StockRecord temp=stockRecordSource1[i];
                    stockRecordSource1[i]=stockRecordSource1[j];
                    stockRecordSource1[j]=temp;
                }
            }
        }

        //先判断是增加还是减少
        if(modifyAvailableAmoun.compareTo(BigDecimal.ZERO)>=0){
            BigDecimal amountAddAvailable = BigDecimal.ZERO;
            int iNeed = -1;
            for (int i = stockRecordSource1.length - 1; i >= 0; i--) {
                amountAddAvailable = amountAddAvailable.add(stockRecordSource1[i].getAmount().subtract(stockRecordSource1[i].getAvailableAmount()));
                //如果加到某个记录够移出数量 则跳出并记录下i
                if (amountAddAvailable.subtract(modifyAvailableAmoun).compareTo(BigDecimal.ZERO) >= 0) {
                    iNeed = i;
                    break;
                }
            }
            StorageLocationView[] storageLocationViews= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
            SupplyView[] supplyViews=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
            if (iNeed == -1) {
                throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量过多，无法增加到超过实际数量。");
            }
            for(int i=stockRecordSource1.length-1;i>=iNeed;i--){
                StockRecord stockRecordNewSave=new StockRecord();
                if(i>iNeed){
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setId(stockRecordSource1[i].getId());
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(stockRecordSource1[i].getRelatedOrderNo());
                    stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setAmount(stockRecordSource1[i].getAmount());
                    stockRecord.setTime(stockRecordSource1[i].getTime());
                    stockRecord.setAvailableAmount(stockRecordSource1[i].getAmount());
                    stockRecordDAO.update(accountBook,new StockRecord[]{stockRecord});
                }
                else if(i==iNeed) {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setId(stockRecordSource1[i].getId());
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(stockRecordSource1[i].getRelatedOrderNo());
                    stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setAmount(stockRecordSource1[i].getAmount());
                    stockRecord.setTime(stockRecordSource1[i].getTime());
                    stockRecord.setAvailableAmount(stockRecordSource1[i].getAmount().subtract(amountAddAvailable.subtract(transferStock.getModifyAvailableAmount())));//最后一个差几个满
                    stockRecordDAO.update(accountBook, new StockRecord[]{stockRecord});
                }
            }
        }

        //减少的情况
        else {
            //排序之后最后一条为最久的
            BigDecimal amountAvailableAll = BigDecimal.ZERO;
            int iNeed = -1;
            for (int i = stockRecordSource1.length - 1; i >= 0; i--) {
                amountAvailableAll = amountAvailableAll.add(stockRecordSource1[i].getAvailableAmount());
                //如果加到某个记录够移出数量 则跳出并记录下i
                if (amountAvailableAll.add(modifyAvailableAmoun).compareTo(BigDecimal.ZERO) >= 0) {
                    iNeed = i;
                    break;
                }
            }

            StorageLocationView[] storageLocationViews= storageLocationService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getStorageLocationId()}));
            SupplyView[] supplyViews=supplyService.find(accountBook,new Condition().addCondition("id",new Integer[]{stockRecordFind.getSupplyId()}));
            if (iNeed == -1) {
                throw new WMSServiceException("物料“"+supplyViews[0].getMaterialName()+"”(单位：“"+stockRecordFind.getUnit()+"”单位数量：“"+stockRecordFind.getUnitAmount()+"”）在库位:“"+storageLocationViews[0].getName()+"”上可用数量不足。需要库存数量："+transferStock.getModifyAvailableAmount().negate()+"，现有库存："+amountAvailableAll);
            }
            for(int i=stockRecordSource1.length-1;i>=iNeed;i--) {
                StockRecord stockRecordNewSave = new StockRecord();
                if (i > iNeed) {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setId(stockRecordSource1[i].getId());
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(stockRecordSource1[i].getRelatedOrderNo());
                    stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setAmount(stockRecordSource1[i].getAmount());
                    stockRecord.setTime(stockRecordSource1[i].getTime());
                    stockRecord.setAvailableAmount(BigDecimal.ZERO);
                    stockRecordDAO.update(accountBook, new StockRecord[]{stockRecord});
                } else if (i == iNeed) {
                    StockRecord stockRecord = new StockRecord();
                    stockRecord.setId(stockRecordSource1[i].getId());
                    stockRecord.setUnit(unit);
                    stockRecord.setUnitAmount(unitAmount);
                    stockRecord.setRelatedOrderNo(stockRecordSource1[i].getRelatedOrderNo());
                    stockRecord.setWarehouseId(stockRecordSource1[i].getWarehouseId());
                    stockRecord.setBatchNo(stockRecordSource1[i].getBatchNo());
                    stockRecord.setInventoryDate(stockRecordSource1[i].getInventoryDate());
                    stockRecord.setStorageLocationId(sourceStorageLocationId);
                    stockRecord.setSupplyId(supplyId);
                    stockRecord.setAmount(stockRecordSource1[i].getAmount());
                    stockRecord.setTime(stockRecordSource1[i].getTime());
                    stockRecord.setAvailableAmount(amountAvailableAll.add(transferStock.getModifyAvailableAmount()));
                    stockRecordDAO.update(accountBook, new StockRecord[]{stockRecord});
                }
            }
        }
    }

    public StockRecord[] find(String accountBook,StockRecordFind stockRecordFind){
        Session session=this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        if(stockRecordFind.getReturnMode().equals("new")){
            //库存查询最新一条用
            String sqlNew="SELECT s1.* FROM StockRecordView AS s1 \n" +
                    "INNER JOIN \n" +
                    "\n" +
                    "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME   FROM StockRecordView As s2 \n" +
                    "\n" +
                    "where s2.WarehouseID=:warehouseId and s2.StorageLocationID=:storageLocationId and s2.SupplyID=:supplyId  and s2.Unit=:unit and s2.UnitAmount=:unitAmount\n" +
                    "\n" +
                    "GROUP BY s2.BatchNo) AS s3 \n" +
                    "\n" +
                    "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time \n" +
                    "  and s1.SupplyID=:supplyId and s1.WarehouseID=:warehouseId and s1.StorageLocationID=:storageLocationId   AND s1.BatchNo=s3.BatchNo \n";
            session.flush();
            query=session.createNativeQuery(sqlNew,StockRecord.class);
            query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
            query.setParameter("storageLocationId",stockRecordFind.getStorageLocationId());
            query.setParameter("supplyId",stockRecordFind.getSupplyId());
            query.setParameter("unit",stockRecordFind.getUnit());
            query.setParameter("unitAmount",stockRecordFind.getUnitAmount());
        }
        //增加数量带批号用
        if(stockRecordFind.getReturnMode().equals("batch")){
            //库存查询最新一条用
            String sqlNew="SELECT s1.* FROM StockRecordView AS s1 \n" +
                    "INNER JOIN \n" +
                    "\n" +
                    "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME   FROM StockRecordView As s2 \n" +
                    "\n" +
                    "where s2.WarehouseID=:warehouseId and s2.StorageLocationID=:storageLocationId and s2.SupplyID=:supplyId  and s2.Unit=:unit and s2.UnitAmount=:unitAmount AND  s2.BatchNo=:batchNo1" +
                    "\n" +
                    "GROUP BY s2.BatchNo) AS s3 \n" +
                    "\n" +
                    "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time \n" +
                    "  and s1.SupplyID=:supplyId and s1.WarehouseID=:warehouseId and s1.StorageLocationID=:storageLocationId   AND s1.BatchNo=s3.BatchNo \n";
            query=session.createNativeQuery(sqlNew,StockRecord.class);
            query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
            query.setParameter("storageLocationId",stockRecordFind.getStorageLocationId());
            query.setParameter("supplyId",stockRecordFind.getSupplyId());
            query.setParameter("unit",stockRecordFind.getUnit());
            query.setParameter("unitAmount",stockRecordFind.getUnitAmount());
            query.setParameter("batchNo1",this.batchTransfer(stockRecordFind.getInventoryDate()));
        }
        StockRecord[] resultArray=null;
        List<StockRecord> resultList = query.list();
        resultArray = (StockRecord[]) Array.newInstance(StockRecord.class,resultList.size());
        resultList.toArray(resultArray);
        return resultArray;
    }

    //盘点单一用
    public Object[] findCheckSupply(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s_all.*,sum(s_all.Amount) as sumAmount FROM \n" +
                    "(SELECT s1.* FROM StockRecordView AS s1\n" +
                    "INNER JOIN\n" +
                    "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.WarehouseID,s2.SupplyID,s2.StorageLocationID  FROM StockRecordView As s2 \n" +
                    "where s2.WarehouseID=:warehouseId and s2.SupplyID IN "+ids+" AND s2.TIME<:endTime \n" +
                    "GROUP BY s2.Unit,s2.UnitAmount,s2.BatchNo,s2.StorageLocationID) as s3\n" +
                    "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time AND s1.WarehouseID=s3.WarehouseID AND s1.SupplyID=s3.SupplyID AND s1.StorageLocationID=s3.StorageLocationID AND s1.BatchNo=s3.BatchNo) \n" +
                    "as s_all GROUP BY s_all.Unit,s_all.UnitAmount,s_all.StorageLocationID";
        String sqlCheckNewSuffix="\n ) as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sumAmount and item.unitamount=q.unitamount and item.unit=q.unit and item.supplyId=q.supplyid and item.storagelocationid=q.storagelocationid and item.comment=\"详细数目\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
        query.setParameter("endTime",stockRecordFind.getTimeEnd());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //盘点单在途数量仓库
    public Object[] findLoadingWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s2.*,sum(s2.RealAmount) as sum from (SELECT loading.* from DeliveryOrderItemView as loading\n" +
                "INNER JOIN \n" +
                "(SELECT  a.id ,a.state from DeliveryOrderView as a) as a1\n" +
                "on a1.id=loading.DeliveryOrderID and a1.state!=4 \n" +
                "WHERE loading.SupplyID in (SELECT su.id from SupplyView as su where su.warehouseId=:warehouseId) and loading.State!=0 and loading.loadingtime<:endTime)as s2\n" +
                "GROUP BY s2.supplyId ";
        String sqlCheckNewSuffix="\n ) as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sum and item.unitamount=q.unitamount and item.unitamount=q.unitamount and item.supplyId=q.supplyid and item.comment=\"在途数量\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("endTime",stockRecordFind.getTimeEnd());
        query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //盘点单在途数量供货
    public Object[] findLoadingSupply(String accountBook, StockRecordFind stockRecordFind,String ids,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s2.*,sum(s2.RealAmount) as sum from (SELECT loading.* from DeliveryOrderItemView as loading\n" +
                "INNER JOIN \n" +
                "(SELECT  a.id ,a.state from DeliveryOrderView as a) as a1\n" +
                "on a1.id=loading.DeliveryOrderID and a1.state!=4 \n" +
                "WHERE loading.SupplyID IN "+ids+" and loading.State!=0 and loading.loadingtime<:endTime) as s2 \n" +
                "GROUP BY s2.supplyId";
        String sqlCheckNewSuffix="\n )as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sum  and item.supplyId=q.supplyid and item.comment=\"在途数量\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("endTime",stockRecordFind.getTimeEnd());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //盘点供货总数用
    public Object[] findCheckSupplyAmountAll(String accountBook,StockRecordFind stockRecordFind,String ids,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s_all.*,sum(s_all.Amount) as sumAmount FROM \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.WarehouseID,s2.SupplyID,s2.StorageLocationID  FROM StockRecordView As s2  where s2.WarehouseID=:warehouseId and  s2.TIME <:end1 and s2.SupplyID IN " +ids+
                "GROUP BY s2.Unit,s2.UnitAmount,s2.BatchNo,s2.StorageLocationID) as s3\n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time AND s1.WarehouseID=s3.WarehouseID AND s1.SupplyID=s3.SupplyID AND s1.StorageLocationID=s3.StorageLocationID AND s1.BatchNo=s3.BatchNo)\n" +
                "as s_all \n" +
                "GROUP BY s_all.supplyId";
        String sqlCheckNewSuffix="\n ) as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sumamount  and item.supplyId=q.supplyid and item.comment=\"仓库总数\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
        query.setParameter("end1",stockRecordFind.getTimeEnd());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //盘点仓库总数
    public Object[] findCheckWarehouseAmountAll(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s_all.*,sum(s_all.Amount) as sumAmount FROM \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.WarehouseID,s2.SupplyID,s2.StorageLocationID  FROM StockRecordView As s2 \n" +
                "where s2.WarehouseID=:warehouseId AND s2.TIME<:endTime "+
                "GROUP BY s2.Unit,s2.UnitAmount,s2.BatchNo,s2.StorageLocationID,s2.SupplyID) as s3\n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time AND s1.WarehouseID=s3.WarehouseID AND s1.SupplyID=s3.SupplyID AND s1.StorageLocationID=s3.StorageLocationID AND s1.BatchNo=s3.BatchNo) \n" +
                "as s_all \n" +
                "GROUP BY s_all.supplyid";
        String sqlCheckNewSuffix="\n ) as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sumAmount   and item.supplyId=q.supplyid  and item.comment=\"仓库总数\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
        query.setParameter("endTime",stockRecordFind.getTimeEnd());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //盘点仓库
    public Object[] findCheckWarehouse(String accountBook, StockRecordFind stockRecordFind,int stockTakingOrderId){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        String sqlCheckNewPerfix="SELECT q.* from ( \n";
        String sqlCheckNew2="SELECT s_all.*,sum(s_all.Amount) as sumAmount FROM \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.WarehouseID,s2.SupplyID,s2.StorageLocationID  FROM StockRecordView As s2 \n" +
                "where s2.WarehouseID=:warehouseId AND s2.TIME<:endTime "+
                "GROUP BY s2.Unit,s2.UnitAmount,s2.BatchNo,s2.StorageLocationID,s2.SupplyID) as s3\n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time AND s1.WarehouseID=s3.WarehouseID AND s1.SupplyID=s3.SupplyID AND s1.StorageLocationID=s3.StorageLocationID AND s1.BatchNo=s3.BatchNo) \n" +
                "as s_all \n" +
                "GROUP BY s_all.Unit,s_all.UnitAmount,s_all.StorageLocationID,s_all.supplyid";
        String sqlCheckNewSuffix="\n ) as q WHERE (SELECT count(*)from StockTakingOrderItem as item where item.stockTakingOrderId=:stockTakingOrderId and item.amount=q.sumAmount and item.unitamount=q.unitamount and item.unit=q.unit and item.supplyId=q.supplyid and item.storagelocationid=q.storagelocationid and item.comment=\"详细数目\")=0";
        query=session.createNativeQuery(sqlCheckNewPerfix+sqlCheckNew2+sqlCheckNewSuffix);
        query.setParameter("warehouseId",stockRecordFind.getWarehouseId());
        query.setParameter("endTime",stockRecordFind.getTimeEnd());
        query.setParameter("stockTakingOrderId",stockTakingOrderId);
        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    //查询库存记录sql内部使用
    public Object[] findBySql(String accountBook, String sql, Object[] o){
        Session session= sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query=null;
        query=session.createNativeQuery(sql);

        for(int i=0;i<o.length;i++) {
            String name="a"+i;
            query.setParameter("a"+i, o[i]);
        }

        Object[] resultArray=null;
        List list = query.list();
        resultArray=list.toArray();
        return resultArray;
    }

    @Override

    //返回每个位置每种供货每种单位的总数量
    public StockRecordViewAndSumGroupBySupplyId[] findByTime(String accountBook,StockRecordFindByTime[] stockRecordFindByTimes){
        Session session=this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        StringBuffer sql=new StringBuffer();

        for(int i=0;i<stockRecordFindByTimes.length;i++){
            sql.append(" s2.supplyId= ");
            sql.append(stockRecordFindByTimes[i].getSupplyId());
            sql.append(" and ");
            sql.append(" s2.time<=\" ");
            sql.append(stockRecordFindByTimes[i].getEndTime());
            sql.append(" \" ");
            if(i!=stockRecordFindByTimes.length-1){
                sql.append(" or ");
            }
        }
        sql.append(" ");
        Query query=null;
            String sql1="SELECT s4.* ,sum(s4.amount) as Sum from \n" +
                    "(SELECT s1.* FROM StockRecordView AS s1 \n" +
                    "INNER JOIN\n" +
                    "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.storagelocationid,s2.supplyid  FROM StockRecordView As s2\n" +
                    "where " +sql+
                    "GROUP BY s2.SupplyID,s2.BatchNo,s2.unit,s2.UnitAmount,s2.StorageLocationID) AS s3 \n" +
                    "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time\n" +
                    "and s1.SupplyID=s3.supplyid and s1.StorageLocationID=s3.StorageLocationID   AND s1.BatchNo=s3.BatchNo) AS s4\n" +
                    "GROUp BY s4.supplyid,s4.unit,s4.storagelocationid";
        session.flush();
        query=session.createNativeQuery(sql1).addEntity(StockRecordViewAndSum.class);
        StockRecordViewAndSum[] resultArray=null;
        List<StockRecordViewAndSum> resultList = query.list();
        resultArray = (StockRecordViewAndSum[]) Array.newInstance(StockRecordViewAndSum.class,resultList.size());
        resultList.toArray(resultArray);
        Map<Integer, List<StockRecordViewAndSum>> groupBySupplyIdMap =
                Stream.of(resultArray).collect(Collectors.groupingBy(StockRecordViewAndSum::getSupplyId));

        Iterator<Map.Entry<Integer,List<StockRecordViewAndSum>>> entries = groupBySupplyIdMap.entrySet().iterator();
        List<StockRecordViewAndSumGroupBySupplyId> resultListGroup=new ArrayList<>();
        StockRecordViewAndSumGroupBySupplyId[] resultArrayGroup=null;
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<Integer, List<StockRecordViewAndSum>> entry = entries.next();
            List<StockRecordViewAndSum> stockRecordViewAndSumList=entry.getValue();
            Integer supplyId=entry.getKey();
            StockRecordViewAndSumGroupBySupplyId stockRecordViewAndSumGroupBySupplyId =new StockRecordViewAndSumGroupBySupplyId();
            StockRecordViewAndSum[] stockRecordViewAndSum=null;
            stockRecordViewAndSum = (StockRecordViewAndSum[]) Array.newInstance(StockRecordViewAndSum.class,stockRecordViewAndSumList.size());
            stockRecordViewAndSumList.toArray(stockRecordViewAndSum);
            stockRecordViewAndSumGroupBySupplyId.setStockRecords(stockRecordViewAndSum);
            stockRecordViewAndSumGroupBySupplyId.setSupplyId(supplyId);
            resultListGroup.add(stockRecordViewAndSumGroupBySupplyId);
    }
    resultArrayGroup=(StockRecordViewAndSumGroupBySupplyId[])Array.newInstance(StockRecordViewAndSumGroupBySupplyId.class,resultListGroup.size());
        resultListGroup.toArray(resultArrayGroup);
        return resultArrayGroup;
    }

    @Override
    public long findCount(String accountBook, Condition cond) throws WMSServiceException{
        return this.stockRecordDAO.findCountNew(accountBook,cond);
    }
}
