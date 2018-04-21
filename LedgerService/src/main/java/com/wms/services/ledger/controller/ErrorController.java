package com.wms.services.ledger.controller;

import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler
    public ResponseEntity<String> handleDatabaseNotFoundException(DatabaseNotFoundException e){
        return new ResponseEntity<String>(String.format("找不到帐套:%s",e.getDatabaseName()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
