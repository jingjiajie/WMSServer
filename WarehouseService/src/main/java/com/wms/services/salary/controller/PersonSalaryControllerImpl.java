package com.wms.services.salary.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.datestructures.AddPersonSalary;
import com.wms.services.salary.datestructures.AddPersonSalaryRequest;
import com.wms.services.salary.datestructures.PersonSalaryViewGroupByTypeAndPeriod;
import com.wms.services.salary.service.PersonSalaryService;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.annotation.W3CDomHandler;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/{accountBook}/person_salary")
public class PersonSalaryControllerImpl implements PersonSalaryController {
    @Autowired
    PersonSalaryService personSalaryService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PersonSalary[] personSalaries){
        return personSalaryService.add(accountBook,personSalaries);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PersonSalary[] personSalaries) {
        personSalaryService.update(accountBook,personSalaries);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        personSalaryService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}/old",method = RequestMethod.GET)
    public PersonSalaryView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PersonSalaryView[] personSalaryViews =personSalaryService.find(accountBook, cond);
        return personSalaryViews;
    }

    @Override
    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PersonSalaryView[] findSum(@PathVariable("accountBook") String accountBook,
                                            @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PersonSalaryView[] personSalaryViews =personSalaryService.find(accountBook, cond);
        if(personSalaryViews.length==0){return personSalaryViews;}
        List<PersonSalaryView> personSalaryViewResult = new ArrayList<>();
        List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriodArrayList = new ArrayList<>();
        for (int i = 0; i < personSalaryViews.length; i++) {
            PersonSalaryViewGroupByTypeAndPeriod personSalaryViewGroupByTypeAndPeriod = new PersonSalaryViewGroupByTypeAndPeriod();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(personSalaryViews[i].getPersonId());
            stringBuffer.append(";");
            stringBuffer.append(personSalaryViews[i].getSalaryPeriodId());
            stringBuffer.append(";");
            stringBuffer.append(personSalaryViews[i].getSalaryTypeId());
            stringBuffer.append(";");
            personSalaryViewGroupByTypeAndPeriod.setGroupCondition(stringBuffer.toString());
            personSalaryViewGroupByTypeAndPeriod.setPersonSalaryViews(personSalaryViews[i]);
            personSalaryViewGroupByTypeAndPeriodArrayList.add(personSalaryViewGroupByTypeAndPeriod);
        }
        PersonSalaryViewGroupByTypeAndPeriod[] resultArray = null;
        resultArray = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriodArrayList.size());
        personSalaryViewGroupByTypeAndPeriodArrayList.toArray(resultArray);
        Map<String, List<PersonSalaryViewGroupByTypeAndPeriod>> personSalaryGroup = Stream.of(resultArray).collect(Collectors.groupingBy(PersonSalaryViewGroupByTypeAndPeriod::getGroupCondition));
        Iterator<Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>>> entries = personSalaryGroup.entrySet().iterator();
        //将每组求和然后加到一个列表中
        while (entries.hasNext()) {
            Map.Entry<String, List<PersonSalaryViewGroupByTypeAndPeriod>> entry = entries.next();
            List<PersonSalaryViewGroupByTypeAndPeriod> personSalaryViewGroupByTypeAndPeriods = entry.getValue();
            PersonSalaryViewGroupByTypeAndPeriod[] resultArray1 = null;
            resultArray1 = (PersonSalaryViewGroupByTypeAndPeriod[]) Array.newInstance(PersonSalaryViewGroupByTypeAndPeriod.class, personSalaryViewGroupByTypeAndPeriods.size());
            personSalaryViewGroupByTypeAndPeriods.toArray(resultArray1);
            BigDecimal amountAll = new BigDecimal(0);
            PersonSalaryView personSalaryView = new PersonSalaryView();
            for (int i = 0; i < resultArray1.length; i++) {
                ReflectHelper.copyFields(resultArray1[i].getPersonSalaryView(),personSalaryView);
                if (personSalaryView.getGiveOut() == 1)
                    if(personSalaryView.getAmount()!=null){
                    amountAll = amountAll.add(personSalaryView.getAmount());}
            }
            personSalaryView.setAmount(amountAll);
            personSalaryView.setSalaryItemName("总金额");
            personSalaryView.setSalaryItemDisplayPriority(0);
            if (resultArray1.length != 0) {
                personSalaryViewResult.add(personSalaryView);
            }
        }
        for(int i=0;i<personSalaryViews.length;i++){
            personSalaryViewResult.add(personSalaryViews[i]);
        }
        personSalaryViewResult.sort(Comparator.comparing(PersonSalaryView::getSalaryItemDisplayPriority));
        Collections.reverse(personSalaryViewResult);
        PersonSalaryView[] personSalaryViewResultArray = (PersonSalaryView[]) Array.newInstance(PersonSalaryView.class, personSalaryViewResult.size());
        personSalaryViewResult.toArray(personSalaryViewResultArray);
        return personSalaryViewResultArray;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.findSum(accountBook,condStr).length;
        //return this.personSalaryService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @RequestMapping(value = "/add_person_salary_by_salary_type",method = RequestMethod.POST)
    @ResponseBody
       public void addPersonSalary(@PathVariable("accountBook") String accountBook,
               @RequestBody AddPersonSalary addPersonSalaries) {
           personSalaryService.addPersonSalaryBySalaryType(accountBook,addPersonSalaries);
   }

    @RequestMapping(value = "/update_newest_period",method = RequestMethod.POST)
    @ResponseBody
    public void updateNewestPeriodPersonSalary(@PathVariable("accountBook") String accountBook,
                                @RequestBody AddPersonSalary addPersonSalaries) {
        personSalaryService.updateNewestPeriodPersonSalary(accountBook,addPersonSalaries);
    }

    @RequestMapping(value = "/refresh_person_salary",method = RequestMethod.POST)
    @ResponseBody
    public void refreshPersonSalary(@PathVariable("accountBook") String accountBook,
                               @RequestBody AddPersonSalaryRequest addPersonSalaryRequest) {
        if(addPersonSalaryRequest.getSalaryTypeIds()==null){
            throw new WMSServiceException("人员薪资类型为空!");
        }
        for(int i=0;i<addPersonSalaryRequest.getSalaryTypeIds().size();i++){
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setSalaryPeriodId(addPersonSalaryRequest.getSalaryPeriodId());
            addPersonSalary.setPersonSalaryIds(addPersonSalaryRequest.getPersonSalaryIds());
            addPersonSalary.setSalaryTypeId(addPersonSalaryRequest.getSalaryTypeIds().get(i));
            addPersonSalary.setWarehouseId(addPersonSalaryRequest.getWarehouseId());
            personSalaryService.refreshPersonSalary(accountBook,addPersonSalary);
        }
    }

    @RequestMapping(value = "/refresh_formula",method = RequestMethod.POST)
    @ResponseBody
    public void refreshFormula(@PathVariable("accountBook") String accountBook,
                               @RequestBody AddPersonSalaryRequest addPersonSalaryRequest) {
        if(addPersonSalaryRequest.getSalaryTypeIds()==null){
            throw new WMSServiceException("人员薪资类型为空!");
        }
        for(int i=0;i<addPersonSalaryRequest.getSalaryTypeIds().size();i++){
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setSalaryPeriodId(addPersonSalaryRequest.getSalaryPeriodId());
            addPersonSalary.setPersonSalaryIds(addPersonSalaryRequest.getPersonSalaryIds());
            addPersonSalary.setSalaryTypeId(addPersonSalaryRequest.getSalaryTypeIds().get(i));
            addPersonSalary.setWarehouseId(addPersonSalaryRequest.getWarehouseId());
            personSalaryService.refreshFormula(accountBook,addPersonSalary);
        }
    }

    @RequestMapping(value = "/refresh_valuation",method = RequestMethod.POST)
    @ResponseBody
    public void refreshValuation(@PathVariable("accountBook") String accountBook,
                               @RequestBody AddPersonSalaryRequest addPersonSalaryRequest) {
        if(addPersonSalaryRequest.getSalaryTypeIds()==null){
            throw new WMSServiceException("人员薪资类型为空!");
        }
        for(int i=0;i<addPersonSalaryRequest.getSalaryTypeIds().size();i++){
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setSalaryPeriodId(addPersonSalaryRequest.getSalaryPeriodId());
            addPersonSalary.setPersonSalaryIds(addPersonSalaryRequest.getPersonSalaryIds());
            addPersonSalary.setSalaryTypeId(addPersonSalaryRequest.getSalaryTypeIds().get(i));
            addPersonSalary.setWarehouseId(addPersonSalaryRequest.getWarehouseId());
            personSalaryService.refreshValuation(accountBook,addPersonSalary);
        }
    }

    @RequestMapping(value = "/add_last_period",method = RequestMethod.POST)
    @ResponseBody
    public void addLastPeriod(@PathVariable("accountBook") String accountBook,
                                 @RequestBody AddPersonSalaryRequest addPersonSalaryRequest) {
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setSalaryPeriodId(addPersonSalaryRequest.getSalaryPeriodId());
            addPersonSalary.setPersonSalaryIds(addPersonSalaryRequest.getPersonSalaryIds());
            addPersonSalary.setWarehouseId(addPersonSalaryRequest.getWarehouseId());
            personSalaryService.addLastPeriod(accountBook,addPersonSalary);
    }

    @RequestMapping(value = "/refresh_formula_and_valuation",method = RequestMethod.POST)
    @ResponseBody
    public void refreshFormulaAndValuation(@PathVariable("accountBook") String accountBook,
                              @RequestBody AddPersonSalaryRequest addPersonSalaryRequest) {
        if(addPersonSalaryRequest.getSalaryTypeIds()==null){
            throw new WMSServiceException("人员薪资类型为空!");
        }
        for(int i=0;i<addPersonSalaryRequest.getSalaryTypeIds().size();i++){
            AddPersonSalary addPersonSalary=new AddPersonSalary();
            addPersonSalary.setSalaryPeriodId(addPersonSalaryRequest.getSalaryPeriodId());
            addPersonSalary.setPersonSalaryIds(addPersonSalaryRequest.getPersonSalaryIds());
            addPersonSalary.setSalaryTypeId(addPersonSalaryRequest.getSalaryTypeIds().get(i));
            addPersonSalary.setWarehouseId(addPersonSalaryRequest.getWarehouseId());
            personSalaryService.refreshFormula(accountBook,addPersonSalary);
            personSalaryService.refreshValuation(accountBook,addPersonSalary);
        }
    }

    @RequestMapping(value = "/remove_no",method = RequestMethod.DELETE)
    public void removeNo() {
        return;
    }

}
