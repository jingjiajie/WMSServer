package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.warehouse.dao.SupplierDAO;
import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class SupplierServicesImpl implements SupplierServices {
    @Autowired
    SupplierDAO supplierDAO;
    @Autowired
    SupplyService supplyService;
    @Autowired
    PersonService personService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Autowired
    WarehouseEntryItemService warehouseEntryItemService;
    @Autowired
    ReturnRecordService returnRecordService;

    @Override
    public int[] add(String accountBook, Supplier[] suppliers) throws WMSServiceException {
        for (int i = 0; i < suppliers.length; i++) {
            Validator validator = new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());

            Validator validator1 = new Validator("供应商序号");
            validator1.notnull().validate(suppliers[i].getSerialNo());

            new Validator("创建时间").validate(suppliers[i].getCreateTime());
            if (suppliers[i].getEnabled() != 0 && suppliers[i].getEnabled() != 1) {
                throw new WMSServiceException("是否启用只能为0和1！");
            }
            if (suppliers[i].getBalanceDelayMonth() != null) {
                if (suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("结算延迟月不能小于0！");
                }
            }
            if (suppliers[i].getInvoiceDelayMonth() != null) {
                if (suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("开票算延迟月不能小于0！");
                }
            }
            //if(suppliers[i].getContractEndTime()!=null&&suppliers[i].getContractStartTime()!=null&&suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime())>=0)
            //{throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");}
        }

        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String no = suppliers[i].getNo();
                if (no.equalsIgnoreCase(suppliers[j].getNo())) {
                    throw new WMSServiceException("供应商代号" + no + "在添加的列表中重复!");
                }
            }
        }
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String name = suppliers[i].getName();
                if (name.equalsIgnoreCase(suppliers[j].getName())) {
                    throw new WMSServiceException("供应商名称" + name + "在添加的列表中重复!");
                }
            }
        }
        Stream.of(suppliers).forEach((supplier) -> {
            Condition cond = new Condition();
            cond.addCondition("name", new String[]{supplier.getName()}).addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("warehouseId", supplier.getWarehouseId());
            if (supplierDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("供应商名：" + supplier.getName() + "已经存在!");
            }
        });
        Stream.of(suppliers).forEach((supplier) -> {
            Condition cond = new Condition();
            cond.addCondition("no", new String[]{supplier.getNo()}).addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("warehouseId", supplier.getWarehouseId());
            if (supplierDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("供应商代号：" + supplier.getNo() + "已经存在!");
            }
        });
        Stream.of(suppliers).forEach((supplier) -> {
            Condition cond = new Condition();
            cond.addCondition("serialNo", new String[]{supplier.getSerialNo()}).addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("warehouseId", supplier.getWarehouseId());
            if (supplierDAO.find(accountBook, cond).length > 0) {
                throw new WMSServiceException("供应商序号：" + supplier.getSerialNo() + "已经存在!");
            }
        });

        //外键检测
        Stream.of(suppliers).forEach(
                (supplier) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", supplier.getWarehouseId()));
                    } else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getCreatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getCreatePersonId()));
                    }
                    if (supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getLastUpdatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getLastUpdatePersonId()));
                    }
                }
        );

        for (int i = 0; i < suppliers.length; i++) {
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
            suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        return supplierDAO.add(accountBook, suppliers);
    }


    @Override
    public void update(String accountBook, Supplier[] suppliers) throws WMSServiceException {
        if (suppliers.length == 0) {
            return;
        }
        for (int i = 0; i < suppliers.length; i++) {
            Validator validator = new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());

            Validator validator1 = new Validator("供应商序号");
            validator1.notnull().validate(suppliers[i].getSerialNo());

            new Validator("创建时间").validate(suppliers[i].getCreateTime());
            if (suppliers[i].getEnabled() != 0 && suppliers[i].getEnabled() != 1) {
                throw new WMSServiceException("是否启用只能为0和1！");
            }
            if (suppliers[i].getBalanceDelayMonth() != null) {
                if (suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("结算延迟月不能小于0！");
                }
            }
            if (suppliers[i].getInvoiceDelayMonth() != null) {
                if (suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("开票算延迟月不能小于0！");
                }
            }
            //if(suppliers[i].getContractEndTime()!=null&&suppliers[i].getContractStartTime()!=null&&suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime())>=0)
            //{throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");}
        }

        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String no = suppliers[i].getNo();
                if (no.equalsIgnoreCase(suppliers[j].getNo())) {
                    throw new WMSServiceException("供应商代号" + no + "在添加的列表中重复!");
                }
            }
        }
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String name = suppliers[i].getName();
                if (name.equalsIgnoreCase(suppliers[j].getName())) {
                    throw new WMSServiceException("供应商名称" + name + "在添加的列表中重复!");
                }
            }
        }
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String name = suppliers[i].getSerialNo();
                if (name.equalsIgnoreCase(suppliers[j].getSerialNo())) {
                    throw new WMSServiceException("供应商序号" + name + "在添加的列表中重复!");
                }
            }
        }

        Stream.of(suppliers).forEach(
                (supplier) -> {
                    if (this.supplierDAO.find(accountBook,
                            new Condition().addCondition("id", supplier.getId())).length == 0) {
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)", supplier.getId()));
                    }
                }
        );
        for (int i = 0; i < suppliers.length; i++) {
            suppliers[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            //TODO
            suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        //外键检测
        Stream.of(suppliers).forEach(
                (supplier) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", supplier.getWarehouseId()));
                    } else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getCreatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getCreatePersonId()));
                    }
                    if (supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getLastUpdatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getLastUpdatePersonId()));
                    }
                }
        );
        supplierDAO.update(accountBook, suppliers);
        Condition cond = new Condition();
        cond.addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("warehouseId", suppliers[0].getWarehouseId());
        Supplier[] suppliersCheck = supplierDAO.findTable(accountBook, cond);
        List<Supplier> supplierListCheck = Arrays.asList(suppliersCheck);
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getName)).reduce((last, cur) -> {
            if (last.getName().equalsIgnoreCase(cur.getName())) {
                throw new WMSServiceException("供应商名称重复:" + cur.getName());
            }
            return cur;
        });
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getNo)).reduce((last, cur) -> {
            if (last.getNo().equalsIgnoreCase(cur.getNo()) && (!last.getNo().equalsIgnoreCase("")) && (!cur.getNo().equalsIgnoreCase("")) && last.getNo() != null) {
                throw new WMSServiceException("供应商代号重复:" + cur.getNo());
            }
            return cur;
        });
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getSerialNo)).reduce((last, cur) -> {
            if (last.getSerialNo().equalsIgnoreCase(cur.getSerialNo()) && (!last.getSerialNo().equalsIgnoreCase("")) && (!cur.getSerialNo().equalsIgnoreCase("")) && last.getSerialNo() != null) {
                throw new WMSServiceException("供应商序号重复:" + cur.getSerialNo());
            }
            return cur;
        });
    }


    @Override
    public void updateHistory(String accountBook, Supplier[] suppliers) throws WMSServiceException {

        for (int i = 0; i < suppliers.length; i++) {
            Validator validator = new Validator("供应商名称");
            validator.notnull().validate(suppliers[i].getName());
            Validator validator1 = new Validator("供应商序号");
            validator1.notnull().validate(suppliers[i].getSerialNo());
            if (suppliers[i].getEnabled() != 0 && suppliers[i].getEnabled() != 1) {
                throw new WMSServiceException("是否启用只能为0和1！");
            }
            if (suppliers[i].getBalanceDelayMonth() != null) {
                if (suppliers[i].getBalanceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("结算延迟月不能小于0！");
                }
            }
            if (suppliers[i].getInvoiceDelayMonth() != null) {
                if (suppliers[i].getInvoiceDelayMonth().compareTo(BigDecimal.ZERO) < 0) {
                    throw new WMSServiceException("开票算延迟月不能小于0！");
                }
            }
            if (suppliers[i].getContractEndTime() != null && suppliers[i].getContractStartTime() != null && suppliers[i].getContractStartTime().compareTo(suppliers[i].getContractEndTime()) >= 0) {
                throw new WMSServiceException("合同截止时间必须在合同开始时间之后！");
            }
            //suppliers[i].setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        Stream.of(suppliers).forEach(
                (supplier) -> {
                    if (this.supplierDAO.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getId()})).length == 0) {
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)", supplier.getId()));
                    }
                }
        );

        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String no = suppliers[i].getNo();
                if (no.equalsIgnoreCase(suppliers[j].getNo())) {
                    throw new WMSServiceException("供应商代号" + no + "在添加的列表中重复!");
                }
            }
        }
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String name = suppliers[i].getName();
                if (name.equalsIgnoreCase(suppliers[j].getName())) {
                    throw new WMSServiceException("供应商名称" + name + "在添加的列表中重复!");
                }
            }
        }
        for (int i = 0; i < suppliers.length; i++) {
            for (int j = i + 1; j < suppliers.length; j++) {
                String name = suppliers[i].getSerialNo();
                if (name.equalsIgnoreCase(suppliers[j].getSerialNo())) {
                    throw new WMSServiceException("供应商序号" + name + "在添加的列表中重复!");
                }
            }
        }

        //外键检测
        Stream.of(suppliers).forEach(
                (supplier) -> {
                    if (this.warehouseService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getWarehouseId()})).length == 0) {
                        throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)", supplier.getWarehouseId()));
                    } else if (this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getCreatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getCreatePersonId()));
                    }
                    if (supplier.getLastUpdatePersonId() != null && this.personService.find(accountBook,
                            new Condition().addCondition("id", new Integer[]{supplier.getLastUpdatePersonId()})).length == 0) {
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)", supplier.getLastUpdatePersonId()));
                    }

                }
        );
        List<Supplier> supplierList = new ArrayList();
        //查出旧供应商
        for (int i = 0; i < suppliers.length; i++) {
            SupplierView[] supplierViews = supplierDAO.find(accountBook, new Condition().addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("id", new Integer[]{suppliers[i].getId()}));
            if (supplierViews.length != 1) {
                throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)", suppliers[i].getId()));
            }
            //新建一条存旧信息
            Supplier supplier = new Supplier();
