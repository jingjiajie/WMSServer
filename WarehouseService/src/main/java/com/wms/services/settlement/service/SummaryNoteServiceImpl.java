package com.wms.services.settlement.service;

import com.wms.services.ledger.service.PersonService;
import com.wms.services.settlement.dao.SummaryDetailsDAO;
import com.wms.services.settlement.dao.SummaryNoteDAO;
import com.wms.services.settlement.datastructures.StockRecordAmount;
import com.wms.services.warehouse.service.*;
import com.wms.utilities.OrderNoGenerator;
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
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SummaryNoteServiceImpl implements SummaryNoteService {

    @Autowired
    SummaryNoteDAO summaryNoteDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    OrderNoGenerator orderNoGenerator;
    @Autowired
    DeliveryOrderItemService deliveryOrderItemService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    SummaryNoteItemService summaryNoteItemService;
    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    SummaryDetailsService summaryDetailsService;
    @Autowired
    SummaryDetailsDAO summaryDetailsDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static final String NO_PREFIX = "H";

    @Override
    public int[] add(String accountBook, SummaryNote[] summaryNotes) throws WMSServiceException
    {
        //生成/检测单号
        Stream.of(summaryNotes).forEach((summaryNote) -> {
            //如果单号留空则自动生成
            if (summaryNote.getNo() == null) {
                summaryNote.setNo(this.orderNoGenerator.generateNextNo(accountBook, SummaryNoteServiceImpl.NO_PREFIX,summaryNote.getWarehouseId()));
            }
        });
        this.validateEntities(accountBook,summaryNotes);
        int[] ids= summaryNoteDAO.add(accountBook,summaryNotes);
        this.validateDuplication(accountBook,summaryNotes);
        return ids;
    }

    @Override
    public void update(String accountBook, SummaryNote[] summaryNotes) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryNotes);
        summaryNoteDAO.update(accountBook, summaryNotes);
        this.validateDuplication(accountBook,summaryNotes);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (summaryNoteDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总单不存在，请重新查询！(%d)", id));
                }
            }

            summaryNoteDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除汇总单信息失败，如果汇总单信息已经被引用，需要先删除引用的内容，才能删除该汇总单");
        }
    }

    @Override
    public SummaryNoteView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SummaryNote[] summaryNotes) throws WMSServiceException{
        Stream.of(summaryNotes).forEach((summaryNote -> {
            new Validator("代号").notEmpty().validate(summaryNote.getNo());
            new Validator("起始时间").notEmpty().validate(summaryNote.getStartTime());
            new Validator("截止时间").notEmpty().validate(summaryNote.getEndTime());
            if(summaryNote.getStartTime().compareTo(summaryNote.getEndTime())>=0)
            {
                throw new WMSServiceException("汇总单的截止时间必须在起始时间之后！单号："+summaryNote.getNo());
            }
            if(this.warehouseService.find(accountBook,
                    new Condition().addCondition("id",summaryNote.getWarehouseId())).length == 0){
                throw new WMSServiceException(String.format("仓库不存在，请重新提交！(%d)",summaryNote.getWarehouseId()));
            }
            if(this.personService.find(accountBook,
                    new Condition().addCondition("id",summaryNote.getCreatePersonId())).length == 0){
                throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",summaryNote.getCreatePersonId()));
            }
        }));
    }

    private void validateDuplication(String accountBook,SummaryNote[] summaryNotes)
    {
        Condition cond = new Condition();
        cond.addCondition("warehouseId",summaryNotes[0].getWarehouseId());
        SummaryNote[] summaryNotesCheck=summaryNoteDAO.findTable(accountBook,cond);
        List<SummaryNote> summaryNoteList= Arrays.asList(summaryNotesCheck);
        summaryNoteList.stream().reduce((last, cur) -> {
            if (last.getNo().equals(cur.getNo())){
                throw new WMSServiceException("汇总单单号重复:"+cur.getNo());
            }
            return cur;
        });
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryNoteDAO.findCount(database,cond);
    }

    @Override
    public void summaryDelivery(String accountBook,SummaryNote summaryNote) throws WMSServiceException{
        Timestamp startTime=summaryNote.getStartTime();
        Timestamp endTime=summaryNote.getEndTime();
        int warehouseId=summaryNote.getWarehouseId();

        if (this.find(accountBook,new Condition().addCondition("id",summaryNote.getId())).length==0){
            throw new WMSServiceException(String.format("汇总单不存在，请重新提交！(%s)",summaryNote.getNo()));
        }
        List<SummaryDetails> summaryDetailsList=new ArrayList();

        //找时间段内发货的出库单
        DeliveryOrderView[] deliveryOrderViews=this.deliveryOrderService.find(accountBook,new Condition().addCondition("warehouseId",warehouseId)
                .addCondition("deliverTime",startTime, ConditionItem.Relation.GREATER_THAN)
                .addCondition("deliverTime",endTime, ConditionItem.Relation.LESS_THAN_OR_EQUAL_TO));
        if (deliveryOrderViews.length==0){
            throw new WMSServiceException(String.format("该时间段仓库里没有出库单发货操作，请重新提交！(%d)",warehouseId));
        }

        Integer[] ids=new Integer[deliveryOrderViews.length];
        for (int i=0;i<deliveryOrderViews.length;i++){
            ids[i]=deliveryOrderViews[i].getId();
        }
        DeliveryOrderItemView[] deliveryOrderItemViews=this.deliveryOrderItemService.find(accountBook,new Condition().addCondition("deliveryOrderId",ids, ConditionItem.Relation.IN));

        if (deliveryOrderItemViews.length!=0) {
            //按科目分组
            Map<Integer, List<DeliveryOrderItemView>> groupBySupplyId =
                    Stream.of(deliveryOrderItemViews).collect(Collectors.groupingBy(DeliveryOrderItemView::getSupplyId));

            Iterator<Map.Entry<Integer, List<DeliveryOrderItemView>>> entries = groupBySupplyId.entrySet().iterator();
            //将每组最新的加到一个列表中
            while (entries.hasNext()) {
                Map.Entry<Integer, List<DeliveryOrderItemView>> entry = entries.next();
                Integer supplyId = entry.getKey();
                List<DeliveryOrderItemView> deliveryOrderItemViewList = entry.getValue();
                DeliveryOrderItemView[] curDeliveryOrderItemViews = (DeliveryOrderItemView[]) Array.newInstance(DeliveryOrderItemView.class, deliveryOrderItemViewList.size());
                deliveryOrderItemViewList.toArray(curDeliveryOrderItemViews);

                BigDecimal deliveryAmount=BigDecimal.ZERO;
                if (curDeliveryOrderItemViews.length!=0) {
                    for (int i = 0; i < curDeliveryOrderItemViews.length; i++) {
                        deliveryAmount = deliveryAmount.add(curDeliveryOrderItemViews[i].getRealAmount());
                    }

                    SummaryNoteItemView[] summaryNoteItemViews = this.summaryNoteItemService.find(accountBook, new Condition().addCondition("summaryNoteId", summaryNote.getId())
                            .addCondition("supplierId", curDeliveryOrderItemViews[0].getSupplierId()));
                    if (summaryNoteItemViews.length == 0) {
                        throw new WMSServiceException(String.format("汇总单条目不存在，请重新提交！(%s)", summaryNote.getNo()));
                    }

                    SummaryDetailsView[] summaryDetailsViews = this.summaryDetailsService.find(accountBook, new Condition().addCondition("summaryNoteItemId", summaryNoteItemViews[0].getId())
                            .addCondition("supplyId", supplyId));

                    if (summaryDetailsViews.length != 1) {
                        throw new WMSServiceException(String.format("汇总单条目详情对应供货不唯一，请重新提交！(%s)", summaryNote.getNo()));
                    }

                    SummaryDetails[] summaryDetails = ReflectHelper.createAndCopyFields(summaryDetailsViews, SummaryDetails.class);

                    summaryDetails[0].setDeliveryAmount(deliveryAmount);
                    summaryDetailsList.add(summaryDetails[0]);
                }
            }
        }


        SummaryDetails[] returnSummaryDetails=new SummaryDetails[summaryDetailsList.size()];
        summaryDetailsList.toArray(returnSummaryDetails);
        this.summaryDetailsService.update(accountBook,returnSummaryDetails);

        SummaryNoteItemView[] summaryNoteItemViews = this.summaryNoteItemService.find(accountBook, new Condition().addCondition("summaryNoteId", summaryNote.getId()));
        SummaryNoteItem[] summaryNoteItems = ReflectHelper.createAndCopyFields(summaryNoteItemViews, SummaryNoteItem.class);

        Stream.of(summaryNoteItems).forEach(summaryNoteItem -> {
            BigDecimal deliveryAmount=BigDecimal.ZERO;
            SummaryDetails[] summaryDetails = this.summaryDetailsDAO.findTable(accountBook, new Condition().addCondition("summaryNoteItemId", summaryNoteItem.getId()));
            if (summaryDetails.length!=0){
                for(int i=0;i<summaryDetails.length;i++){
                    deliveryAmount = deliveryAmount.add(summaryDetails[i].getDeliveryAmount());
                }
            }
            summaryNoteItem.setTotalDeliveryAmount(deliveryAmount);
        });
        this.summaryNoteItemService.update(accountBook,summaryNoteItems);
    }

    public void generateSummaryNotes(String accountBook,int warehouseId,int summaryNoteId) throws WMSServiceException{
        Session session = this.sessionFactory.getCurrentSession();
        session.flush();
        int[] ids=null;
        try {
            session.createNativeQuery("USE " + accountBook + ";").executeUpdate();
        } catch (Throwable ex) {
            throw new DatabaseNotFoundException(accountBook);
        }
        try {
            Query query = null;
            //SqlQuery query1=session.createSQLQuery("call testCall()");
            String sql = "CALL main_copy("+summaryNoteId+","+warehouseId+")";
            //String sql="call testCall("+100+")";
            query=session.createNativeQuery(sql);
            List list = query.list();
            //List<StockRecordAmount[]> stockRecordAmountList = (List<StockRecordAmount[]>)query.list();
            int a=0;

        } catch (Exception e) {
            throw new WMSServiceException("查询人员薪资出错！");
        }
    }
}
