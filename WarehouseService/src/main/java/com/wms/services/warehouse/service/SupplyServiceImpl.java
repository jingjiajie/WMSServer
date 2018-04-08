package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.services.warehouse.model.Supplier;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyService {
    @Autowired
    SupplyDAO supplyDAO;
    @Autowired
    SupplierServices supplierServices;
    @Autowired
    MaterialService materialService;
    @Transactional
    public int[] add(String accountBook, Supply[] supplies) throws WMSServiceException{
        for (int i=0;i<supplies.length;i++) {

            //判断物料ID是否为空
            String materialID = supplies[i].getMaterialId().toString();;//获取供货物料名称
            if (materialID == null || materialID.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料不能为空！");
            }
            //判断供货商ID是否为空
            String SupplierId = supplies[i].getSupplierId().toString();;//获取供货商名称
            if (SupplierId == null || SupplierId.trim().length() <=0) {       //判断是否为空
                throw new WMSServiceException("供货商不能为空！");
            }

            Supply[] suppliesRepeat=null;//新建一个数组，物料复述
            Condition condition = Condition.fromJson("{'conditions':[{'key':'SupplierId','values':['"+SupplierId+"'],'relation':'EQUAL'}],[{'key':'MaterialId','values':['"+materialID+"'],'relation':'EQUAL'}],'orders':[{'key':'SupplierId','order':'ASC'}]}");
            //添加供货商-物料关联查询条件，按供货商ID排序
            try {
                suppliesRepeat = supplyDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (suppliesRepeat.length > 0) {
                throw new WMSServiceException("物料-供货商组合 " + materialID + " 已经存在！");
            }
            //判断物料-供货商组合是否唯一
        }
        for (int i=0;i<supplies.length;i++)
        {
            supplies[i].setWarehouseId(1);//先添加一个仓库ID，后面修正
            supplies[i].setCreatePersonId(19);//先添加一个创建人员ID，后面修正
            supplies[i].setCreateTime(new Timestamp(System.currentTimeMillis()));//添加创建时间
        }

        try{
            return supplyDAO.add(accountBook,supplies);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Supply[] supplies) throws WMSServiceException{

        for (int i=0;i<supplies.length;i++) {

            Supply[] suppliesRepeat=null;//新建一个数组，物料复述
            int actid = supplies[i].getId();//获取要修改供货信息的Id

            if (actid == 0) {       //判断是否输入所要查询科目的ID int类型默认为0 数据库id默认从1开始 所以可以用0判断，参考AccountTitle
                throw new WMSServiceException("所选供货信息条目不存在!");
            }

            //判断物料ID是否为空
            String materialID = supplies[i].getMaterialId().toString();;//获取供货物料名称
            if (materialID == null || materialID.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料不能为空！");
            }
            //判断供货商ID是否为空
            String SupplierId = supplies[i].getSupplierId().toString();;//获取供货商名称
            if (SupplierId == null || SupplierId.trim().length() <=0) {       //判断是否为空
                throw new WMSServiceException("供货商不能为空！");
            }

            Condition condition = Condition.fromJson("{'conditions':[{'key':'SupplierId','values':['"+SupplierId+"'],'relation':'EQUAL'}],[{'key':'MaterialId','values':['"+materialID+"'],'relation':'EQUAL'}],'orders':[{'key':'SupplierId','order':'ASC'}]}");
            //添加供货商-物料关联查询条件，按供货商ID排序
            try {
                suppliesRepeat = supplyDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (suppliesRepeat.length > 0) {
                throw new WMSServiceException("物料-供货商组合 " + materialID + SupplierId + " 存在！");
            }
            //判断物料-供货商组合是否已经存在，不存在则往下执行
            supplies[i].setWarehouseId(1);
            supplies[i].setLastUpdatePersonId(19);//预设值
            supplies[i].setLastUpdateTime(new Timestamp(System.currentTimeMillis()));//更新时间
        }

        try {
            supplyDAO.update(accountBook, supplies);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

/*
        Supply[] supplies;
        ReceiptTicketItem[] receiptTicketItems;//收货单零件条目
        int idLength=ids.length;//取要删除的Id个数
        int[] idsModify=null;//定义修改的数组ID
        List<int[]> idsList = Arrays.asList(ids);//百度一下用法

        List<int[]> IDRemove=null;//定义删除的数组ID
        for (int i=0;i<idLength;i++)
        {
            Supply[] supplyRefference=null;
            int SupplyID=ids[i];

            Condition condition = Condition.fromJson("{'conditions':[{'key':'SupplyID','values':['" + SupplyID + "'],'relation':'EQUAL'}]}");
            receiptTicketItems =receiptTicketItemService.find(accountBook,condition);
            if(receiptTicketItems.length>0){
                Condition conditionSupplier= Condition.fromJson("{'conditions':[{'key':'id','values':['" + SupplyID + "'],'relation':'EQUAL'}]}");
                supplyRefference=supplyDAO.find("WMS_Template",conditionSupplier);
                throw new WMSServiceException("供应信息被引用无法删除!");
            }
        }
*/
        try {
            supplyDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public Supply[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.supplyDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
