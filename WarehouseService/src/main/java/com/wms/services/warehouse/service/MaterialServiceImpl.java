package com.wms.services.warehouse.service;

import com.wms.services.warehouse.WarehouseService;
import com.wms.services.warehouse.dao.MaterialDAO;
import com.wms.services.warehouse.model.Material;
import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.services.warehouse.model.Supply;
import com.wms.services.warehouse.model.Warehouse;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    MaterialDAO materialDAO;
    @Autowired
    SupplyService supplyService;
    SupplyDAO  supplyDAO;

    @Transactional
    public int[] add(String accountBook, Material[] materials) throws WMSServiceException{
        for (int i=0;i<materials.length;i++) {
            //判断物料是否为空
            String materialName = materials[i].getName();;//获取供货物料名称
            if (materialName == null || materialName.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料名称不能为空！");
            }
            String materialNo = materials[i].getNo();//获取供货物料代号
            if (materialNo == null || materialNo.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料代号不能为空！");
            }
            int materialWarehouseId =  materials[i].getWarehouseId();//获取供货物料代号
            if (materialWarehouseId == 0) {       //判断是否为空
                throw new WMSServiceException("仓库ID不能为空！");
            }

            Supply[] materialsRepeat=null;//新建一个数组，物料复述
            Condition condition = Condition.fromJson("{’conditions':[{'key':'Name','values':['"+materialName+"'],'relation':'EQUAL'}],'orders':[{'key':'Name','order':'ASC'}]}");
            //这里用单引号，避免了用双引号要转义字符
            //添加供货商-物料关联查询条件，按供货商ID排序
            try {
                materialsRepeat = supplyDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (materialsRepeat.length > 0) {
                throw new WMSServiceException("物料名称 " + materialName + " 已经存在！");
            }
            //判断物料是否唯一

            materials[i].setWarehouseId(1);//先添加一个仓库ID，后面修正

        }

        try{
            return materialDAO.add(accountBook,materials);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Material[] materials) throws WMSServiceException{

        for (int i=0;i<materials.length;i++) {

            int actid = materials[i].getId();//获取要修改信息的Id
            if (actid == 0) {       //判断id，参考AccountTitle
                throw new WMSServiceException("修改失败，所选物料不存在!");
            }

            //判断物料是否为空
            String materialName = materials[i].getName();;//获取供货物料名称
            if (materialName == null || materialName.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料名称不能为空！");
            }
            String materialNo = materials[i].getNo();//获取供货物料代号
            if (materialNo == null || materialNo.trim().length() <=0) {       //判断是否为空，trim()去除字符串两边空格
                throw new WMSServiceException("物料代号不能为空！");
            }

            int materialWarehouseId =  materials[i].getWarehouseId();//获取供货物料代号
            if (materialWarehouseId == 0) {       //判断是否为空
                throw new WMSServiceException("仓库ID不能为空！");
            }
            /*
            Condition condition1 = Condition.fromJson("{'conditions':[{'key':'Id','values':['"+materialWarehouseId+"'],'relation':'EQUAL'}],'orders':[{'key':'Id','order':'ASC'}]}");
            Warehouse[] judgeWarehouseId = null;
            try {
                judgeWarehouseId = WarehouseService.find("Warehouse",condition1);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook "+accountBook+" not found!");
            }
            if (judgeWarehouseId.length <= 0) {
                throw new WMSServiceException("没有找到仓库ID为:"+materialWarehouseId+" 的仓库");
            }
            */


            Supply[] materialsRepeat=null;//新建一个数组，物料复述
            Condition condition = Condition.fromJson("{'conditions':[{'key':'Name','values':['"+materialName+"'],'relation':'EQUAL'}],'orders':[{'key':'Name','order':'ASC'}]}");
            //添加查询条件
            try {
                materialsRepeat = supplyDAO.find(accountBook, condition);
            } catch (DatabaseNotFoundException ex) {
                throw new WMSServiceException("Accountbook " + accountBook + " not found!");
            }
            if (materialsRepeat.length > 0) {
                throw new WMSServiceException("物料名称 " + materialName + " 已经存在！");
            }
            //判断物料是否唯一

        }

        try {
            materialDAO.update(accountBook, materials);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        //被供货引用无法删除，后需改为停用服务，需要请求消息窗口
        Supply[] supplies;
        int idLength=ids.length;
        for (int i=0;i<idLength;i++)
        {
            Material[] materialRefference=null;
            int MaterialID=ids[i];
            Condition condition = Condition.fromJson("{'conditions':[{'key':'materialID','values':['" + MaterialID + "'],'relation':'EQUAL'}]}");
            supplies =supplyService.find(accountBook,condition);
            if(supplies.length>0){
                Condition conditionMaterial= Condition.fromJson("{'conditions':[{'key':'id','values':['" + MaterialID + "'],'relation':'EQUAL'}]}");
                materialRefference=materialDAO.find("WMS_Template",conditionMaterial);
                throw new WMSServiceException("物料 " +materialRefference[0].getName() + " 被引用无法删除!");
            }
        }


        try {
            materialDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public Material[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.materialDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