//            supplier.setCreateTime(supplierViews[0].getCreateTime());
//            supplier.setLastUpdatePersonId(supplierViews[0].getLastUpdatePersonId());
//            supplier.setAddress(supplierViews[0].getAddress());
//            supplier.setBalanceDelayMonth(supplierViews[0].getBalanceDelayMonth());
//            supplier.setBankAccount(supplierViews[0].getBankAccount());
//            supplier.setBankName(supplierViews[0].getBankName());
//            supplier.setBankNo(supplierViews[0].getBankNo());
//            supplier.setContractNo(supplierViews[0].getContractNo());
//            supplier.setContractEndTime(supplierViews[0].getContractEndTime());
//            supplier.setContractStartTime(supplierViews[0].getContractStartTime());
//            supplier.setContractState(supplierViews[0].getContractState());
//            supplier.setFullName(supplierViews[0].getFullName());
//            supplier.setInvoiceDelayMonth(supplierViews[0].getInvoiceDelayMonth());
//            supplier.setEnabled(supplierViews[0].getEnabled());
//            supplier.setLastUpdateTime(supplierViews[0].getLastUpdateTime());
//            supplier.setCreatePersonId(supplierViews[0].getCreatePersonId());
//            supplier.setContractStorageArea(supplierViews[0].getContractStorageArea());
//            supplier.setEnterpriseCode(supplierViews[0].getEnterpriseCode());
//            supplier.setFixedStorageCost(supplierViews[0].getFixedStorageCost());
//            supplier.setName(supplierViews[0].getName());
//            supplier.setNetArea(supplierViews[0].getNetArea());
//            supplier.setNo(supplierViews[0].getNo());
//            supplier.setRecipientName(supplierViews[0].getRecipientName());
//            supplier.setTaxpayerNumber(supplierViews[0].getTaxpayerNumber());
//            supplier.setTel(supplierViews[0].getTel());
//            supplier.setZipCode(supplierViews[0].getZipCode());
//            supplier.setWarehouseId(supplierViews[0].getWarehouseId());
//            supplier.setSupplierPassward(supplierViews[0].getSupplierPassward());
//            supplier.setIsHistory(1);
//            supplier.setSerialNo(supplierViews[0].getSerialNo());
//            supplier.setNewestSupplierId(suppliers[i].getId());
            ReflectHelper.copyFields(suppliers[0], supplier);
            supplier.setNewestSupplierId(supplierViews[0].getId());
            supplier.setIsHistory(1);
            supplierList.add(supplier);
        }
        //更新和保存
        Supplier[] resultArray = null;
        resultArray = (Supplier[]) Array.newInstance(Supplier.class, supplierList.size());
        supplierList.toArray(resultArray);
        supplierDAO.update(accountBook, suppliers);
        supplierDAO.add(accountBook, resultArray);
        Condition cond = new Condition();
        cond.addCondition("isHistory", new Integer[]{new Integer(0)}).addCondition("warehouseId", suppliers[0].getWarehouseId());
        Supplier[] suppliersCheck = supplierDAO.findTable(accountBook, cond);
        List<Supplier> supplierListCheck = Arrays.asList(suppliersCheck);
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getName)).reduce((last, cur) -> {
            if (last.getName().equalsIgnoreCase(cur.getName())) {
                throw new WMSServiceException("供应商名称重复:" + cur.getName());
            }
            return cur;
        });
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getNo)).reduce((last, cur) -> {
            if (last.getNo().equalsIgnoreCase(cur.getNo()) && (!last.getNo().equalsIgnoreCase("")) && (!cur.getNo().equalsIgnoreCase("")) && last.getNo() != null) {
                throw new WMSServiceException("供应商代号重复:" + cur.getNo());
            }
            return cur;
        });
        supplierListCheck.stream().sorted(Comparator.comparing(Supplier::getSerialNo)).reduce((last, cur) -> {
            if (last.getSerialNo().equalsIgnoreCase(cur.getSerialNo()) && (!last.getSerialNo().equalsIgnoreCase("")) && (!cur.getSerialNo().equalsIgnoreCase("")) && last.getSerialNo() != null) {
                throw new WMSServiceException("供应商序号重复:" + cur.getSerialNo());
            }
            return cur;
        });
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        for (int id : ids) {
            if (supplierDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                throw new WMSServiceException(String.format("删除供应商不存在，请重新查询！(%d)", id));
            }
        }

        try {
            supplierDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除供应商失败，如果供应商已经被引用，需要先删除引用的内容，才能删除该供应商");
        }
    }

    @Override
    public SupplierView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public SupplierView[] findHistory(String accountBook, Condition cond) throws WMSServiceException {
        cond.addCondition("isHistory", new Integer[]{1});
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public SupplierView[] findNew(String accountBook, Condition cond) throws WMSServiceException {
        cond.addCondition("isHistory", new Integer[]{0});
        return this.supplierDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String database, Condition cond) throws WMSServiceException {
        return this.supplierDAO.findCount(database, cond);
    }

    @Override
    public long findCountNew(String database, Condition cond) throws WMSServiceException {
        cond.addCondition("isHistory", new Integer[]{0});
        return this.supplierDAO.findCount(database, cond);
    }

    @Override
    public long findCountHistory(String database, Condition cond) throws WMSServiceException {
        cond.addCondition("isHistory", new Integer[]{1});
        return this.supplierDAO.findCount(database, cond);
    }

    public SupplierAmount[] supplierRemind(String accountBook, int supplierId) {
        //首先查找该供应商的合格品数量
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query = null;
        String sqlCheckNew2 = "SELECT s_all.supplierName,s_all.materialName,sum(s_all.Amount) as sumAmount,s_all.supplyId FROM \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.WarehouseID,s2.SupplyID,s2.StorageLocationID,s2.State  FROM StockRecordView As s2 \n" +
                "where s2.SupplierID=:supplierId  and s2.State =2\n" +
                "GROUP BY s2.Unit,s2.UnitAmount,s2.BatchNo,s2.StorageLocationID,s2.SupplyID) as s3\n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time AND s1.WarehouseID=s3.WarehouseID AND s1.SupplyID=s3.SupplyID \n" +
                "AND s1.StorageLocationID=s3.StorageLocationID AND s1.BatchNo=s3.BatchNo and s1.State=s3.State)\n" +
                "as s_all \n" +
                "GROUP BY s_all.supplyid";
        query = session.createNativeQuery(sqlCheckNew2);
        query.setParameter("supplierId", supplierId);
        Object[] resultArray = null;
        List list = query.list();
        resultArray = list.toArray();
        List<SupplierAmount> supplierAmountArrayList = new ArrayList<>();
        //供应商名称 物料名称 数量 supplyId
        for (int i = 0; i < resultArray.length; i++) {
            SupplierAmount supplierAmount = new SupplierAmount();
            Object[] objects = (Object[]) resultArray[i];
            supplierAmount.setSupplierName((String) objects[0]);
            supplierAmount.setMaterialName((String) objects[1]);
            supplierAmount.setAmount((BigDecimal) objects[2]);
            supplierAmount.setSupplyId((int) objects[3]);
            SupplyView[] supplyViews = supplyService.find(accountBook, new Condition().addCondition("id", supplierAmount.getSupplyId()));
            if (supplyViews.length != 1) {
                throw new WMSServiceException("查找供货出错！");
            }
            if (supplyViews[0].getSupplySaftyStock() != null) {
                if (supplyViews[0].getSupplySaftyStock().compareTo(new BigDecimal(0)) > 0) {
                    supplierAmount.setAmountNeed(supplyViews[0].getSupplySaftyStock());
                    supplierAmountArrayList.add(supplierAmount);
                }
            }
        }
        SupplierAmount[] supplierAmounts = (SupplierAmount[]) Array.newInstance(SupplierAmount.class, supplierAmountArrayList.size());
        ;
        supplierAmountArrayList.toArray(supplierAmounts);
        return supplierAmounts;
    }

    //返回每个时间的总数 所有的入库、出库信息
    //早库存 晚库存 入库详细 理论出库总数
    public List<DailyReports> generateDailyReports(String accountBook, DailyReportRequest dailyReportRequest) {
        List<DailyReports> dailyReportsList = new ArrayList<>();
//        //找出这段时间之前 每种供货的数量 加到列表里 作为初期数量 时间应该是这段时间的起始时间
//        StockRecordFind stockRecordFindPrime = new StockRecordFind();
//        stockRecordFindPrime.setSupplierId(dailyReportRequest.getSupplierId());
//        stockRecordFindPrime.setTimeEnd(dailyReportRequest.getStartTime());
//        SupplyView[] supplyViews = supplyService.find(accountBook, new Condition().addCondition("supplierId", dailyReportRequest.getSupplierId()));
//        Object[] objectPrime = this.findSupplierStockByTime(accountBook, stockRecordFindPrime, "");
//        for (int j = 0; j < objectPrime.length; j++) {
//            //物料代号 物料名 状态 总数量
//            Object[] o = (Object[]) objectPrime[j];
//            DailyReports dailyReports = new DailyReports();
//            dailyReports.setMaterialNo((String) o[0]);
//            dailyReports.setMaterialName((String) o[1]);
//            dailyReports.setMaterialProductLine((String) o[5]);
//            dailyReports.setState((int) o[2]);
//            dailyReports.setRealStock((BigDecimal) o[3]);
//            dailyReports.setSupplyId((int) o[4]);
//            dailyReports.setTimestamp(dailyReportRequest.getStartTime());
//            dailyReports.setType(DailyReports.AMOUNT_PRIME);
//            dailyReportsList.add(dailyReports);
//        }
//        //把不存在零件显示为0
//        for (int i = 0; i < supplyViews.length; i++) {
//            for (int s = 0; s < 3; s++) {
//                boolean exist = false;
//                for (int j = 0; j < objectPrime.length; j++) {
//                    //物料代号 物料名 状态 总数量
//                    Object[] o = (Object[]) objectPrime[j];
//                    if ((int) o[4] == supplyViews[i].getId() && (int) o[2] == s) {
//                        exist = true;
//                    }
//                }
//                //如果不存在
//                if (!exist) {
//                    DailyReports dailyReports = new DailyReports();
//                    dailyReports.setMaterialName(supplyViews[i].getMaterialName());
//                    dailyReports.setMaterialNo(supplyViews[i].getMaterialNo());
//                    dailyReports.setMaterialProductLine(supplyViews[i].getMaterialProductLine());
//                    dailyReports.setState(s);
//                    dailyReports.setRealStock(BigDecimal.ZERO);
//                    dailyReports.setSupplyId(supplyViews[i].getId());
//                    dailyReports.setTimestamp(dailyReportRequest.getStartTime());
//                    dailyReports.setType(DailyReports.AMOUNT_PRIME);
//                    dailyReportsList.add(dailyReports);
//                }
//            }
//        }
//
//        StockRecordFind stockRecordFindEnd = new StockRecordFind();
//        stockRecordFindEnd.setSupplierId(dailyReportRequest.getSupplierId());
//        stockRecordFindEnd.setTimeEnd(dailyReportRequest.getEndTime());
//        Object[] objectEnd = this.findSupplierStockByTime(accountBook, stockRecordFindEnd, "");
//        for (int j = 0; j < objectEnd.length; j++) {
//            //物料代号 物料名 状态 总数量
//            Object[] o = (Object[]) objectEnd[j];
//            DailyReports dailyReports = new DailyReports();
//            dailyReports.setMaterialNo((String) o[0]);
//            dailyReports.setMaterialName((String) o[1]);
//            dailyReports.setMaterialProductLine((String) o[5]);
//            dailyReports.setState((int) o[2]);
//            dailyReports.setRealStock((BigDecimal) o[3]);
//            dailyReports.setSupplyId((int) o[4]);
//            dailyReports.setTimestamp(dailyReportRequest.getEndTime());
//            dailyReports.setType(DailyReports.AMOUNT_END);
//            dailyReportsList.add(dailyReports);
//        }
//        //把不存在零件显示为0
//        for (int i = 0; i < supplyViews.length; i++) {
//            for (int s = 0; s < 3; s++) {
//                boolean exist = false;
//                for (int j = 0; j < objectEnd.length; j++) {
//                    //物料代号 物料名 状态 总数量
//                    Object[] o = (Object[]) objectEnd[j];
//                    if ((int) o[4] == supplyViews[i].getId() && (int) o[2] == s) {
//                        exist = true;
//                    }
//                }
//                //如果不存在
//                if (!exist) {
//                    DailyReports dailyReports = new DailyReports();
//                    dailyReports.setMaterialNo(supplyViews[i].getMaterialNo());
//                    dailyReports.setMaterialName(supplyViews[i].getMaterialName());
//                    dailyReports.setMaterialProductLine(supplyViews[i].getMaterialProductLine());
//                    dailyReports.setState(s);
//                    dailyReports.setRealStock(BigDecimal.ZERO);
//                    dailyReports.setSupplyId(supplyViews[i].getId());
//                    dailyReports.setTimestamp(dailyReportRequest.getStartTime());
//                    dailyReports.setType(DailyReports.AMOUNT_END);
//                    dailyReportsList.add(dailyReports);
//                }
//            }
//        }
        //找出供应商一段时间内的出库单条目和入库单条目 出库单条目中实际数量不为0的
        DeliveryOrderItemView[] deliveryOrderItemViews = deliveryOrderItemService.find(accountBook, new Condition().addCondition("deliveryOrderItemCreatTime", new Timestamp[]{dailyReportRequest.getStartTime(), dailyReportRequest.getEndTime()}, ConditionItem.Relation.BETWEEN).addCondition("supplierId", dailyReportRequest.getSupplierId()));
        //找创建时间是这段时间之内的条目
        WarehouseEntryItemView[] warehouseEntryItemViews = warehouseEntryItemService.find(accountBook, new Condition().addCondition("entryItemCreatTime", new Timestamp[]{dailyReportRequest.getStartTime(), dailyReportRequest.getEndTime()}, ConditionItem.Relation.BETWEEN).addCondition("supplierId", dailyReportRequest.getSupplierId()));
        //出库总数
        DailyReports dailyReportsDeliver = new DailyReports();
        dailyReportsDeliver.setAmountDiff(BigDecimal.ZERO);
        for (DeliveryOrderItemView deliveryOrderItemView : deliveryOrderItemViews) {
            dailyReportsDeliver.setSupplyId(deliveryOrderItemView.getSupplyId());
            dailyReportsDeliver.setMaterialName(deliveryOrderItemView.getMaterialName());
            dailyReportsDeliver.setMaterialNo(deliveryOrderItemView.getMaterialNo());
            dailyReportsDeliver.setMaterialProductLine(deliveryOrderItemView.getMaterialProductLine());
            if (deliveryOrderItemView.getState() == DeliveryOrderService.DELIVERY_TYPE_Qualified) {
                dailyReportsDeliver.setState(TransferStock.QUALIFIED);
            } else {
                dailyReportsDeliver.setState(TransferStock.UNQUALIFIED);
            }
            dailyReportsDeliver.setSupplierName(deliveryOrderItemView.getSupplierName());
            dailyReportsDeliver.setAmountDiff(dailyReportsDeliver.getAmountDiff().add(deliveryOrderItemView.getRealAmount()));
            dailyReportsDeliver.setType(DailyReports.AMOUNT_DIFF_DELIVERY_STATE);
            dailyReportsDeliver.setTimestamp(deliveryOrderItemView.getDeliveryOrderItemCreatTime());
            dailyReportsList.add(dailyReportsDeliver);
        }
        for (WarehouseEntryItemView warehouseEntryItem : warehouseEntryItemViews) {
            DailyReports dailyReports = new DailyReports();
            dailyReports.setSupplyId(warehouseEntryItem.getSupplyId());
            dailyReports.setMaterialName(warehouseEntryItem.getMaterialName());
            dailyReports.setMaterialNo((warehouseEntryItem.getMaterialNo()));
            dailyReports.setMaterialProductLine(warehouseEntryItem.getMaterialProductLine());
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.BEING_INSPECTED || warehouseEntryItem.getState() == WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                dailyReports.setState(TransferStock.WAITING_FOR_INSPECTION);
            }
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                dailyReports.setState(TransferStock.QUALIFIED);
            }
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED) {
                dailyReports.setState(TransferStock.UNQUALIFIED);
            }
            dailyReports.setSupplierName(warehouseEntryItem.getSupplierName());
            dailyReports.setAmountDiff(warehouseEntryItem.getRealAmount());
            dailyReports.setType(DailyReports.AMOUNT_DIFF_ENTRY_STATE);
            dailyReports.setTimestamp(warehouseEntryItem.getEntryItemCreatTime());
            dailyReportsList.add(dailyReports);
        }
        Collections.sort(dailyReportsList, new DailyReportsComparator());
        return dailyReportsList;
    }
