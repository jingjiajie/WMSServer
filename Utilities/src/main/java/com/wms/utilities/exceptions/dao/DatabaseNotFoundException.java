package com.wms.utilities.exceptions.dao;

public class DatabaseNotFoundException extends WMSDAOException{
    public DatabaseNotFoundException(String databaseName){
        super("Database \""+databaseName+"\" not found!");
    }
}
