package com.wms.utilities.exceptions.dao;

public class DatabaseNotFoundException extends WMSDAOException {
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    private String databaseName;

    public DatabaseNotFoundException(String databaseName) {
        super("Database \"" + databaseName + "\" not found!");
        this.setDatabaseName(databaseName);
    }
}
