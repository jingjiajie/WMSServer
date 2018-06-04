package com.wms.services;
import com.wms.services.warehouse.datastructures.StockRecordGroup;
import com.wms.services.warehouse.datastructures.StockTakingOrderItemAdd;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.services.warehouse.service.StockTakingOrderItemService;
import com.wms.services.warehouse.service.StockTakingOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.StockRecordView;
import com.wms.utilities.model.StockTakingOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
@ComponentScan("com.wms")
@ImportResource({"classpath:applicationContext.xml"})
public class WarehouseService {
    public static void main(String args[]) {
        ApplicationContext applicationContext = SpringApplication.run(WarehouseService.class, args);
        System.out.println("仓库服务启动...");
        //MaterialService materialService=applicationContext.getBean(MaterialService.class);
        //materialService.remove("WMS_Template",new int[]{20});
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, 2018);//设置年
        gc.set(Calendar.MONTH, 5);//这里0是1月..以此向后推
        gc.set(Calendar.DAY_OF_MONTH, 5);//设置天
        date = gc.getTime();
        long a = date.getTime();
        Timestamp time2 = new Timestamp(date.getTime());

        StockRecordService stockRecordService = applicationContext.getBean(StockRecordService.class);
        StockRecordView[] stockRecordViews=stockRecordService.find("WMS_Template",new Condition().addCondition("warehouseId",new Integer[]{-1}));
        List<StockRecordGroup> stockRecordGroupList=new ArrayList<>();
        for(int i=0;i<stockRecordViews.length;i++){
            StockRecordGroup stockRecordGroup=new StockRecordGroup();
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
            stockRecordGroup.setGroup(stringBuffer.toString());
            stockRecordGroup.setStockRecordView(stockRecordViews[i]);
            stockRecordGroupList.add(stockRecordGroup);
        }
        StockRecordGroup[] resultArray=null;
        resultArray = (StockRecordGroup[]) Array.newInstance(StockRecordGroup.class,stockRecordGroupList.size());
        stockRecordGroupList.toArray(resultArray);
        Map<String,List<StockRecordGroup>> stockRecordGroup = Stream.of(resultArray).collect(Collectors.groupingBy(StockRecordGroup::getGroup));
        Iterator<Map.Entry<String,List<StockRecordGroup>>> entries = stockRecordGroup.entrySet().iterator();
        List<StockRecordView> stockRecordViewList=new ArrayList<>();
        //将每组最新的加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<String, List<StockRecordGroup>> entry = entries.next();
            List<StockRecordGroup> stockRecordGroup1=entry.getValue();
            StockRecordGroup[] resultArray1=null;
            resultArray1 = (StockRecordGroup[]) Array.newInstance(StockRecordGroup.class,stockRecordGroup1.size());
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
        StockRecordView[] stockRecordViews1=stockRecordService.findNewest("WMS_Template",new Condition().addCondition("warehouseId",new Integer[]{-1}));
        /*
        TransferStock transferStock=new TransferStock();
        transferStock.setSupplyId(5);
        transferStock.setUnit("1");
        transferStock.setUnitAmount(new BigDecimal(1));
        transferStock.setSourceStorageLocationId(23154);
        transferStock.setNewStorageLocationId(23154);
        transferStock.setNewUnit("1");
        transferStock.setNewUnitAmount(new BigDecimal(1));
        transferStock.setAmount(new BigDecimal(10));
        transferStock.setRelatedOrderNo("xxxxxxxxxxxx");
        stockRecordService.RealTransferStockUnitFlexible("WMS_Template",transferStock);
        /*
        SupplierServices supplierServices = applicationContext.getBean(SupplierServices.class);
        Supplier supplier=new Supplier();
        supplier.setId(1);
        supplier.setNo("111123123131");
        supplier.setName("adsdasdadsadadssa");
        supplier.setWarehouseId(1);
        supplier.setCreatePersonId(19);
        supplier.setCreateTime(time2);
        //supplierServices.updateHistory("WMS_Template",new Supplier[]{supplier});

*/
        /*
        DeliveryOrderService deliveryOrderService = applicationContext.getBean(DeliveryOrderService.class);
        TransferAuto transferAuto=new TransferAuto();
        transferAuto.setPersonId(19);
        transferAuto.setWarehouseId(1);
        deliveryOrderService.transferAuto("WMS_Template",transferAuto);


        Object[] o=new Object[]{5,0};
        stockRecordService.findBySql("WMS_Template","SELECT loading.* from DeliveryOrderItemView as loading \n" +
                "WHERE loading.SupplyID =:a0 and (SELECT d.state from DeliveryOrderView as d WHERE d.id=loading.DeliveryOrderID)=:a1",o);
        StockRecordFind stockRecordFind = new StockRecordFind();
        stockRecordFind.setSupplyId(5);
        stockRecordFind.setStorageLocationId(21);
        stockRecordFind.setWarehouseId(1);
        stockRecordFind.setUnitAmount(new BigDecimal(1));
        Object[] stockRecordSource1 = stockRecordService.findCheckSupply("WMS_Template", stockRecordFind);
        */

/*
        StockTakingOrderService stockTakingOrderService = applicationContext.getBean(StockTakingOrderService.class);
        StockTakingOrderItemService stockTakingOrderServiceItem = applicationContext.getBean(StockTakingOrderItemService.class);
        StockTakingOrderItemAdd stockTakingOrderItemAdd=new StockTakingOrderItemAdd();
        stockTakingOrderItemAdd.setStockTakingOrderId(1);
        stockTakingOrderItemAdd.setPersonId(19);
        stockTakingOrderItemAdd.setWarehouseId(1);
        stockTakingOrderServiceItem.addStockTakingOrderItemSingle("WMS_Template",new int[]{5},stockTakingOrderItemAdd);
        StockTakingOrder stockTakingOrder=new StockTakingOrder();
        stockTakingOrder.setId(1);
        stockTakingOrder.setNo("5115111111");
        stockTakingOrder.setCreatePersonId(19);
        stockTakingOrder.setWarehouseId(-100);
        stockTakingOrder.setCreateTime(time2);
        stockTakingOrderService.update("WMS_Template",new StockTakingOrder[]{stockTakingOrder});
*/
    }
}