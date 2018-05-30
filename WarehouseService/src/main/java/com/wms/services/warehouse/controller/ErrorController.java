package com.wms.services.warehouse.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.wms.utilities.exceptions.dao.WMSDAOException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler
    public ResponseEntity<String> handleWMSServiceException(WMSServiceException ex){
        //ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleWMSDAOException(WMSDAOException ex){
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return new ResponseEntity<>("数据格式错误，请检查输入数据",HttpStatus.BAD_REQUEST);
    }
}
