package com.wms.services.warehouse.controller;

import com.wms.services.warehouse.service.RefreshGlobalDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/{accountBook}/refreshGlobalDate")
public class RefreshGlobalGlobalDateControllerImpl implements RefreshGlobalDateController {
    @Autowired
    RefreshGlobalDateService refreshGlobalDateService;

    @RequestMapping(value = "/{warehouseId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public  Map<String,Object[]> refreshGlobalDate(@PathVariable("accountBook") String accountBook,
                                                   @PathVariable("warehouseId") int warehouseId)
    {
        return refreshGlobalDateService.refreshGlobalDate(accountBook,warehouseId);
    }

}