/*
    public List<DailyReports> generateDailyReportsByYear(String accountBook, int supplyId) {
        List<DailyReports> dailyReportsList = new ArrayList<>();
        Calendar dayC1 = new GregorianCalendar();
        Calendar dayC2 = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yy-MM-dd");
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        Date dayStart = new Date();
        Date dayEnd = new Date();
        try {
            dayStart = df.parse(year + "-1-1"); //按照yyyy-MM-dd格式转换为日期
            dayEnd = df.parse(year + "-12-31");
        } catch (Exception e) {
            throw new WMSServiceException("请检查时间格式是否正确1");
        }
        dayC1.setTime(dayStart); //设置calendar的日期
        dayC2.setTime(dayEnd);
        for (; dayC1.compareTo(dayC2) <= 0; ) {
            //dayC1在dayC2之前就循环
            int month = dayC1.get(Calendar.MONTH) + 1;
            int day = dayC1.get(Calendar.DATE);
            Date date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(year + '-' + month + '-' + day);
            } catch (Exception e) {
                throw new WMSServiceException("请检查时间格式是否正确1");
            }
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            Timestamp timestampStart = new Timestamp(date.getTime());
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
            Timestamp timestampEnd = new Timestamp(date.getTime());
            dayC1.add(Calendar.DAY_OF_YEAR, 1);  //加1天
            DailyReportRequest dailyReportRequest = new DailyReportRequest();
            dailyReportRequest.setStartTime(timestampStart);
            dailyReportRequest.setEndTime(timestampEnd);
            dailyReportsList = this.generateDailyReportsByYear(accountBook, supplyId, dailyReportRequest, dailyReportsList);
        }
        return dailyReportsList;
    }
*/
    //返回每个时间的总数 所有的入库、出库信息
    //早库存 晚库存 入库详细 理论出库总数
    public List<DailyReports> generateDailyReportsByYear(String accountBook, int supplyId, DailyReportRequest dailyReportRequest, List<DailyReports> dailyReportsList) {
        SupplyView[] supplyViews = supplyService.find(accountBook, new Condition().addCondition("id", supplyId));
        if (supplyViews.length != 1) {
            return dailyReportsList;
        }
        //找出供应商一段时间内的出库单条目和入库单条目 出库单条目中实际数量不为0的
        DeliveryOrderItemView[] deliveryOrderItemViews = deliveryOrderItemService.find(accountBook, new Condition().addCondition("deliveryOrderItemCreatTime", new Timestamp[]{dailyReportRequest.getStartTime(), dailyReportRequest.getEndTime()}, ConditionItem.Relation.BETWEEN).addCondition("supplierId", dailyReportRequest.getSupplierId()));
        //找创建时间是这段时间之内的条目
        WarehouseEntryItemView[] warehouseEntryItemViews = warehouseEntryItemService.find(accountBook, new Condition().addCondition("entryItemCreatTime", new Timestamp[]{dailyReportRequest.getStartTime(), dailyReportRequest.getEndTime()}, ConditionItem.Relation.BETWEEN).addCondition("supplierId", dailyReportRequest.getSupplierId()));
        //出货和退货的所有情况
        DailyReports dailyReportsTotal = new DailyReports();
        dailyReportsTotal.setTimestamp(dailyReportRequest.getStartTime());
        dailyReportsTotal.setAmountDiff(BigDecimal.ZERO);
        dailyReportsTotal.setReturnAmountQualified(BigDecimal.ZERO);
        dailyReportsTotal.setReturnAmountUnqualified(BigDecimal.ZERO);
        dailyReportsTotal.setReturnToSupplierQualified(BigDecimal.ZERO);
        dailyReportsTotal.setReturnToSupplierUnqualified(BigDecimal.ZERO);
        dailyReportsTotal.setSupplyId(supplyViews[0].getId());
        dailyReportsTotal.setMaterialName(supplyViews[0].getMaterialName());
        dailyReportsTotal.setMaterialNo(supplyViews[0].getMaterialNo());
        dailyReportsTotal.setMaterialProductLine(supplyViews[0].getMaterialProductLine());
        dailyReportsTotal.setSupplierName(supplyViews[0].getSupplierName());
        for (DeliveryOrderItemView deliveryOrderItemView : deliveryOrderItemViews) {
            if (deliveryOrderItemView.getState() == DeliveryOrderService.DELIVERY_TYPE_Qualified) {
                if (deliveryOrderItemView.getDestinationName().equals("供应商")) {
                    dailyReportsTotal.setAmountDiff(dailyReportsTotal.getAmountDiff().add(deliveryOrderItemView.getRealAmount()));
                } else {
                    dailyReportsTotal.setReturnToSupplierQualified(dailyReportsTotal.getReturnToSupplierQualified().add(deliveryOrderItemView.getRealAmount()));
                }
            } else {
                if (deliveryOrderItemView.getDestinationName().equals("供应商")) {
                    dailyReportsTotal.setReturnToSupplierUnqualified(dailyReportsTotal.getReturnToSupplierUnqualified().add(deliveryOrderItemView.getRealAmount()));
                } else {
                    //TODO 不良品发运
                }
            }
        }
        //查找动力退中都
        ReturnAmount returnAmount = this.returnRecordService.findAmount(accountBook, supplyId, dailyReportRequest.getStartTime(), dailyReportRequest.getEndTime());
        if (returnAmount.getSupplyId() != -1) {
            dailyReportsTotal.setReturnAmountUnqualified(returnAmount.getAmountQualified());
            dailyReportsTotal.setReturnAmountUnqualified(returnAmount.getAmountUnqualified());
        }
        dailyReportsTotal.setType(DailyReports.TOTAl);
        dailyReportsList.add(dailyReportsTotal);
        for (WarehouseEntryItemView warehouseEntryItem : warehouseEntryItemViews) {
            DailyReports dailyReports = new DailyReports();
            dailyReports.setSupplyId(warehouseEntryItem.getSupplyId());
            dailyReports.setMaterialName(warehouseEntryItem.getMaterialName());
            dailyReports.setMaterialNo((warehouseEntryItem.getMaterialNo()));
            dailyReports.setMaterialProductLine(warehouseEntryItem.getMaterialProductLine());
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.BEING_INSPECTED || warehouseEntryItem.getState() == WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE) {
                dailyReports.setState(TransferStock.WAITING_FOR_INSPECTION);
            }
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.QUALIFIED) {
                dailyReports.setState(TransferStock.QUALIFIED);
            }
            if (warehouseEntryItem.getState() == WarehouseEntryItemService.UNQUALIFIED) {
                dailyReports.setState(TransferStock.UNQUALIFIED);
            }
            dailyReports.setEntryNo(warehouseEntryItem.getWarehouseEntryInboundDeliveryNo());
            dailyReports.setSupplierName(warehouseEntryItem.getSupplierName());
            dailyReports.setAmountDiff(warehouseEntryItem.getRealAmount());
            dailyReports.setType(DailyReports.AMOUNT_DIFF_ENTRY_STATE);
            dailyReports.setTimestamp(warehouseEntryItem.getEntryItemCreatTime());
            dailyReportsList.add(dailyReports);
        }
        StockRecordFind stockRecordFindEnd = new StockRecordFind();
        stockRecordFindEnd.setTimeEnd(dailyReportRequest.getEndTime());
        stockRecordFindEnd.setSupplyId(supplyId);
        Object[] objectEnd = this.findSupplyStockByTime(accountBook, stockRecordFindEnd);
        DailyReports dailyReports = new DailyReports();
        for (int j = 0; j < objectEnd.length; j++) {
            //物料代号 物料名 状态 总数量
            Object[] o = (Object[]) objectEnd[j];
            dailyReports.setMaterialNo((String) o[0]);
            dailyReports.setMaterialName((String) o[1]);
            dailyReports.setMaterialProductLine((String) o[5]);
            if ((int) o[2] == TransferStock.QUALIFIED) {
                dailyReports.setRealStockQualified((BigDecimal) o[3]);
            } else if ((int) o[2] == TransferStock.UNQUALIFIED) {
                dailyReports.setRealStockUnqualified((BigDecimal) o[3]);
            } else if ((int) o[2] == TransferStock.WAITING_FOR_INSPECTION) {
                dailyReports.setRealStockWaitingForInspection((BigDecimal) o[3]);
            }
            dailyReports.setSupplyId((int) o[4]);
            dailyReports.setTimestamp(dailyReportRequest.getEndTime());
            dailyReports.setType(DailyReports.AMOUNT_END);
        }
        //把不存在零件显示为0
        for (int i = 0; i < supplyViews.length; i++) {
            for (int s = 0; s < 3; s++) {
                boolean exist = false;
                for (int j = 0; j < objectEnd.length; j++) {
                    //物料代号 物料名 状态 总数量
                    Object[] o = (Object[]) objectEnd[j];
                    if ((int) o[4] == supplyViews[i].getId() && (int) o[2] == s) {
                        exist = true;
                    }
                }
                //如果不存在
                if (!exist) {
                    dailyReports.setMaterialNo(supplyViews[i].getMaterialNo());
                    dailyReports.setMaterialName(supplyViews[i].getMaterialName());
                    dailyReports.setMaterialProductLine(supplyViews[i].getMaterialProductLine());
                    dailyReports.setState(s);
                    if (s == TransferStock.QUALIFIED) {
                        dailyReports.setRealStockQualified(BigDecimal.ZERO);
                    } else if (s == TransferStock.UNQUALIFIED) {
                        dailyReports.setRealStockUnqualified(BigDecimal.ZERO);
                    } else if (s == TransferStock.WAITING_FOR_INSPECTION) {
                        dailyReports.setRealStockWaitingForInspection(BigDecimal.ZERO);
                    }
                    dailyReports.setSupplyId(supplyViews[i].getId());
                    dailyReports.setTimestamp(dailyReportRequest.getStartTime());
                    dailyReports.setType(DailyReports.AMOUNT_END);
                }
            }
            dailyReportsList.add(dailyReports);
        }
        Collections.sort(dailyReportsList, new DailyReportsComparator());
        return dailyReportsList;
    }

    //物料代号 物料名 状态 总数量 supplyId 物料系列
    private Object[] findSupplierStockByTime(String accountBook, StockRecordFind stockRecordFind, String supplyId) {
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query = null;
        //库存查询最新一条用
        String sqlNew = "select s_all.materialNo,s_all.materialName,s_all.state,sum(s_all.amount) as sum_amount,s_all.supplyId,s_all.MaterialProductLine from \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.supplyId,s2.State,StorageLocationID FROM StockRecordView As s2\n" +
                "where s2.supplierId=:supplierId and s2.time<=:checkTime \n" +
                "GROUP BY s2.BatchNo,s2.Unit,s2.UnitAmount,s2.State,s2.supplyId,s2.StorageLocationID) AS s3 \n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time and s1.state=s3.state \n" +
                "and s1.supplierId=:supplierId " + supplyId + ") as s_all\n" +
                "GROUP BY s_all.state,s_all.supplyId";
        session.flush();
        query = session.createNativeQuery(sqlNew);
        query.setParameter("supplierId", stockRecordFind.getSupplierId());
        query.setParameter("checkTime", stockRecordFind.getTimeEnd());
        Object[] resultArray = null;
        List<Object[]> resultList = query.list();
        resultArray = (Object[]) Array.newInstance(Object.class, resultList.size());
        resultList.toArray(resultArray);
        return resultArray;
    }

    //物料代号 物料名 状态 总数量 supplyId 物料系列
    private Object[] findSupplyStockByTime(String accountBook, StockRecordFind stockRecordFind) {
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        Query query = null;
        //库存查询最新一条用
        String sqlNew = "select s_all.materialNo,s_all.materialName,s_all.state,sum(s_all.amount) as sum_amount,s_all.supplyId,s_all.MaterialProductLine from \n" +
                "(SELECT s1.* FROM StockRecordView AS s1\n" +
                "INNER JOIN\n" +
                "(SELECT s2.BatchNo,s2.Unit,s2.UnitAmount,Max(s2.Time) AS TIME,s2.supplyId,s2.State,StorageLocationID FROM StockRecordView As s2\n" +
                "where   s2.supplyId=:supplyId and s2.time<=:checkTime \n" +
                "GROUP BY s2.BatchNo,s2.Unit,s2.UnitAmount,s2.State,s2.supplyId,s2.StorageLocationID) AS s3 \n" +
                "ON s1.Unit=s3.Unit AND s1.UnitAmount=s3.UnitAmount AND s1.Time=s3.Time and s1.state=s3.state \n" +
                " ) as s_all\n" +
                "GROUP BY s_all.state,s_all.supplyId";
        session.flush();
        query = session.createNativeQuery(sqlNew);
        query.setParameter("supplyId", stockRecordFind.getSupplyId());
        query.setParameter("checkTime", stockRecordFind.getTimeEnd());
        Object[] resultArray = null;
        List<Object[]> resultList = query.list();
        resultArray = (Object[]) Array.newInstance(Object.class, resultList.size());
        resultList.toArray(resultArray);
        return resultArray;
    }

    public List<DailyReports> generateDailyReportsByYear(String accountBook,int supplyId) {
        List<DailyReports> dailyReportsList = new ArrayList<>();
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            //库存查询最新一条用
            String sqlNew = "call GenerateDailyAmountSearch("+supplyId+")";
            session.flush();
            query = session.createNativeQuery(sqlNew);
            List<Object[]> list = query.list();
            //time TIMESTAMP,
            //entryAmountWait
            //entryAmountQua
            //entryAmountUnq
            //amountDiff
            //realStockQualified
            //realStockUnqualified
            // realStockWaitingForInspection
            //state
            //type
            // entryNo
            // returnAmountQualified
            // returnAmountUnqualified
            // returnToSupplierQualified
            //returnToSupplierUnqualified
            if (list != null && list.size() > 0) {
                for (Object[] objects : list) {
                    DailyReports dailyReports = new DailyReports();
                    dailyReports.setTimestamp((Timestamp)objects[0]);
                    dailyReports.setEntryAmountWait((BigDecimal) objects[1]);
                    dailyReports.setEntryAmountQua((BigDecimal) objects[2]);
                    dailyReports.setEntryAmountUnq((BigDecimal) objects[3]);
                    dailyReports.setAmountDiff((BigDecimal) objects[4]);
                    dailyReports.setRealStockQualified((BigDecimal) objects[5]);
                    dailyReports.setRealStockUnqualified((BigDecimal) objects[6]);
                    dailyReports.setRealStockWaitingForInspection((BigDecimal) objects[7]);
                    //dailyReports.setState((int) objects[8]);
                    dailyReports.setType((int) objects[9]);
                    dailyReports.setEntryNo((String)objects[10]);
                    dailyReports.setReturnAmountQualified((BigDecimal) objects[11]);
                    dailyReports.setReturnAmountUnqualified((BigDecimal) objects[12]);
                    dailyReports.setReturnToSupplierQualified((BigDecimal) objects[13]);
                    dailyReports.setReturnToSupplierUnqualified((BigDecimal) objects[14]);
                    dailyReportsList.add(dailyReports);
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dailyReportsList, new DailyReportsComparator());
        return dailyReportsList;
    }
}
